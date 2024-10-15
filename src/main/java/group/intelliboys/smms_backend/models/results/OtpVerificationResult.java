package group.intelliboys.smms_backend.models.results;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OtpVerificationResult {
    private String id;
    private String message;
    private boolean isEmailSame;
    private boolean isSmsOtpSame;
}
