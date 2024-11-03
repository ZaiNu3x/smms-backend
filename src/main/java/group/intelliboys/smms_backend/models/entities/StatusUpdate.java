package group.intelliboys.smms_backend.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@Builder
@Entity
@Table(name = "status_update")
public class StatusUpdate {
    @Id
    @Column(length = 36)
    private String id;

    @Column(nullable = false)
    private float latitude;

    @Column(nullable = false)
    private float longitude;

    @Column(nullable = false)
    private float altitude;

    @Column(nullable = false)
    private float corneringAngle;

    @Column(nullable = false)
    private int speed;

    @Column(nullable = false)
    private String direction;

    private boolean isWearingHelmet;

    @ManyToOne
    @JoinColumn(name = "travel_history_id")
    @JsonIgnore
    @ToString.Exclude
    private TravelHistory travelHistory;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
