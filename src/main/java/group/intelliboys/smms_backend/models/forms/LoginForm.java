package group.intelliboys.smms_backend.models.forms;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginForm {
    @NotBlank(message = "Email should not blank!")
    @Size(max = 64, min = 8)
    private String email;
    @NotBlank(message = "Password should not blank!")
    @Size(max = 32, min = 8)
    private String password;
    @NotBlank(message = "Device ID should not blank!")
    @Size(max = 32, min = 8)
    private String deviceId;
    @NotBlank(message = "Device Name should not blank!")
    @Size(max = 32, min = 8)
    private String deviceName;
}
