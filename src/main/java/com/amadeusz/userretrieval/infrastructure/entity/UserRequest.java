package com.amadeusz.userretrieval.infrastructure.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class UserRequest {

    @Id
    @Column(name = "LOGIN")
    private String login;
    @Column(name = "REQUEST_COUNT")
    private long requestCount;
}
