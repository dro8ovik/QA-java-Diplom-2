import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class Specifications {
    public static RequestSpecification reqSpec(String url) {
        return new RequestSpecBuilder()
                .setBaseUri(url)
                .setContentType(ContentType.JSON)
                .build();
    }

    public static void installSpecs(RequestSpecification reqSpec) {
        RestAssured.requestSpecification = reqSpec;
    }
}
