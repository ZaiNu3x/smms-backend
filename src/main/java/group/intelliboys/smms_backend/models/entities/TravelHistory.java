package group.intelliboys.smms_backend.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "travel_history")
public class TravelHistory {
    @Id
    @Column(length = 36)
    private String id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    @ToString.Exclude
    private User user;

    @OneToMany(mappedBy = "travelHistory", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
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
