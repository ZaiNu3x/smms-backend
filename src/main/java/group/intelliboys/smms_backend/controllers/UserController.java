package group.intelliboys.smms_backend.controllers;

import group.intelliboys.smms_backend.models.dtos.UserProfile;
import group.intelliboys.smms_backend.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<UserProfile> getUserProfileInfo() {
        log.info("Executed!");
        UserProfile profile = userService.getUserProfileInfo();
        return new ResponseEntity<>(profile, HttpStatus.OK);
    }
}
