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
@Table(name = "status_update")
public class TravelStatusUpdate {
    @Column(nullable = false)
    private long version;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private float latitude;

    @Column(nullable = false)
    private float longitude;

    @Column(nullable = false)
    private float altitude;

    @Column(nullable = false)
    private float drivingAngle;

    @Column(nullable = false)
    private int speedInKmH;

    @Column(nullable = false)
    private String direction;

    private boolean isWearingHelmet;

    @ManyToOne
    @JoinColumn(name = "travel_history_id")
    @JsonIgnore
    @ToString.Exclude
    private TravelHistory travelHistory;

    @OneToOne(mappedBy = "travelStatusUpdate", fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private AccidentHistory accidentHistory;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
