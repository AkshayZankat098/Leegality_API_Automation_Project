import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;


public class BaseService {

    public static String baseUrl = "https://restful-booker.herokuapp.com";

    public static String auth = "/auth";
    public static String booking = "/booking";
    public static String ping = "/ping";

    public static RequestSpecification requestSpec =
    
            RestAssured.given()
                    .baseUri(baseUrl)
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json");
}