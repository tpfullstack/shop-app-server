package fr.fullstack.shopapp.repository.jpa;

import fr.fullstack.shopapp.model.SyncStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SyncStatusRepository extends JpaRepository<SyncStatus, Long> {
    boolean existsBySyncCompletedTrue();
}