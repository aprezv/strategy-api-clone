package com.strategy.api.domain.users.events;

import com.strategy.api.domain.users.User;
import lombok.Builder;
import lombok.Data;
import org.springframework.context.ApplicationEvent;

/**
 * Created on 2021-07-22.
 */
@Data
public class UserRefreshEvent extends ApplicationEvent {
    private final User user;

    @Builder
    public UserRefreshEvent(final Object source,
                            final User user) {
        super(source);
        this.user = user;
    }
}
