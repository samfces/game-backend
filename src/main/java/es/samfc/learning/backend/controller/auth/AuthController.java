package es.samfc.learning.backend.controller.auth;

import es.samfc.learning.backend.controller.payload.MessageResponse;
import es.samfc.learning.backend.controller.payload.auth.*;
import es.samfc.learning.backend.model.auth.LoginData;
import es.samfc.learning.backend.model.auth.PlayerCredentials;
import es.samfc.learning.backend.model.auth.RefreshToken;
import es.samfc.learning.backend.model.player.Player;
import es.samfc.learning.backend.repository.CredentialsRepository;
import es.samfc.learning.backend.repository.PlayerRepository;
import es.samfc.learning.backend.repository.RefreshTokenRepository;
import es.samfc.learning.backend.security.encryption.Encoders;
import es.samfc.learning.backend.security.jwt.JwtTokenUtil;
import es.samfc.learning.backend.security.service.UserDetailsServiceImpl;
import es.samfc.learning.backend.services.impl.PlayerService;
import es.samfc.learning.backend.utils.password.PasswordChecker;
import es.samfc.learning.backend.utils.player.PlayerConstructor;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

/**
 * Controlador de autenticación
 * Controlador que maneja las peticiones de autenticación y registro de usuarios.
 */

@RestController
public class AuthController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    private final Encoders encoders;
    private final UserDetailsServiceImpl userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final PlayerConstructor playerConstructor;
    private final PlayerService playerService;

    //Repositories
    private final PlayerRepository playerRepository;
    private final CredentialsRepository credentialsRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${app.refresh-token.expiration-ms}") private int refreshTokenExpirationMs;
    @Value("${app.jwt.expiration-ms}") private int jwtExpirationMs;
    @Value("${app.password.min-strength}") private int passwordMinStrength;

    /**
     * Constructor de la clase
     * Constructor que inicializa los servicios de autenticación y registro de usuarios.
     *
     * @param encoders Objeto de codificación de contraseñas
     * @param userDetailsService Servicio de usuarios
     * @param authenticationManager Gestor de autenticación
     * @param playerConstructor Constructor de jugadores
     * @param playerConstructor Constructor de jugadores
     * @param playerRepository Repositorio de jugadores
     * @param credentialsRepository Repositorio de credenciales
     * @param jwtTokenUtil Utilidades de token JWT
     * @param refreshTokenRepository Repositorio de tokens de refresco
     */
    public AuthController(Encoders encoders,
                          UserDetailsServiceImpl userDetailsService,
                          AuthenticationManager authenticationManager,
                          PlayerConstructor playerConstructor,
                          PlayerService playerService,
                          PlayerRepository playerRepository,
                          CredentialsRepository credentialsRepository,
                          JwtTokenUtil jwtTokenUtil,
                          RefreshTokenRepository refreshTokenRepository) {
        this.encoders = encoders;
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
        this.playerConstructor = playerConstructor;
        this.playerService = playerService;
        this.playerRepository = playerRepository;
        this.credentialsRepository = credentialsRepository;
        this.jwtTokenUtil = jwtTokenUtil;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    /**
     * Método POST para iniciar sesión
     * Método POST que inicia la sesión del usuario con el token JWT.
     *
     * @param login Objeto de solicitud de inicio de sesión
     * @return Objeto de respuesta con el mensaje de inicio de sesión
     */
    @ApiResponse(responseCode = "200", description = "Login correcto")
    @ApiResponse(responseCode = "400", description = "Datos incorrectos")
    @ApiResponse(responseCode = "401", description = "No autenticado")
    @PostMapping("/api/v1/auth/login")
    public ResponseEntity<MessageResponse> login(
            @RequestBody
            @Parameter(description = "Cuerpo de la solicitud en el que se incluye el nombre de usuario y la contraseña", required = true)
            LoginRequest login,
            HttpServletRequest request
    ) {
        LOGGER.info("POST /api/v1/auth/login");
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtTokenUtil.generateJwtToken(authentication);

        refreshTokenRepository.deleteByUsername(login.getUsername());

        RefreshToken refreshToken = RefreshToken.generate(login.getUsername(), new Date(System.currentTimeMillis() + refreshTokenExpirationMs));
        refreshTokenRepository.save(refreshToken);

        Player player = playerService.getPlayer(SecurityContextHolder.getContext().getAuthentication().getName());
        if (player != null) {
            LoginData loginData = new LoginData.Builder()
                    .host(request.getRemoteHost())
                    .timestamp(new Date())
                    .player(player)
                    .build();
            player.getLoginDatas().add(loginData);
            playerService.savePlayer(player);
        }



        return ResponseEntity.ok(new MessageResponse.Builder()
                .status(HttpStatus.OK)
                .payload("path", "/api/v1/auth/login")
                .payload("message", "Sesión iniciada correctamente")
                .payload("data", Map.of(
                    "token", jwt,
                    "tokenExpiration", new Date(System.currentTimeMillis() + jwtExpirationMs),
                    "refreshToken", Map.of(
                            "token", refreshToken.getToken(),
                            "expiration", new Date(System.currentTimeMillis() + refreshTokenExpirationMs)
                        )
                ))
                .build()
        );
    }

    @ApiResponse(responseCode = "200", description = "Token de refresco actualizado correctamente")
    @ApiResponse(responseCode = "400", description = "Token de refresco no válido")
    @ApiResponse(responseCode = "401", description = "No autenticado")
    @PostMapping("/api/v1/auth/refresh")
    public ResponseEntity<MessageResponse> refresh(
            @RequestBody
            @Parameter(description = "Cuerpo de la solicitud en el que se incluye el token de refresco", required = true)
            RefreshRequest refresh
    ) {
        LOGGER.info("POST /api/v1/auth/refresh");
        Optional<RefreshToken> refreshTokenOptional = refreshTokenRepository.findByToken(refresh.getRefreshToken());
        if (refreshTokenOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    new MessageResponse.Builder()
                            .status(HttpStatus.UNAUTHORIZED)
                            .payload("path", "/api/v1/auth/refresh")
                            .payload("message", "No existe un token de refresco para el usuario")
                            .build()
            );
        }

        RefreshToken refreshToken = refreshTokenOptional.get();
        if (refreshToken.getExpirationDate().before(new Date())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    new MessageResponse.Builder()
                            .status(HttpStatus.UNAUTHORIZED)
                            .payload("path", "/api/v1/auth/refresh")
                            .payload("message", "El token de refresco ha expirado")
                            .build()
            );
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(refreshToken.getUsername());
        User user = new User(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());

        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtTokenUtil.generateJwtToken(authentication);

        return ResponseEntity.ok(new MessageResponse.Builder()
                .status(HttpStatus.OK)
                .payload("path", "/api/v1/auth/refresh")
                .payload("message", "Sesión actualizada correctamente")
                .payload("data", Map.of(
                    "token", jwt,
                    "refreshToken", new Date(System.currentTimeMillis() + jwtExpirationMs)
                ))
                .build()
        );
    }

    @ApiResponse(responseCode = "200", description = "Sesión cerrada correctamente")
    @ApiResponse(responseCode = "401", description = "No autenticado")
    @PostMapping("/api/v1/auth/logout")
    public ResponseEntity<MessageResponse> logout() {
        LOGGER.info("POST /api/v1/auth/logout");
        UserDetails userDetails = userDetailsService.loadUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        refreshTokenRepository.findByUsername(userDetails.getUsername()).ifPresent(refreshTokenRepository::delete);
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(new MessageResponse.Builder()
                .status(HttpStatus.OK)
                .payload("path", "/api/v1/auth/logout")
                .payload("message", "Sesión cerrada correctamente")
                .payload("data", Map.of(
                    "token", "",
                    "refreshToken", ""
                ))
                .build()
        );
    }

    @ApiResponse(responseCode = "201", description = "Usuario creado correctamente")
    @ApiResponse(responseCode = "400", description = "ID no especificado")
    @ApiResponse(responseCode = "401", description = "No autenticado")
    @PostMapping("/api/v1/auth/register")
    public ResponseEntity<MessageResponse> register(
            @RequestBody
            @Parameter(description = "Cuerpo de la solicitud en el que se incluye el nombre del usuario y la contraseña", required = true)
            RegisterRequest register
    ) {
        LOGGER.info("POST /api/v1/auth/register");

        if (playerRepository.existsByName(register.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    new MessageResponse.Builder()
                            .status(HttpStatus.CONFLICT)
                            .payload("path", "/api/v1/auth/register")
                            .payload("message", "Usuario ya existente")
                            .build()
            );
        }

        if (playerConstructor.buildPlayer(register)) {
            return ResponseEntity.ok(
                    new MessageResponse.Builder()
                            .status(HttpStatus.OK)
                            .payload("path", "/api/v1/auth/register")
                            .payload("message", "Registro exitoso")
                            .build()
            );
        }

        return ResponseEntity.internalServerError().body(
                new MessageResponse.Builder()
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .payload("path", "/api/v1/auth/register")
                        .payload("message", "Error al registrar el usuario")
                        .build()
        );
    }

    @ApiResponse(responseCode = "200", description = "Contraseña actualizada correctamente")
    @ApiResponse(responseCode = "400", description = "Contraseña actual incorrecta o no válida")
    @ApiResponse(responseCode = "401", description = "No autenticado")
    @PostMapping("/api/v1/auth/password/change")
    public ResponseEntity<MessageResponse> changePassword(
            @RequestBody
            @Parameter(description = "Cuerpo de la solicitud en el que se incluye la contraseña actual y la nueva contraseña", required = true)
            PasswordChangeRequest passwordChange
    ) {
        LOGGER.info("POST /api/v1/auth/password/change");

        UserDetails userDetails = userDetailsService.loadUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        if (!encoders.getPasswordEncoder().matches(passwordChange.getOldPassword(), userDetails.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    new MessageResponse.Builder()
                            .status(HttpStatus.UNAUTHORIZED)
                            .payload("path", "/api/v1/auth/password/change")
                            .payload("message", "Contraseña actual incorrecta")
                            .build()
            );
        }

        if (passwordChange.getOldPassword().equals(passwordChange.getNewPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new MessageResponse.Builder()
                            .status(HttpStatus.BAD_REQUEST)
                            .payload("path", "/api/v1/auth/password/change")
                            .payload("message", "La nueva contraseña no puede ser igual a la actual")
                            .build()
            );
        }

        int passwordStrength = PasswordChecker.getPasswordStrength(passwordChange.getNewPassword());
        LOGGER.info("Password strength: {}", passwordStrength);
        if (passwordStrength < passwordMinStrength) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new MessageResponse.Builder()
                            .status(HttpStatus.BAD_REQUEST)
                            .payload("path", "/api/v1/auth/password/change")
                            .payload("message", "La nueva contraseña no es segura")
                            .build()
            );
        }

        String encodedPassword = encoders.getPasswordEncoder().encode(passwordChange.getNewPassword());

        Player player = playerRepository.findByName(SecurityContextHolder.getContext().getAuthentication().getName());
        PlayerCredentials credentials = credentialsRepository.findById(player.getUniqueId()).orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        credentials.setPassword(encodedPassword);
        credentialsRepository.save(credentials);

        refreshTokenRepository.findByUsername(userDetails.getUsername()).ifPresent(refreshTokenRepository::delete);

        return ResponseEntity.ok(new MessageResponse.Builder()
                .status(HttpStatus.OK)
                .payload("path", "/api/v1/auth/password/change")
                .payload("message", "Contraseña actualizada correctamente. Inicia sesión con la nueva contraseña.")
                .build());
    }

    /**
     * Método POST para cambiar la dirección de correo electrónico del jugador.
     * @param emailChange Cuerpo de la solicitud en el que se incluye la nueva dirección de correo electrónico.
     * @param request Request HTTP.
     * @return ResponseEntity<MessageResponse> Respuesta con la dirección de correo electrónico actualizada correctamente.
     */
    @PostMapping("/api/v1/auth/email/change")
    @ApiResponse(responseCode = "200", description = "Dirección de correo electrónico actualizada correctamente")
    @ApiResponse(responseCode = "400", description = "Dirección de correo electrónico no válida")
    @ApiResponse(responseCode = "401", description = "No autenticado")
    @ApiResponse(responseCode = "403", description = "Sin permisos")
    public ResponseEntity<MessageResponse> changeEmail(
            @RequestBody
            @Parameter(description = "Cuerpo de la solicitud en el que se incluye la nueva dirección de correo electrónico", required = true)
            EmailChangeRequest emailChange,
            HttpServletRequest request
    ) {
        LOGGER.info("POST /api/v1/auth/email/change");

        UserDetails userDetails = userDetailsService.loadUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        if (!encoders.getPasswordEncoder().matches(emailChange.getPassword(), userDetails.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    new MessageResponse.Builder()
                            .status(HttpStatus.UNAUTHORIZED)
                            .payload("path", "/api/v1/auth/email/change")
                            .payload("message", "Contraseña actual incorrecta")
                            .build()
            );
        }

        Player player = playerService.getPlayer(SecurityContextHolder.getContext().getAuthentication().getName());
        PlayerCredentials credentials = credentialsRepository.findById(player.getUniqueId()).orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        credentials.setEmail(emailChange.getEmail());
        credentialsRepository.save(credentials);

        return ResponseEntity.ok(new MessageResponse.Builder()
                .status(HttpStatus.OK)
                .payload("path", "/api/v1/auth/email/change")
                .payload("message", "Dirección de correo electrónico actualizada correctamente. Inicia sesión con la nueva dirección de correo electrónico.")
                .build());
    }



}
