package ma.appsegov.authservice.security;

import lombok.RequiredArgsConstructor;
import ma.appsegov.authservice.DTO.TokenDTO;
import ma.appsegov.authservice.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TokenGenerator {

    private final JwtEncoder accessTokenEncoder;

    @Qualifier("jwtRefreshTokenEncoder")
    private final JwtEncoder refreshTokenEncoder;

    private String createAccessToken(Authentication authentication){
        User user = (User) authentication.getPrincipal();
        Instant now = Instant.now();
        System.out.println(user.getAuthorities());
        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .issuer("Apps_Egov")
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.HOURS))
                .subject(user.getId().toString())
                .claim("email",user.getEmail())
                .claim("roles",user.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority) // Extract role name
                        .collect(Collectors.toList()))
                .build();
        return accessTokenEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue();
    }
    private String createRefreshToken(Authentication authentication){
        User user = (User) authentication.getPrincipal();
        Instant now = Instant.now();

        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .issuer("Apps_Egov")
                .issuedAt(now)
                .expiresAt(now.plus(3, ChronoUnit.DAYS))
                .subject(user.getId().toString())
                .claim("email",user.getEmail())
                .claim("roles",user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))

                .build();
        return refreshTokenEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue();
    }

    boolean shouldRefreshToken(Jwt jwt) {
        Instant now = Instant.now();
        Instant expiresAt = jwt.getExpiresAt();
        Duration duration = Duration.between(now, expiresAt);
        long daysUntilExpired = duration.toDays();
        return daysUntilExpired < 7;
    }

    public TokenDTO createToken(Authentication authentication){
        if(!(authentication.getPrincipal() instanceof User user)){
            throw new BadCredentialsException(
                    MessageFormat.format("principal {0} is not of User type",authentication.getPrincipal().getClass())
            ) ;
        }
        TokenDTO tokenDTO = new TokenDTO();
        tokenDTO.setUserId(user.getId());
        tokenDTO.setAccessToken(createAccessToken(authentication));

        String refreshToken;

        /*
          if(authentication.getCredentials() instanceof Jwt jwt){
            Instant now = Instant.now();
            Instant expiresAt = jwt.getExpiresAt();
            Duration duration = Duration.between(now,expiresAt);
            long daysUntilExpired = duration.toDays();
            if(daysUntilExpired < 7){
                refreshToken = createRefreshToken(authentication);
            }else{
                refreshToken = jwt.getTokenValue();
            }
        }else{
            refreshToken = createRefreshToken(authentication);
        }
        */

        if (authentication.getCredentials() instanceof Jwt jwt) {
            refreshToken = shouldRefreshToken(jwt) ? createRefreshToken(authentication) : jwt.getTokenValue();
        } else {
            refreshToken = createRefreshToken(authentication);
        }

        tokenDTO.setRefreshToken(refreshToken);
        return tokenDTO;
    }
}

