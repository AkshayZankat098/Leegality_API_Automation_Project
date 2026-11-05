import io.restassured.response.Response;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

public class PartialUpdateBookingTests {

    private String token;
    private int bookingId;

    @BeforeClass
    public void createAuthToken() {
        token = AuthenticationConfig.getToken();
        assertNotNull(token);
    }

    @BeforeMethod
    public void createBookingForPatch() {
        Response response = ApiRequestMethods.post(
                "/booking", ApiRequestMethods.getPayload("booking.createRequest"));
        ApiAssertions.assertBookingCreated(response);
        bookingId = response.jsonPath().getInt("bookingid");
    }

    @AfterMethod(alwaysRun = true)
    public void deleteTestBooking() {
        if (bookingId > 0) {
            ApiAssertions.assertStatusCode(
                    ApiRequestMethods.delete("/booking/" + bookingId, token), 201);
        }
    }

    @Test
    public void updatesFirstnameWithValidString() {
        Response response = patch("booking.patchFirstnameRequest", token);

        ApiAssertions.assertStatusCode(response, 200);
        assertEquals(response.jsonPath().getString("firstname"), "Only first name");
    }

    @Test
    public void updatesFirstnameWithSpecialCharacters() {
        Response response = patch("booking.patchSpecialFirstnameRequest", token);

        ApiAssertions.assertStatusCode(response, 200);
        assertEquals(response.jsonPath().getString("firstname"), "@#$%^");
    }

    @Test
    public void updatesFirstnameWithNumber() {
        Response response = patch("booking.patchNumericFirstnameRequest", token);

        ApiAssertions.assertStatusCode(response, 200);
        assertEquals(response.jsonPath().getInt("firstname"), 500);
    }

    @Test
    public void returnsBadRequestForMalformedFirstnameJson() {
        Response response = patch("booking.patchMalformedFirstnameRequest", token);

        ApiAssertions.assertStatusCode(response, 400);
    }

    @Test
    public void updatesLastnameWithValidString() {
        Response response = patch("booking.patchLastnameRequest", token);

        ApiAssertions.assertStatusCode(response, 200);
        assertEquals(response.jsonPath().getString("lastname"), "Change lastname");
    }

    @Test
    public void updatesLastnameWithNumber() {
        Response response = patch("booking.patchNumericLastnameRequest", token);

        ApiAssertions.assertStatusCode(response, 200);
        assertEquals(response.jsonPath().getInt("lastname"), 26535);
    }

    @Test
    public void updatesLastnameWithNull() {
        Response response = patch("booking.patchNullLastnameRequest", token);

        ApiAssertions.assertStatusCode(response, 200);
        assertNull(response.jsonPath().get("lastname"));
    }

    @Test
    public void updatesTotalpriceWithNumber() {
        Response response = patch("booking.patchTotalpriceRequest", token);

        ApiAssertions.assertStatusCode(response, 200);
        assertEquals(response.jsonPath().getInt("totalprice"), 2365);
    }

    @Test
    public void convertsStringTotalpriceToNull() {
        Response response = patch("booking.patchStringTotalpriceRequest", token);

        ApiAssertions.assertStatusCode(response, 200);
        assertNull(response.jsonPath().get("totalprice"));
    }

    @Test
    public void updatesDepositpaidWithBoolean() {
        Response response = patch("booking.patchDepositpaidRequest", token);

        ApiAssertions.assertStatusCode(response, 200);
        assertEquals(response.jsonPath().getBoolean("depositpaid"), true);
    }

    @Test
    public void convertsStringDepositpaidToTrue() {
        Response response = patch("booking.patchStringDepositpaidRequest", token);

        ApiAssertions.assertStatusCode(response, 200);
        assertEquals(response.jsonPath().getBoolean("depositpaid"), true);
    }

    @Test
    public void updatesBookingdates() {
        Response response = patch("booking.patchBookingdatesRequest", token);

        ApiAssertions.assertStatusCode(response, 200);
        assertEquals(response.jsonPath().getString("bookingdates.checkin"), "2026-01-01");
        assertEquals(response.jsonPath().getString("bookingdates.checkout"), "2026-01-01");
    }

    @Test
    public void convertsStringCheckinToInvalidDateValue() {
        Response response = patch("booking.patchInvalidCheckinRequest", token);

        ApiAssertions.assertStatusCode(response, 200);
        assertEquals(response.jsonPath().getString("bookingdates.checkin"), "0NaN-aN-aN");
    }

    @Test
    public void convertsStringCheckoutToInvalidDateValue() {
        Response response = patch("booking.patchInvalidCheckoutRequest", token);

        ApiAssertions.assertStatusCode(response, 200);
        assertEquals(response.jsonPath().getString("bookingdates.checkout"), "0NaN-aN-aN");
    }

    @Test
    public void normalizesDateWithSeparators() {
        Response response = patch("booking.patchInvalidDateFormatRequest", token);

        ApiAssertions.assertStatusCode(response, 200);
        assertEquals(response.jsonPath().getString("bookingdates.checkout"), "2026-05-14");
    }

    @Test
    public void allowsCheckinAfterCheckout() {
        Response response = patch("booking.patchCheckinAfterCheckoutRequest", token);

        ApiAssertions.assertStatusCode(response, 200);
        assertEquals(response.jsonPath().getString("bookingdates.checkin"), "2027-01-01");
        assertEquals(response.jsonPath().getString("bookingdates.checkout"), "2026-01-01");
    }

    @Test
    public void returnsMethodNotAllowedForDuplicateFirstnameFields() {
        Response response = patch("booking.patchMultipleFirstnameRequest", token);

        ApiAssertions.assertStatusCode(response, 405);
    }

    @Test
    public void returnsForbiddenWhenCookieIsMissing() {
        Response response = given()
                .spec(BaseService.requestSpec)
                .body(ApiRequestMethods.getPayload("booking.patchFirstnameRequest"))
                .patch("/booking/" + bookingId);

        ApiAssertions.assertStatusCode(response, 403);
    }

    @Test
    public void returnsForbiddenWhenCookieIsInvalid() {
        Response response = patch("booking.patchFirstnameRequest", "$ba012cf156c6test");

        ApiAssertions.assertStatusCode(response, 403);
    }

    private Response patch(String payloadName, String authToken) {
        return ApiRequestMethods.patch(
                "/booking/" + bookingId,
                ApiRequestMethods.getPayload(payloadName),
                authToken);
    }
}
