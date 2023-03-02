package com.strategy.api.auth.events;

import com.strategy.api.domain.users.User;
import lombok.Builder;
import lombok.Data;
import org.springframework.context.ApplicationEvent;

/**
 * Created on 2020-11-10.
 */
@Data
public class UserCreatedEvent extends ApplicationEvent {
    private final User user;
    private final Boolean workspaceCreator;

    @Builder
    public UserCreatedEvent(final Object source,
                            final User user,
                            final Boolean workspaceCreator) {
        super(source);
        this.user = user;
        this.workspaceCreator = workspaceCreator;
    }
}
