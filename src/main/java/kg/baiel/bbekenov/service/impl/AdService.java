package kg.baiel.bbekenov.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kg.baiel.bbekenov.entity.Ad;
import kg.baiel.bbekenov.repository.AdRepository;
import kg.baiel.bbekenov.service.IAdService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AdService implements IAdService {
    private final AdRepository adRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger log = LoggerFactory.getLogger(AdService.class);

    public AdService(AdRepository adRepository, RestTemplate restTemplate) {
        this.adRepository = adRepository;
        this.restTemplate = restTemplate;
    }


    @Override
    public void getAdsFromLalafo() {
        String url = "https://lalafo.kg/api/search/v3/feed/search?expand=url&per-page=20&category_id=2048";

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Language", "ru");
            headers.set("Country-Id", "12");
            headers.set("Device", "web");
            headers.set("User-Agent", "Mozilla/5.0");
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));

            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                String body = response.getBody();

                JsonNode root = objectMapper.readTree(body);
                JsonNode items = root.get("items");

                if (items == null || !items.isArray() || items.isEmpty()) {
                    log.info("Найдено 0 объявлений");
                    return;
                }

                List<Ad> ads = new ArrayList<>();
                for (JsonNode adNode : items) {
                    Ad ad = new Ad();
                    ad.setTitle(adNode.path("title").asText());
                    ad.setCity(adNode.path("city").asText());
                    ad.setPrice(adNode.path("price").asText());

                    // главное изображение
                    if (adNode.has("images") && adNode.get("images").isArray() && adNode.get("images").size() > 0) {
                        ad.setImageUrl(adNode.get("images").get(0).path("original_url").asText());
                    } else {
                        ad.setImageUrl(adNode.path("image").asText());
                    }

                    ad.setPostedAt(LocalDateTime.now());
                    ad.setFetchedAt(LocalDateTime.now());

                    ads.add(ad);
                }

                adRepository.saveAll(ads);
                log.info("Сохранено {} объявлений в базу", ads.size());

            } else {
                log.warn("Ошибка при получении данных: {}", response.getStatusCode());
            }

        } catch (Exception e) {
            log.error("Ошибка при запросе Lalafo API", e);
        }
    }

    @Override
    public List<Ad> getTestResult() {
        return adRepository.findTop100ByOrderByPostedAtDesc();
    }
}
