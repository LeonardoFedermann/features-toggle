package com.project.featurestoggle.utils;

public class Constants {
    public static final String PASSWORD_FORMAT_REGEX = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{5,20}$";
    public static final int NAME_LIMIT_OF_CHARACTERS = 40;
    public static final int NAME_MINIMUM_CHARACTERS = 3;
    public static final String USER_NOT_FOUND_MESSAGE = "User not found";
}
