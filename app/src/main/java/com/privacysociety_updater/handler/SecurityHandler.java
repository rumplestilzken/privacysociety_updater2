package com.privacysociety_updater.handler;

public class SecurityHandler {
    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        SecurityHandler.password = password;
    }

    private static String password = "";
}
