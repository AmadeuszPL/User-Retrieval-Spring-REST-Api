package com.amadeusz.userretrieval.infrastructure.restcontroller;

import com.amadeusz.userretrieval.application.model.User;
import com.amadeusz.userretrieval.application.service.UserRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1.0.0/users")
public class UserRetrievalRestController {

    @Autowired
    private UserRequestService userRequestService;

    @GetMapping("/{login}")
    @ResponseBody
    public User retrieveUserByLogin(@PathVariable String login) {
        User user = userRequestService.retrieveUserByLogin(login);
        userRequestService.registerUserRetrieval(user);
        return user;
    }
}
