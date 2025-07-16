package kumar541.projects.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    
    private RSAKey rsaKeys;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean

    public JWKSource<com.nimbusds.jose.proc.SecurityContext> jwkSource(){

        rsaKeys= Jwks.generateRsa();
        JWKSet jwkSet= new JWKSet(rsaKeys);
        return (jwksSelector,SecurityContext)->jwksSelector.select(jwkSet);
        
    }

    @Bean

    public AuthenticationManager authManager (UserDetailsService userDetailsService) {

        var authProvider = new DaoAuthenticationProvider();

        authProvider.setPasswordEncoder(passwordEncoder());

        authProvider.setUserDetailsService (userDetailsService);

        return new ProviderManager(authProvider);
    }

     



    @Bean
    JwtDecoder jwtDecoder() throws JOSEException{
        return NimbusJwtDecoder.withPublicKey(rsaKeys.toRSAPublicKey()).build();
    }
    @Bean
    JwtEncoder jwtEncoder(JWKSource<com.nimbusds.jose.proc.SecurityContext> jwkSource) {
        return new NimbusJwtEncoder(jwkSource);
    }



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

            http
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
                .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/").permitAll()
                    .requestMatchers("/api/v1/auth/token").permitAll()
                    .requestMatchers("/api/v1/auth/users/add").permitAll()
                    .requestMatchers("/api/v1/auth/users").hasAuthority("SCOPE_ADMIN")
                    .requestMatchers("/api/v1/auth/profile/{user_id}/update-authorities").hasAuthority("SCOPE_ADMIN")
                    .requestMatchers("/api/v1/auth/profile").authenticated()
                    .requestMatchers("/api/v1/auth/profile/delete").authenticated()
                    .requestMatchers("/api/v1/auth/profile/update-password").authenticated()
                    .requestMatchers("/api/v1/albums/{album_id}/update").authenticated()
                    .requestMatchers("/api/v1/albums/{album_id}/photos/{photo_id}/update").authenticated()
                    .requestMatchers("/api/v1/albums/{album_id}/upload-photos").authenticated()
                    .requestMatchers("/api/v1/albums/add").authenticated()
                    .requestMatchers("/api/v1/albums").authenticated()
                    .requestMatchers("/api/v1/albums/{album_id}").authenticated()
                    .requestMatchers("/api/v1/albums/{album_id}/photos/{photo_id}/download-thumbnail").authenticated()
                    .requestMatchers("/api/v1/albums/{album_id}/photos/{photo_id}/download-photo").authenticated()
                    .requestMatchers("/api/v1/albums/{album_id}/photos/{photo_id}/delete").authenticated()
                    .requestMatchers("/api/v1/albums/{album_id}/delete").authenticated()
                    .requestMatchers("/swagger-ui/**").permitAll()
                    .requestMatchers("/v3/api-docs/**").permitAll()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(csrf -> csrf.disable())
                .headers(headers -> headers.frameOptions(frame -> frame.disable()));

            return http.build();
    }
    
}
