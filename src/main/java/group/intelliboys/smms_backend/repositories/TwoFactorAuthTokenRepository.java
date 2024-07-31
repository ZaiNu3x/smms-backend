package group.intelliboys.smms_backend.repositories;

import group.intelliboys.smms_backend.models.entities.TwoFactorAuthToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TwoFactorAuthTokenRepository extends JpaRepository<TwoFactorAuthToken, String> {

    @Modifying
    @Query("UPDATE TwoFactorAuthToken t SET t.hashedEmailOtp = ?1 WHERE formId = ?2")
    void updateHashedEmailOtp(String newHashedEmailOtp, String formId);

    @Modifying
    @Query("UPDATE TwoFactorAuthToken t SET t.hashedSmsOtp = ?1 WHERE formId = ?2")
    void updateHashedSmsOtp(String newHashedSmsOtp, String formId);

    TwoFactorAuthToken findByFormId(String formId);

    @Modifying
    @Query("UPDATE TwoFactorAuthToken t SET t.status = ?1 WHERE formId = ?2")
    void updateStatusByFormId(String newStatus, String formId);
}
