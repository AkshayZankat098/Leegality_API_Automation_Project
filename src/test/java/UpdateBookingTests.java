import io.restassured.response.Response;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

public class UpdateBookingTests {

    private String token;
    private int bookingId;

    @BeforeClass
    public void createAuthToken() {
        token = AuthenticationConfig.getToken();
        assertNotNull(token);
    }

    @BeforeMethod
    public void createBookingForUpdate() {
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
    public void updatesBookingAndVerifiesItWithGet() {
        Response putResponse = update("booking.putValidRequest", token);

        ApiAssertions.assertStatusCode(putResponse, 200);
        assertEquals(putResponse.jsonPath().getString("firstname"), "Change test Change Again");

        Response getResponse = ApiRequestMethods.get("/booking/" + bookingId);
        ApiAssertions.assertStatusCode(getResponse, 200);
        assertEquals(getResponse.jsonPath().getString("firstname"), "Change test Change Again");
        assertEquals(getResponse.jsonPath().getString("lastname"), "Brown");
        assertEquals(getResponse.jsonPath().getInt("totalprice"), 111);
        assertEquals(getResponse.jsonPath().getBoolean("depositpaid"), true);
        assertEquals(getResponse.jsonPath().getString("bookingdates.checkin"), "2018-01-01");
        assertEquals(getResponse.jsonPath().getString("bookingdates.checkout"), "2019-01-01");
        assertEquals(getResponse.jsonPath().getString("additionalneeds"), "Breakfast");
    }

    @Test
    public void returnsMethodNotAllowedForNonExistingBookingId() {
        Response response = ApiRequestMethods.put(
                "/booking/5000000", ApiRequestMethods.getPayload("booking.putValidRequest"), token);

        ApiAssertions.assertStatusCode(response, 405);
    }

    @Test
    public void returnsForbiddenWhenAuthenticationIsMissing() {
        Response response = given()
                .spec(BaseService.requestSpec)
                .body(ApiRequestMethods.getPayload("booking.putValidRequest"))
                .put("/booking/" + bookingId);

        ApiAssertions.assertStatusCode(response, 403);
    }

    @Test
    public void returnsForbiddenWhenAuthenticationIsInvalid() {
        Response response = update("booking.putValidRequest", "$ba012cf156c6test");

        ApiAssertions.assertStatusCode(response, 403);
    }

    @Test
    public void returnsBadRequestWhenFirstnameIsMissing() {
        ApiAssertions.assertStatusCode(update("booking.putMissingFirstnameRequest", token), 400);
    }

    @Test
    public void returnsBadRequestWhenLastnameIsMissing() {
        ApiAssertions.assertStatusCode(update("booking.putMissingLastnameRequest", token), 400);
    }

    @Test
    public void returnsBadRequestWhenTotalpriceIsMissing() {
        ApiAssertions.assertStatusCode(update("booking.putMissingTotalpriceRequest", token), 400);
    }

    @Test
    public void returnsBadRequestWhenDepositpaidIsMissing() {
        ApiAssertions.assertStatusCode(update("booking.putMissingDepositpaidRequest", token), 400);
    }

    @Test
    public void returnsBadRequestWhenBookingdatesIsMissing() {
        ApiAssertions.assertStatusCode(update("booking.putMissingBookingdatesRequest", token), 400);
    }

    @Test
    public void returnsBadRequestWhenCheckinIsMissing() {
        ApiAssertions.assertStatusCode(update("booking.putMissingCheckinRequest", token), 400);
    }

    @Test
    public void returnsBadRequestWhenCheckoutIsMissing() {
        ApiAssertions.assertStatusCode(update("booking.putMissingCheckoutRequest", token), 400);
    }

    @Test
    public void acceptsRequestWhenAdditionalneedsIsMissing() {
        ApiAssertions.assertStatusCode(update("booking.putMissingAdditionalneedsRequest", token), 200);
    }

    @Test
    public void returnsServerErrorWhenFirstnameIsNumeric() {
        ApiAssertions.assertStatusCode(update("booking.putNumericFirstnameRequest", token), 500);
    }

    @Test
    public void returnsServerErrorWhenLastnameIsNumeric() {
        ApiAssertions.assertStatusCode(update("booking.putNumericLastnameRequest", token), 500);
    }

    @Test
    public void acceptsStringTotalpriceAsNull() {
        Response response = update("booking.putStringTotalpriceRequest", token);

        ApiAssertions.assertStatusCode(response, 200);
        assertNull(response.jsonPath().get("totalprice"));
    }

    @Test
    public void convertsStringDepositpaidToTrue() {
        Response response = update("booking.putStringDepositpaidRequest", token);

        ApiAssertions.assertStatusCode(response, 200);
        assertEquals(response.jsonPath().getBoolean("depositpaid"), true);
    }

    @Test
    public void convertsInvalidCheckinDate() {
        Response response = update("booking.putInvalidCheckinRequest", token);

        ApiAssertions.assertStatusCode(response, 200);
        assertEquals(response.jsonPath().getString("bookingdates.checkin"), "0NaN-aN-aN");
    }

    @Test
    public void convertsInvalidCheckoutDate() {
        Response response = update("booking.putInvalidCheckoutRequest", token);

        ApiAssertions.assertStatusCode(response, 200);
        assertEquals(response.jsonPath().getString("bookingdates.checkout"), "0NaN-aN-aN");
    }

    @Test
    public void acceptsNumericAdditionalneeds() {
        Response response = update("booking.putNumericAdditionalneedsRequest", token);

        ApiAssertions.assertStatusCode(response, 200);
        assertEquals(response.jsonPath().getInt("additionalneeds"), 52365);
    }

    @Test
    public void acceptsCheckinDateAfterCheckoutDate() {
        Response response = update("booking.putCheckinAfterCheckoutRequest", token);

        ApiAssertions.assertStatusCode(response, 200);
        assertEquals(response.jsonPath().getString("bookingdates.checkin"), "2030-01-05");
        assertEquals(response.jsonPath().getString("bookingdates.checkout"), "2026-01-01");
    }

    @Test
    public void acceptsInvalidDateSequence() {
        Response response = update("booking.putInvalidDateSequenceRequest", token);

        ApiAssertions.assertStatusCode(response, 200);
        assertEquals(response.jsonPath().getString("bookingdates.checkin"), "2027-01-01");
        assertEquals(response.jsonPath().getString("bookingdates.checkout"), "2026-01-01");
    }

    @Test
    public void returnsBadRequestForMalformedJson() {
        Response response = ApiRequestMethods.put(
                "/booking/" + bookingId, ApiRequestMethods.getPayload("booking.putInvalidJsonRequest"), token);

        ApiAssertions.assertStatusCode(response, 400);
    }

    @Test
    public void updatesBookingWithoutContentTypeHeader() {
        Response response = given()
                .baseUri(BaseService.baseUrl)
                .cookie("token", token)
                .body(ApiRequestMethods.getPayload("booking.putValidNoHeadersRequest"))
                .put("/booking/" + bookingId);

        ApiAssertions.assertStatusCode(response, 200);
        assertEquals(response.jsonPath().getString("firstname"), "Akshay");
    }

    @Test
    public void updatesBookingWithoutAcceptHeader() {
        Response response = given()
                .baseUri(BaseService.baseUrl)
                .contentType("application/json")
                .cookie("token", token)
                .body(ApiRequestMethods.getPayload("booking.putValidNoHeadersRequest"))
                .put("/booking/" + bookingId);

        ApiAssertions.assertStatusCode(response, 200);
        assertEquals(response.jsonPath().getString("firstname"), "Akshay");
    }

    private Response update(String payloadName, String authToken) {
        return ApiRequestMethods.put(
                "/booking/" + bookingId,
                ApiRequestMethods.getPayload(payloadName),
                authToken);
    }
}
