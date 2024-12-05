package group.intelliboys.smms_backend.controllers;

import group.intelliboys.smms_backend.models.entities.user.AccidentEntry;
import group.intelliboys.smms_backend.services.AccidentService;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(("/push/notification"))
public class AccidentController {
    @Autowired
    private AccidentService accidentService;

    @PostMapping("/accident")
    public ResponseEntity<String> sendAccidentOtp(@RequestBody AccidentEntry accidentEntry) throws MessagingException {
        //log.info(accidentEntry.toString());
        accidentService.sendEmailWithAttachments(accidentEntry);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/test")
    public String test() {
        return "Test Success!";
    }
}
