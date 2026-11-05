import io.restassured.response.Response;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

public class CreateBookingTests {

    private String token;
    private int createdBookingId;
    private int secondCreatedBookingId;

    @BeforeClass
    public void createAuthToken() {
        token = AuthenticationConfig.getToken();
        assertNotNull(token);
    }

    @BeforeMethod
    public void resetCreatedBookingIds() {
        createdBookingId = 0;
        secondCreatedBookingId = 0;
    }

    @AfterMethod(alwaysRun = true)
    public void deleteCreatedBookings() {
        if (createdBookingId > 0) {
            ApiAssertions.assertStatusCode(
                    ApiRequestMethods.delete("/booking/" + createdBookingId, token), 201);
        }
        if (secondCreatedBookingId > 0) {
            ApiAssertions.assertStatusCode(
                    ApiRequestMethods.delete("/booking/" + secondCreatedBookingId, token), 201);
        }
    }

    @Test
    public void createsBookingWithValidJsonPayload() {
        Response response = createBooking("booking.validCreateRequest");

        ApiAssertions.assertBookingCreated(response);
    }

    @Test
    public void verifiesCreatedBookingMatchesSubmittedPayload() {
        Response response = createBooking("booking.validCreateRequest");

        ApiAssertions.assertBookingCreated(response);
        assertEquals(response.jsonPath().getString("booking.firstname"), "Akshay");
        assertEquals(response.jsonPath().getString("booking.lastname"), "Zankat");
        assertEquals(response.jsonPath().getInt("booking.totalprice"), 500);
        assertEquals(response.jsonPath().getBoolean("booking.depositpaid"), true);
        assertEquals(response.jsonPath().getString("booking.bookingdates.checkin"), "2026-11-01");
        assertEquals(response.jsonPath().getString("booking.bookingdates.checkout"), "2026-11-10");
        assertEquals(response.jsonPath().getString("booking.additionalneeds"), "Breakfast");
    }

    @Test
    public void returnsServerErrorWhenFirstnameIsMissing() {
        Response response = createBooking("booking.missingFirstnameRequest");

        ApiAssertions.assertStatusCode(response, 500);
    }

    @Test
    public void returnsServerErrorWhenLastnameIsMissing() {
        Response response = createBooking("booking.missingLastnameRequest");

        ApiAssertions.assertStatusCode(response, 500);
    }

    @Test
    public void createsBookingWhenAdditionalNeedsIsMissing() {
        Response response = createBooking("booking.missingAdditionalNeedsRequest");

        ApiAssertions.assertBookingCreated(response);
        Map<String, Object> booking = response.jsonPath().getMap("booking");
        assertFalse(booking.containsKey("additionalneeds"));
    }

    @Test
    public void createsDifferentBookingIdsForRepeatedRequests() {
        Response firstResponse = createBooking("booking.validCreateRequest");
        Response secondResponse = createBooking("booking.validCreateRequest");

        ApiAssertions.assertBookingCreated(firstResponse);
        ApiAssertions.assertBookingCreated(secondResponse);
        assertTrue(createdBookingId != secondCreatedBookingId);
    }

    @Test
    public void returnsServerErrorWhenFirstnameIsNumeric() {

        Response response = createBooking("booking.numericFirstnameRequest");
        ApiAssertions.assertStatusCode(response, 500);
    }

    @Test
    public void returnsServerErrorWhenLastnameIsNumeric() {
        Response response = createBooking("booking.numericLastnameRequest");

        ApiAssertions.assertStatusCode(response, 500);
    }

    @Test
    public void acceptsStringTotalPriceAsNull() {
        Response response = createBooking("booking.stringTotalPriceRequest");

        ApiAssertions.assertBookingCreated(response);
        assertNull(response.jsonPath().get("booking.totalprice"));
    }

    @Test
    public void convertsStringDepositPaidToTrue() {
        Response response = createBooking("booking.stringDepositPaidRequest");

        ApiAssertions.assertBookingCreated(response);
        assertEquals(response.jsonPath().getBoolean("booking.depositpaid"), true);
    }

    @Test
    public void handlesInvalidCheckinDate() {
        Response response = createBooking("booking.invalidCheckinRequest");

        ApiAssertions.assertBookingCreated(response);
        assertEquals(response.jsonPath().getString("booking.bookingdates.checkin"), "0NaN-aN-aN");
    }

    @Test
    public void handlesInvalidCheckoutDate() {
        Response response = createBooking("booking.invalidCheckoutRequest");

        ApiAssertions.assertBookingCreated(response);
        assertEquals(response.jsonPath().getString("booking.bookingdates.checkout"), "0NaN-aN-aN");
    }

    @Test
    public void allowsCheckinDateAfterCheckoutDate() {
        Response response = createBooking("booking.checkinAfterCheckoutRequest");

        ApiAssertions.assertBookingCreated(response);
        assertEquals(response.jsonPath().getString("booking.bookingdates.checkin"), "2028-11-01");
        assertEquals(response.jsonPath().getString("booking.bookingdates.checkout"), "2026-11-01");
    }

    @Test
    public void returnsBadRequestForUnquotedSpecialCharacterInJson() {
        Response response = createBooking("booking.invalidSpecialCharacterJson");

        ApiAssertions.assertStatusCode(response, 400);
    }

    @Test
    public void returnsServerErrorForEmptyJsonObject() {
        Response response = createBooking("auth.emptyBody");

        ApiAssertions.assertStatusCode(response, 500);
    }

    @Test
    public void returnsBadRequestForMalformedJson() {
        Response response = createBooking("booking.invalidJson");

        ApiAssertions.assertStatusCode(response, 400);
    }

    @Test
    public void returnsServerErrorWhenBookingDatesAreMissing() {
        Response response = createBooking("booking.missingBookingDatesRequest");

        ApiAssertions.assertStatusCode(response, 500);
    }

    @Test
    public void createsBookingWithMaximumValues() {
        Response response = createBooking("booking.maxValuesRequest");

        ApiAssertions.assertBookingCreated(response);
        assertEquals(response.jsonPath().getString("booking.firstname"),
                "AkshayAkshayAkshayAkshayAkshayAkshayAkshayAkshayAkshayAkshayAkshayAkshayAkshayAkshayAkshayAkshayAkshayAkshayAkshayAkshayAkshayAkshayAkshayAkshay");
        assertEquals(response.jsonPath().getString("booking.lastname"),
                "AkshayAkshayAkshayAkshayAkshayAkshayAkshayAkshayAkshayAkshayAkshayAkshayAkshayAkshayAkshayAkshayAkshayAkshayAkshayAkshayAkshayAkshayAkshayAkshayAkshayAkshayAkshayAkshayAkshayAkshay");
        assertEquals(response.jsonPath().getInt("booking.totalprice"), 5);
        assertEquals(response.jsonPath().getBoolean("booking.depositpaid"), true);
    }

    private Response createBooking(String payloadName) {

    Response response = ApiRequestMethods.post("/booking", ApiRequestMethods.getPayload(payloadName));

        if (response.statusCode() == 200) {
            int bookingId = response.jsonPath().getInt("bookingid");
            if (createdBookingId == 0) {
                createdBookingId = bookingId;
            } else {
                secondCreatedBookingId = bookingId;
            }
        }

        return response;
    }
}
