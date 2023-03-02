package com.strategy.api.web.users.resources;

import com.strategy.api.domain.users.UserService;
import com.strategy.api.web.users.dto.RecoverChangePasswordRequest;
import com.strategy.api.web.users.dto.SendRecoverLinkRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created on 2020-08-04.
 */
@RestController
@RequestMapping("/recover-password")
public class RecoverPasswordResource {
    private final UserService userService;

    public RecoverPasswordResource(final UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void sendRecoveryLink(@RequestBody SendRecoverLinkRequest request) {
        userService.sendRecoveryLink(request);
    }

    @PostMapping("/change")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(@RequestBody RecoverChangePasswordRequest request) {
        userService.changePassword(request);
    }

    @PostMapping("/token")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void validateToken(@RequestBody RecoverChangePasswordRequest request) {
        userService.findUserByRecoveryPasswordToken(request);
    }
}
