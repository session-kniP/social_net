package com.sessionknip.socialnet.web.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class TokenFilter extends GenericFilterBean {

    private final TokenProvider provider;

    public TokenFilter(TokenProvider provider) {
        this.provider = provider;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        String token = provider.resolveToken((HttpServletRequest) request);

        try {
            if (token != null && provider.validateToken(token)) {
                Authentication authentication = provider.getAuthentication(token);

                if (authentication != null) {
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (TokenProviderException e) {
            SecurityContextHolder.clearContext();
            throw new ServletException("Can't apply filter to token", e);
        }

        filterChain.doFilter(request, response);
    }
}
