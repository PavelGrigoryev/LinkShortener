package by.grigoryev.linkshortener.repository;

import by.grigoryev.linkshortener.model.Link;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LinkRepository extends JpaRepository<Link, Long> {

    Optional<Link> findFirstByShortLinkOrderByIdDesc(String shortLink);

}
