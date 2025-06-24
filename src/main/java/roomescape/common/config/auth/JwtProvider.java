package roomescape.common.config.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.common.exception.RestApiException;
import roomescape.common.exception.status.AuthErrorStatus;
import roomescape.member.domain.Role;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Date;

@Component
public class JwtProvider {
    public static final String NAME = "token";

    private final SecretKey secretKey;
    @Getter
    private final Duration expiry;

    public JwtProvider(
            @Value("${security.jwt.secret_key}")
            String secretKey,

            @Value("${security.jwt.expiry}")
            Duration expiry
    ) {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        this.expiry = expiry;
    }

    public String generateToken(Long memberId, Role role) {
        Date expiration = new Date(System.currentTimeMillis() + expiry.toMillis());

        return Jwts.builder()
                .claim("memberId", memberId)
                .claim("role", role)
                .expiration(expiration)
                .signWith(secretKey)
                .compact();
    }

    public void validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);

//                  JwtException – if parsing, signature verification, or JWT validation fails.
//                  IllegalArgumentException – if either the jws or unencodedPayload are null or empty.
        } catch (JwtException e) {
            throw new RestApiException(AuthErrorStatus.INVALID_TOKEN);
        } catch (IllegalArgumentException e) {
            throw new RestApiException(AuthErrorStatus.PAYLOAD_BLANK);
        }
    }

    public Long getMemberId(String token) {
        validateToken(token);

        return getClaims(token).get("memberId", Long.class);
    }

    public Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }


}
