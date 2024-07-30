package group.intelliboys.smms_backend.repositories;

import group.intelliboys.smms_backend.models.entities.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, String> {
    Optional<Token> findByValue(String value);
}
