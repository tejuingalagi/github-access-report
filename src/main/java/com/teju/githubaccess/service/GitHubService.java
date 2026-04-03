package com.teju.githubaccess.service;

import com.teju.githubaccess.client.GitHubClient;
import com.teju.githubaccess.model.Repo;
import com.teju.githubaccess.model.RepoAccess;
import com.teju.githubaccess.model.User;
import com.teju.githubaccess.model.UserRepoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GitHubService {

    @Autowired
    private GitHubClient gitHubClient;

    public List<UserRepoResponse> getAccessReport(String org) {

        List<Repo> repos = gitHubClient.getRepos(org);

        // Thread-safe map (important for parallel)
        Map<String, List<RepoAccess>> map = new ConcurrentHashMap<>();

        // Parallel processing (for scale)
        repos.parallelStream().forEach(repo -> {

            List<User> users = gitHubClient.getCollaborators(org, repo.getName());

            for (User user : users) {
            	RepoAccess repoAccess = new RepoAccess();
            	repoAccess.setRepository(repo.getName());
            	repoAccess.setAccess("COLLABORATOR");

            	map.computeIfAbsent(user.getLogin(),
            	        k -> Collections.synchronizedList(new ArrayList<>()))
            	   .add(repoAccess);;
            }
        });

        // Convert Map → List<Response>
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