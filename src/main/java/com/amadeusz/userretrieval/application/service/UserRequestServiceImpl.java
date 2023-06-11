package com.amadeusz.userretrieval.application.service;

import com.amadeusz.userretrieval.application.mapper.UserMapper;
import com.amadeusz.userretrieval.application.model.User;
import com.amadeusz.userretrieval.application.model.UserDto;
import com.amadeusz.userretrieval.infrastructure.UserRequestJpaRepository;
import com.amadeusz.userretrieval.infrastructure.entity.UserRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserRequestServiceImpl implements UserRequestService {

    private static final String USER_RETRIEVAL_RESOURCE_URL = "https://api.github.com/users/";

    private final UserMapper userMapper;
    private final UserRequestJpaRepository userRequestJpaRepository;
    private final RestTemplate restTemplate;

    @Override
    public User retrieveUserByLogin(@NonNull String login) {
        String userRetrievalByLoginUrl = USER_RETRIEVAL_RESOURCE_URL + login;
        UserDto retrievedUserDto = restTemplate.getForObject(userRetrievalByLoginUrl, UserDto.class);
        return userMapper.map(retrievedUserDto);
    }

    @Override
    public void registerUserRetrieval(@NonNull User user) {
        Optional<UserRequest> userRequestInRepository = userRequestJpaRepository.findById(user.login());
        UserRequest userRequest = userRequestInRepository.map(uR -> new UserRequest(uR.getLogin(), uR.getRequestCount() + 1))
                .orElseGet(() -> new UserRequest(user.login(), 1));
        userRequestJpaRepository.save(userRequest);
    }
}
