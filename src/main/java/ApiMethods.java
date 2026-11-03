import static io.restassured.RestAssured.given;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class ApiMethods {

    private static final String REQUEST_DATA_RESOURCE = "Request_JSON.json";

    public static TestCaseData loadTestCase(String testCaseName) throws IOException {
        InputStream resource = ApiMethods.class.getClassLoader()
                .getResourceAsStream(REQUEST_DATA_RESOURCE);
        if (resource == null) {
            throw new IOException("Test data resource not found: " + REQUEST_DATA_RESOURCE);
        }

        String requestJson;
        try (resource) {
            requestJson = new String(resource.readAllBytes(), StandardCharsets.UTF_8);
        }
        JsonPath requestData = JsonPath.from(requestJson);
        String casePath = testCaseName + ".";

        return new TestCaseData(
                requestData.getString(casePath + "requestBody"),
                requestData.getInt(casePath + "expectedStatusCode"),
                requestData.getString(casePath + "expectedResponseField"),
                requestData.getString(casePath + "expectedResponseValue"));
    }

    public static Response POST(TestCaseData testCase) {
        var request = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON);

        if (testCase.getRequestBody() != null) {
            request.body(testCase.getRequestBody());
        }

        return request.when().post(ApiConfig_Routes.BASE_URL);
    }

    public static final class TestCaseData {
        private final String requestBody;
        private final int expectedStatusCode;
        private final String expectedResponseField;
        private final String expectedResponseValue;

        private TestCaseData(
                String requestBody,
                int expectedStatusCode,
                String expectedResponseField,
                String expectedResponseValue) {
            this.requestBody = requestBody;
            this.expectedStatusCode = expectedStatusCode;
            this.expectedResponseField = expectedResponseField;
            this.expectedResponseValue = expectedResponseValue;
        }

        public String getRequestBody() {
            return requestBody;
        }

        public int getExpectedStatusCode() {
            return expectedStatusCode;
        }

        public String getExpectedResponseField() {
            return expectedResponseField;
        }

        public String getExpectedResponseValue() {
            return expectedResponseValue;
        }
    }

    public static Response PUT(String endpoint, String payload) {
        return given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(payload)
                .when()
                .put(endpoint);
    }

    public static Response GET(String endpoint) {
        return given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .get(endpoint);
    }

    public static Response DELETE(String endpoint) {
        return given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .delete(endpoint);
    }
}
