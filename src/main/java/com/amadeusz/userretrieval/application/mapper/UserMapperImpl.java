package com.amadeusz.userretrieval.application.mapper;

import com.amadeusz.userretrieval.application.model.User;
import com.amadeusz.userretrieval.application.model.UserDto;
import org.springframework.stereotype.Component;

@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User map(UserDto userDto) {
        double calculations = 0.0d;
        if (userDto.followers() != 0) {
            calculations = (double) 6 / userDto.followers() * (2 + userDto.public_repos());
        }
        return new User(userDto.id(), userDto.login(), userDto.name(), userDto.type(),
                userDto.avatar_url(), userDto.created_at(), calculations);
    }
}
