import io.restassured.response.Response;

public class AuthenticationConfig {

    private static final String AUTH_ENDPOINT = "/auth";

    public static Response createToken(Object requestBody) {
        return ApiRequestMethods.post(AUTH_ENDPOINT, requestBody);
    }

    public static String getToken() {
        return createToken(ApiRequestMethods.getPayload("auth.validCredentials"))
                .jsonPath()
                .getString("token");
    }
}