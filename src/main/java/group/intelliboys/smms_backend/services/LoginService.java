package group.intelliboys.smms_backend.services;

import group.intelliboys.smms_backend.configs.security.jwt.JwtService;
import group.intelliboys.smms_backend.models.dtos.UserAuthInfo;
import group.intelliboys.smms_backend.models.entities.Token;
import group.intelliboys.smms_backend.models.entities.TwoFactorAuthToken;
import group.intelliboys.smms_backend.models.entities.User;
import group.intelliboys.smms_backend.models.forms.LoginForm;
import group.intelliboys.smms_backend.models.results.LoginResult;
import group.intelliboys.smms_backend.models.results.ResentOtpResult;
import group.intelliboys.smms_backend.models.results.TwoFAVerificationResult;
import group.intelliboys.smms_backend.models.tokens.TwoFAVerificationToken;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@Transactional
public class LoginService {
    private static final Logger log = LoggerFactory.getLogger(LoginService.class);

    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserService userService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private OtpService otpService;
    @Autowired
    private TwoFactorAuthService twoFactorAuthService;

    public LoginResult doLogin(LoginForm loginForm) {
        String formId = UUID.randomUUID().toString();

        UserAuthInfo userAuthInfo = userService.getUserAuthInfoByEmail(loginForm.getEmail());

        if (userAuthInfo != null) {
            boolean isPasswordMatches = BCrypt.checkpw(loginForm.getPassword(), userAuthInfo.getPassword());

            if (isPasswordMatches) {
                if (userAuthInfo.is2faEnabled()) {
                    String emailOtp = otpService.generateOtp();
                    String smsOtp = otpService.generateOtp();

                    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                    String hashedEmailOtp = passwordEncoder.encode(emailOtp);
                    String hashedSmsOtp = passwordEncoder.encode(smsOtp);

                    log.info("Email Otp: {}", emailOtp);
                    log.info("SMS Otp: {}", smsOtp);

                    User user = userService.getUserReferenceByEmail(userAuthInfo.getEmail());

                    TwoFactorAuthToken twoFactorAuthToken = TwoFactorAuthToken.builder()
                            .formId(formId)
                            .hashedEmailOtp(hashedEmailOtp)
                            .hashedSmsOtp(hashedSmsOtp)
                            .deviceId(loginForm.getDeviceId())
                            .deviceName(loginForm.getDeviceName())
                            .status("UNVERIFIED")
                            .user(user)
                            .build();

                    twoFactorAuthService.saveTwoFactorAuthToken(twoFactorAuthToken);
                    otpService.sendEmailOtp(user.getEmail(), emailOtp);

                    return LoginResult.builder()
                            .formId(formId)
                            .message("Please Verify!")
                            .status("NEED_VERIFICATION")
                            .build();
                } else {
                    String jwt = jwtService.generateToken(userAuthInfo);
                    User userRef = userService.getUserReferenceByEmail(loginForm.getEmail());

                    Token token = Token.builder()
                            .value(jwt)
                            .deviceId(loginForm.getDeviceId())
                            .deviceName(loginForm.getDeviceName())
                            .user(userRef)
                            .build();

                    tokenService.saveToken(token);

                    return LoginResult.builder()
                            .formId(formId)
                            .token(jwt)
                            .message("Authentication Success!")
                            .status("AUTHENTICATION_SUCCESS")
                            .build();
                }
            } else {
                return LoginResult.builder()
                        .formId(formId)
                        .message("Wrong Password")
                        .status("AUTHENTICATION_FAILED")
                        .build();
            }
        } else {
            return LoginResult.builder()
                    .formId(formId)
                    .message("Email not found!")
                    .status("EMAIL_NOT_FOUND")
                    .build();
        }
    }

