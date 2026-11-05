# Restful Booker API Automation

Java API tests for the Restful Booker sandbox, built with REST Assured, TestNG,
and Gradle. The suite covers the assignment's health, authentication, and
booking endpoints.

## Run

Requires JDK 17 or newer. From the project root:

```powershell
.\gradlew.bat test
```

The Gradle wrapper is included. TestNG results are written to
`build/reports/tests/test/index.html`; the build also generates an Allure report
under `build/reports/allure-report/allureReport/`.

## Test approach

- Shared URL and JSON headers: `src/main/java/BaseService.java`
- Reusable HTTP methods and JSON test-data loading:
  `src/main/java/ApiRequestMethods.java`
- Token creation: `src/main/java/AuthenticationConfig.java`
- Test payloads: `src/test/resources/Payloads_Data.json`
- Test classes in `src/test/java/` cover auth, health check, booking ID filters,
  booking retrieval, creation, full and partial updates, and deletion.

Tests check response codes and selected response fields. Scenarios include
valid requests, authentication failures, missing or malformed input, and
boundary-like values.

## Limitations and current status

The sandbox is shared and resets periodically. Most write tests create their
own bookings and attempt cleanup, but some tests still rely on shared booking
data, and cleanup/authentication failures can leave records behind. The suite
does not yet consistently assert the intended contract for invalid prices and
dates, and response-schema validation and request/response attachments are not
implemented.

The latest recorded local run (November 5, 2026) had 121 tests: 87 passed,
21 failed, and 13 were skipped. Treat this as work in progress, not a passing
release; regenerate and review the reports after changes.

`BUGS.md` currently contains a defect-report template; confirmed assignment
findings still need to be documented with reproduction steps and expected vs.
actual behavior.
