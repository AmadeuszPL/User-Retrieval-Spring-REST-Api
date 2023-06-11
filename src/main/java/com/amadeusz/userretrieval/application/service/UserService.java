package com.amadeusz.userretrieval.application.service;

import com.amadeusz.userretrieval.application.model.User;
import lombok.NonNull;

public interface UserService {

    User retrieveUserByLogin(@NonNull String login);
}
