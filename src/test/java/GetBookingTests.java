import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

public class GetBookingTests {

    @Test
    public void retrievesExistingBooking() {
        Response response = ApiRequestMethods.get("/booking/5");

        assertEquals(response.statusCode(), 200);
        assertEquals(response.jsonPath().getString("firstname"), "Sally");
        assertEquals(response.jsonPath().getString("lastname"), "Smith");
        assertEquals(response.jsonPath().getInt("totalprice"), 237);
        assertEquals(response.jsonPath().getBoolean("depositpaid"), true);
        assertEquals(response.jsonPath().getString("bookingdates.checkin"), "2017-10-15");
        assertEquals(response.jsonPath().getString("bookingdates.checkout"), "2018-06-23");
        assertEquals(response.jsonPath().getString("additionalneeds"), "Breakfast");
    }

    @Test
    public void returnsNotFoundForNonExistingBookingId() {
        Response response = ApiRequestMethods.get("/booking/525326598");

        ApiAssertions.assertStatusCode(response, 404);
    }

    @Test
    public void returnsNotFoundForInvalidBookingIdFormat() {
        Response response = ApiRequestMethods.get("/booking/Akshay");

        ApiAssertions.assertStatusCode(response, 404);
    }

    @Test
    public void returnsBookingIdsWhenBookingIdIsOmitted() {
        Response response = ApiRequestMethods.get("/booking");

        ApiAssertions.assertBookingIdsResponse(response, 200);
    }

    @Test
    public void returnsJsonByDefaultWhenAcceptHeaderIsOmitted() {
        Response response = given()
                .baseUri(BaseService.baseUrl)
                .get("/booking");

        ApiAssertions.assertStatusCode(response, 200);
        assertEquals(response.getContentType().contains("application/json"), true);
        List<Map<String, Object>> bookingIds = response.jsonPath().getList("$");
        ApiAssertions.assertNotNullValue(bookingIds);
    }

    @Test
    public void returnsNotFoundForSpecialCharactersInBookingId() {
        Response response = ApiRequestMethods.get("/booking/%24");

        ApiAssertions.assertStatusCode(response, 404);
    }
}