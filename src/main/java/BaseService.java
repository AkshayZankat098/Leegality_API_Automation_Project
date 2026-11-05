import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

public class BaseService {

    public static final String baseUrl = "https://restful-booker.herokuapp.com";

    public static final RequestSpecification requestSpec = RestAssured.given()
            .baseUri(baseUrl)
            .header("Content-Type", "application/json")
            .header("Accept", "application/json");
}