package group.intelliboys.smms_backend.models.entities.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccidentEntry {
    private String accidentHistoryId;
    private float latitude;
    private float longitude;
    private byte[] frontCameraSnap;
    private byte[] backCameraSnap;
    private String email;
    private String message;
    private LocalDateTime createdAt;
}
