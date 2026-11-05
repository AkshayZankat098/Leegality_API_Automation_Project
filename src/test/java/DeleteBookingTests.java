import io.restassured.response.Response;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertNotNull;

public class DeleteBookingTests {

    private String token;
    private int bookingId;

    @BeforeClass
    public void createAuthToken() {
        token = AuthenticationConfig.getToken();
        assertNotNull(token);
    }

    @BeforeMethod
    public void createBookingForDelete() {
        Response response = ApiRequestMethods.post(
                "/booking", ApiRequestMethods.getPayload("booking.createRequest"));
        ApiAssertions.assertBookingCreated(response);
        bookingId = response.jsonPath().getInt("bookingid");
    }

    @AfterMethod(alwaysRun = true)
    public void deleteTestBookingIfStillPresent() {
        if (bookingId > 0) {
            ApiAssertions.assertStatusCode(
                    ApiRequestMethods.delete("/booking/" + bookingId, token), 201);
        }
    }

    @Test
    public void deletesBookingWithValidCookieAuthentication() {
        Response response = ApiRequestMethods.delete("/booking/" + bookingId, token);

        ApiAssertions.assertStatusCode(response, 201);
        bookingId = 0;
    }

    @Test
    public void returnsMethodNotAllowedWhenDeletingBookingTwice() {
        int deletedBookingId = bookingId;
        Response firstResponse = ApiRequestMethods.delete("/booking/" + deletedBookingId, token);
        ApiAssertions.assertStatusCode(firstResponse, 201);
        bookingId = 0;

        Response secondResponse = ApiRequestMethods.delete(
                "/booking/" + deletedBookingId, token);

        ApiAssertions.assertStatusCode(secondResponse, 405);
    }

    @Test
    public void returnsMethodNotAllowedForNonExistingBookingId() {
        Response response = ApiRequestMethods.delete("/booking/5000000000", token);

        ApiAssertions.assertStatusCode(response, 405);
    }

    @Test
    public void returnsForbiddenWithInvalidAuthenticationToken() {
        Response response = given()
                .spec(BaseService.requestSpec)
                .cookie("token", "ac1d45edea")
                .delete("/booking/" + bookingId);

        ApiAssertions.assertStatusCode(response, 403);
    }

    @Test
    public void returnsForbiddenWithoutAuthentication() {
        Response response = given()
                .spec(BaseService.requestSpec)
                .delete("/booking/" + bookingId);

        ApiAssertions.assertStatusCode(response, 403);
    }

    @Test
    public void deletesBookingWithBasicAuthentication() {
        Response response = given()
                .spec(BaseService.requestSpec)
                .auth()
                .preemptive()
                .basic("admin", "password123")
                .delete("/booking/" + bookingId);

        ApiAssertions.assertStatusCode(response, 201);
        bookingId = 0;
    }
}
