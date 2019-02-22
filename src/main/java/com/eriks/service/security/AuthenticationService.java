package com.eriks.service.security;

import com.eriks.service.config.model.SecurityProperties;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;

import static io.jsonwebtoken.Jwts.parser;

@Component
public class AuthenticationService {

  @Autowired
  private SecurityProperties securityProperties;

    private static final String TOKEN_PREFIX = "Bearer";
    private static final String AUTHORIZAZATION_HEADER = "Authorization";

    public Authentication getAuthentication(final HttpServletRequest request) {
        final String token = request.getHeader(AUTHORIZAZATION_HEADER);

        if (token != null && !token.isEmpty()) {
            final SecretKey secretKey = Keys.hmacShaKeyFor(securityProperties.getJwtAuthSecret().getBytes());
            final String email = parser().setSigningKey(secretKey).parseClaimsJws(token.replace(TOKEN_PREFIX, "")).getBody().getSubject();

            if (email != null) {
                return new UsernamePasswordAuthenticationToken(email, null, Collections.<SimpleGrantedAuthority>emptyList());
            }
        }

        return null;
    }
}
