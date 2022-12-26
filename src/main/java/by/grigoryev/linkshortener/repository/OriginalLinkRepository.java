package by.grigoryev.linkshortener.repository;

import by.grigoryev.linkshortener.model.OriginalLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OriginalLinkRepository extends JpaRepository<OriginalLink, Long> {
}
