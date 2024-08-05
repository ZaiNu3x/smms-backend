package group.intelliboys.smms_backend.models.results;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class RegistrationResult {
    private final String formId;
    private final String message;
    private final String status;
    private boolean isEmailExists;
    private boolean isPhoneNumberExists;
}