    public TwoFAVerificationResult doVerify(TwoFAVerificationToken twoFAVerificationToken) {
        TwoFactorAuthToken twoFactorAuthToken = twoFactorAuthService.getTwoFactorAuthTokenByFormId(twoFAVerificationToken.getFormId());

        if (twoFactorAuthToken != null) {
            if (twoFactorAuthToken.getStatus().equals("UNVERIFIED")) {
                if (twoFactorAuthToken.getAttempts() >= 0 && twoFactorAuthToken.getAttempts() < 3) {
                    String hashedEmailOtp = twoFactorAuthToken.getHashedEmailOtp();
                    String hashedSmsOtp = twoFactorAuthToken.getHashedSmsOtp();
                    String emailOtp = twoFAVerificationToken.getEmailOtp();
                    String smsOtp = twoFAVerificationToken.getSmsOtp();

                    boolean isEmailOtpMatches = false, isSmsOtpMatches = false;

                    PasswordEncoder encoder = new BCryptPasswordEncoder();

                    if (encoder.matches(emailOtp, hashedEmailOtp)) {
                        isEmailOtpMatches = true;
                    }

                    if (encoder.matches(smsOtp, hashedSmsOtp)) {
                        isSmsOtpMatches = true;
                    }

                    if (isEmailOtpMatches && isSmsOtpMatches) {
                        User user = twoFactorAuthToken.getUser();
                        UserAuthInfo userAuthInfo = userService.getUserAuthInfoByEmail(user.getEmail());

                        String jwt = jwtService.generateToken(userAuthInfo);
                        User userRef = userService.getUserReferenceByEmail(user.getEmail());

                        Token token = Token.builder()
                                .value(jwt)
                                .deviceId(twoFactorAuthToken.getDeviceId())
                                .deviceName(twoFactorAuthToken.getDeviceName())
                                .user(userRef)
                                .build();

                        tokenService.saveToken(token);
                        twoFactorAuthService.updateStatus("VERIFIED", twoFactorAuthToken.getFormId());

                        return TwoFAVerificationResult.builder()
                                .formId(twoFactorAuthToken.getFormId())
                                .status("VERIFIED")
                                .message("Verification Success!")
                                .token(jwt)
                                .isEmailOtpMatches(isEmailOtpMatches)
                                .isSmsOtpMatches(isSmsOtpMatches)
                                .build();
                    } else {
                        byte attempts = twoFactorAuthToken.getAttempts();
                        attempts++;
                        twoFactorAuthToken.setAttempts(attempts);
                        twoFactorAuthService.saveTwoFactorAuthToken(twoFactorAuthToken);

                        return TwoFAVerificationResult.builder()
                                .formId(twoFactorAuthToken.getFormId())
                                .status("WRONG_OTP")
                                .message("Wrong Otp!")
                                .isEmailOtpMatches(isEmailOtpMatches)
                                .isSmsOtpMatches(isSmsOtpMatches)
                                .build();
                    }
                } else {
                    // RESEND OTP
                    String newEmailOtp = otpService.generateOtp();
                    String newSmsOtp = otpService.generateOtp();

                    log.info("New Email OTP: {}", newEmailOtp);
                    log.info("New SMS OTP: {}", newSmsOtp);

                    PasswordEncoder encoder = new BCryptPasswordEncoder();

                    String newHashedEmailOtp = encoder.encode(newEmailOtp);
                    String newHashedSmsOtp = encoder.encode(newSmsOtp);

                    twoFactorAuthService.updateHashedEmailOtp(newHashedEmailOtp, twoFactorAuthToken.getFormId());
                    twoFactorAuthService.updateHashedSmsOtp(newHashedSmsOtp, twoFactorAuthToken.getFormId());
                    twoFactorAuthService.resetAttempts(twoFactorAuthToken.getFormId());

                    return TwoFAVerificationResult.builder()
                            .formId(twoFAVerificationToken.getFormId())
                            .status("ATTEMPT_LIMIT_EXCEEDS")
                            .message("New OTP Resent!")
                            .build();
                }
            } else if (twoFactorAuthToken.getStatus().equals("VERIFIED")) {
                return TwoFAVerificationResult.builder()
                        .formId(twoFAVerificationToken.getFormId())
                        .status("ALREADY_VERIFIED")
                        .message("2FA form already verified!")
                        .build();
            } else {
                return TwoFAVerificationResult.builder()
                        .formId(twoFAVerificationToken.getFormId())
                        .status("INVALID_STATUS")
                        .message("Invalid Status!")
                        .build();
            }
        } else {
            return TwoFAVerificationResult.builder()
                    .formId(twoFAVerificationToken.getFormId())
                    .status("NOT_EXISTS")
                    .message("2FA form does not exists!")
                    .build();
        }
    }

    public ResentOtpResult resendEmailOtp(String formId) {
        TwoFactorAuthToken twoFactorAuthToken = twoFactorAuthService.getTwoFactorAuthTokenByFormId(formId);

        if (twoFactorAuthToken != null) {
            User user = twoFactorAuthToken.getUser();
            String rawEmailOtp = otpService.generateOtp();

            PasswordEncoder encoder = new BCryptPasswordEncoder();
            String newHashedEmailOtp = encoder.encode(rawEmailOtp);

            twoFactorAuthService.updateHashedEmailOtp(newHashedEmailOtp, formId);
            otpService.sendEmailOtp(user.getEmail(), rawEmailOtp);

            log.info("New Email Otp: {}", rawEmailOtp);

            return ResentOtpResult.builder()
                    .status("NEW_EMAIL_OTP_RESENT_SUCCESSFULLY")
                    .message("New email otp has been resent!")
                    .build();
        } else {
            return ResentOtpResult.builder()
                    .status("2FA_VERIFICATION_FORM_NOT_EXISTS")
                    .message("2fa verification form not found!")
                    .build();
        }
    }

    public ResentOtpResult resendSmsOtp(String formId) {
        return null;
    }
}
