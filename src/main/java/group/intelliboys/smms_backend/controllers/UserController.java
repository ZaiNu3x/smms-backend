package group.intelliboys.smms_backend.controllers;

import group.intelliboys.smms_backend.models.dtos.UserProfile;
import group.intelliboys.smms_backend.models.dtos.UserProfileImpl;
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
        UserProfile profile = userService.getUserProfileInfo();
        return new ResponseEntity<>(profile, HttpStatus.OK);
    }

    @PutMapping("/profile/update")
    public ResponseEntity<?> updateUserProfileInfo(@RequestBody UserProfileImpl profile) {
        int result = userService.updateUserProfile(profile);

        if (result == 1) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
    }
}
