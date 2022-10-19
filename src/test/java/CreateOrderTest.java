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
    private static RegisteredUserResponse registeredUser;
    private static List<String> ingredients;
    private static CreateOrderRequest order;

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
    public void createOrderSuccessTest() {
        registeredUser = Utils.registerUser(user);
        ingredients = new ArrayList<>(Arrays.asList(Utils.getIngredientIdByIndex(0), Utils.getIngredientIdByIndex(1)));
        order = new CreateOrderRequest(ingredients);
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
        ingredients = new ArrayList<>(Arrays.asList(Utils.getIngredientIdByIndex(0), Utils.getIngredientIdByIndex(1)));
        order = new CreateOrderRequest(ingredients);
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
        registeredUser = Utils.registerUser(user);
        ingredients = new ArrayList<>();
        order = new CreateOrderRequest(ingredients);
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
        registeredUser = Utils.registerUser(user);
        ingredients = new ArrayList<>(Arrays.asList(Utils.getIngredientIdByIndex(0) + 1, Utils.getIngredientIdByIndex(1)));
        order = new CreateOrderRequest(ingredients);
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
