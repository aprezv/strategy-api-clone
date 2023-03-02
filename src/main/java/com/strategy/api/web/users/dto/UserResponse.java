package com.strategy.api.web.users.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.strategy.api.domain.users.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Objects;

/**
 * Created on 2020-08-04.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse {
    private Long id;
    private String name;
    private String username;
    private String profileImageUrl;
    private String timeZone;
    private Date lastLogin;
    private Boolean hasAnswer;
    private User.Role role;
    private Integer registeredCount;
    private String dashboardUrl;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserResponse that = (UserResponse) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
