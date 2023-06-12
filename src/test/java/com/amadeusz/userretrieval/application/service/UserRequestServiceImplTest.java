package com.amadeusz.userretrieval.application.service;

import com.amadeusz.userretrieval.application.mapper.UserMapperImpl;
import com.amadeusz.userretrieval.application.model.User;
import com.amadeusz.userretrieval.application.model.UserDto;
import com.amadeusz.userretrieval.infrastructure.UserRequestJpaRepository;
import com.amadeusz.userretrieval.infrastructure.entity.UserRequest;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class UserRequestServiceImplTest {

    private static final String USER_RETRIEVAL_RESOURCE_URL = "https://api.github.com/users/";
    private static final String USER_TO_BE_RETRIEVED_LOGIN = "octocat";
    private static final Long USER_TO_BE_RETRIEVED_ID = 583231L;
    private static final String USER_TO_BE_RETRIEVED_NAME = "The Octocat";
    private static final String USER_TO_BE_RETRIEVED_TYPE = "User";
    private static final String USER_TO_BE_RETRIEVED_AVATAR_URL = "https://avatars.githubusercontent.com/u/583231?v=4";
    private static final long USER_TO_BE_RETRIEVED_NO_FOLLOWERS = 0L;
    private static final long USER_TO_BE_RETRIEVED_SOME_FOLLOWERS = 2L;
    private static final int USER_TO_BE_RETRIEVED_NO_PUBLIC_REPOS = 0;
    private static final int USER_TO_BE_RETRIEVED_SOME_PUBLIC_REPOS = 10;
    private static final double RETRIEVED_USER_WITH_SOME_FOLLOWERS_AND_PUBLIC_REPOS_CALCULATION_RESULT = 36.0d;
    private static final double RETRIEVED_USER_WITH_NO_FOLLOWERS_CALCULATION_RESULT = 0.0d;
    private static final double RETRIEVED_USER_WITH_NO_PUBLIC_REPOS_CALCULATION_RESULT = 6.0d;
    private static final LocalDateTime USER_TO_BE_RETRIEVED_CREATED_AT = LocalDateTime.parse("2011-01-25T18:44:36Z",
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'"));

    @Autowired
    private UserRequestJpaRepository userRequestJpaRepositoryMock = mock(UserRequestJpaRepository.class);
    @Autowired
    private RestTemplate restTemplateMock = mock(RestTemplate.class);
    @Autowired
    private UserRequestService userRequestService = new UserRequestServiceImpl(new UserMapperImpl(),
            userRequestJpaRepositoryMock,
            restTemplateMock);

    @Test
    void shouldRetrieveUserByLogin() {
        UserDto userDto =
                prepareExpectedUserDtoReturnedByRestTemplate(USER_TO_BE_RETRIEVED_SOME_FOLLOWERS, USER_TO_BE_RETRIEVED_SOME_PUBLIC_REPOS);
        when(restTemplateMock.getForObject(USER_RETRIEVAL_RESOURCE_URL + USER_TO_BE_RETRIEVED_LOGIN, UserDto.class))
                .thenReturn(userDto);

        User retrievedUser = userRequestService.retrieveUserByLogin(USER_TO_BE_RETRIEVED_LOGIN);

        assertThat(retrievedUser)
                .isNotNull()
                .extracting(User::id,
                        User::login,
                        User::type,
                        User::name,
                        User::avatarUrl,
                        User::createdAt,
                        User::calculations)
                .containsExactly(
                        USER_TO_BE_RETRIEVED_ID,
                        USER_TO_BE_RETRIEVED_LOGIN,
                        USER_TO_BE_RETRIEVED_TYPE,
                        USER_TO_BE_RETRIEVED_NAME,
                        USER_TO_BE_RETRIEVED_AVATAR_URL,
                        USER_TO_BE_RETRIEVED_CREATED_AT,
                        RETRIEVED_USER_WITH_SOME_FOLLOWERS_AND_PUBLIC_REPOS_CALCULATION_RESULT);
    }

    @Test
    void shouldRetrieveUserByLogin_withNoFollowers() {
        UserDto userDto =
                prepareExpectedUserDtoReturnedByRestTemplate(USER_TO_BE_RETRIEVED_NO_FOLLOWERS, USER_TO_BE_RETRIEVED_SOME_PUBLIC_REPOS);
        when(restTemplateMock.getForObject(USER_RETRIEVAL_RESOURCE_URL + USER_TO_BE_RETRIEVED_LOGIN, UserDto.class))
                .thenReturn(userDto);

        User retrievedUser = userRequestService.retrieveUserByLogin(USER_TO_BE_RETRIEVED_LOGIN);

        assertThat(retrievedUser)
                .isNotNull()
                .extracting(User::id,
                        User::login,
                        User::type,
                        User::name,
                        User::avatarUrl,
                        User::createdAt,
                        User::calculations)
                .containsExactly(
                        USER_TO_BE_RETRIEVED_ID,
                        USER_TO_BE_RETRIEVED_LOGIN,
                        USER_TO_BE_RETRIEVED_TYPE,
                        USER_TO_BE_RETRIEVED_NAME,
                        USER_TO_BE_RETRIEVED_AVATAR_URL,
                        USER_TO_BE_RETRIEVED_CREATED_AT,
                        RETRIEVED_USER_WITH_NO_FOLLOWERS_CALCULATION_RESULT);
    }

    @Test
    void shouldRetrieveUserByLogin_withNoPublicRepos() {
        UserDto userDto =
                prepareExpectedUserDtoReturnedByRestTemplate(USER_TO_BE_RETRIEVED_SOME_FOLLOWERS, USER_TO_BE_RETRIEVED_NO_PUBLIC_REPOS);
        when(restTemplateMock.getForObject(USER_RETRIEVAL_RESOURCE_URL + USER_TO_BE_RETRIEVED_LOGIN, UserDto.class))
                .thenReturn(userDto);

        User retrievedUser = userRequestService.retrieveUserByLogin(USER_TO_BE_RETRIEVED_LOGIN);

        assertThat(retrievedUser)
                .isNotNull()
                .extracting(User::id,
                        User::login,
                        User::type,
                        User::name,
                        User::avatarUrl,
                        User::createdAt,
                        User::calculations)
                .containsExactly(
                        USER_TO_BE_RETRIEVED_ID,
                        USER_TO_BE_RETRIEVED_LOGIN,
                        USER_TO_BE_RETRIEVED_TYPE,
                        USER_TO_BE_RETRIEVED_NAME,
                        USER_TO_BE_RETRIEVED_AVATAR_URL,
                        USER_TO_BE_RETRIEVED_CREATED_AT,
                        RETRIEVED_USER_WITH_NO_PUBLIC_REPOS_CALCULATION_RESULT);
    }

    private static UserDto prepareExpectedUserDtoReturnedByRestTemplate(long userToBeRetrievedSomeFollowers, int userToBeRetrievedNoPublicRepos) {
        return new UserDto(USER_TO_BE_RETRIEVED_ID,
                USER_TO_BE_RETRIEVED_LOGIN,
                USER_TO_BE_RETRIEVED_TYPE,
                USER_TO_BE_RETRIEVED_NAME,
                USER_TO_BE_RETRIEVED_AVATAR_URL,
                USER_TO_BE_RETRIEVED_CREATED_AT,
                userToBeRetrievedSomeFollowers,
                userToBeRetrievedNoPublicRepos);
    }

    @Test
    void shouldThrowException_whenTryingToRetrieveUserByLogin_withNullLogin() {
        assertThatThrownBy(() -> userRequestService.retrieveUserByLogin(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("login is marked non-null but is null");
    }

    @Test
    void shouldRegisterUserRetrieval() {
        ArgumentCaptor<UserRequest> actuallySavedUserRequest = ArgumentCaptor.forClass(UserRequest.class);
        User user = prepareUserToBeRetrieved();
        Optional<UserRequest> optionalUserRequest = prepareOptionalUserRequest(user);
        when(userRequestJpaRepositoryMock.findById(USER_TO_BE_RETRIEVED_LOGIN))
                .thenReturn(optionalUserRequest);

        userRequestService.registerUserRetrieval(user);

        verify(userRequestJpaRepositoryMock).save(actuallySavedUserRequest.capture());
        assertThat(actuallySavedUserRequest.getValue())
                .isNotNull()
                .extracting(UserRequest::getLogin,
                        UserRequest::getRequestCount)
                .containsExactly(optionalUserRequest.get().getLogin(),
                        optionalUserRequest.get().getRequestCount() + 1);
    }

    @Test
    void shouldRegisterUserRetrieval_thatWasNotRegisteredBefore() {
        ArgumentCaptor<UserRequest> actuallySavedUserRequest = ArgumentCaptor.forClass(UserRequest.class);
        User user = prepareUserToBeRetrieved();
        Optional<UserRequest> optionalUserRequest = Optional.empty();
        when(userRequestJpaRepositoryMock.findById(USER_TO_BE_RETRIEVED_LOGIN))
                .thenReturn(optionalUserRequest);

        userRequestService.registerUserRetrieval(user);

        verify(userRequestJpaRepositoryMock).save(actuallySavedUserRequest.capture());
        assertThat(actuallySavedUserRequest.getValue())
                .isNotNull()
                .extracting(UserRequest::getLogin,
                        UserRequest::getRequestCount)
                .containsExactly(user.login(), 1L);
    }

    @Test
    void shouldThrowException_whenTryingToRegisterUserRetrieval_withNullUser() {
        assertThatThrownBy(() -> userRequestService.registerUserRetrieval(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("user is marked non-null but is null");
    }

    private static Optional<UserRequest> prepareOptionalUserRequest(User user) {
        UserRequest userRequestFromRepository = new UserRequest();
        userRequestFromRepository.setRequestCount(4);
        userRequestFromRepository.setLogin(user.login());
        return Optional.of(userRequestFromRepository);
    }

    private static User prepareUserToBeRetrieved() {
        return new User(USER_TO_BE_RETRIEVED_ID,
                USER_TO_BE_RETRIEVED_LOGIN,
                USER_TO_BE_RETRIEVED_NAME,
                USER_TO_BE_RETRIEVED_TYPE,
                USER_TO_BE_RETRIEVED_AVATAR_URL,
                USER_TO_BE_RETRIEVED_CREATED_AT,
                0.0d);
    }

}