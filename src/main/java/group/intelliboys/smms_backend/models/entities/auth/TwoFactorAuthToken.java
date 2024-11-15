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
@Table(name = "two_factor_auth_token")
public class TwoFactorAuthToken {
    @Id
    @Column(length = 36)
    private String formId;

    @Column(nullable = false, length = 72)
    private String hashedEmailOtp;

    @Column(nullable = false, length = 72)
    private String hashedSmsOtp;

    @Column(nullable = false, length = 32)
    private String deviceId;

    @Column(nullable = false, length = 16)
    private String deviceName;

    @Column(nullable = false, length = 10)
    private String status;

    @Column(nullable = false)
    private byte attempts;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    @ToString.Exclude
    private User user;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
