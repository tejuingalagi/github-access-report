package com.teju.githubaccess.model;

import lombok.Data;

@Data
public class User {
    private String login;
    private Permissions permissions;
}