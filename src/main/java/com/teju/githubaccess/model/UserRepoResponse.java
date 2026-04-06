package com.teju.githubaccess.model;

import lombok.Data;
import java.util.List;

@Data
public class UserRepoResponse {
    private String username;
    private List<RepoAccess> repositories;
}