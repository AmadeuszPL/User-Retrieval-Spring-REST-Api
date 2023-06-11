package com.amadeusz.userretrieval.application.model;

import java.time.LocalDateTime;

public record User(Long id,
                   String login,
                   String name,
                   String type,
                   String avatarUrl,
                   LocalDateTime createdAt,
                   double calculations) {
}
