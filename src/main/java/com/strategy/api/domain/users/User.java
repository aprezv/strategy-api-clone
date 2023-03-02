package com.strategy.api.domain.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.strategy.api.domain.padron.model.Militante;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {
    private Long id;
    private String username;
    private String name;
    private String profileImageUrl;
    private String timeZone;
    private Boolean timezoneNotified;
    private Role role;
    private Map<String, Object> metadata;
    private Date lastLogin;
    private Boolean hasAnswer;
    private Militante militante;
    @JsonIgnore
    private String password;

    private Integer registeredCount;

    @JsonIgnore
    private Date createdDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public enum Role {
        SUPERADMIN, COORDINADOR, MULTIPLICADOR, SUPPORT
    }

}
