package group.intelliboys.smms_backend.models.dtos;

import java.time.LocalDate;

public interface UserProfile {
    long getVersion();

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

    // List<TravelHistory> getTravelHistories();

    // List<MonitoringWhitelist> getMonitoringWhitelists();

    // List<SearchHistory> getSearchHistories();

    // List<Club> getClubs();

    // Settings getSettings();
}
