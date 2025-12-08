package kg.baiel.bbekenov.repository;

import kg.baiel.bbekenov.entity.Ad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdRepository extends JpaRepository<Ad, Long> {
    List<Ad> findTop100ByOrderByPostedAtDesc();
}
