package com.teju.githubaccess.service;

import com.teju.githubaccess.client.GitHubClient;
import com.teju.githubaccess.model.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GitHubService {

    private final GitHubClient gitHubClient;

    public GitHubService(GitHubClient gitHubClient) {
        this.gitHubClient = gitHubClient;
    }

    public List<UserRepoResponse> getAccessReport(String org) {

        List<Repo> repos = gitHubClient.getRepos(org);

        Map<String, List<RepoAccess>> map = new ConcurrentHashMap<>();

        repos.parallelStream().forEach(repo -> {

            List<User> users = gitHubClient.getCollaborators(org, repo.getName());

            for (User user : users) {

                RepoAccess repoAccess = new RepoAccess();
                repoAccess.setRepository(repo.getName());

                String access;

                if (user.getPermissions() != null) {
                    if (user.getPermissions().isAdmin()) {
                        access = "ADMIN";
                    } else if (user.getPermissions().isPush()) {
                        access = "WRITE";
                    } else {
                        access = "READ";
                    }
                } else {
                    access = "UNKNOWN";
                }

                repoAccess.setAccess(access);

                map.computeIfAbsent(
                        user.getLogin(),
                        k -> Collections.synchronizedList(new ArrayList<>())
                ).add(repoAccess);
            }
        });

        List<UserRepoResponse> response = new ArrayList<>();

        for (Map.Entry<String, List<RepoAccess>> entry : map.entrySet()) {
            UserRepoResponse dto = new UserRepoResponse();
            dto.setUsername(entry.getKey());
            dto.setRepositories(entry.getValue());
            response.add(dto);
        }

        return response;
    }
}