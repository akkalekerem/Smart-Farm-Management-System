package com.smartfarm.smartfarmmanagementsystem.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class AiService {

    @Value("${ai.api.key}")
    private String apiKey;

    @Value("${ai.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String getFarmInsight(String fieldName, String cropType, double temp, double moisture, double wind, double light, double ec) {
        String bitkiAdi = (cropType != null && !cropType.isEmpty()) ? cropType : "bitkiler";

        // Tüm sensör verilerini içeren uzman seviyesi prompt
        String prompt = String.format(
                "ROL: Uzman Ziraat Mühendisi ve Tarımsal Veri Analisti.\n" +
                        "BAĞLAM: '%s' isimli tarlada %s yetiştiriliyor.\n" +
                        "GÜNCEL VERİLER: Sıcaklık %.1f°C, Toprak Nemi %%%.1f, Rüzgar %.1f km/s, Işık Şiddeti %.0f lx, Toprak EC (Besin) %.2f mS/cm.\n" +
                        "GÖREV: Bu verileri %s bitkisinin biyolojik ihtiyaçları, fotosentez gereksinimi ve besin emilimi dengesi açısından analiz et. " +
                        "Eğer değerlerde bir dengesizlik (stres, besin noksanlığı, aşırı ışık vb.) varsa çözüm öner; her şey normalse verim artışı için teknik bir tavsiye ver.\n" +
                        "SINIRLAMALAR: Sadece 2 cümle yaz. Samimi ama profesyonel ol. Giriş cümlesi veya selamlama yapma.",
                fieldName, bitkiAdi, temp, moisture, wind, light, ec, bitkiAdi
        );

        try {
            // 2. GÜVENLİ JSON OLUŞTURMA (Manual String birleştirme yerine Map kullanıyoruz)
            // Bu yapı 404/400 hatalarının en büyük sebebi olan tırnak işareti sorunlarını çözer.
            Map<String, Object> textPart = Map.of("text", prompt);
            Map<String, Object> partList = Map.of("parts", List.of(textPart));
            Map<String, Object> contentList = Map.of("contents", List.of(partList));

            String requestBody = objectMapper.writeValueAsString(contentList);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            String fullUrl = apiUrl + "?key=" + apiKey;
            HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

            // 3. İstek gönderimi ve Detaylı Loglama
            System.out.println("AI İsteği gönderiliyor: " + apiUrl);
            ResponseEntity<String> response = restTemplate.postForEntity(fullUrl, request, String.class);

            // 4. Yanıt İşleme
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                JsonNode root = objectMapper.readTree(response.getBody());
                return root.path("candidates")
                        .get(0)
                        .path("content")
                        .path("parts")
                        .get(0)
                        .path("text")
                        .asText();
            }
        } catch (HttpClientErrorException e) {
            System.err.println("API Hatası: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
            return null;
        } catch (Exception e) {
            System.err.println("Beklenmedik Hata: " + e.getMessage());
            return null;
        }
        return null;
    }
}