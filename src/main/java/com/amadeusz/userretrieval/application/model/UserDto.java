package com.amadeusz.userretrieval.application.model;

import java.time.LocalDateTime;

public record UserDto(long id,
                      String login,
                      String type,
                      String name,
                      String avatar_url,
                      LocalDateTime created_at,
                      long followers,
                      int public_repos) {
}
