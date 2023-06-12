package com.amadeusz.userretrieval.infrastructure.restcontroller;

import com.amadeusz.userretrieval.application.model.User;
import com.amadeusz.userretrieval.infrastructure.ExceptionResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserRetrievalRestControllerTest {

    public static final String EXPECTED_USER_LOGIN = "AmadeuszPL";
    public static final String NOT_EXISTING_USER_LOGIN = "AmadeuszPL2";
    public static final String EXPECTED_USER_TYPE = "User";
    public static final String USERS_API_PATH = "/api/v1.0.0/users/";
    @Value(value="${local.server.port}")
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldRetrieveUserByLogin() {
        assertThat(this.restTemplate.getForObject("http://localhost:" + port
                        + USERS_API_PATH + EXPECTED_USER_LOGIN, User.class))
                .isNotNull()
                .extracting(User::login,
                        User::type)
                .containsExactly(EXPECTED_USER_LOGIN,
                        EXPECTED_USER_TYPE);
    }

    @Test
    void shouldRetrieveExceptionResponse_whenTryingToRetrieveUser_andUserWithGivenLoginNotExist() {
        assertThat(this.restTemplate.getForObject("http://localhost:" + port
                + USERS_API_PATH + NOT_EXISTING_USER_LOGIN, ExceptionResponse.class))
                .isNotNull()
                .extracting(ExceptionResponse::getError,
                        ExceptionResponse::getDescription)
                .containsExactly("NotFound",
                        "404 Not Found: \"{\"message\":\"Not Found\",\"documentation_url\":\"https://docs.github.com/rest/reference/users#get-a-user\"}\"");
    }

}