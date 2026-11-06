import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import io.restassured.response.Response;

public class GetBookingIdsTests {

    private int filterBookingId;
    private String token;

    @BeforeClass
    public void createBookingForFilterTests() {
        token = AuthenticationConfig.getToken();
        ApiAssertions.assertNotNullValue(token);

        Response response = ApiRequestMethods.post(
                "/booking", ApiRequestMethods.getPayload("booking.filterRequest"));
        ApiAssertions.assertBookingCreated(response);
        filterBookingId = response.jsonPath().getInt("bookingid");
    }

    @AfterClass(alwaysRun = true)
    public void deleteFilterTestBooking() {
        if (filterBookingId > 0 && token != null) {
            Response response = ApiRequestMethods.delete("/booking/" + filterBookingId, token);
            ApiAssertions.assertStatusCode(response, 201);
        }
    }

    @Test
    public void filtersByFirstName() {
        Response response = ApiRequestMethods.get("/booking?firstname=Akshay");

        ApiAssertions.assertContainsBookingId(response, 200, filterBookingId);
    }

    @Test
    public void filtersByLastName() {
        Response response = ApiRequestMethods.get("/booking?lastname=Zankat");

        ApiAssertions.assertContainsBookingId(response, 200, filterBookingId);
    }

    @Test
    public void filtersByFirstAndLastName() {
        Response response = ApiRequestMethods.get("/booking?firstname=Akshay&lastname=Zankat");

        ApiAssertions.assertContainsBookingId(response, 200, filterBookingId);
    }

    @Test
    public void filtersByCheckInDate() {
        Response response = ApiRequestMethods.get("/booking?checkin=2026-11-01");

        ApiAssertions.assertContainsBookingId(response, 200, filterBookingId);
    }

    @Test
    public void filtersByCheckOutDate() {
        Response response = ApiRequestMethods.get("/booking?checkout=2026-11-10");

        ApiAssertions.assertContainsBookingId(response, 200, filterBookingId);
    }

    @Test
    public void filtersByAllSupportedParameters() {
        Response response = ApiRequestMethods.get(
                "/booking?firstname=Akshay&lastname=Zankat&checkin=2026-11-01&checkout=2026-11-10");

        ApiAssertions.assertContainsBookingId(response, 200, filterBookingId);
    }

    @Test
    public void returnsEmptyArrayWhenCheckInDateIsAfterCheckOutDate() {
        Response response = ApiRequestMethods.get(
                "/booking?checkin=2028-12-05&checkout=2026-11-05");

        ApiAssertions.assertEmptyBookingIds(response, 200);
    }

    @Test
    public void returnsServerErrorForInvalidCheckInAndCheckOutDateFormats() {
        Response response = ApiRequestMethods.get(
                "/booking?checkin=202658-2365-2658&checkout=202658-2365-2658");

        ApiAssertions.assertStatusCode(response, 500);
    }

    @Test
    public void returnsEmptyArrayWhenNoBookingMatches() {
        Response response = ApiRequestMethods.get("/booking?firstname=UnknownUser123");

        ApiAssertions.assertEmptyBookingIds(response, 200);
    }

    @Test
    public void handlesCheckInDateWithDifferentFormat() {
        Response response = ApiRequestMethods.get("/booking?checkin=04-11-2026");

        ApiAssertions.assertBookingIdsResponse(response, 200);
    }

    @Test
    public void returnsServerErrorForTextCheckInDate() {
        Response response = ApiRequestMethods.get("/booking?checkin=TestUserString");

        ApiAssertions.assertStatusCode(response, 500);
    }

    @Test
    public void handlesImpossibleCheckInDate() {
        Response response = ApiRequestMethods.get("/booking?checkin=2026-13-45");

        ApiAssertions.assertStatusCode(response, 500);
    }

    @Test
    public void handlesEmptyOptionalParameters() {
        Response response = ApiRequestMethods.get("/booking?firstname=&lastname=");

        ApiAssertions.assertEmptyBookingIds(response, 200);
    }

    @Test
    public void handlesVeryLongFirstName() {
        String longName = "A".repeat(10_000);
        Response response = ApiRequestMethods.get("/booking?firstname=" + longName);

        ApiAssertions.assertEmptyBookingIds(response, 200);
    }

    @Test
    public void handlesEncodedSpecialCharactersInFirstName() {
        Response response = ApiRequestMethods.get("/booking?firstname=%40%23%24%25%5E");

        ApiAssertions.assertEmptyBookingIds(response, 200);
    }

    @Test
    public void handlesDuplicateFirstNameParameters() {
        Response response = ApiRequestMethods.get(
                "/booking?firstname=Akshay&firstname=Akshay");

        ApiAssertions.assertStatusCode(response, 500);
    }

    @Test
    public void ignoresUnsupportedQueryParameters() {
        Response response = ApiRequestMethods.get("/booking?age=25&gender=male");

        ApiAssertions.assertContainsBookingId(response, 200, filterBookingId);
    }

    @Test
    public void handlesNullLikeQueryValues() {
        Response response = ApiRequestMethods.get("/booking?firstname=null&lastname=null");

        ApiAssertions.assertEmptyBookingIds(response, 200);
    }
}