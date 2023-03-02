package com.strategy.api.auth.services;

import com.strategy.api.auth.config.AuthConstants;
import com.strategy.api.auth.config.JwtTokenUtil;
import com.strategy.api.auth.exceptions.EmailNotConfirmedException;
import com.strategy.api.auth.exceptions.InvalidCredentialsException;
import com.strategy.api.domain.users.UserService;
import com.strategy.api.persistence.users.converters.UserJpaConverter;
import com.strategy.api.web.users.dto.JwtRequest;
import com.strategy.api.web.users.dto.JwtResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;

/**
 * Created on 2021-03-29.
 */
@Service
public class JwtAuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserService userDetailsService;
    private final String oauthPassword;

    public JwtAuthenticationService(final AuthenticationManager authenticationManager,
                                    final JwtTokenUtil jwtTokenUtil,
                                    final UserService userDetailsService,
                                    final @Value("${application.oauthPassword}") String oauthPassword,
                                    final ApplicationEventPublisher applicationEventPublisher,
                                    final UserJpaConverter userJpaConverter) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
        this.oauthPassword = oauthPassword;
    }

    public JwtResponse createAuthenticationToken(JwtRequest authenticationRequest) throws Exception {
        authenticate(authenticationRequest);

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);


        return new JwtResponse(token);
    }

    public Cookie createTokenCookie(JwtRequest authenticationRequest) throws Exception {
        JwtResponse tokenResponse = createAuthenticationToken(authenticationRequest);
        return getTokenCookie(tokenResponse);
    }

    public Cookie getTokenCookie(JwtResponse tokenResponse) {
        return getTokenCookie(tokenResponse.getToken());
    }

    public Cookie getTokenCookie(String token) {
        Cookie cookie = new Cookie(AuthConstants.COOKIE_ACCESS_TOKEN_NAME, token);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        return cookie;
    }

    public JwtResponse createAuthenticationToken(String userName) throws Exception {
        authenticate(userName);
        final UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
        final String token = jwtTokenUtil.generateToken(userDetails);
        return new JwtResponse(token);
    }

    public Cookie createTokenCookie(String userName) throws Exception {
        JwtResponse tokenResponse = createAuthenticationToken(userName);
        return getTokenCookie(tokenResponse);
    }

    private void authenticate(JwtRequest request) throws Exception {

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new InvalidCredentialsException();
        } catch (LockedException e) {
            throw new EmailNotConfirmedException();
        }
    }

    private void authenticate(String email) throws Exception {

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, oauthPassword));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new InvalidCredentialsException();
        } catch (LockedException e) {
            throw new EmailNotConfirmedException();
        }
    }

    public Cookie getLogoutCookie() {
        return jwtTokenUtil.getLogoutCookie();
    }
}
