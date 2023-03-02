package com.strategy.api.domain.users;

/**
 * Created on 2021-01-20.
 */
public interface UserRepository {
    User save(User user);
    User edit(User user, Boolean changeUsername);
    User findByEmail(String email);
    User findByCedula(String cedula);
    boolean userExist(String username);

    void increaseRegisteredCount(User user, int i);
    void increaseRegisteredCount(Long userId, int i);

    void deleteByCedula(String cedula);

    User findById(Long id);
    Integer getRegisteredCount(Long userId);

    void updateCreatedBy(Long userId, Long createdById);
    //User findUserBySlackUserId(String slackUserId);
    //User findBySlackUserIdAndWorkspaceId(String slackUserId, String workspaceId);
}
