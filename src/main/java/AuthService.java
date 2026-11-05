import static BaseService.requestSpec;
import io.restassured.RestAssured;
import io.restassured.response.Response;


public class AuthService {

    public static String createToken() {

        String body = "{"
                + "\"username\":\"admin\","
                + "\"password\":\"password\""
                + "}";

        Response response = RestAssured.given()
                .spec(requestSpec)
                .body(body)
                .post("/auth");

        return response.jsonPath().getString("token");
    }

    
}