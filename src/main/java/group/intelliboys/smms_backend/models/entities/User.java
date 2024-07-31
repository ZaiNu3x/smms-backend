package group.intelliboys.smms_backend.models.entities;

import group.intelliboys.smms_backend.models.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "user")
public class User {
    @Id
    @Column(length = 64)
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

    @Column(length = 4096)
    private byte[] profilePic;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY,
            cascade = CascadeType.REMOVE)
    private List<Token> tokens = new ArrayList<>();

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PostPersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PostUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
