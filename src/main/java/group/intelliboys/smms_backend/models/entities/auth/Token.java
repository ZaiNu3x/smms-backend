package group.intelliboys.smms_backend.models.entities.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import group.intelliboys.smms_backend.models.entities.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "token")
public class Token {
    @Id
    @Column(length = 512)
    private String value;

    @Column(nullable = false, length = 32)
    private String deviceId;

    @Column(nullable = false, length = 32)
    private String deviceName;

    @Column(nullable = false)
    private boolean isBlacklisted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    @ToString.Exclude
    private User user;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false, updatable = false)
    private LocalDateTime expiresAt;

    public boolean isExpired() {
        return expiresAt.isBefore(LocalDateTime.now());
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        expiresAt = LocalDateTime.now().plusHours(1);
    }
}
