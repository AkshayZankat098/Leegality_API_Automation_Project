import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class RequestSpecificationFactory {

    public static RequestSpecification getRequestSpecification() {

        return new RequestSpecBuilder()
                .setBaseUri(ApiConfig.BASE_URL)
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .build();
    }
}
