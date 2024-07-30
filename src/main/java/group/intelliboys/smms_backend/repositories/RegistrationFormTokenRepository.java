package group.intelliboys.smms_backend.repositories;

import group.intelliboys.smms_backend.models.entities.RegistrationFormToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistrationFormTokenRepository extends JpaRepository<RegistrationFormToken, String> {
    RegistrationFormToken findByEmail(String email);

    @Modifying
    @Query("UPDATE RegistrationFormToken t SET t.hashedEmailVerificationOtp = ?1 WHERE t.id = ?2")
    void updateHashedEmailOtp(String newOtp, String formId);

    @Modifying
    @Query("UPDATE RegistrationFormToken t SET t.hashedSmsVerificationOtp = ?1 WHERE t.id = ?2")
    void updateHashedSmsOtp(String newOtp, String formId);
}
