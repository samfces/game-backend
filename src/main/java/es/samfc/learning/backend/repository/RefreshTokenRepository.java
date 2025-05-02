package es.samfc.learning.backend.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import es.samfc.learning.backend.model.auth.RefreshToken;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {

    Optional<RefreshToken> findByUsername(String username);
    Optional<RefreshToken> findByToken(String token);

    @Transactional
    void deleteByUsername(String username);

}
