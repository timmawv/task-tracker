package avlyakulov.timur.taskTrackerApi.config;

import avlyakulov.timur.taskTrackerApi.dto.UserDto;
import avlyakulov.timur.taskTrackerApi.entity.User;
import avlyakulov.timur.taskTrackerApi.exception.AppException;
import avlyakulov.timur.taskTrackerApi.mapper.UserMapper;
import avlyakulov.timur.taskTrackerApi.repository.UserRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class UserAuthProvider {

    @Value("${security.jwt.token.secret-key}")
    private String secretKey;

    private final String UTC_TIMEZONE = "UTC";

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createToken(UserDto userDto) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime validity = now.plusHours(1L);
        Instant instantUTC = now.atZone(ZoneId.of(UTC_TIMEZONE)).toInstant();
        Instant validityUTC = validity.atZone(ZoneId.of(UTC_TIMEZONE)).toInstant();

        return JWT.create()
                .withIssuer(userDto.getEmail())
                .withIssuedAt(instantUTC)
                .withExpiresAt(validityUTC)
                .withClaim("id", userDto.getId())
                .sign(Algorithm.HMAC256(secretKey));
    }

    public Authentication validateToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);

        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decoded = verifier.verify(token);
        UserDto user = UserDto.builder()
                .id(decoded.getClaim("id").asLong())
                .email(decoded.getIssuer())
                .build();

        return new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
    }

    public Authentication validateTokenStrongly(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);

        JWTVerifier verifier = JWT.require(algorithm).build();

        DecodedJWT decoded = verifier.verify(token);

        User user = userRepository.findByEmail(decoded.getIssuer())
                .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));

        return new UsernamePasswordAuthenticationToken(userMapper.toDto(user), null, Collections.emptyList());
    }
}
