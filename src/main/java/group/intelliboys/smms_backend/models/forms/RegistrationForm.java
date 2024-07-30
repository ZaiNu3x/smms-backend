package group.intelliboys.smms_backend.models.forms;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegistrationForm {
    private String formId;
    private String email;
    private String password;
    private String confirmPassword;
    private String phoneNumber;
    private String lastName;
    private String firstName;
    private String middleName;
    private char sex;
    private LocalDate birthDate;
    private String address;
    private byte[] profilePic;
}
