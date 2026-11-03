import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.io.IOException;

public class API01_CreateTokenTest extends BaseApiTest {

    @Test
    public void TC10_validCredentialsGenerateToken() throws IOException {
        Response response = runScenario("TC10");
        response.prettyPrint();
        System.out.println("All assertions passed.");
    }

    @Test
    public void TC11_requestWithoutBody() throws IOException {
        runScenario("TC11");
    }

    @Test
    public void TC12_emptyJsonBodyReturnsBadCredentials() throws IOException {
        runScenario("TC12");
    }

    @Test
    public void TC13_missingUsernameReturnsBadCredentials() throws IOException {
        runScenario("TC13");
    }

    @Test
    public void TC14_missingPasswordReturnsBadCredentials() throws IOException {
        runScenario("TC14");
    }

    @Test
    public void TC15_invalidUsernameReturnsBadCredentials() throws IOException {
        runScenario("TC15");
    }

    @Test
    public void TC16_invalidPasswordReturnsBadCredentials() throws IOException {
        runScenario("TC16");
    }

    @Test
    public void TC17_invalidCredentialsReturnBadCredentials() throws IOException {
        runScenario("TC17");
    }

    @Test
    public void TC18_numericUsernameReturnsBadCredentials() throws IOException {
        runScenario("TC18");
    }

    @Test
    public void TC19_booleanPasswordReturnsBadCredentials() throws IOException {
        runScenario("TC19");
    }

    @Test
    public void TC20_malformedJsonReturnsBadRequest() throws IOException {
        runScenario("TC20");
    }
}
