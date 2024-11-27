package group.intelliboys.smms_backend.models.entities.user;

import group.intelliboys.smms_backend.models.entities.auth.Token;
import group.intelliboys.smms_backend.models.entities.auth.TwoFactorAuthToken;
import group.intelliboys.smms_backend.models.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "user")
@DynamicUpdate
public class User {
    @Column(nullable = false)
    private long version;

    @Id
    @Column(length = 64)
    private String email;

    @Column(nullable = false, length = 72)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Role role;

    @Column(nullable = false, length = 13)
    private String phoneNumber;

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

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Token> tokens = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TwoFactorAuthToken> twoFactorAuthTokens = new ArrayList<>();

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        age = (byte) Period.between(birthDate, LocalDate.now()).getYears();
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        age = (byte) Period.between(birthDate, LocalDate.now()).getYears();
        updatedAt = LocalDateTime.now();
    }
}
