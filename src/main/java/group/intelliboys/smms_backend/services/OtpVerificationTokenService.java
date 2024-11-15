package group.intelliboys.smms_backend.services;

import group.intelliboys.smms_backend.models.entities.OtpVerificationToken;
import group.intelliboys.smms_backend.models.results.global.OtpVerificationResult;
import group.intelliboys.smms_backend.models.results.global.ResentOtpResult;
import group.intelliboys.smms_backend.models.tokens.ChangePasswordToken;
import group.intelliboys.smms_backend.repositories.OtpVerificationTokenRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Transactional
public class OtpVerificationTokenService {
    @Autowired
    private OtpVerificationTokenRepository otpVerificationTokenRepository;
    @Autowired
    private OtpService otpService;

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
                    .isEmailOtpSame(isEmailOtpSame)
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

    public ResentOtpResult resendEmailOtp(String formId) {
        OtpVerificationToken otpVerificationToken = otpVerificationTokenRepository.findById(formId)
                .orElse(null);

        if (otpVerificationToken != null) {
            if (otpVerificationToken.isVerified()) {
                return ResentOtpResult.builder()
                        .message("Forgot password form is already verified!")
                        .status("FORM_ALREADY_VERIFIED")
                        .build();
            } else {
                String newEmailOtp = otpService.generateOtp();
                String hashedNewEmailOtp = new BCryptPasswordEncoder().encode(newEmailOtp);
                otpVerificationTokenRepository.updateHashedEmailOtp(formId, hashedNewEmailOtp);
                log.info("New Email Otp: {}", newEmailOtp);

                return ResentOtpResult.builder()
                        .message("Email Otp resent!")
                        .status("NEW_EMAIL_OTP_RESENT")
                        .build();
            }
        } else {
            return ResentOtpResult.builder()
                    .message("Forgot password form not found!")
                    .status("FORM_NOT_FOUND")
                    .build();
        }
    }

    public ResentOtpResult resendSmsOtp(String formId) {
        OtpVerificationToken otpVerificationToken = otpVerificationTokenRepository.findById(formId)
                .orElse(null);

        if (otpVerificationToken != null) {
            if (otpVerificationToken.isVerified()) {
                return ResentOtpResult.builder()
                        .message("Forgot password form is already verified!")
                        .status("FORM_ALREADY_VERIFIED")
                        .build();
            } else {
                String newSmsOtp = otpService.generateOtp();
                String hashedNewSmsOtp = new BCryptPasswordEncoder().encode(newSmsOtp);
                otpVerificationTokenRepository.updateHashedSmsOtp(formId, hashedNewSmsOtp);
                log.info("New SMS Otp: {}", newSmsOtp);

                return ResentOtpResult.builder()
                        .message("SMS Otp resent!")
                        .status("NEW_SMS_OTP_RESENT")
                        .build();
            }
        } else {
            return ResentOtpResult.builder()
                    .message("Forgot password form not found!")
                    .status("FORM_NOT_FOUND")
                    .build();
        }
    }
}
