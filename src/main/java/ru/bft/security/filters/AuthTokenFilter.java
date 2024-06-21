package ru.bft.security.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.bft.security.config.ApplicationContextHolder;
import ru.bft.security.service.CustomUserDetails;
import ru.bft.security.utils.JwtUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {

    private static final Logger LOG = LoggerFactory.getLogger(AuthTokenFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
            LOG.info("AuthTokenFilter called for URI: {}", request.getRequestURI());
            JwtUtils jwtUtils = new JwtUtils();
            try {
                String jwt = jwtUtils.getJwt(request);
                if(jwt != null && jwtUtils.validate(jwt)) {
                    String username = jwtUtils.getUserNameFromJwtToken(jwt);
                    CustomUserDetails userDetailsService = ApplicationContextHolder.getContext().getBean(CustomUserDetails.class);
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, new WebAuthenticationDetailsSource().buildDetails(request), userDetails.getAuthorities());
                    LOG.info("Roles from JWT: {}", userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            } catch (Exception e) {
                LOG.error("Cannot set user authentication: {}", e.getMessage());
            }
            filterChain.doFilter(request, response);
    }
}

