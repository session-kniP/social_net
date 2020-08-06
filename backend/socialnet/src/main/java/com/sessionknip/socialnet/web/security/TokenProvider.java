package com.sessionknip.socialnet.web.security;


import com.sessionknip.socialnet.web.domain.Role;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TokenProvider {

    @Value("${jwt.token.secret}")
    private String secret;

    @Value("${jwt.token.expired}")
    private Long expiration;

    private final UserDetailsServiceImpl userDetailsService;
    private final BCryptPasswordEncoder encoder;

    public TokenProvider(UserDetailsServiceImpl userDetailsService, BCryptPasswordEncoder encoder) {
        this.userDetailsService = userDetailsService;
        this.encoder = encoder;
    }

    @PostConstruct
    protected void init() {
        secret = Base64.getEncoder().encodeToString(secret.getBytes());
    }

    //token from username (and roles)
    //needs in claims(subject), issue time, expiration time, signification
    public String createToken(String username, List<Role> roles) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("roles", getRoleNames(roles));

        Date issuedAt = new Date();
        Date expirationDate = new Date(issuedAt.getTime() + expiration);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(issuedAt)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    //username from token
    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
    }

    //is authenticated
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    //get token value from http request
    public String resolveToken(HttpServletRequest request) {
        String tokenValue = request.getHeader("Authorization");
        if (tokenValue != null && tokenValue.startsWith("Bearer_")) {
            return tokenValue.substring(7);
        }
        return null;
    }

    //token validation
    public boolean validateToken(String token) throws TokenProviderException {

        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);

            // if expiration time less than current time then return false
            return !claims.getBody().getExpiration().before(new Date());

        } catch (SignatureException | ExpiredJwtException | MalformedJwtException e) {
            throw new TokenProviderException("Can't validate token");
        }
    }

    public List<String> getRoleNames(List<Role> roles) {
        return roles.stream().map(Enum::name).collect(Collectors.toList());
    }


}
