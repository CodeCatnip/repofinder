package com.example.repofinder.rest;

import com.example.repofinder.exception.UserNotFoundException;
import com.example.repofinder.service.GitHubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class UserRestController {

    private GitHubService gitHubService;

    @Autowired
    public UserRestController(GitHubService gitHubService){
        this.gitHubService = gitHubService;
    }

    @GetMapping("/users/{username}/repos")
    public ResponseEntity<List<Map<String, Object>>> getUserRepos(@PathVariable String username, @RequestHeader(HttpHeaders.ACCEPT) String acceptHeader){
        if(!"application/json".equals(acceptHeader)){
            throw new UserNotFoundException("Accept header must be application/json");
        } else {
            List<Map<String, Object>> repos = gitHubService.getUserRepos(username);
            return ResponseEntity.ok(repos);
        }
    }
}