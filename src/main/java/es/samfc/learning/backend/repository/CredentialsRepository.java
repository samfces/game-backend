package es.samfc.learning.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import es.samfc.learning.backend.model.auth.PlayerCredentials;

import java.util.UUID;

public interface CredentialsRepository extends JpaRepository<PlayerCredentials, UUID> {
}
