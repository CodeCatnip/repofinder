package com.example.repofinder;

import com.example.repofinder.exception.RestExceptionHandler;
import com.example.repofinder.exception.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RestExceptionHandlerTest {

    @InjectMocks
    private RestExceptionHandler restExceptionHandler;

    @Test
    void should_ReturnCorrectData_When_UserNotFoundExceptionThrown(){
        //given
        UserNotFoundException e = new UserNotFoundException("User not found");

        //when
        ResponseEntity<Map<String, Object>> responseEntity = restExceptionHandler.handleException(e);

        //then
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NOT_FOUND.value(), responseEntity.getBody().get("status"));
        assertEquals("User not found", responseEntity.getBody().get("message"));
    }

    @Test
    void should_ReturnCorrectData_When_NullException(){
        //given
        UserNotFoundException e = new UserNotFoundException(null);

        //when
        ResponseEntity<Map<String, Object>> responseEntity = restExceptionHandler.handleException(e);

        //then
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NOT_FOUND.value(), responseEntity.getBody().get("status"));
        assertNull(responseEntity.getBody().get("message"));
    }
}