package group.intelliboys.smms_backend.services;

import group.intelliboys.smms_backend.configs.security.jwt.JwtService;
import group.intelliboys.smms_backend.models.dtos.UserAuthInfo;
import group.intelliboys.smms_backend.models.entities.Token;
import group.intelliboys.smms_backend.models.entities.User;
import group.intelliboys.smms_backend.models.forms.LoginForm;
import group.intelliboys.smms_backend.models.responses.LoginResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Transactional
public class LoginService {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserService userService;
    @Autowired
    private TokenService tokenService;

    public LoginResponse doLogin(LoginForm loginForm) {
        String formId = UUID.randomUUID().toString();

        UserAuthInfo userAuthInfo = userService.getUserAuthInfoByEmail(loginForm.getEmail());

        if (userAuthInfo != null) {
            boolean isPasswordMatches = BCrypt.checkpw(loginForm.getPassword(), userAuthInfo.getPassword());

            if (isPasswordMatches) {
                if (userAuthInfo.is2faEnabled()) {
                    return LoginResponse.builder()
                            .formId(formId)
                            .token(null)
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

                    return LoginResponse.builder()
                            .formId(formId)
                            .token(jwt)
                            .message("Authentication Success!")
                            .status("AUTHENTICATION_SUCCESS")
                            .build();
                }
            } else {
                return LoginResponse.builder()
                        .formId(formId)
                        .token(null)
                        .message("Wrong Password")
                        .status("AUTHENTICATION_FAILED")
                        .build();
            }
        } else {
            return LoginResponse.builder()
                    .formId(formId)
                    .token(null)
                    .message("Email not found!")
                    .status("EMAIL_NOT_FOUND")
                    .build();
        }
    }
}
