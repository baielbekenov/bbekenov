package kg.baiel.bbekenov.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdDto {
    private Long id;
    private String title;
    private String price;
    private String city;
    private String url;
    private String imageUrl;
    private LocalDateTime postedAt;
    private LocalDateTime fetchedAt;
}
