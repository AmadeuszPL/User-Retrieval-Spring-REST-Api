package com.amadeusz.userretrieval.application.service;

import com.amadeusz.userretrieval.application.model.User;
import lombok.NonNull;

public interface UserRequestService {

    User retrieveUserByLogin(@NonNull String login);

    void registerUserRetrieval(@NonNull User user);
}
