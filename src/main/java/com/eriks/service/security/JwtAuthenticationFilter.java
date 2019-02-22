package com.eriks.service.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private AuthenticationService authenticationService;

    @Override
    protected void doFilterInternal(final HttpServletRequest req, final HttpServletResponse res, final FilterChain fc) throws ServletException, IOException {
        SecurityContextHolder.getContext().setAuthentication(authenticationService.getAuthentication(req));

        fc.doFilter(req, res);
    }
}
