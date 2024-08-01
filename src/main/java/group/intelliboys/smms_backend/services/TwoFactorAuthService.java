package group.intelliboys.smms_backend.services;

import group.intelliboys.smms_backend.models.entities.TwoFactorAuthToken;
import group.intelliboys.smms_backend.repositories.TwoFactorAuthTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TwoFactorAuthService {
    @Autowired
    private TwoFactorAuthTokenRepository twoFactorAuthTokenRepository;

    public void saveTwoFactorAuthToken(TwoFactorAuthToken twoFactorAuthToken) {
        twoFactorAuthTokenRepository.save(twoFactorAuthToken);
    }

    public void updateHashedEmailOtp(String newHashedEmailOtp, String formId) {
        twoFactorAuthTokenRepository.updateHashedEmailOtp(newHashedEmailOtp, formId);
    }

    public void updateHashedSmsOtp(String newHashedSmsOtp, String formId) {
        twoFactorAuthTokenRepository.updateHashedSmsOtp(newHashedSmsOtp, formId);
    }

    public TwoFactorAuthToken getTwoFactorAuthTokenByFormId(String formId) {
        return twoFactorAuthTokenRepository.findByFormId(formId);
    }

    public void updateStatus(String newStatus, String formId) {
        twoFactorAuthTokenRepository.updateStatusByFormId(newStatus, formId);
    }

    public void resetAttempts(String formId) {
        twoFactorAuthTokenRepository.resetAttempts(formId);
    }

    public boolean isExistsByFormId(String formId) {
        return twoFactorAuthTokenRepository.existsByFormId(formId);
    }
}
