package es.samfc.gamebackend.security.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import es.samfc.gamebackend.model.player.Player;
import es.samfc.gamebackend.model.auth.PlayerCredentials;
import es.samfc.gamebackend.repository.CredentialsRepository;
import es.samfc.gamebackend.repository.PlayerRepository;

import java.util.ArrayList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final CredentialsRepository credentialsRepository;
    private final PlayerRepository playerRepository;

    public UserDetailsServiceImpl(CredentialsRepository credentialsRepository, PlayerRepository playerRepository) {
        this.credentialsRepository = credentialsRepository;
        this.playerRepository = playerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Player player = playerRepository.findByName((username));
        if (player == null) {
            throw new UsernameNotFoundException("User Not Found with username: " + username);
        }
        PlayerCredentials credentials = credentialsRepository.findById(player.getUniqueId()).orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
        return new User(player.getName(), credentials.getPassword(), new ArrayList<>());
    }
}