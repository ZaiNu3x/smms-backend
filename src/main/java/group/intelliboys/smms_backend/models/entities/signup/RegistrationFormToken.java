package group.intelliboys.smms_backend.models.entities.signup;

import group.intelliboys.smms_backend.models.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "registration_form_token",
        indexes = {@Index(name = "idx_email", columnList = "email")})
public class RegistrationFormToken {
    @Id
    @Column(length = 36)
    private String id;

    @Column(nullable = false, length = 64)
    private String email;

    @Column(nullable = false, length = 72)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Role role;

    private boolean is2faEnabled;

    @Column(nullable = false, length = 32)
    private String lastName;

    @Column(nullable = false, length = 32)
    private String firstName;

    @Column(length = 32)
    private String middleName;

    @Column(nullable = false)
    private char sex;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Column(nullable = false)
    private byte age;

    @Column(nullable = false)
    private String address;

    @Column(length = 2_048_000)
    private byte[] profilePic;

    @Column(nullable = false, length = 13)
    private String phoneNumber;

    @Column(nullable = false, length = 72)
    private String hashedEmailVerificationOtp;

    @Column(nullable = false, length = 72)
    private String hashedSmsVerificationOtp;

    @Column(nullable = false)
    private boolean isVerified;

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
