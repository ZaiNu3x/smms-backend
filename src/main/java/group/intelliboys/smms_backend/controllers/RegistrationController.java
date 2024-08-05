package group.intelliboys.smms_backend.controllers;

import group.intelliboys.smms_backend.models.forms.RegistrationForm;
import group.intelliboys.smms_backend.models.forms.UserAuthForm;
import group.intelliboys.smms_backend.models.results.RegistrationResult;
import group.intelliboys.smms_backend.models.results.TwoFAVerificationResult;
import group.intelliboys.smms_backend.models.tokens.TwoFAVerificationToken;
import group.intelliboys.smms_backend.services.RegistrationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/register")
public class RegistrationController {
    @Autowired
    private RegistrationService registrationService;

    /**
     * This Endpoint URL will check the database if the
     * entered account email and phone number is already existed
     * in the system.
     **/
    @PostMapping("/is-account-exists")
    public ResponseEntity<RegistrationResult> isAccountExists(@RequestBody @Valid UserAuthForm form) {
        RegistrationResult result = registrationService.verifyIfAccountNotExists(form);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * This Endpoint URL will try to save the user registration form in database
     **/
    @PostMapping("/submit")
    public ResponseEntity<RegistrationResult> submit(@RequestBody @Valid RegistrationForm form) {
        RegistrationResult result = registrationService.saveRegistrationFormToken(form);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/verify")
    public ResponseEntity<TwoFAVerificationResult> verifyRegistration(@RequestBody @Valid TwoFAVerificationToken token) {
        TwoFAVerificationResult result = registrationService.verifyRegistration(token);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
