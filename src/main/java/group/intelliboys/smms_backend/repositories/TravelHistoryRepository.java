package group.intelliboys.smms_backend.repositories;

import group.intelliboys.smms_backend.models.entities.TravelHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TravelHistoryRepository extends JpaRepository<TravelHistory, String> {
}
