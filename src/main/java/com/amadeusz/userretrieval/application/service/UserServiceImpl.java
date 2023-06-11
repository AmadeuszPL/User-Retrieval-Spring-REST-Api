package com.amadeusz.userretrieval.application.service;

import com.amadeusz.userretrieval.application.mapper.UserMapper;
import com.amadeusz.userretrieval.application.model.User;
import com.amadeusz.userretrieval.application.model.UserDto;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final String USER_RETRIEVAL_RESOURCE_URL = "https://api.github.com/users/";

    private final UserMapper userMapper;
    private final RestTemplate restTemplate;

    @Override
    public User retrieveUserByLogin(@NonNull String login) {
        String userRetrievalByLoginUrl = USER_RETRIEVAL_RESOURCE_URL + login;
        UserDto retrievedUserDto = restTemplate.getForObject(userRetrievalByLoginUrl, UserDto.class);
        return userMapper.map(retrievedUserDto);
    }
}
