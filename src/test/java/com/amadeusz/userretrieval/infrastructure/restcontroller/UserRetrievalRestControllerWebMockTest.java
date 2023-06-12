package com.amadeusz.userretrieval.infrastructure.restcontroller;

import com.amadeusz.userretrieval.application.model.User;
import com.amadeusz.userretrieval.application.service.UserRequestService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatusCode;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class UserRetrievalRestControllerWebMockTest {

    private static final String USER_TO_BE_RETRIEVED_LOGIN = "octocat";
    private static final Long USER_TO_BE_RETRIEVED_ID = 583231L;
    private static final String USER_TO_BE_RETRIEVED_NAME = "The Octocat";
    private static final String USER_TO_BE_RETRIEVED_TYPE = "User";
    private static final String USER_TO_BE_RETRIEVED_AVATAR_URL = "https://avatars.githubusercontent.com/u/583231?v=4";
    private static final LocalDateTime USER_TO_BE_RETRIEVED_CREATED_AT = LocalDateTime.parse("2011-01-25T18:44:36Z",
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'"));
    public static final String USER_CONTROLLER_PATH = "/api/v1.0.0/users/";
    public static final int ERROR_HTTP_STATUS_CODE = 404;
    public static final String EXPECTED_EXCEPTION = "HttpClientErrorException";
    public static final String EXPECTED_EXCEPTION_DESCRIPTION = "404 NOT_FOUND";

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserRequestService requestService;

    @Test
    void shouldRetrieveUserByLogin() throws Exception {
        when(requestService.retrieveUserByLogin(USER_TO_BE_RETRIEVED_LOGIN))
                .thenReturn(generateSampleUser());
        this.mockMvc.perform(get(USER_CONTROLLER_PATH + USER_TO_BE_RETRIEVED_LOGIN))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(USER_TO_BE_RETRIEVED_ID))
                .andExpect(MockMvcResultMatchers.jsonPath("$.login").value(USER_TO_BE_RETRIEVED_LOGIN))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(USER_TO_BE_RETRIEVED_NAME))
                .andExpect(MockMvcResultMatchers.jsonPath("$.avatarUrl").value(USER_TO_BE_RETRIEVED_AVATAR_URL))
                .andExpect(MockMvcResultMatchers.jsonPath("$.createdAt").value(USER_TO_BE_RETRIEVED_CREATED_AT.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.calculations").value(0.0d))
                .andDo(print());
    }

    @Test
    void shouldHandleException_whenTryingToRetrieveUserByLogin_andExceptionWasThrown() throws Exception {
        HttpClientErrorException httpClientErrorException = new HttpClientErrorException(HttpStatusCode.valueOf(ERROR_HTTP_STATUS_CODE));
        doThrow(httpClientErrorException)
                .when(requestService)
                .retrieveUserByLogin(USER_TO_BE_RETRIEVED_LOGIN);

        this.mockMvc.perform(get(USER_CONTROLLER_PATH + USER_TO_BE_RETRIEVED_LOGIN))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value(EXPECTED_EXCEPTION))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(EXPECTED_EXCEPTION_DESCRIPTION))
                .andDo(print());
    }

    private static User generateSampleUser() {
        return new User(USER_TO_BE_RETRIEVED_ID,
                USER_TO_BE_RETRIEVED_LOGIN,
                USER_TO_BE_RETRIEVED_NAME,
                USER_TO_BE_RETRIEVED_TYPE,
                USER_TO_BE_RETRIEVED_AVATAR_URL,
                USER_TO_BE_RETRIEVED_CREATED_AT,
                0.0d);
    }

}