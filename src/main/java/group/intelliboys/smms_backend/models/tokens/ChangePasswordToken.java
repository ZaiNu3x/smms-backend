package group.intelliboys.smms_backend.models.tokens;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChangePasswordToken {
    private String id;
    private String emailOtp;
    private String smsOtp;
}
