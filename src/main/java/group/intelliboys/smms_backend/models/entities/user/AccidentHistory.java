package group.intelliboys.smms_backend.models.entities.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "accident_history")
public class AccidentHistory {
    @Column(nullable = false)
    private long version;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "travel_status_update_id")
    @JsonIgnore
    @ToString.Exclude
    private TravelStatusUpdate travelStatusUpdate;

    private byte[] frontCameraSnap;

    private byte[] backCameraSnap;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
