package com.strategy.api.web.users.resources;

import com.strategy.api.domain.users.PromoteService;
import com.strategy.api.domain.users.UserRepository;
import com.strategy.api.web.users.dto.PromoteRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created on 6/14/22.
 */
@RestController
@RequestMapping("/promote")
public class PromoteResource {
    private final PromoteService promoteService;

    public PromoteResource(PromoteService promoteService) {
        this.promoteService = promoteService;
    }

    @PostMapping
    public ResponseEntity promote(@RequestBody PromoteRequest request) {
        promoteService.promote(request);
        return ResponseEntity.ok()
                .build();
    }
}
