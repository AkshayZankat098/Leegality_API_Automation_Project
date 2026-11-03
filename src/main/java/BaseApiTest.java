import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;

import java.io.IOException;

public class BaseApiTest {

    protected RequestSpecification requestSpec;
    protected String baseUrl;


    // 1. RequestSpecification + Common Headers

    @BeforeMethod
    public void setupRequestSpecification() {
        baseUrl = ApiConfig_Routes.BASE_URL;

        requestSpec = new RequestSpecBuilder()

                .setBaseUri(baseUrl)

                .setContentType(ContentType.JSON)

                .setAccept(ContentType.JSON)

                .addHeader("User-Agent", "Java-RestAssured-Framework")

                .build();
    }


    // 5. Validates whether the actual API response status code matches the expected status code
    protected void assertStatusCode(Response response, int expectedStatusCode) {
        Assert.assertEquals(response.getStatusCode(), expectedStatusCode, "Status code validation failed");
    }

    protected void assertJsonFieldNotEmpty(Response response, String fieldName) {
        Assert.assertNotNull(response.jsonPath().getString(fieldName), fieldName + " should not be null");
        Assert.assertFalse(response.jsonPath().getString(fieldName).trim().isEmpty(),
                fieldName + " should not be blank");
    }

    protected Response runScenario(String testCaseName) throws IOException {
        ApiMethods.TestCaseData testCase = ApiMethods.loadTestCase(testCaseName);
        Response response = ApiMethods.POST(testCase);

        assertStatusCode(response, testCase.getExpectedStatusCode());

        if (testCase.getExpectedResponseField() != null) {
            if (testCase.getExpectedResponseValue() == null) {
                assertJsonFieldNotEmpty(response, testCase.getExpectedResponseField());
            } else {
                Assert.assertEquals(
                        response.jsonPath().getString(testCase.getExpectedResponseField()),
                        testCase.getExpectedResponseValue());
            }
        }

        return response;
    }
}
