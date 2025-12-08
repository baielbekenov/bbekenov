package kg.baiel.bbekenov.entity;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "ads")
public class Ad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String price;
    private String city;
    private String url;
    private String imageUrl;
    private LocalDateTime postedAt;
    private LocalDateTime fetchedAt = LocalDateTime.now();
}
