package com.cm20257.frontend;

/**
 * This is a singleton used to handle the currently logged in user's token
 */
public class UserHandler {
    private static final UserHandler instance = new UserHandler();
    private String token;
    private String username;

    UserHandler() {
        token = "";
        username = "";
    }

    public static synchronized String getToken() {
        return instance.token;
    }

    public static synchronized String getUsername() {
        return instance.username;
    }

    public static synchronized void setToken(String token) {
        instance.token = token;
    }

    public static synchronized void setUsername(String name) {
        instance.username = name;
    }

}
