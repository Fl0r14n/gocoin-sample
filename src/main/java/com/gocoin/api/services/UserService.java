package com.gocoin.api.services;

import java.util.Collection;
import java.util.Map;

import com.gocoin.api.resources.Application;
import com.gocoin.api.resources.Token;
import com.gocoin.api.resources.User;

/**
 * A service that knows how to perform operations for users
 */
public interface UserService {

    User getResourceOwner(Token t);

    User getUser(Token t, String userId);

    Collection<User> listUsers(Token t, Map<String, String> parameters);

    User createUser(Token t, User u, Map<String, String> parameters);

    User updateUser(Token t, User u, Map<String, String> parameters);

    boolean deleteUser(Token t, User u, Map<String, String> parameters);

    boolean updatePassword(Token t, User u, Map<String, String> parameters);

    boolean resetPassword(Token t, User u);

    boolean resetPasswordWithToken(Token t, User u, Map<String, String> parameters);

    boolean requestConfirmationEmail(Token t, User u);

    boolean confirmUser(Token t, User u, Map<String, String> parameters);

    Collection<Application> getUserApplications(Token t, User u);
}
