package com.strategy.api.web.users.dto;

import com.strategy.api.domain.users.User;
import lombok.Data;

/**
 * Created on 8/7/22.
 */
@Data
public class ChangeRoleRequest {
    User.Role role;
}
