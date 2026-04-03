package com.teju.githubaccess.model;

import lombok.Data;

@Data
public class Permissions {
    private boolean admin;
    private boolean push;
    private boolean pull;
}