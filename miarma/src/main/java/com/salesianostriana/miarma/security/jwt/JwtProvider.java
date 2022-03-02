package com.salesianostriana.miarma.security.jwt;

import com.salesianostriana.miarma.models.user.UserEntity;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

@Log
@Service
public class JwtProvider {

    public static final String TOKEN_TYPE = "JWT";
    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";

    @Value("${jwt.secret:MIIBOgIBAAJBAIt2YC8+C7WRsP+4zkfxpQeR22Um6NMmH+4f/TD+pxVuEnuri7oA\n" +
            "sAUrBIat87clIhACp/VFXZtVGjvdbyRkUxMCAwEAAQJAd37UZ2ZIbikJefOFfIuG\n" +
            "lffZ/s/aCqHOFiD+tu5bh7XiEBOIAncd4LNd1FO0ECoNfX1PW8hxytiFvjfenN4j\n" +
            "4QIhAODwsKQ5O1bLhS4SUd4Rai2ObKs1NonnC2T1LxPysGXrAiEAnrgrcBi2r4Hy\n" +
            "NwgPkPv3O/3gOG4lB9x382kQ5b6ftXkCIBVCak97IUKYxaOPE7G7T5+yoE6mHtt1\n" +
            "vLvz+FkYTNe1AiA1VCQkMemP5tV/cBHq4P4dBhxEWZ3PmmAjRPYMe5Ql4QIhAKio\n" +
            "BGqplANeey3EtnACHFYcull1hpkO5+MUpLaBRZMF}")
    private String jwtSecret;

    @Value("${jwt.duration:86400}") // 1 día
    private int jwtLifeInSeconds;

    private JwtParser parser;

    @PostConstruct
    public void init() {
        parser = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                .build();
    }

    public boolean validateToken(String token) {

        try {
            parser.parseClaimsJws(token);
            return true;
        } catch (SignatureException | MalformedJwtException
                | ExpiredJwtException | UnsupportedJwtException | IllegalArgumentException ex) {
            log.info("Error con el token: " + ex.getMessage());
        }
        return false;

    }

    public UUID getUserIdFromJwt(String token) {
        return UUID.fromString(parser.parseClaimsJws(token).getBody().getSubject());
    }

    public String generateToken(Authentication authentication) {

        UserEntity user = (UserEntity) authentication.getPrincipal();

        Date tokenExpirationDate = Date
                .from(LocalDateTime
                        .now()
                        .plusSeconds(jwtLifeInSeconds)
                        .atZone(ZoneId.systemDefault()).toInstant());


        return Jwts.builder()
                .setHeaderParam("typ", TOKEN_TYPE)
                .setSubject(user.getId().toString())
                .setIssuedAt(tokenExpirationDate)
                .claim("fullname", user.getFullname())
                .claim("role", user.getRole().name())
                .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                .compact();


    }

}
