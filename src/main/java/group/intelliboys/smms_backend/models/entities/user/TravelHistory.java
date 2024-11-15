package group.intelliboys.smms_backend.models.entities.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "travel_history")
public class TravelHistory {
    @Column(nullable = false)
    private long version;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    @ToString.Exclude
    private User user;

    @OneToMany(mappedBy = "travelHistory", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<TravelStatusUpdate> travelStatusUpdates;

    @Column(nullable = false)
    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @Column(nullable = false)
    private float startLatitude;

    @Column(nullable = false)
    private float startLongitude;

    @Column(nullable = false)
    private float startAltitude;

    private String startLocationName;

    private float endLatitude;

    private float endLongitude;

    private float endAltitude;

    private String endLocationName;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
