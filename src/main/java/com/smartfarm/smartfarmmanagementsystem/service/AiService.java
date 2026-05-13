package com.smartfarm.smartfarmmanagementsystem.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AiService {

    @Value("${ai.api.key}")
    private String apiKey;

    @Value("${ai.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    // DİKKAT: İkinci parametre Crop nesnesinden String cropType'a çevrildi
    public String getFarmInsight(String fieldName, String cropType, double temp, double moisture, double wind) {

        // Eğer ürün ismi girilmemişse genel bir isim verelim
        String bitkiAdi = (cropType != null && !cropType.isEmpty()) ? cropType : "bitkiler";

        // 1. Prompt Hazırlığı: Yapay zekaya artık sınır değerlerini biz vermiyoruz, o biliyor.
        String prompt = String.format(
                "Sen uzman bir ziraat mühendisisin. Çiftçinin '%s' adlı tarlasında %s ekili. " +
                        "Şu anki sıcaklık %.1f derece, toprak nemi %%%.1f, rüzgar hızı %.1f km/s. " +
                        "Bu bilgilere dayanarak çiftçiye tarlasının durumu hakkında 2 cümlelik, samimi, " +
                        "uzman ve proaktif bir tavsiye ver. Sadece tavsiyeyi yaz, selamlama yapma.",
                fieldName, bitkiAdi, temp, moisture, wind
        );

        // 2. Gemini API JSON formatı
        String requestBody = "{ \"contents\": [{ \"parts\": [{ \"text\": \"" + prompt + "\" }] }] }";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String fullUrl = apiUrl + "?key=" + apiKey;
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        try {
            // 3. İstek gönderimi
            ResponseEntity<String> response = restTemplate.postForEntity(fullUrl, request, String.class);

            // 4. JSON Parçalama
            JsonNode root = objectMapper.readTree(response.getBody());
            return root.path("candidates").get(0).path("content").path("parts").get(0).path("text").asText();

        } catch (Exception e) {
            System.out.println("Yapay zeka servisinde hata: " + e.getMessage());
            return null;
        }
    }
}