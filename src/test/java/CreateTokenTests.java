import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class CreateTokenTests {

    @Test
    public void createTokenWithValidCredentials() {
        
        Response response = AuthenticationConfig.createToken(ApiRequestMethods.getPayload("auth.validCredentials"));

        assertEquals(response.statusCode(), 200);
        assertEquals(response.jsonPath().getMap("$").size(), 1);
        assertNotNull(response.jsonPath().getString("token"));
        assertFalse(response.jsonPath().getString("token").isBlank());
    }

    @Test
    public void createTokenWithoutRequestBody() {
        Response response = AuthenticationConfig.createToken(ApiRequestMethods.getPayload("auth.noBody"));

        assertEquals(response.statusCode(), 200);
        assertEquals(response.jsonPath().getString("reason"), "Bad credentials");
    }

    @Test
    public void createTokenWithEmptyJsonBody() {
        Response response = AuthenticationConfig.createToken(ApiRequestMethods.getPayload("auth.emptyBody"));

        assertEquals(response.statusCode(), 200);
        assertEquals(response.jsonPath().getString("reason"), "Bad credentials");
    }

    @Test
    public void createTokenWithoutUsername() {
        Response response = AuthenticationConfig.createToken(
                ApiRequestMethods.getPayload("auth.missingUsername"));

        assertEquals(response.statusCode(), 200);
        assertEquals(response.jsonPath().getString("reason"), "Bad credentials");
    }

    @Test
    public void createTokenWithBlankUsername() {
        Response response = AuthenticationConfig.createToken(
                ApiRequestMethods.getPayload("auth.blankUsername"));

        assertEquals(response.statusCode(), 200);
        assertEquals(response.jsonPath().getString("reason"), "Bad credentials");
    }

    @Test
    public void createTokenWithoutPassword() {
        Response response = AuthenticationConfig.createToken(
                ApiRequestMethods.getPayload("auth.missingPassword"));

        assertEquals(response.statusCode(), 200);
        assertEquals(response.jsonPath().getString("reason"), "Bad credentials");
    }

    @Test
    public void createTokenWithInvalidUsername() {
        Response response = AuthenticationConfig.createToken(
                ApiRequestMethods.getPayload("auth.invalidUsername"));

        assertEquals(response.statusCode(), 200);
        assertEquals(response.jsonPath().getString("reason"), "Bad credentials");
    }

    @Test
    public void createTokenWithInvalidPassword() {
        Response response = AuthenticationConfig.createToken(
                ApiRequestMethods.getPayload("auth.invalidPassword"));

        assertEquals(response.statusCode(), 200);
        assertEquals(response.jsonPath().getString("reason"), "Bad credentials");
    }

    @Test
    public void createTokenWithBothInvalidCredentials() {
        Response response = AuthenticationConfig.createToken(
                ApiRequestMethods.getPayload("auth.invalidBothCredentials"));

        assertEquals(response.statusCode(), 200);
        assertEquals(response.jsonPath().getString("reason"), "Bad credentials");
    }

    @Test
    public void createTokenWithNumericUsername() {
        Response response = AuthenticationConfig.createToken(
                ApiRequestMethods.getPayload("auth.numericUsername"));

        assertEquals(response.statusCode(), 200);
        assertEquals(response.jsonPath().getString("reason"), "Bad credentials");
    }

    @Test
    public void createTokenWithBooleanPassword() {
        Response response = AuthenticationConfig.createToken(
                ApiRequestMethods.getPayload("auth.booleanPassword"));

        assertEquals(response.statusCode(), 200);
        assertEquals(response.jsonPath().getString("reason"), "Bad credentials");
    }

    @Test
    public void createTokenWithMalformedJson() {
        Response response = AuthenticationConfig.createToken(
                ApiRequestMethods.getPayload("auth.malformedJson"));

        assertEquals(response.statusCode(), 400);
    }

    @Test
    public void generateDifferentTokenForEachRequest() {
        Object requestBody = ApiRequestMethods.getPayload("auth.validCredentials");
        Response firstResponse = AuthenticationConfig.createToken(requestBody);
        Response secondResponse = AuthenticationConfig.createToken(requestBody);
        String firstToken = firstResponse.jsonPath().getString("token");
        String secondToken = secondResponse.jsonPath().getString("token");

        assertEquals(firstResponse.statusCode(), 200);
        assertEquals(secondResponse.statusCode(), 200);
        assertNotNull(firstToken);
        assertNotNull(secondToken);
        assertFalse(firstToken.equals(secondToken));
    }

    @Test
    public void generatedTokenAuthorizesPutPatchAndDelete() {
        String token = AuthenticationConfig.getToken();
        Response createResponse = ApiRequestMethods.post(
                "/booking", ApiRequestMethods.getPayload("booking.createRequest"));
        int bookingId = createResponse.jsonPath().getInt("bookingid");

        assertEquals(createResponse.statusCode(), 200);
        assertTrue(bookingId > 0);
        assertNotNull(token);

        String bookingEndpoint = "/booking/" + bookingId;
        Response putResponse = ApiRequestMethods.put(
                bookingEndpoint, ApiRequestMethods.getPayload("booking.updateRequest"), token);
        Response patchResponse = ApiRequestMethods.patch(
                bookingEndpoint, ApiRequestMethods.getPayload("booking.partialUpdateRequest"), token);
        Response deleteResponse = ApiRequestMethods.delete(bookingEndpoint, token);

        assertEquals(putResponse.statusCode(), 200);
        assertEquals(patchResponse.statusCode(), 200);
        assertEquals(deleteResponse.statusCode(), 201);
    }
}
