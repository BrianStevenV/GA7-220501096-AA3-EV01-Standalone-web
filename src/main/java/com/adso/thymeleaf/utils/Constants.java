package com.adso.thymeleaf.utils;

public class Constants {
    public Constants(){ throw new IllegalStateException("Utility class"); }

    public static final String GET_USER_FORM = "/user/form";
    public static final String URI = "/user";

    public static final String MODEL_ATTRIBUTE_ACTION = "action";
    public static final String MODEL_ATTRIBUTE_USER = "user";
    public static final String MODEL_ATTRIBUTE_MESSAGE = "message";
    public static final String MODEL_ATTRIBUTE_SEARCHED = "searched";

    public static final String ACTION_GET_ALL_USERS =  "getAllUsers";
    public static final String ACTION_UPDATE_USER = "updateUser";
    public static final String ACTION_GET_USER = "getUser";
    public static final String ACTION_ATTRIBUTE_USERS = "users";

    public static final String USER_FORM = "user_form";

    public static final String MESSAGE_CREATE_USER = "User created successfully!";
    public static final String MESSAGE_DELETE_USER = "User deleted successfully!";
    public static final String MESSAGE_USER_NOT_FOUND = "User not found";
    public static final String MESSAGE_USER_ALREADY_EXISTS = "User already exists";
    public static final String MESSAGE_USER_DELETED = "User deleted successfully";
    public static final String MESSAGE_USER_UPDATED = "User updated successfully";


    public static final String ERROR_MESSAGE_ACTION_SAVE = "Error saving user: ";
    public static final String ERROR_MESSAGE_ACTION_DELETE = "Error deleting user: ";
    public static final String ERROR_MESSAGE_ACTION_UPDATE = "Error updating user: ";
    public static final String REDIRECT = "redirect:/";
}
