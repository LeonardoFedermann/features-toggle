package com.project.featurestoggle.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.project.featurestoggle.data.UserRepository;
import com.project.featurestoggle.domains.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class AuthenticationService implements UserDetailsService {
    @Value("${api.security.token.secret}")
    private String secret;

    @Autowired
    private UserRepository userRepository;

    private String issuer = "Features Toggle API";

    private Instant getExpirationDate() {
        return LocalDateTime.now().plusDays(1).toInstant(ZoneOffset.UTC);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username);
    }

    public String generateToken(User user) {
        var algorithm = Algorithm.HMAC256(secret);
        return JWT.create()
                .withIssuer(issuer)
                .withSubject(user.getUsername())
                .withClaim("id", user.getId())
                .withExpiresAt(getExpirationDate())
                .sign(algorithm);
    }

    public String getSubject(String token) {
        try {
            var algorithm = Algorithm.HMAC256(secret);

            return JWT.require(algorithm).withIssuer(issuer).build().verify(token).getSubject();
        } catch (JWTVerificationException exception) {
            throw new RuntimeException("Invalid or expired token");
        }
    }
}
