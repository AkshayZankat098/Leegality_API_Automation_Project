import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static io.restassured.RestAssured.given;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class ApiMethods {

    public static TestCaseData loadTestCase (String name) throws IOException {

        String json = Files.readString(Path.of("src/test/resources/Request_JSON.json"));
        JsonPath data = JsonPath.from(json);
        return new TestCaseData(data.getString(name + ".requestBody"),
                data.getInt(name + ".expectedStatusCode"),
                data.getString(name + ".expectedResponseField"),
                data.getString(name + ".expectedResponseValue"));
    }


        public static Response POST(TestCaseData data) {
            return given()
                .body(data.getRequestBody())
                
                .when().post(ApiConfig_Routes.CreateToken_BASE_URL);
        }

        // Store API test data
        public static class TestCaseData {
            private final String requestBody;
            private final int expectedStatusCode;
            private final String expectedResponseField;
            private final String expectedResponseValue;

            public TestCaseData(String body, int status, String field, String value) {
                this.requestBody = body;
                this.expectedStatusCode = status;
                this.expectedResponseField = field;
                this.expectedResponseValue = value;
            }

            public String getRequestBody() { return requestBody; }
            public int getExpectedStatusCode() { return expectedStatusCode; }
            public String getExpectedResponseField() { return expectedResponseField; }
            public String getExpectedResponseValue() { return expectedResponseValue; }
        }


    public static Response PUT(String endpoint, String payload) {
        return given()
                .body(payload)
                .when()
                .put(endpoint);
    }

    public static Response GET(String endpoint) {
        return given()
                .when()
                .get(endpoint);
    }

    public static Response DELETE(String endpoint) {
        return given()
                .when()
                .delete(endpoint);
    }
}
