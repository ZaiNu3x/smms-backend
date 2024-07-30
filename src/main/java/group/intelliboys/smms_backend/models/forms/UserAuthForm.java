package group.intelliboys.smms_backend.models.forms;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class UserAuthForm {
    private final String email;
    private final String phoneNumber;
}
