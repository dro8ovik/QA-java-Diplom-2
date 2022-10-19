import Requests.RegisterUserRequest;
import Responses.RegisteredUserResponse;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;

public class GetOrdersTest {

    private static RegisterUserRequest user;
    private static RegisteredUserResponse registeredUser;

    @Before
    public void setup(){
        Utils.setReqSpec(TestData.HOST_URL, ContentType.JSON);
        user = new RegisterUserRequest(TestData.USER_EMAIL, TestData.USER_PASS, TestData.USER_NAME);
        Utils.cleanTestUserData(user);
    }

    @After
    public void teardown() {
        if (registeredUser != null)
            Utils.deleteUser(registeredUser);
    }
    @Test
    public void getOrdersSuccessTest() {
        registeredUser = Utils.registerUser(user);
        Utils.createOrder(registeredUser);
        Utils.createOrder(registeredUser);
        Response response = given()
                .header("authorization", registeredUser.getAccessToken())
                .when()
                .get(TestData.ENDPOINT_ORDERS);
        Assert.assertEquals(200, response.statusCode());
        Assert.assertEquals(true, response.jsonPath().get("success"));
        Assert.assertEquals(2, response.jsonPath().getList("orders").size());
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
