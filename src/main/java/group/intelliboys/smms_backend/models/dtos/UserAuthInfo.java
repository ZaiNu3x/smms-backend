package group.intelliboys.smms_backend.models.dtos;

import group.intelliboys.smms_backend.models.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAuthInfo {
    private String email;
    private String password;
    private Role role;
    private boolean is2faEnabled;
}
