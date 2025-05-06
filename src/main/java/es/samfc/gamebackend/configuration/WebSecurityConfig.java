package es.samfc.gamebackend.configuration;

import es.samfc.gamebackend.events.filter.PostEventCallingFilter;
import es.samfc.gamebackend.events.filter.PreEventCallingFilter;
import es.samfc.gamebackend.security.encryption.Encoders;
import es.samfc.gamebackend.security.handling.CustomAccessDeniedHandler;
import es.samfc.gamebackend.security.jwt.JwtRequestFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuración de seguridad
 * Configuración de filtros de seguridad y manejadores de acceso denegado para la aplicación.
 */


@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final JwtRequestFilter jwtRequestFilter;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final PreEventCallingFilter preEventCallingFilter;
    private final PostEventCallingFilter postEventCallingFilter;

    private static final String[] WHITELIST = {
            // Authentication
            "/api/v1/auth/login",
            "/api/v1/auth/register",

            // Swagger UI v3 (OpenAPI)
            "/v3/api-docs/**",
            "/swagger-ui/**",

            //Other
            "/favicon.ico"
    };

    public WebSecurityConfig(
            JwtRequestFilter jwtRequestFilter,
            CustomAccessDeniedHandler accessDeniedHandler, PreEventCallingFilter preEventCallingFilter,
            PostEventCallingFilter postEventCallingFilter
    ) {
        this.jwtRequestFilter = jwtRequestFilter;
        this.accessDeniedHandler = accessDeniedHandler;
        this.preEventCallingFilter = preEventCallingFilter;
        this.postEventCallingFilter = postEventCallingFilter;
    }

    @Bean
    public SecurityFilterChain applicationSecurityFilter(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(config -> config
                    .requestMatchers(WHITELIST).permitAll()
                    .anyRequest().authenticated()
            )
            .exceptionHandling(exception -> exception
                    .accessDeniedHandler(accessDeniedHandler)
            )
            .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(preEventCallingFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterAfter(postEventCallingFilter, UsernamePasswordAuthenticationFilter.class)
            .sessionManagement(config -> config.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }


    @Bean
    public AuthenticationManager authenticationManager(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);

        return new ProviderManager(authenticationProvider);
    }

    @Bean
    public PasswordEncoder passwordEncoder(Encoders encoders) {
        return encoders.getPasswordEncoder();
    }
}
