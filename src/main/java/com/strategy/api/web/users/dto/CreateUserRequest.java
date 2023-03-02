package com.strategy.api.web.users.dto;

import com.strategy.api.domain.users.User;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

/**
 * Created on 2020-08-03.
 */
@Data
@Builder
public class CreateUserRequest {
    private String email;
    private String password;
    private Boolean verified;
    private Boolean active;
    private String name;
    private String activationToken;
    private String timeZone;
    private String profileImageUrl;
    private Map<String, Object> metadata;
}
