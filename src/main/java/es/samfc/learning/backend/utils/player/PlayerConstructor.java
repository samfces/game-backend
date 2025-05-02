package es.samfc.learning.backend.utils.player;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import es.samfc.learning.backend.controller.payload.auth.RegisterRequest;
import es.samfc.learning.backend.model.auth.PlayerCredentials;
import es.samfc.learning.backend.model.economy.EconomyType;
import es.samfc.learning.backend.model.permission.BackendPermissionType;
import es.samfc.learning.backend.model.permission.BackendPermissions;
import es.samfc.learning.backend.model.player.Player;
import es.samfc.learning.backend.repository.CredentialsRepository;
import es.samfc.learning.backend.repository.PlayerRepository;
import es.samfc.learning.backend.security.encryption.Encoders;
import es.samfc.learning.backend.services.impl.EconomiesService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class PlayerConstructor {

    private final Encoders encoders;
    private final PlayerRepository playerRepository;
    private final CredentialsRepository credentialsRepository;
    private final EconomiesService economiesService;

    @Value("${app.permissions.default-permissions}")
    private String defaultPermissions;

    public PlayerConstructor(Encoders encoders, PlayerRepository playerRepository, CredentialsRepository credentialsRepository, EconomiesService economiesService) {
        this.encoders = encoders;
        this.playerRepository = playerRepository;
        this.credentialsRepository = credentialsRepository;
        this.economiesService = economiesService;
    }


    public boolean buildPlayer(RegisterRequest register) {


        Optional<UUID> uuid = PlayerUUIDFetcher.getUUID(register.getUsername());
        if (uuid.isEmpty()) {
            String uuidString = "OfflinePlayer:" + register.getUsername();
            uuid = Optional.of(UUID.nameUUIDFromBytes(uuidString.getBytes()));
        }

        //Setup Credentials
        PlayerCredentials credentials = new PlayerCredentials.Builder(uuid.get())
                .email(register.getEmail())
                .password(encoders.getPasswordEncoder().encode(register.getPassword()))
                .build();

        credentialsRepository.save(credentials);

        //Setup Permissions
        List<BackendPermissionType> defaultPermList = List.of(defaultPermissions.split(";")).stream().map(BackendPermissionType::valueOf).toList();
        BackendPermissions permissions = new BackendPermissions.Builder()
                .permissions(defaultPermList)
                .build();


        //Setup EconomyTypes
        List<EconomyType> economyTypes = economiesService.getEconomyTypes();

        //Setup Player
        Player player = new Player.Builder()
                .uniqueId(uuid.get())
                .name(register.getUsername())
                .backendPermissions(permissions)
                .economyTypes(economyTypes)
                .build();

        playerRepository.save(player);

        return true;
    }
}
