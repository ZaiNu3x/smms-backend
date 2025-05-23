package group.intelliboys.smms_backend.repositories.user;

import group.intelliboys.smms_backend.models.dtos.UserAuthInfo;
import group.intelliboys.smms_backend.models.dtos.UserProfile;
import group.intelliboys.smms_backend.models.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    @Query("SELECT new group.intelliboys.smms_backend.models.dtos.UserAuthInfo (u.email, u.password, u.role, u.is2faEnabled) FROM User u WHERE u.email = ?1")
    Optional<UserAuthInfo> getUserAuthInfoByEmail(String email);

    @Modifying
    @Query("UPDATE User u SET u.password = ?1 WHERE u.email = ?2")
    void updatePassword(String newHashedPassword, String email);

    String findPasswordByEmail(String email);

    Optional<UserProfile> findByEmail(String email);

    User getReferenceByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String email);
}
