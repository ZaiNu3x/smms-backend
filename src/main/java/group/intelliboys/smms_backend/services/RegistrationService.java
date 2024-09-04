package group.intelliboys.smms_backend.services;

import group.intelliboys.smms_backend.configs.security.jwt.JwtService;
import group.intelliboys.smms_backend.models.entities.RegistrationFormToken;
import group.intelliboys.smms_backend.models.entities.User;
import group.intelliboys.smms_backend.models.enums.Role;
import group.intelliboys.smms_backend.models.forms.RegistrationForm;
import group.intelliboys.smms_backend.models.forms.UserAuthForm;
import group.intelliboys.smms_backend.models.results.RegistrationResult;
import group.intelliboys.smms_backend.models.results.ResentOtpResult;
import group.intelliboys.smms_backend.models.results.TwoFAVerificationResult;
import group.intelliboys.smms_backend.models.tokens.TwoFAVerificationToken;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.UUID;

@Service
@Transactional
public class RegistrationService {
    private static final Logger log = LoggerFactory.getLogger(RegistrationService.class);

    @Autowired
    private RegistrationFormTokenService registrationFormTokenService;
    @Autowired
    private UserService userService;
    @Autowired
    private OtpService otpService;
    @Autowired
    private JwtService jwtService;

    public RegistrationResult saveRegistrationFormToken(RegistrationForm form) {
        boolean isEmailExists = userService.isEmailAlreadyRegistered(form.getEmail());
        boolean isPhoneNumberExists = userService.isPhoneNumberAlreadyRegistered(form.getPhoneNumber());

        if (!isEmailExists && !isPhoneNumberExists) {
            if (form.getConfirmPassword().equals(form.getPassword())) {
                PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

                String rawEmailOtp = otpService.generateOtp();
                String rawSmsOtp = otpService.generateOtp();
                String hashedEmailOtp = passwordEncoder.encode(rawEmailOtp);
                String hashedSmsOtp = passwordEncoder.encode(rawSmsOtp);

                RegistrationFormToken registrationFormToken = RegistrationFormToken.builder()
                        .id(form.getFormId())
                        .email(form.getEmail())
                        .password(passwordEncoder.encode(form.getPassword()))
                        .role(Role.ROLE_USER)
                        .lastName(form.getLastName())
                        .firstName(form.getFirstName())
                        .middleName(form.getMiddleName())
                        .sex(form.getSex())
                        .birthDate(form.getBirthDate())
                        .age((byte) Period.between(form.getBirthDate(), LocalDate.now()).getYears())
                        .address(form.getAddress())
                        .profilePic(form.getProfilePic())
                        .phoneNumber(form.getPhoneNumber())
                        .hashedEmailVerificationOtp(hashedEmailOtp)
                        .hashedSmsVerificationOtp(hashedSmsOtp)
                        .build();

                registrationFormTokenService.saveRegistrationFormToken(registrationFormToken);
                otpService.sendEmailOtp(form.getEmail(), rawEmailOtp);
                otpService.sendSmsOtp(form.getPhoneNumber(), rawSmsOtp);

                log.info("Email OTP: {}", rawEmailOtp);
                log.info("SMS OTP: {}", rawSmsOtp);

                return RegistrationResult.builder()
                        .formId(form.getFormId())
                        .message("Please Verify your registration!")
                        .status("NEED_VERIFICATION")
                        .build();

            } else {
                return RegistrationResult.builder()
                        .formId(form.getFormId())
                        .message("Password & Confirm Password is not same!")
                        .status("NOT_SAME_PASSWORD")
                        .build();
            }
        } else {
            return RegistrationResult.builder()
                    .formId(form.getFormId())
                    .message("email or phone number is exists!")
                    .status("NOT_SAME_PASSWORD")
                    .isEmailExists(isEmailExists)
                    .isPhoneNumberExists(isPhoneNumberExists)
                    .build();
        }
    }

    public RegistrationResult verifyIfAccountNotExists(UserAuthForm form) {
        String formId = UUID.randomUUID().toString();

        boolean isEmailAlreadyExists = userService.isEmailAlreadyRegistered(form.getEmail());
        boolean isPhoneNumberAlreadyExists = userService.isPhoneNumberAlreadyRegistered(form.getPhoneNumber());

        if (!isEmailAlreadyExists && !isPhoneNumberAlreadyExists) {
            return RegistrationResult.builder()
                    .formId(formId)
                    .message("Email and Phone number are not exists!")
                    .status("EMAIL_PHONE_NOT_NUMBER_EXISTS")
                    .build();

        } else {
            return RegistrationResult.builder()
                    .formId(formId)
                    .message("Email or Phone number are already exists!")
                    .status("EMAIL_PHONE_NUMBER_EXISTS")
                    .isEmailExists(isEmailAlreadyExists)
                    .isPhoneNumberExists(isPhoneNumberAlreadyExists)
                    .build();
        }
    }

