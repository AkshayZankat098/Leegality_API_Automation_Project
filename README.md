# Java REST Assured API Automation Framework

A Gradle-based Java API test framework for the Restful Booker API using REST Assured and TestNG.

## Requirements

- JDK 17 or newer
- Gradle 8.14.2 or newer

The Gradle wrapper is included, so a separate Gradle installation is not required.

## Run the tests

From the project root:

```powershell
.\gradlew.bat test
```

To run against a different API, override the default base URL. The system property takes precedence over the environment variable and `config.properties`:

```powershell
.\gradlew.bat test -Dbase.url=https://api.example.com
```

Alternatively, set `API_BASE_URL` in the environment.

## Project layout

- `src/main/java/BaseService.java` — shared API configuration and request specification
- `src/main/java/AuthService.java` — authentication operations
- `src/main/java/BookingService.java` — booking and health-check operations
- `src/main/java/TestData.java` — shared test-data loading
- `src/main/resources/config.properties` — default API base URL
- `src/test/java` — TestNG tests for authentication and booking operations
- `src/test/resources/testdata.json` — authentication scenarios and booking request data

Gradle writes test reports to `build/reports/tests/test/index.html`.
