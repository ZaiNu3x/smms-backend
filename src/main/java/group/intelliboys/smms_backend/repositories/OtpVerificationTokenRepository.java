package group.intelliboys.smms_backend.repositories;

import group.intelliboys.smms_backend.models.entities.OtpVerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OtpVerificationTokenRepository extends JpaRepository<OtpVerificationToken, String> {
}
