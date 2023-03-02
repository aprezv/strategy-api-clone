package com.strategy.api.integration;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * Created on 9/5/22.
 */
@Data
public class TokenResponse {
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
    private String IDApplication;
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
    private String IDApplicationGrant;
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
    private String Token;
}
