package com.strategy.api.domain.users.events;

import com.strategy.api.domain.users.User;
import lombok.Builder;
import lombok.Data;
import org.springframework.context.ApplicationEvent;

/**
 * Created on 2021-01-29.
 */
@Data
public class PasswordResetSuccessEvent extends ApplicationEvent {
    private final User user;

    @Builder
    public PasswordResetSuccessEvent(final Object source,
                                     final User user) {
        super(source);
        this.user = user;
    }
}
