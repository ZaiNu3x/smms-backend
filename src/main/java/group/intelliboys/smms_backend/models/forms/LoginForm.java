package group.intelliboys.smms_backend.models.forms;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginForm {
    private String email;
    private String password;
    private String deviceId;
    private String deviceName;
}
