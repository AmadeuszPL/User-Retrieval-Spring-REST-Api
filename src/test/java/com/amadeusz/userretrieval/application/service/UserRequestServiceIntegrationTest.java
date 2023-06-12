package com.amadeusz.userretrieval.application.service;

import com.amadeusz.userretrieval.application.model.User;
import com.amadeusz.userretrieval.infrastructure.repository.UserRequestJpaRepository;
import com.amadeusz.userretrieval.infrastructure.repository.entity.UserRequest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class UserRequestServiceIntegrationTest {

    private static final String USER_TO_BE_RETRIEVED_LOGIN = "octocat";
    private static final Long USER_TO_BE_RETRIEVED_ID = 583231L;
    private static final String USER_TO_BE_RETRIEVED_NAME = "The Octocat";
    private static final String USER_TO_BE_RETRIEVED_TYPE = "User";
    private static final String USER_TO_BE_RETRIEVED_AVATAR_URL = "https://avatars.githubusercontent.com/u/583231?v=4";
    private static final LocalDateTime USER_TO_BE_RETRIEVED_CREATED_AT = LocalDateTime.parse("2011-01-25T18:44:36Z",
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'"));

    @Autowired
    private UserRequestService userRequestService;
    @Autowired
    private UserRequestJpaRepository userRequestJpaRepository;

    @Test
    void shouldRetrieveUserByLogin() {
        User user = userRequestService.retrieveUserByLogin(USER_TO_BE_RETRIEVED_LOGIN);

        assertThat(user).isNotNull()
                .extracting(User::id,
                        User::login,
                        User::name,
                        User::type,
                        User::avatarUrl,
                        User::createdAt)
                .containsExactly(USER_TO_BE_RETRIEVED_ID,
                        USER_TO_BE_RETRIEVED_LOGIN,
                        USER_TO_BE_RETRIEVED_NAME,
                        USER_TO_BE_RETRIEVED_TYPE,
                        USER_TO_BE_RETRIEVED_AVATAR_URL,
                        USER_TO_BE_RETRIEVED_CREATED_AT);
    }

    @Test
    void shouldRegisterUserRetrieval_whenRetrievalWasRegisteredBeforeForThisUser() {
        User userForRetrievalRegistering = generateUserForRetrievalRegistering();

        userRequestService.registerUserRetrieval(userForRetrievalRegistering);
        userRequestService.registerUserRetrieval(userForRetrievalRegistering);
        UserRequest referenceById = userRequestJpaRepository.findById(USER_TO_BE_RETRIEVED_LOGIN).get();

        assertThat(referenceById)
                .isNotNull();
        assertThat(referenceById.getRequestCount()).isEqualTo(2L);
    }

    @Test
    void shouldRegisterUserRetrieval_whenRetrievalWasNotRegisteredBeforeForThisUser() {
        User userForRetrievalRegistering = generateUserForRetrievalRegistering();

        userRequestService.registerUserRetrieval(userForRetrievalRegistering);
        UserRequest referenceById = userRequestJpaRepository.findById(USER_TO_BE_RETRIEVED_LOGIN).get();

        assertThat(referenceById)
                .isNotNull();
        assertThat(referenceById.getRequestCount()).isEqualTo(1L);
    }

    private static User generateUserForRetrievalRegistering() {
        return new User(USER_TO_BE_RETRIEVED_ID,
                USER_TO_BE_RETRIEVED_LOGIN,
                USER_TO_BE_RETRIEVED_NAME,
                USER_TO_BE_RETRIEVED_TYPE,
                USER_TO_BE_RETRIEVED_AVATAR_URL,
                USER_TO_BE_RETRIEVED_CREATED_AT,
                0.0);
    }

}