package by.grigoryev.linkshortener.repository;

import by.grigoryev.linkshortener.model.ShortLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShortLinkRepository extends JpaRepository<ShortLink, Long> {

    Optional<ShortLink> findFirstByLinkOrderByIdDesc(String link);

}
