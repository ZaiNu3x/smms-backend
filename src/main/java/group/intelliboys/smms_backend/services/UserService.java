package group.intelliboys.smms_backend.services;

import group.intelliboys.smms_backend.models.dtos.UserAuthInfo;
import group.intelliboys.smms_backend.models.dtos.UserProfile;
import group.intelliboys.smms_backend.models.entities.OtpVerificationToken;
import group.intelliboys.smms_backend.models.entities.user.User;
import group.intelliboys.smms_backend.models.results.forgot_password.ForgotPasswordResult;
import group.intelliboys.smms_backend.models.tokens.ForgotPasswordToken;
import group.intelliboys.smms_backend.repositories.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
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
                    .email(email)
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

    public boolean isPhoneNumberAlreadyRegistered(String phoneNumber) {
        return userRepository.existsByPhoneNumber(phoneNumber);
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public ForgotPasswordResult forgotUserPassword(ForgotPasswordToken token) {
        log.info(token.toString());

        OtpVerificationToken otpVerificationToken = otpVerificationTokenService
                .findOtpVerificationTokenById(token.getFormId());

        if (otpVerificationToken != null) {
            User user = userRepository.findById(otpVerificationToken.getEmail())
                    .orElse(null);

            if (user != null) {
                String email = user.getEmail();
                String newPassword = token.getNewPassword();
                String confirmNewPassword = token.getConfirmPassword();

                if (confirmNewPassword.equals(newPassword)) {
                    PasswordEncoder encoder = new BCryptPasswordEncoder();
                    String password = encoder.encode(newPassword);
                    userRepository.updatePassword(password, email);

                    return ForgotPasswordResult.builder()
                            .message("Password Changed successful!")
                            .status("CHANGE_PASSWORD_SUCCESS")
                            .build();
                }

            } else {
                return ForgotPasswordResult.builder()
                        .message("User not found!")
                        .status("CHANGE_PASSWORD_FAILED")
                        .build();
            }

        }
        return null;
    }

    public UserProfile getUserProfileInfo() {
        boolean isLoggedIn = SecurityContextHolder.getContext().getAuthentication().isAuthenticated();

        if (isLoggedIn) {
            String loggedInUser = SecurityContextHolder.getContext().getAuthentication().getName();

            return userRepository.findByEmail(loggedInUser)
                    .orElse(null);
        } else {
            return null;
        }
    }

    @Modifying
    public int updateUserProfile(UserProfile profile) {
        User user = userRepository.getReferenceByEmail(profile.getEmail());

        if (profile.getLastName() != null) {
            user.setLastName(profile.getLastName());
        }

        if (profile.getFirstName() != null) {
            user.setFirstName(profile.getFirstName());
        }

        if (profile.getMiddleName() != null) {
            user.setMiddleName(profile.getMiddleName());
        }

        if (profile.getSex() != '\0') {
            user.setSex(profile.getSex());
        }

        if (profile.getBirthDate() != null) {
            user.setBirthDate(profile.getBirthDate());
        }

        if (profile.getAddress() != null) {
            user.setAddress(profile.getAddress());
        }

        if (profile.getProfilePic() != null) {
            user.setProfilePic(profile.getProfilePic());
        }

        userRepository.save(user);
        return 1;
    }
}
