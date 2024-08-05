package com.project.featurestoggle.infra.security;

import com.project.featurestoggle.data.UserRepository;
import com.project.featurestoggle.services.AuthenticationService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationService authenticationService;

    private String getToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (!Objects.isNull(authHeader)) {
            return authHeader.replace("Bearer ", "");
        }

        return null;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) {
        String token = getToken(request);

        if (!Objects.isNull(token)) {
            var subject = authenticationService.getSubject(token);
            var user = userRepository.findByEmail(subject);
            var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        try {
            filterChain.doFilter(request, response);
        } catch(IOException exception) {
            System.out.println(exception);
        } catch(ServletException exception) {
            System.out.println(exception);
        }
    }
}
