package com.strategy.api.web.users.resources;

import com.strategy.api.auth.exceptions.UsernameNotFoundException;
import com.strategy.api.base.model.http.EditFieldRequest;
import com.strategy.api.domain.users.User;
import com.strategy.api.domain.users.UserRepository;
import com.strategy.api.domain.users.UserService;
import com.strategy.api.persistence.users.UserEntity;
import com.strategy.api.web.register.exceptions.CoordinatorNotExistsException;
import com.strategy.api.web.users.dto.ChangePasswordRequest;
import com.strategy.api.web.users.dto.ChangeRoleRequest;
import com.strategy.api.web.users.dto.UserResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;


/**
 * Created on 2020-08-04.
 */
@RestController
@RequestMapping("/users")
public class UserResource {
    private final UserService userService;
    private final UserRepository userRepository;

    public UserResource(final UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }


    @PatchMapping("/me")
    public ResponseEntity updateField(@RequestBody EditFieldRequest request, @AuthenticationPrincipal UserEntity userEntity) {
        User user = userService.editField(userEntity, request);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/me")
    public ResponseEntity getCurrentUser(@AuthenticationPrincipal UserEntity user) {

        return ResponseEntity.ok(
                UserResponse.builder()
                        .name(user.getName())
                        .username(user.getUsername())
                        .profileImageUrl(user.getProfileImageUrl())
                        .timeZone(user.getTimeZone())
                        .role(user.getRole())
                        .id(user.getId())
                        .dashboardUrl(user.getDashboardUrl())
                        .registeredCount(user.getRegisteredCount())
                        .lastLogin(user.getLastLogin())
                        .build()
        );

    }

    @PostMapping("/change-password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(@RequestBody ChangePasswordRequest request) {
        userService.changePassword(request.getOldPassword(), request.getNewPassword());
    }

    @PostMapping("/reset-password/{cedula}")
    public ResponseEntity resetPassword(@PathVariable String cedula) {
        userService.resetPassword(cedula);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/validate/{cedula}")
    public ResponseEntity validate(@PathVariable String cedula) {
        User  user = Optional.ofNullable(userRepository.findByCedula(cedula))
                .orElseThrow(CoordinatorNotExistsException::new);

        return ResponseEntity.ok(
                Map.of("nombre", user.getName())
        );
    }


    @PutMapping("/{id}/change-role")
    @PreAuthorize("hasAnyAuthority('SUPERADMINN', 'SUPPORT')")
    public ResponseEntity changeRole(@PathVariable Long id, @RequestBody ChangeRoleRequest request) {
        User  user = Optional.ofNullable(userRepository.findById(id))
                .orElseThrow(CoordinatorNotExistsException::new);
        user.setRole(request.getRole());
        userRepository.save(user);
        return ResponseEntity.ok().build();
    }

}
