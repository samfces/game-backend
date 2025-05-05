package es.samfc.gamebackend.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import es.samfc.gamebackend.model.auth.RefreshToken;

import java.util.Optional;
import java.util.UUID;

/**
 * Repositorio de tokens de refresco.
 * Los tokens de refresco son utilizados para validar la sesión de un jugador.
 */
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {

    Optional<RefreshToken> findByUsername(String username);
    Optional<RefreshToken> findByToken(String token);

    @Transactional
    void deleteByUsername(String username);

}
