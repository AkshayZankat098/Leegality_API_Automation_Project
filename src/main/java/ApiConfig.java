import java.util.ResourceBundle;

public class ApiConfig {

    public static final String BASE_URL = "https://restful-booker.herokuapp.com";

    public static final String AUTH_ENDPOINT = "/auth";

    private static final ResourceBundle TEST_DATA = loadTestData();

    public static final String USERNAME = TEST_DATA.getString("username");
    public static final String PASSWORD = TEST_DATA.getString("password");

    private static ResourceBundle loadTestData() {
        return ResourceBundle.getBundle("config");
    }
}
