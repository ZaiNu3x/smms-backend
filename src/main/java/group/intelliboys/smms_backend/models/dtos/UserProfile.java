package group.intelliboys.smms_backend.models.dtos;

import java.time.LocalDate;

public interface UserProfile {
    String getEmail();

    String getPhoneNumber();

    String getLastName();

    String getFirstName();

    String getMiddleName();

    char getSex();

    LocalDate getBirthDate();

    byte getAge();

    String getAddress();

    byte[] getProfilePic();
}
