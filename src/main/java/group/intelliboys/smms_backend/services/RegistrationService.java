package group.intelliboys.smms_backend.services;

import group.intelliboys.smms_backend.models.entities.RegistrationFormToken;
import group.intelliboys.smms_backend.models.enums.Role;
import group.intelliboys.smms_backend.models.forms.RegistrationForm;
import group.intelliboys.smms_backend.models.results.RegistrationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.UUID;

@Service
public class RegistrationService {
    @Autowired
    private RegistrationFormTokenService registrationFormTokenService;
    @Autowired
    private UserService userService;
    @Autowired
    private OtpService otpService;

    public RegistrationResult saveRegistrationFormToken(RegistrationForm form) {
        String generatedId = UUID.randomUUID().toString();

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
                        .id(generatedId)
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
                        .hashedEmailVerificationOtp(hashedEmailOtp)
                        .hashedPhoneNumberVerificationOtp(hashedSmsOtp)
                        .build();

                registrationFormTokenService.saveRegistrationFormToken(registrationFormToken);
                otpService.sendEmailOtp(form.getEmail(), rawEmailOtp);
                otpService.sendSmsOtp(form.getPhoneNumber(), rawSmsOtp);

                return RegistrationResult.builder()
                        .formId(generatedId)
                        .message("Please Verify your registration!")
                        .status("NEED_VERIFICATION")
                        .build();

            } else {
                return RegistrationResult.builder()
                        .formId(generatedId)
                        .message("Password & Confirm Password is not same!")
                        .status("NOT_SAME_PASSWORD")
                        .build();
            }
        } else {
            return RegistrationResult.builder()
                    .formId(generatedId)
                    .message("email or phone number is exists!")
                    .status("NOT_SAME_PASSWORD")
                    .isEmailExists(isEmailExists)
                    .isPhoneNumberExists(isPhoneNumberExists)
                    .build();
        }
    }

    public RegistrationResult verifyIfAccountNotExists(RegistrationForm form) {
        boolean isEmailAlreadyExists = userService.isEmailAlreadyRegistered(form.getEmail());
        boolean isPhoneNumberAlreadyExists = userService.isPhoneNumberAlreadyRegistered(form.getPhoneNumber());

        if (isEmailAlreadyExists && isPhoneNumberAlreadyExists) {
            return RegistrationResult.builder()
                    .message("Email & Phone number is already exists!")
                    .status("EMAIL_PHONE_NUMBER_EXISTS")
                    .isEmailExists(true)
                    .isPhoneNumberExists(true)
                    .build();
        } else {
            return RegistrationResult.builder()
                    .message("Email & Phone number is not exists!")
                    .status("EMAIL_PHONE_NOT_NUMBER_EXISTS")
                    .isEmailExists(isEmailAlreadyExists)
                    .isPhoneNumberExists(isPhoneNumberAlreadyExists)
                    .build();
        }
    }
}
