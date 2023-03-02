package com.strategy.api.web.users.dto;

import lombok.Data;

import javax.validation.constraints.Size;

/**
 * Created on 2020-11-24.
 */
@Data
public class RecoverChangePasswordRequest {
    private String token;
    @Size(min = 6, max = 100)
    private String newPassword;
}
