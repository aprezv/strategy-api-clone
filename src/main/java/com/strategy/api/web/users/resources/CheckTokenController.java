package com.strategy.api.web.users.resources;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created on 2/2/22.
 */
@RestController
public class CheckTokenController {
    @PostMapping("/check-token")
    public ResponseEntity<?> checkToken()  {
        return ResponseEntity.ok()
                .build();
    }
}
