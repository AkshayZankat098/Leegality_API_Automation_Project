import io.restassured.response.Response;

import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class ApiAssertions {

    public static void assertNotNullValue(Object value) {
        assertNotNull(value);
    }

    public static void assertBookingCreated(Response response) {
        assertEquals(response.statusCode(), 200);
        assertTrue(response.jsonPath().getInt("bookingid") > 0);
    }

    public static void assertStatusCode(Response response, int expectedStatusCode) {
        assertEquals(response.statusCode(), expectedStatusCode);
    }

    public static void assertContainsBookingId(Response response, int expectedStatusCode, int bookingId) {
        assertBookingIdsResponse(response, expectedStatusCode);
        assertTrue(bookingIds(response).contains(Map.of("bookingid", bookingId)));
    }

    public static void assertEmptyBookingIds(Response response, int expectedStatusCode) {
        assertEquals(response.statusCode(), expectedStatusCode);
        assertTrue(bookingIds(response).isEmpty());
    }

    public static void assertBookingIdsResponse(Response response, int expectedStatusCode) {
        assertEquals(response.statusCode(), expectedStatusCode);
        List<Map<String, Object>> ids = bookingIds(response);
        assertNotNull(ids);
        for (Map<String, Object> booking : ids) {
            assertEquals(booking.size(), 1);
            assertNotNull(booking.get("bookingid"));
        }
    }

    private static List<Map<String, Object>> bookingIds(Response response) {
        return response.jsonPath().getList("$");
    }
}
