package group.intelliboys.smms_backend.repositories;

import group.intelliboys.smms_backend.models.entities.RegistrationFormToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistrationFormTokenRepository extends JpaRepository<RegistrationFormToken, String> {
    RegistrationFormToken findByEmail(String email);
}
