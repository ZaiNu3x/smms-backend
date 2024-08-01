package group.intelliboys.smms_backend.controllers;

import group.intelliboys.smms_backend.models.forms.LoginForm;
import group.intelliboys.smms_backend.models.results.LoginResult;
import group.intelliboys.smms_backend.models.results.ResentOtpResult;
import group.intelliboys.smms_backend.models.results.TwoFAVerificationResult;
import group.intelliboys.smms_backend.models.tokens.TwoFAVerificationToken;
import group.intelliboys.smms_backend.services.LoginService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private LoginService loginService;

    @PostMapping("")
    public ResponseEntity<LoginResult> doLogin(@RequestBody @Valid LoginForm loginForm) {
        LoginResult loginResult = loginService.doLogin(loginForm);
        return new ResponseEntity<>(loginResult, HttpStatus.OK);
    }

    @PostMapping("/2fa/verify")
    public ResponseEntity<TwoFAVerificationResult> do2faVerification(@RequestBody @Valid TwoFAVerificationToken twoFAVerificationToken) {
        TwoFAVerificationResult twoFAVerificationResult = loginService.doVerify(twoFAVerificationToken);
        return new ResponseEntity<>(twoFAVerificationResult, HttpStatus.OK);
    }

    @PutMapping("/2fa/resend/email-otp/{formId}")
    public ResponseEntity<ResentOtpResult> resendEmailOtp(@PathVariable("formId") String formId) {
        ResentOtpResult resentOtpResult = loginService.resendEmailOtp(formId);
        return new ResponseEntity<>(resentOtpResult, HttpStatus.OK);
    }

    @PutMapping("/2fa/resend/sms-otp/{formId}")
    public ResponseEntity<ResentOtpResult> resendSmsOtp(@PathVariable("formId") String formId) {
        ResentOtpResult resentOtpResult = loginService.resendSmsOtp(formId);
        return new ResponseEntity<>(resentOtpResult, HttpStatus.OK);
    }
}
