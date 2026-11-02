# Java REST Assured API Automation Framework

A Gradle-based Java 17 API test framework using REST Assured and JUnit 5. The sample tests exercise the JSONPlaceholder posts API.

## Requirements

- JDK 17 or newer
- Gradle 8.14.2 or newer

## Run the tests

From the project root:

```powershell
.\gradlew.bat test
```

To run against a different API, override the default base URL:

```powershell
.\gradlew.bat test -Dbase.url=https://api.example.com
```

You can also set `API_BASE_URL` in the environment. The system property takes precedence, followed by the environment variable and then `src/main/resources/config.properties`.

## Project layout

- `src/main/java/com/leegality/api/config` — reusable API configuration and shared request specification
- `src/main/resources/config.properties` — default API base URL
- `src/test/java/com/leegality/api/tests` — reusable base test and sample API tests

Gradle writes test reports to `build/reports/tests/test/index.html`.
