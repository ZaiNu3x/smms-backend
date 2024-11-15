package group.intelliboys.smms_backend.controllers;

import group.intelliboys.smms_backend.models.results.forgot_password.ForgotPasswordResult;
import group.intelliboys.smms_backend.models.results.global.OtpVerificationResult;
import group.intelliboys.smms_backend.models.results.global.ResentOtpResult;
import group.intelliboys.smms_backend.models.tokens.ChangePasswordToken;
import group.intelliboys.smms_backend.models.tokens.ForgotPasswordToken;
import group.intelliboys.smms_backend.services.OtpVerificationTokenService;
import group.intelliboys.smms_backend.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/forgot-password")
public class ForgotPasswordController {
    private UserService userService;
    private OtpVerificationTokenService otpVerificationTokenService;

    public ForgotPasswordController(UserService userService, OtpVerificationTokenService otpVerificationTokenService) {
        this.userService = userService;
        this.otpVerificationTokenService = otpVerificationTokenService;
    }

    @GetMapping("/search-account/{email}")
    public ResponseEntity<Map<String, Object>> searchAccount(@PathVariable String email) {
        Map<String, Object> isExists = userService.isEmailAvailableForChangePassword(email);
        return new ResponseEntity<>(isExists, HttpStatus.OK);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<OtpVerificationResult> verifyOtp(@RequestBody ChangePasswordToken token) {
        log.info(token.toString());
        OtpVerificationResult result = otpVerificationTokenService.findOtpVerificationTokenById(token);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/submit")
    public ResponseEntity<ForgotPasswordResult> submitNewPassword(@RequestBody ForgotPasswordToken token) {
        log.info(token.toString());
        ForgotPasswordResult result = userService.forgotUserPassword(token);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/resend/email-otp/{formId}")
    public ResponseEntity<ResentOtpResult> resendEmailOtp(@PathVariable("formId") String formId) {
        ResentOtpResult result = otpVerificationTokenService.resendEmailOtp(formId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/resend/sms-otp/{formId}")
    public ResponseEntity<ResentOtpResult> resendSmsOtp(@PathVariable("formId") String formId) {
        ResentOtpResult result = otpVerificationTokenService.resendSmsOtp(formId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
