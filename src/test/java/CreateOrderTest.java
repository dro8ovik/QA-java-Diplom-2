import Requests.CreateOrderRequest;
import Requests.RegisterUserRequest;
import Responses.RegisteredUserResponse;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;

public class CreateOrderTest{
    private static RegisterUserRequest user;

    @Before
    public void setup(){
        Utils.setReqSpec(TestData.HOST_URL, ContentType.JSON);
        user = new RegisterUserRequest(TestData.USER_EMAIL, TestData.USER_PASS, TestData.USER_NAME);
        Utils.cleanTestUserData(user);
        Utils.registerUser(user);
    }

    @After
    public void tearDown(){
        Utils.cleanTestUserData(user);
    }
    @Test
    public void createOrderSuccessTest() {
        RegisteredUserResponse registeredUser = Utils.getRegisteredUser(user);
        List<String> ingredients = new ArrayList<>(Arrays.asList(Utils.getIngredientIdByIndex(0), Utils.getIngredientIdByIndex(1)));
        CreateOrderRequest order = new CreateOrderRequest(ingredients);
        Response response = given()
                .header("authorization", registeredUser.getAccessToken())
                .and()
                .body(order)
                .when()
                .post(TestData.ENDPOINT_ORDERS);
        Assert.assertEquals(200, response.statusCode());
        Assert.assertEquals(true, response.jsonPath().get("success"));
        Assert.assertNotNull(response.jsonPath().get("order.number"));
    }

    @Test
    public void createOrderUnauthorizedSuccessTest() {
        List<String> ingredients = new ArrayList<>(Arrays.asList(Utils.getIngredientIdByIndex(0), Utils.getIngredientIdByIndex(1)));
        CreateOrderRequest order = new CreateOrderRequest(ingredients);
        Response response = given()
                .body(order)
                .when()
                .post(TestData.ENDPOINT_ORDERS);
        Assert.assertEquals(200, response.statusCode());
        Assert.assertEquals(true, response.jsonPath().get("success"));
        Assert.assertNotNull(response.jsonPath().get("order.number"));
    }

    @Test
    public void createOrderWithoutIngredientsErrorTest() {
        RegisteredUserResponse registeredUser = Utils.getRegisteredUser(user);
        CreateOrderRequest order = new CreateOrderRequest(new ArrayList<>());
        Response response = given()
                .header("authorization", registeredUser.getAccessToken())
                .and()
                .body(order)
                .when()
                .post(TestData.ENDPOINT_ORDERS);
        Assert.assertEquals(400, response.statusCode());
        Assert.assertEquals(false, response.jsonPath().get("success"));
        Assert.assertEquals(TestData.ERROR_MESSAGE_WITHOUT_INGREDIENTS, response.jsonPath().get("message"));
    }

    @Test
    public void createOrderIncorrectIngredientErrorTest() {
        RegisteredUserResponse registeredUser = Utils.getRegisteredUser(user);
        List<String> ingredients = new ArrayList<>(Arrays.asList(Utils.getIngredientIdByIndex(0) + 1, Utils.getIngredientIdByIndex(1)));
        CreateOrderRequest order = new CreateOrderRequest(ingredients);
        given()
                .header("authorization", registeredUser.getAccessToken())
                .and()
                .body(order)
                .when()
                .post(TestData.ENDPOINT_ORDERS)
                .then()
                .statusCode(500);
    }
}
