package group.intelliboys.smms_backend.models.tokens;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TwoFAVerificationToken {
    @NotBlank(message = "Form ID must not be null!")
    @Size(max = 36)
    private String formId;

    @NotBlank(message = "Email OTP must not be null!")
    @Size(max = 36)
    private String emailOtp;

    @NotBlank(message = "SMS OTP must not be null!")
    @Size(max = 36)
    private String smsOtp;
}
