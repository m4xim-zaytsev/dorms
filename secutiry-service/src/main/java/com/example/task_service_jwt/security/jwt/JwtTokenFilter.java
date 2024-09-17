package com.example.task_service_jwt.security.jwt;

import com.example.task_service_jwt.security.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.http.Cookie;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UserDetailsServiceImpl userDetailsService;



    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwtToken = getToken(request);
            checkToken(request, jwtToken);
        } catch (Exception e) {
            log.error("Cannot set user authentication via JWT Headers: {}", e.getMessage());
        }

        {
            Cookie[] cookies = request.getCookies();

            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("jwtToken")) {
                        String jwtToken = cookie.getValue();
                        checkToken(request, jwtToken);
                    }
                }
            }
        }



        filterChain.doFilter(request,response);
    }

    private void checkToken(HttpServletRequest request, String jwtToken) {
        if (jwtToken != null) {
            log.info("JWT Token: {}", jwtToken);  // Логирование токена
            if (jwtUtils.validate(jwtToken)) {
                String username = jwtUtils.getUsername(jwtToken);
                log.info("JWT Username: {}", username);  // Логирование пользователя

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                log.warn("Invalid JWT Token");
            }
        }
    }

    private String getToken(HttpServletRequest request) {
        String headerAuth = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")){
            return headerAuth.substring(7);
        }

        return null;
    }
}
