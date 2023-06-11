package com.amadeusz.userretrieval.infrastructure;

import com.amadeusz.userretrieval.infrastructure.entity.UserRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRequestJpaRepository extends JpaRepository<UserRequest, String> {
}
