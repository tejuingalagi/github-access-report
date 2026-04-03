package com.teju.githubaccess.client;

import com.teju.githubaccess.model.Repo;
import com.teju.githubaccess.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;
import java.util.List;

@Service
public class GitHubClient {

    @Autowired
    private WebClient webClient;

    // Get repos (USER based)
    public List<Repo> getRepos(String org) {
        try {
            return webClient.get()
                    .uri("/users/{org}/repos?per_page=100", org)
                    .retrieve()
                    .bodyToFlux(Repo.class)
                    .collectList()
                    .block();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    // Get contributors (works for public repos)
    public List<User> getCollaborators(String org, String repo) {
        try {
            return webClient.get()
                    .uri("/repos/{org}/{repo}/contributors?per_page=100", org, repo)
                    .retrieve()
                    .bodyToFlux(User.class)
                    .collectList()
                    .block();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}