    public TwoFAVerificationResult verifyRegistration(TwoFAVerificationToken token) {
        RegistrationFormToken formToken = registrationFormTokenService.getRegistrationFormTokenById(token.getFormId());

        if (formToken != null) {
            PasswordEncoder encoder = new BCryptPasswordEncoder();

            boolean isEmailOtpMatches = encoder.matches(token.getEmailOtp(), formToken.getHashedEmailVerificationOtp());
            boolean isSmsOtpMatches = encoder.matches(token.getSmsOtp(), formToken.getHashedSmsVerificationOtp());

            if (isEmailOtpMatches && isSmsOtpMatches) {
                User user = User.builder()
                        .email(formToken.getEmail())
                        .password(formToken.getPassword())
                        .role(formToken.getRole())
                        .is2faEnabled(false)
                        .lastName(formToken.getLastName())
                        .firstName(formToken.getFirstName())
                        .middleName(formToken.getMiddleName())
                        .sex(formToken.getSex())
                        .birthDate(formToken.getBirthDate())
                        .address(formToken.getAddress())
                        .profilePic(formToken.getProfilePic())
                        .phoneNumber(formToken.getPhoneNumber())
                        .build();

                userService.saveUser(user);

                registrationFormTokenService.deleteRegistrationFormById(formToken.getId());

                return TwoFAVerificationResult.builder()
                        .formId(formToken.getId())
                        .message("Registration Verified!")
                        .status("VERIFICATION_SUCCESS")
                        .isEmailOtpMatches(true)
                        .isSmsOtpMatches(true)
                        .build();
            } else {
                return TwoFAVerificationResult.builder()
                        .formId(formToken.getId())
                        .message("Email or SMS Otp did not matches!")
                        .status("VERIFICATION_FAILED")
                        .isEmailOtpMatches(isEmailOtpMatches)
                        .isSmsOtpMatches(isSmsOtpMatches)
                        .build();
            }
        } else {

            return TwoFAVerificationResult.builder()
                    .formId(token.getFormId())
                    .message("Registration Form not found!")
                    .status("REGISTRATION_FORM_NOT_FOUND")
                    .build();
        }
    }

    public ResentOtpResult resendEmailOtp(String formId) {
        RegistrationFormToken formToken = registrationFormTokenService.getRegistrationFormTokenById(formId);

        if (formToken != null) {
            if (!formToken.isVerified()) {
                String newRawEmailOtp = otpService.generateOtp();
                log.info("New Email OTP: {}", newRawEmailOtp);
                PasswordEncoder encoder = new BCryptPasswordEncoder();
                String newHashedEmailOtp = encoder.encode(newRawEmailOtp);
                registrationFormTokenService.updateHashedEmailOtp(newHashedEmailOtp, formId);
                otpService.sendEmailOtp(formToken.getEmail(), newRawEmailOtp);

                return ResentOtpResult.builder()
                        .message("New Email Otp has been sent!")
                        .status("NEW_EMAIL_OTP_SENT")
                        .build();
            } else {
                return ResentOtpResult.builder()
                        .message("Registration Form has been already verified!")
                        .status("REGISTRATION_FORM_ALREADY_VERIFIED")
                        .build();
            }

        } else {
            return ResentOtpResult.builder()
                    .message("No Registration Form has found!")
                    .status("REGISTRATION_FORM_NOT_FOUND")
                    .build();
        }
    }

    public ResentOtpResult resendSmsOtp(String formId) {
        RegistrationFormToken formToken = registrationFormTokenService.getRegistrationFormTokenById(formId);

        if (formToken != null) {
            if (!formToken.isVerified()) {
                String newRawSmsOtp = otpService.generateOtp();
                log.info("New Email OTP: {}", newRawSmsOtp);
                PasswordEncoder encoder = new BCryptPasswordEncoder();
                String newHashedSmsOtp = encoder.encode(newRawSmsOtp);
                registrationFormTokenService.updateHashedSmsOtp(newHashedSmsOtp, formId);
                otpService.sendSmsOtp(formToken.getEmail(), newRawSmsOtp);

                return ResentOtpResult.builder()
                        .message("New Email Otp has been sent!")
                        .status("NEW_EMAIL_OTP_SENT")
                        .build();
            } else {
                return ResentOtpResult.builder()
                        .message("Registration Form has been already verified!")
                        .status("REGISTRATION_FORM_ALREADY_VERIFIED")
                        .build();
            }

        } else {
            return ResentOtpResult.builder()
                    .message("No Registration Form has found!")
                    .status("REGISTRATION_FORM_NOT_FOUND")
                    .build();
        }
    }
}
