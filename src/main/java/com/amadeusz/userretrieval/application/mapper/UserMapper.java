package com.amadeusz.userretrieval.application.mapper;

import com.amadeusz.userretrieval.application.model.User;
import com.amadeusz.userretrieval.application.model.UserDto;

public interface UserMapper {

    User map(UserDto userDto);
}
