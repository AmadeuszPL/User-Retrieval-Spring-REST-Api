package com.amadeusz.userretrieval.application.service;

import com.amadeusz.userretrieval.UserRetrievalConfiguration;
import com.amadeusz.userretrieval.application.mapper.UserMapperImpl;
import com.amadeusz.userretrieval.application.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

@RestClientTest
@ContextConfiguration(classes = {UserServiceImpl.class, UserMapperImpl.class, UserRetrievalConfiguration.class})
class UserServiceImplIntegrationTest {

    private static final String USER_TO_BE_RETRIEVED_LOGIN = "octocat";
    private static final Long USER_TO_BE_RETRIEVED_ID = 583231L;
    private static final String USER_TO_BE_RETRIEVED_NAME = "The Octocat";
    private static final String USER_TO_BE_RETRIEVED_AVATAR_URL = "https://avatars.githubusercontent.com/u/583231?v=4";
    private static final LocalDateTime USER_TO_BE_RETRIEVED_CREATED_AT = LocalDateTime.parse("2011-01-25T18:44:36Z",
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'"));
    @Autowired
    private UserService userService;

    @Test
    void shouldRetrieveUserByLogin() {
        User user = userService.retrieveUserByLogin(USER_TO_BE_RETRIEVED_LOGIN);

        assertThat(user).isNotNull()
                .extracting(User::id,
                        User::login,
                        User::name,
                        User::avatarUrl,
                        User::createdAt)
                .containsExactly(USER_TO_BE_RETRIEVED_ID,
                        USER_TO_BE_RETRIEVED_LOGIN,
                        USER_TO_BE_RETRIEVED_NAME,
                        USER_TO_BE_RETRIEVED_AVATAR_URL,
                        USER_TO_BE_RETRIEVED_CREATED_AT);
    }

}