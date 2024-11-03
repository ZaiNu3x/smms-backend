package group.intelliboys.smms_backend.controllers;

import group.intelliboys.smms_backend.models.dtos.AccountVersion;
import group.intelliboys.smms_backend.models.dtos.UserProfile;
import group.intelliboys.smms_backend.models.entities.User;
import group.intelliboys.smms_backend.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/version")
    public ResponseEntity<?> getAccountVersion() {
        AccountVersion accountVersion = userService.getAccountVersion();
        return new ResponseEntity<>(accountVersion, HttpStatus.OK);
    }

    @PostMapping("/sync")
    public ResponseEntity<?> synchronizedUserData(@RequestBody User user) {
        log.info(user.toString());
        User result = userService.synchronizedUserData(user);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
