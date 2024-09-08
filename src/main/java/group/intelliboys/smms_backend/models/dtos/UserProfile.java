package group.intelliboys.smms_backend.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserProfile {
    private String email;
    private String phoneNumber;
    private String lastName;
    private String firstName;
    private String middleName;
    private char sex;
    private LocalDate birthDate;
    private byte age;
    private String address;
    private byte[] profilePic;
}
