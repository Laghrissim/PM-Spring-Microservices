package ma.appsegov.authservice.security;

import ma.appsegov.authservice.model.User;
import ma.appsegov.authservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class JwtToUserConverter implements Converter<Jwt, UsernamePasswordAuthenticationToken> {
    @Autowired
    UserRepository userRepository;
    @Override
    public UsernamePasswordAuthenticationToken convert(Jwt jwt){
        User user = userRepository.findUserById(Long.valueOf(jwt.getSubject()));

        return new UsernamePasswordAuthenticationToken(user,jwt,user.getAuthorities());
    }
}
