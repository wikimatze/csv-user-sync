# CSV user sync

TLDR; A Spring Boot application that synchronizes `user accounts` and `roles` (permissions) from legacy SAP CSV exports into a PostgreSQL database. The service runs as a `daily scheduled job` and will be the foundation for a future modern authentication system using OpenID Connect.

This is my first Spring Boot application from scratch. So I'm better documenting
more than I should so that I can later refer to it later and learn from my mistakes.


## Tech and Dev-Stack

| Tool           | Version | Purpose                            | Link                                                                     |
|----------------|---------|------------------------------------|--------------------------------------------------------------------------|
| Spring Boot    | 4.1.0   | Framework                          | [spring.io/projects/spring-boot](https://spring.io/projects/spring-boot) |
| Kotlin         | –       | Programming language               | [kotlinlang.org](https://kotlinlang.org/)                                |
| Keycloak       | –       | Identity & Access Management (IAM) | [keycloak.org](https://www.keycloak.org/)                                |
| PostgreSQL     | –       | Database                           | [postgresql.org](https://www.postgresql.org/)                            |
| Docker Compose | –       | Container orchestration            | [docker.com/compose](https://www.docker.com/products/docker-compose/)    |


I'm not using my preferred editor Vim because [IntelliJ IDEA 2026.1.4 with JDK 25](https://www.jetbrains.com/idea/download/?section=linux) is packaging
all Java and project dependencies for me very nicely. Please note, that IntelliJ is also pushing AI everywhere, but I disabled
that plugin and want to explore things on my own.


## Project generation steps & dependencies

- ![spring-boot-intellij-setup](doc/images/spring-boot-intellij-setup.png)
- ![spring-boot-intellij-developer-tools](doc/images/spring-boot-intellij-developer-tools.png)
- ![spring-boot-intellij-developer-web](doc/images/spring-boot-intellij-web.png)
- ![spring-boot-intellij-developer-security](doc/images/spring-boot-intellij-security.png)
- ![spring-boot-intellij-developer-sql](doc/images/spring-boot-intellij-sql.png)
- [flyway](https://documentation.red-gate.com/flyway/getting-started-with-flyway): for migrations and table creation


## Plan

- adding a parser for the CSV file (in a TDD like approach) => [MR](https://github.com/wikimatze/csv-user-sync/pull/1)
- adding DB migration, entity and repository for user => [MR](https://github.com/wikimatze/csv-user-sync/pull/2)
- adding DB migration, entity and repository for roles => [MR](https://github.com/wikimatze/csv-user-sync/pull/4)
- adding generic filter for the parser => [MR](https://github.com/wikimatze/csv-user-sync/pull/3)


## Architecture

As a mermaid diagram


```mermaid
flowchart LR

    subgraph SAP["Bestehendes System"]
        A["SAP"]
        B["CSV-Export (täglich)"]
        A --> B
    end

    subgraph Import["Import-Anwendung"]
        C["CSV Parser"]
        D["Validierung"]
        E["Mapping"]
        F["Persistierung"]

        C --> D --> E --> F
    end

    subgraph DB["DB"]
        H[(Users)]
        I[(Roles/Permissions)]
    end

    subgraph Auth["Zukünftige Authentifizierung"]
        K["Keycloak<br/>oder Entra ID <br/>oder Okta"]
    end

    subgraph API["Modernisierte APIs"]
        L["REST APIs"]
        M["Banken / Anwendungen"]
    end

    B --> C
    F --> H
    F --> I

    H --> K
    I --> K

    K -->|"OpenID Connect / OAuth2"| L
    L --> M
    

    classDef sap fill:#f5f5f5,stroke:#666;
    classDef poc fill:#d4edda,stroke:#2e7d32;
    classDef db fill:#fff3cd,stroke:#f9a825;
    classDef target fill:#dbeafe,stroke:#1565c0;

    class A,B sap;
    class C,D,E,F,G poc;
    class H,I,J db;
    class K,L,M target;
```


## Useful commands

- `./gradlew build` ... build and install all the dependencies
- `./gradlew bootRun` ... start the application
- `./gradlew build --refresh-dependencies` ... update dependencies
