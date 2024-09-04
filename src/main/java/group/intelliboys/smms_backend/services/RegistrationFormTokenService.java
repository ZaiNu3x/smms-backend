package group.intelliboys.smms_backend.services;

import group.intelliboys.smms_backend.models.entities.RegistrationFormToken;
import group.intelliboys.smms_backend.repositories.RegistrationFormTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegistrationFormTokenService {
    @Autowired
    private RegistrationFormTokenRepository registrationFormTokenRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private OtpService otpService;

    public RegistrationFormToken getRegistrationFormTokenByEmail(String email) {
        return registrationFormTokenRepository.findByEmail(email);
    }

    public RegistrationFormToken getRegistrationFormTokenById(String id) {
        return registrationFormTokenRepository.findById(id)
                .orElse(null);
    }

    public void saveRegistrationFormToken(RegistrationFormToken registrationFormToken) {
        registrationFormTokenRepository.save(registrationFormToken);
    }

    public void updateHashedEmailOtp(String newOtp, String formId) {
        registrationFormTokenRepository.updateHashedEmailOtp(newOtp, formId);
    }

    public void updateHashedSmsOtp(String newOtp, String formId) {
        registrationFormTokenRepository.updateHashedSmsOtp(newOtp, formId);
    }

    public void deleteRegistrationFormById(String formId) {
        registrationFormTokenRepository.deleteById(formId);
    }
}
