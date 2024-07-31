package group.intelliboys.smms_backend.controllers;

import group.intelliboys.smms_backend.models.forms.LoginForm;
import group.intelliboys.smms_backend.models.responses.LoginResponse;
import group.intelliboys.smms_backend.services.LoginService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private LoginService loginService;

    @PostMapping("")
    public ResponseEntity<LoginResponse> doLogin(@RequestBody @Valid LoginForm loginForm) {
        LoginResponse loginResponse = loginService.doLogin(loginForm);
        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
    }
}
