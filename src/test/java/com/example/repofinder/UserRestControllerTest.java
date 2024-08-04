package com.example.repofinder;

import com.example.repofinder.exception.UserNotFoundException;
import com.example.repofinder.rest.UserRestController;
import com.example.repofinder.service.GitHubService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserRestControllerTest {

    @InjectMocks
    private UserRestController userRestController;

    @Mock
    private GitHubService gitHubServiceMock;


    @Test
    void should_ThrowException_When_InvalidAcceptHeader(){
        //given
        String username = "someUsername";
        String invalidAcceptHeader = "text/plain";

        //when
        Executable executable = ()-> userRestController.getUserRepos(username, invalidAcceptHeader);

        //then
        assertThrows(UserNotFoundException.class, executable);

    }

    @Test
    void should_ReturnRepos_When_ValidAcceptHeader(){
        //given
        String username = "someUsername";
        String validAcceptHeader = "application/json";
        List<Map<String, Object>> expected = List.of(
                Map.of("Repository Name", "repo1", "Owner Login", username, "Branches", List.of(Map.of("Branch Name", "main", "Last Commit SHA", "sha123"))),
                Map.of("Repository Name", "repo2", "Owner Login", username, "Branches", List.of(Map.of("Branch Name", "main", "Last Commit SHA", "sha456")))
        );
        when(gitHubServiceMock.getUserRepos(username)).thenReturn(expected);

        //when
        ResponseEntity<List<Map<String, Object>>> actual = userRestController.getUserRepos(username, validAcceptHeader);

        //then
        assertEquals(200, actual.getStatusCodeValue());
        assertEquals(expected,actual.getBody());
    }

    @Test
    void should_ThrowException_When_UserDoesNotExist(){
        //given
        String username = "nonExistingUser";
        String validAcceptHeader = "application/json";

        when(gitHubServiceMock.getUserRepos(username)).thenThrow(UserNotFoundException.class);

        //when
        Executable executable = ()-> userRestController.getUserRepos(username, validAcceptHeader);

        //then
        assertThrows(UserNotFoundException.class, executable);

    }


}