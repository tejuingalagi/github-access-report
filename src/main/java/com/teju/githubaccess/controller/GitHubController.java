package com.teju.githubaccess.controller;

import com.teju.githubaccess.model.UserRepoResponse;
import com.teju.githubaccess.service.GitHubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/github")
public class GitHubController {

    @Autowired
    private GitHubService gitHubService;

    @GetMapping("/access-report")
    public List<UserRepoResponse> getReport(@RequestParam String org) {
        return gitHubService.getAccessReport(org);
    }
}