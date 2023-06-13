# User Retrieval Spring REST Api

## General approach

Application with some of Java 17 features with use of Spring Boot.
After hitting API own REST Endpoint:
```
GET /api/v1.0.0/users/{login}
```
Service will hit remote REST Endpoint of GitHub API, retrieving data about USER,
mapping this data to wanted format and returning in own API
as JSON containing:

```json
{
"id": "...",
"login": "...",
"name": "...",
"type": "...",
"avatarUrl": "...",
"createdAt": "...",
"calculations": "..."
}
```

For persistence, I've used H2 database and JpaRepository, but thanks to using Spring
you can easily switch to any database engine.

I used lombok for:
- constructor dependency injection
- generating required constructors
- generating getters and setters
- @NonNull validation with custom exception

As testing frameworks I've used JUnit5 + AssertJ + Mockito + Spring MockMvc.
Persistence tests are supposed to be stateless.

Application is divided in Infrastructure and Application packages.
In Application package there are services, models and related mappers.
In Infrastructure package are REST controllers, entities, repositories (persistence),
and Api specific Exception Handlers.

## Business logic assumptions - simplifications

1. RestTemplate default exceptions are enough for purpose of this task.
2. Data received from GitHub api will be complete and correct
   ( for example no negative number of repositories / followers ).