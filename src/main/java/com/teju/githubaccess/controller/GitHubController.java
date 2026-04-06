package com.teju.githubaccess.controller;

import com.teju.githubaccess.model.UserRepoResponse;
import com.teju.githubaccess.service.GitHubService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/github")
public class GitHubController {

    private final GitHubService gitHubService;

    public GitHubController(GitHubService gitHubService) {
        this.gitHubService = gitHubService;
    }

    @GetMapping("/access-report")
    public List<UserRepoResponse> getReport(@RequestParam String org) {
        return gitHubService.getAccessReport(org);
    }
}