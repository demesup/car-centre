# Car Centre Documentation

## Tech Stack

- Java 17
- Maven
- MySQL connector
- Lombok
- Logback

## Why This Stack

- Java is appropriate for layered CRUD applications.
- MySQL matches the persistence-oriented inventory use case.
- Lombok and Logback reduce boilerplate and improve runtime observability.

## Methodology

- Use a repository/controller style separation.
- Keep database access isolated from domain logic.
- Package the app with Maven for repeatable builds.

