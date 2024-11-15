package group.intelliboys.smms_backend.repositories;

import group.intelliboys.smms_backend.models.entities.OtpVerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OtpVerificationTokenRepository extends JpaRepository<OtpVerificationToken, String> {
    @Modifying
    @Query("UPDATE OtpVerificationToken o SET o.emailOtp = ?2 WHERE o.id = ?1")
    void updateHashedEmailOtp(String formId, String newEmailOtp);

    @Modifying
    @Query("UPDATE OtpVerificationToken o SET o.smsOtp = ?2 WHERE o.id = ?1")
    void updateHashedSmsOtp(String formId, String newEmailOtp);
}
