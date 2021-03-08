package com.cm20257.frontend;

/**
 * This is a singleton used to handle the currently logged in user's token
 */
public class UserHandler {
    private static final UserHandler INSTANCE = new UserHandler();
    private String token;
    private String username;

    UserHandler() {
        token = "";
    }

    public static synchronized String getToken() {
        return INSTANCE.token;
    }

    public static synchronized String getUsername() {
        return INSTANCE.username;
    }

    public static synchronized void setToken(String token) {
        INSTANCE.token = token;
    }

    public static synchronized void setUsername(String name) {
        INSTANCE.username = name;
    }

}
