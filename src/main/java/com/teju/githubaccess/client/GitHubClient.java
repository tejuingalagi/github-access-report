package com.teju.githubaccess.client;

import com.teju.githubaccess.model.Repo;
import com.teju.githubaccess.model.User;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;
import java.util.List;

@Service
public class GitHubClient {

    private final WebClient webClient;

    public GitHubClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public List<Repo> getRepos(String org) {
        try {
            return webClient.get()
                    .uri("/orgs/{org}/repos?per_page=100", org)
                    .retrieve()
                    .onStatus(status -> status.isError(), response ->
                            response.bodyToMono(String.class)
                                    .map(msg -> new RuntimeException("GitHub API error (org repos): " + msg))
                    )
                    .bodyToFlux(Repo.class)
                    .collectList()
                    .block();

        } catch (Exception e) {
            // fallback to user repos
            try {
                return webClient.get()
                        .uri("/users/{org}/repos?per_page=100", org)
                        .retrieve()
                        .onStatus(status -> status.isError(), response ->
                                response.bodyToMono(String.class)
                                        .map(msg -> new RuntimeException("GitHub API error (user repos): " + msg))
                        )
                        .bodyToFlux(Repo.class)
                        .collectList()
                        .block();

            } catch (Exception ex) {
                return Collections.emptyList();
            }
        }
    }

    public List<User> getCollaborators(String org, String repo) {
        try {
            return webClient.get()
                    .uri("/repos/{org}/{repo}/collaborators?per_page=100", org, repo)
                    .retrieve()
                    .onStatus(status -> status.isError(), response ->
                            response.bodyToMono(String.class)
                                    .map(msg -> new RuntimeException("GitHub API error (collaborators): " + msg))
                    )
                    .bodyToFlux(User.class)
                    .collectList()
                    .block();

        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}