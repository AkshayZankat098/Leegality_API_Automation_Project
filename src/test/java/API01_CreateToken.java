import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class API01_CreateToken {

    @Test
    public void Test01 () {
        
        String requestBody = """
                {
                    "username": "%s",
                    "password": "%s"
                }
                """.formatted(ApiConfig.USERNAME, ApiConfig.PASSWORD);

        given()
                .baseUri(ApiConfig.BASE_URL)
                .contentType("application/json")
                .body(requestBody)

        .when()
                .post(ApiConfig.AUTH_ENDPOINT)

        .then()
                .statusCode(200)
                .body("token", notNullValue())
                .body("token", not(emptyString()))
                .body("token", instanceOf(String.class));
    }
}
