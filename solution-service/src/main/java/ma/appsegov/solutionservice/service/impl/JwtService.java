package ma.appsegov.solutionservice.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import ma.appsegov.solutionservice.DTO.ContactDTO;
import ma.appsegov.solutionservice.DTO.UserDTO;
import ma.appsegov.solutionservice.authentication.CustomAuthentication;
import ma.appsegov.solutionservice.client.PrincipalRestClient;
import ma.appsegov.solutionservice.client.TokenKeyRestClient;
import ma.appsegov.solutionservice.client.request.UserIdRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final TokenKeyRestClient tokenKeyRestClient;
    private final PrincipalRestClient principalRestClient;

    public Long extractSubject(String token) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return Long.valueOf(extractClaim(token, Claims::getSubject));
    }

    public void setUserPrincipal(String token) throws NoSuchAlgorithmException, InvalidKeySpecException {
        setPrincipal(token);
    }


    private void setPrincipal(String token) throws NoSuchAlgorithmException, InvalidKeySpecException {

        UserIdRequest request = new UserIdRequest();
        request.setId(extractSubject(token));

        UserDTO userDTO = principalRestClient.getPrincipal(request);
        CustomAuthentication authentication = new CustomAuthentication(userDTO);

        SecurityContextHolder.getContext().setAuthentication(authentication);

    }

    public <T> T extractClaim(String token, Function<Claims, T> claimResolver) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String publicKey = tokenKeyRestClient.getAccessTokenPublicKey();
        RSAPublicKey rsaPublicKey = convertFromPEM(publicKey);
        Map<String, Object> claims = extractAllClaims(token, rsaPublicKey);
        return claimResolver.apply((Claims) claims);
    }

    public RSAPublicKey convertFromPEM(String pemPublicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        // Remove the PEM header and footer
        String publicKeyPEM = pemPublicKey.replace("-----BEGIN RSA PUBLIC KEY-----", "").replace("-----END RSA PUBLIC KEY-----", "").replace("\n", "");

        // Decode the Base64-encoded public key
        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyPEM);

        // Construct an X509EncodedKeySpec with the decoded bytes
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);

        // Get an RSA key factory instance
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        // Generate the RSAPublicKey from the key spec
        return (RSAPublicKey) keyFactory.generatePublic(keySpec);
    }

    public boolean isTokenValid(String token) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return extractClaim(token, Claims::getExpiration);
    }

    private Map<String, Object> extractAllClaims(String token, RSAPublicKey publicKey) {
        Jws<Claims> jws = Jwts.parserBuilder().setSigningKey(publicKey).build().parseClaimsJws(token);

        return jws.getBody();
    }

    private void findContactByUserId(Long id) {

        UserIdRequest request = new UserIdRequest();
        request.setId(id);

        ContactDTO contactDTO = principalRestClient.findContactByUserId(request);
    }
}
