import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class ApiRequestMethods {

    public static Response post(String endpoint, Object requestBody) {

        var request = RestAssured.given().spec(BaseService.requestSpec);
        if (requestBody != null) {
            request.body(requestBody);
        }
        return request.post(endpoint);
    }

    public static Response get(String endpoint) {
        return RestAssured.given()
                .spec(BaseService.requestSpec)
                .get(endpoint);
    }

    public static Response put(String endpoint, Object requestBody, String token) {
        return RestAssured.given()
                .spec(BaseService.requestSpec)
                .cookie("token", token)
                .body(requestBody)
                .put(endpoint);
    }

    public static Response patch(String endpoint, Object requestBody, String token) {
        return RestAssured.given()
                .spec(BaseService.requestSpec)
                .cookie("token", token)
                .body(requestBody)
                .patch(endpoint);
    }

    public static Response delete(String endpoint, String token) {
        return RestAssured.given()
                .spec(BaseService.requestSpec)
                .cookie("token", token)
                .delete(endpoint);
    }

    public static Object getPayload (String jsonPath) {

        InputStream resource = ApiRequestMethods.class.getClassLoader().getResourceAsStream("Payloads_Data.json");

        if (resource == null) {
            throw new IllegalStateException("Could not find Payloads_Data.json on the test classpath");
        }

        try (InputStream input = resource) {
            String json = new String(input.readAllBytes(), StandardCharsets.UTF_8);
            return JsonPath.from(json).get(jsonPath);
        } catch (IOException exception) {
            throw new IllegalStateException("Could not read Payloads_Data.json", exception);
        }
    }
}