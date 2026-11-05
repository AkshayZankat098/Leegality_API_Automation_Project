import io.restassured.response.Response;
import org.testng.annotations.Test;

public class HealthCheckTests {

    @Test
    public void returnsCreatedForValidHealthCheckRequest() {
        Response response = ApiRequestMethods.get("/ping");

        ApiAssertions.assertStatusCode(response, 201);
    }

    @Test
    public void returnsNotFoundForUnexpectedHealthCheckPath() {
        Response response = ApiRequestMethods.get("/ping/test5");

        ApiAssertions.assertStatusCode(response, 404);
    }
}
