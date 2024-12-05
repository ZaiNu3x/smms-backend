package group.intelliboys.smms_backend.services;

import group.intelliboys.smms_backend.models.entities.user.AccidentEntry;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AccidentService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendEmailWithAttachments(AccidentEntry accidentEntry) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(accidentEntry.getEmail());
        helper.setSubject("Accident Detected!");
        helper.setText("Latitude: " + accidentEntry.getLatitude() + "\n" +
                "Longitude: " + accidentEntry.getLongitude() + "\n" +
                "Link: https://map.project-osrm.org/?z=15&center=" + accidentEntry.getLatitude() + "%2C" + accidentEntry.getLongitude() +
                "&loc=" + accidentEntry.getLatitude() + "%2C" + accidentEntry.getLongitude() + "&hl=en&alt=0&srv=1\n" +
                "Date: " + accidentEntry.getCreatedAt().toString(), true);

        // Attach the first image
        ByteArrayResource resource1 = new ByteArrayResource(accidentEntry.getFrontCameraSnap());
        helper.addAttachment("frontCameraSnap.jpg", resource1);

        // Attach the second image
        ByteArrayResource resource2 = new ByteArrayResource(accidentEntry.getBackCameraSnap());
        helper.addAttachment("backCameraSnap.jpg", resource2);

        // Send the email
        mailSender.send(message);
        log.info("Accident Successfully sent!");
    }
}
