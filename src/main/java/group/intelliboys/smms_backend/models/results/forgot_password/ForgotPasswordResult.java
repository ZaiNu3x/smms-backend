package group.intelliboys.smms_backend.models.results.forgot_password;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ForgotPasswordResult {
    private String message;
    private String status;
}
