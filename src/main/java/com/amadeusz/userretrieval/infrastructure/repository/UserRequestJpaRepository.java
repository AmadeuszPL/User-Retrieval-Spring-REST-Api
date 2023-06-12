package com.amadeusz.userretrieval.infrastructure.repository;

import com.amadeusz.userretrieval.infrastructure.repository.entity.UserRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRequestJpaRepository extends JpaRepository<UserRequest, String> {
}
