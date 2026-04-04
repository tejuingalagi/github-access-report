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

    // Supports both ORG and USER
    public List<Repo> getRepos(String org) {
        try {
            // Try organization first
            return webClient.get()
                    .uri("/orgs/{org}/repos?per_page=100", org)
                    .retrieve()
                    .bodyToFlux(Repo.class)
                    .collectList()
                    .block();

        } catch (Exception e) {
            System.err.println("Org not found, falling back to user repos");

            try {
                // Fallback to user repos
                return webClient.get()
                        .uri("/users/{org}/repos?per_page=100", org)
                        .retrieve()
                        .bodyToFlux(Repo.class)
                        .collectList()
                        .block();

            } catch (Exception ex) {
                System.err.println("Error fetching repos: " + ex.getMessage());
                return Collections.emptyList();
            }
        }
    }

    public List<User> getCollaborators(String org, String repo) {
        try {
            return webClient.get()
                    .uri("/repos/{org}/{repo}/collaborators?per_page=100", org, repo)
                    .retrieve()
                    .bodyToFlux(User.class)
                    .collectList()
                    .block();

        } catch (Exception e) {
            System.err.println("Error fetching collaborators: " + e.getMessage());
            return Collections.emptyList();
        }
    }
}