package es.samfc.gamebackend.utils.player;

import es.samfc.gamebackend.controller.payload.auth.RegisterRequest;
import es.samfc.gamebackend.model.auth.PlayerCredentials;
import es.samfc.gamebackend.model.permission.BackendPermissionType;
import es.samfc.gamebackend.model.permission.BackendPermissions;
import es.samfc.gamebackend.model.player.Player;
import es.samfc.gamebackend.repository.CredentialsRepository;
import es.samfc.gamebackend.security.encryption.Encoders;
import es.samfc.gamebackend.services.impl.EconomiesService;
import es.samfc.gamebackend.services.impl.PlayerService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class PlayerConstructorTest {

    @Autowired private PlayerConstructor playerConstructor;
    @Autowired private PlayerService playerService;
    @Autowired private CredentialsRepository credentialsRepository;
    @Autowired private Encoders encoders;
    @Autowired private EconomiesService economiesService;

    @Value("${app.permissions.default-permissions}")
    private String defaultPermissions;

    @Test
    @DisplayName("Check if autowired dependencies are correctly injected")
    void contextLoads() {
        assertThat(playerConstructor).isNotNull();
        assertThat(playerService).isNotNull();
        assertThat(credentialsRepository).isNotNull();
        assertThat(encoders).isNotNull();
        assertThat(defaultPermissions).isEqualTo("VIEW_BALANCE;VIEW_OTHERS_BALANCE;VIEW_PERMISSIONS");
    }

    @Test
    @DisplayName("Check building with empty RegisterRequest")
    void buildWithEmptyRequest(){
        RegisterRequest request = new RegisterRequest();
        assertThat(playerConstructor.buildPlayer(request)).isFalse();
    }

    @Test
    @DisplayName("Check building with valid RegisterRequest")
    void buildPlayer() {

        //Remove possible existing test values
        Player test = playerService.getPlayer("Notch");
        if (test != null) {
            playerService.deletePlayer(test);
            PlayerCredentials creds = credentialsRepository.getReferenceById(test.getUniqueId());
            if (creds != null) credentialsRepository.delete(creds);
        }

        RegisterRequest request = new RegisterRequest("Notch", "notch@notch.com", "Password123_");
        assertThat(playerConstructor.buildPlayer(request)).isTrue();

        //Check if player was saved correctly
        Player player = playerService.getPlayer(request.getUsername());
        assertThat(player).isNotNull();

        //Check player credentials
        PlayerCredentials credentials = credentialsRepository.getReferenceById(player.getUniqueId());
        assertThat(credentials).isNotNull();
        assertThat(credentials.getEmail()).isEqualTo(request.getEmail());
        assertThat(
            encoders.getPasswordEncoder().matches(
                "Password123_",
                credentials.getPassword()
            )
        ).isTrue();

        //Check permissions
        BackendPermissions permissions = player.getBackendPermissions();
        assertThat(permissions).isNotNull();
        Stream.of(defaultPermissions.split(";")).map(BackendPermissionType::valueOf).forEach(permission ->
            assertThat(permission).isIn(permissions.getPermissionList())
        );

        //Check economy types
        economiesService.getEconomyTypes().forEach(type -> {
            assertThat(player.getEconomies().stream().anyMatch(value -> value.getType() == type)).isTrue();
        });


        //Cleanup test entities
        UUID id = player.getUniqueId();
        playerService.deletePlayer(player);
        assertThat(playerService.getPlayer(id)).isNull();

        credentialsRepository.delete(credentials);
        assertThat(credentialsRepository.existsById(id)).isFalse();
    }
}