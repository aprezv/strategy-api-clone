package com.strategy.api.auth.config;

import com.strategy.api.auth.exceptions.UsernameNotFoundException;
import com.strategy.api.domain.users.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Slf4j
@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    private final UserService userService;
    private final JwtTokenUtil tokenUtil;

    @Autowired
    public JwtRequestFilter(final UserService userService,
                            final JwtTokenUtil tokenUtil) {
        this.userService = userService;
        this.tokenUtil = tokenUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {
        //final String requestTokenHeader = request.getHeader("authorization");
        String jwtToken = null;

        final Optional<Cookie> optionalCookie = request.getCookies() == null
                ? Optional.empty()
                : Arrays.stream(request.getCookies())
                .filter(c -> c.getName().equals(AuthConstants.COOKIE_ACCESS_TOKEN_NAME))
                .findAny();

        if (optionalCookie.isPresent()) {
            final Cookie jwtCookie = optionalCookie.get();
            jwtToken = jwtCookie.getValue();
        }

        authenticateWithToken(jwtToken, request, response);

        chain.doFilter(request, response);
    }

    public boolean authenticateWithToken(String jwtToken, HttpServletRequest request, HttpServletResponse response) {

        try {
            String username;
            username = tokenUtil.getUsernameFromToken(jwtToken);
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userService.loadUserByUsername(username);
                if (tokenUtil.validateToken(jwtToken, userDetails)) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken
                            .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                    return true;
                }
            }

        } catch (IllegalArgumentException e) {
            //log.info("Unable to build JWT Token");
        } catch (ExpiredJwtException e) {
            log.info("JWT Token has expired");
        } catch (UsernameNotFoundException | org.springframework.security.core.userdetails.UsernameNotFoundException e) {
            SecurityContextHolder.getContext().setAuthentication(null);
            response.addCookie(tokenUtil.getLogoutCookie());
        }

        return false;
    }
}
