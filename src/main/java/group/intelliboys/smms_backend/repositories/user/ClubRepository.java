package group.intelliboys.smms_backend.repositories.user;

import group.intelliboys.smms_backend.models.entities.user.Club;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClubRepository extends JpaRepository<Club, Long> {
}
