package es.samfc.gamebackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import es.samfc.gamebackend.model.auth.PlayerCredentials;

import java.util.UUID;

/**
 * Repositorio de credenciales de un jugador.
 */
public interface CredentialsRepository extends JpaRepository<PlayerCredentials, UUID> {
}
