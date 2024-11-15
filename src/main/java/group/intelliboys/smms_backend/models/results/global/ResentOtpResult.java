package group.intelliboys.smms_backend.models.results.global;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ResentOtpResult {
    private final String status;
    private final String message;
}
