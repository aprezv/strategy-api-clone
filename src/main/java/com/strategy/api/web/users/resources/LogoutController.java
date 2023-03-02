package com.strategy.api.web.users.resources;

import com.strategy.api.auth.services.JwtAuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * Created on 2/2/22.
 */
@RestController
public class LogoutController {
    private final JwtAuthenticationService authenticationService;

    public LogoutController(final JwtAuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response)  {
        response.addCookie(authenticationService.getLogoutCookie());
        return ResponseEntity.ok()
                .build();
    }
}
