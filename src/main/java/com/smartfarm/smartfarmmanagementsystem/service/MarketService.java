package com.smartfarm.smartfarmmanagementsystem.service;

import com.smartfarm.smartfarmmanagementsystem.dto.MarketPriceDTO;
import com.smartfarm.smartfarmmanagementsystem.dto.MarketResponseDTO;
import com.smartfarm.smartfarmmanagementsystem.entity.ApiUsage;
import com.smartfarm.smartfarmmanagementsystem.repository.ApiUsageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MarketService {

    private final ApiUsageRepository apiUsageRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    // application.properties dosyasından değerleri okuyoruz
    @Value("${api.collectapi.key}")
    private String apiKey;

    @Value("${api.collectapi.url}")
    private String apiUrl;

    /**
     * @Cacheable sayesinde veriler hafızaya alınır.
     * Böylece her sayfa yenilemede API'ye gidip aylık limitinizi tüketmez.
     */
    @Cacheable(value = "marketPrices")
    public List<MarketPriceDTO> getMarketPrices() {
        // 1. Veritabanından limit kaydını kontrol et
        ApiUsage usage = apiUsageRepository.findById("MARKET_API")
                .orElse(new ApiUsage());

        // 2. Ay başı kontrolü: Eğer yeni bir aya geçildiyse sayacı sıfırla
        if (usage.getLastCallDate() == null || usage.getLastCallDate().getMonth() != LocalDateTime.now().getMonth()) {
            usage.setApiName("MARKET_API");
            usage.setCallCount(0);
        }

        // 3. Limit Kontrolü: 10 istek dolduysa API'ye hiç gitme
        if (usage.getCallCount() >= 10) {
            System.out.println("LOG: Aylık 10 API isteği limiti doldu. Veri çekilemedi.");
            return Collections.emptyList();
        }

        // 4. API İsteği Hazırlığı
        HttpHeaders headers = new HttpHeaders();
        headers.set("content-type", "application/json");
        // apiKey değeri "apikey TOKEN" formatında olmalı
        headers.set("authorization", apiKey);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            // exchange metodu ile JSON'ı otomatik MarketResponseDTO'ya eşliyoruz
            ResponseEntity<MarketResponseDTO> response = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.GET,
                    entity,
                    MarketResponseDTO.class
            );

            if (response.getBody() != null && response.getBody().isSuccess()) {
                // 5. İstek başarılı; sayacı artır ve son arama tarihini güncelle
                usage.setCallCount(usage.getCallCount() + 1);
                usage.setLastCallDate(LocalDateTime.now());
                apiUsageRepository.save(usage);

                return response.getBody().getResult();
            } else {
                System.err.println("LOG: API başarısız yanıt döndü.");
            }
        } catch (Exception e) {
            // Hata detayını konsola yazdırıyoruz (500, 401 vb.)
            System.err.println("LOG: API Çağrısında Hata Oluştu: " + e.getMessage());
        }

        return Collections.emptyList();
    }
}