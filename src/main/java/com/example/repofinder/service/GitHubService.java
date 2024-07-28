package com.example.repofinder.service;

import com.example.repofinder.exception.UserNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GitHubService {

    private final WebClient webClient;

    public GitHubService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.github.com").build();
    }

    public List<Map<String, Object>> getUserRepos(String username){
        try {
            List<Map> repos = webClient.get()
                    .uri("/users/{username}/repos", username)
                    .retrieve()
                    .bodyToFlux(Map.class)
                    .collectList()
                    .block();

            if (repos == null) {
                throw new UserNotFoundException("User not found");
            } else {
                return repos.stream()
                        .filter(repo -> !Boolean.TRUE.equals(repo.get("fork")))
                        .map(repo -> {
                            Map<String, Object> repoDetails = Map.of(
                                    "Repository Name", repo.get("name"),
                                    "Owner Login", ((Map<String, Object>) repo.get("owner")).get("login"),
                                    "Branches", getBranches(username, (String) repo.get("name"))
                            );
                            return repoDetails;
                        })
                        .collect(Collectors.toList());
            }



        } catch (WebClientResponseException.NotFound e){
            throw new UserNotFoundException("User not found");
        }

    }

    private List<Map<String, String>> getBranches(String username, String repoName) {
        return webClient.get()
                .uri("/repos/{username}/{repoName}/branches", username, repoName)
                .retrieve()
                .bodyToFlux(Map.class)
                .collectList()
                .block()
                .stream()
                .map(branch -> Map.of(
                        "Branch Name", (String) branch.get("name"),
                        "Last Commit SHA", ((Map<String, String>) branch.get("commit")).get("sha")
                ))
                .collect(Collectors.toList());
    }

}
