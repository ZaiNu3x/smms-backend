package group.intelliboys.smms_backend.models.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class LoginResponse {
    private final String formId;
    private final String message;
    private final String status;
    private final String token;
}
