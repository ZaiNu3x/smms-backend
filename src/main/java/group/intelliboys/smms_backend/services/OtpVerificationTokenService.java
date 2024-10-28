package group.intelliboys.smms_backend.services;

import group.intelliboys.smms_backend.models.entities.OtpVerificationToken;
import group.intelliboys.smms_backend.models.results.OtpVerificationResult;
import group.intelliboys.smms_backend.models.tokens.ChangePasswordToken;
import group.intelliboys.smms_backend.repositories.OtpVerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class OtpVerificationTokenService {
    @Autowired
    private OtpVerificationTokenRepository otpVerificationTokenRepository;

    public void saveOtpVerificationToken(OtpVerificationToken token) {
        otpVerificationTokenRepository.save(token);
    }

    public OtpVerificationResult findOtpVerificationTokenById(ChangePasswordToken token) {
        OtpVerificationToken otpVerificationToken = otpVerificationTokenRepository
                .findById(token.getId()).orElse(null);

        if (otpVerificationToken != null) {
            PasswordEncoder encoder = new BCryptPasswordEncoder();
            boolean isEmailOtpSame = encoder.matches(token.getEmailOtp(), otpVerificationToken.getEmailOtp());
            boolean isSmsOtpSame = encoder.matches(token.getSmsOtp(), otpVerificationToken.getSmsOtp());

            if (isEmailOtpSame && isSmsOtpSame) {
                otpVerificationToken.setVerified(true);
                otpVerificationTokenRepository.save(otpVerificationToken);
            }

            return OtpVerificationResult.builder()
                    .id(otpVerificationToken.getId())
                    .message("TOKEN_EXISTS")
                    .isEmailSame(isEmailOtpSame)
                    .isSmsOtpSame(isSmsOtpSame)
                    .build();
        } else {
            return OtpVerificationResult.builder()
                    .message("TOKEN_NOT_EXISTS")
                    .build();
        }
    }

    public OtpVerificationToken findOtpVerificationTokenById(String id) {
        return otpVerificationTokenRepository.findById(id)
                .orElse(null);
    }
}
