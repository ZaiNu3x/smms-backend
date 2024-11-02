package group.intelliboys.smms_backend.models.entities;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@Entity
@Table(name = "travel_history")
public class TravelHistory {
    @Id
    @Column(length = 36)
    private String id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "travelHistory", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<StatusUpdate> statusUpdates;

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
