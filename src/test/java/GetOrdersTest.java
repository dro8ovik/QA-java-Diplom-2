import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;

public class GetOrdersTest {

    private static CreateUserRequest user;

    @Before
    public void setup() {
        Specifications.installSpecs(Specifications.reqSpec(TestData.URL));
        user = new CreateUserRequest(TestData.USER_EMAIL, TestData.USER_PASS, TestData.USER_NAME);
    }

    @Test
    public void getOrdersSuccessTest() {
        Utils.registerUser(user);
        RegisteredUserResponse registeredUser = Utils.getRegisteredUser(user);
        Utils.createOrder(registeredUser);
        Utils.createOrder(registeredUser);
        Response response = given()
                .header("authorization", registeredUser.getAccessToken())
                .when()
                .get(TestData.ENDPOINT_ORDERS);
        Assert.assertEquals(200, response.statusCode());
        Assert.assertEquals(true, response.jsonPath().get("success"));
        Assert.assertTrue(response.jsonPath().getList("orders").size()>=2);
    }

    @Test
    public void getOrdersUnauthorizedErrorTest() {
        Response response = given()
                .when()
                .get(TestData.ENDPOINT_ORDERS);
        Assert.assertEquals(401, response.statusCode());
        Assert.assertEquals(false, response.jsonPath().get("success"));
        Assert.assertEquals(TestData.ERROR_MESSAGE_UNAUTHORIZED, response.jsonPath().get("message"));
    }
}
