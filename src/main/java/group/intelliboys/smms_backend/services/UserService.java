package group.intelliboys.smms_backend.services;

import group.intelliboys.smms_backend.exceptions.EmailNotFoundException;
import group.intelliboys.smms_backend.models.dtos.UserAuthInfo;
import group.intelliboys.smms_backend.models.dtos.UserProfile;
import group.intelliboys.smms_backend.models.entities.OtpVerificationToken;
import group.intelliboys.smms_backend.models.entities.User;
import group.intelliboys.smms_backend.models.tokens.ChangePasswordToken;
import group.intelliboys.smms_backend.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@Transactional
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OtpService otpService;
    @Autowired
    private OtpVerificationTokenService otpVerificationTokenService;

    public UserAuthInfo getUserAuthInfoByEmail(String email) {
        return userRepository.getUserAuthInfoByEmail(email)
                .orElse(null);
    }

    public User getUserReferenceByEmail(String email) {
        return userRepository.getReferenceByEmail(email);
    }

    public boolean isEmailAlreadyRegistered(String email) {
        return userRepository.existsByEmail(email);
    }

    public Map<String, Object> isEmailAvailableForChangePassword(String email) {
        boolean emailExists = userRepository.existsByEmail(email);

        if (emailExists) {
            PasswordEncoder encoder = new BCryptPasswordEncoder();

            String id = UUID.randomUUID().toString();
            String rawEmailOtp = otpService.generateOtp();
            String rawSmsOtp = otpService.generateOtp();
            String hashedEmailOtp = encoder.encode(rawEmailOtp);
            String hashedSmsOtp = encoder.encode(rawSmsOtp);

            log.info("Email Otp {}", rawEmailOtp);
            log.info("SMS Otp {}", rawSmsOtp);

            OtpVerificationToken otpVerificationToken = OtpVerificationToken.builder()
                    .id(id)
                    .emailOtp(hashedEmailOtp)
                    .smsOtp(hashedSmsOtp)
                    .build();
            otpVerificationTokenService.saveOtpVerificationToken(otpVerificationToken);
            otpService.sendEmailOtp(email, rawEmailOtp);

            Map<String, Object> response = new HashMap<>();
            response.put("id", id);
            response.put("isExists", true);

            return response;
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("id", null);
            response.put("isExists", false);
            return response;
        }
    }

    public void verifyChangePasswordOtp(ChangePasswordToken token) {

    }

    public boolean isPhoneNumberAlreadyRegistered(String phoneNumber) {
        return userRepository.existsByPhoneNumber(phoneNumber);
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public void changeUserPassword(String email, String oldPassword, String newPassword) {
        String password = userRepository.findPasswordByEmail(email);

        if (password != null) {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

            if (passwordEncoder.matches(oldPassword, password)) {
                userRepository.updatePassword(passwordEncoder.encode(newPassword), email);
            }
        } else {
            throw new EmailNotFoundException(email + " not found!");
        }
    }

    public UserProfile getUserProfileInfo() {
        boolean isLoggedIn = SecurityContextHolder.getContext().getAuthentication().isAuthenticated();

        if (isLoggedIn) {
            String loggedInUser = SecurityContextHolder.getContext().getAuthentication().getName();

            UserProfile userProfile = userRepository.getUserProfileInfo(loggedInUser)
                    .orElse(null);

            return userProfile;
        } else {
            return null;
        }
    }
}
