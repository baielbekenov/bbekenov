package kg.baiel.bbekenov.cotroller;
import kg.baiel.bbekenov.service.IAdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ads")
public class TestResultController {
    private final IAdService adService;

    @Autowired
    public TestResultController(IAdService adService) {
        this.adService = adService;
    }

    @GetMapping("/fetchFromLalafo")
    public ResponseEntity<String> fetchAds() {
        try {
            adService.getAdsFromLalafo();
            return ResponseEntity.ok("Успешно сохранено в базе!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Ошибка при сохранении в базе: " + e.getMessage());
        }
    }
}
