package group.intelliboys.smms_backend.models.results.global;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class TwoFAVerificationResult {
    private final String formId;
    private final String message;
    private final String status;
    private final String token;
    private final boolean isEmailOtpMatches;
    private final boolean isSmsOtpMatches;
}
