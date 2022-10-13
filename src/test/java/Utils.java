import io.restassured.response.Response;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;

public class Utils {

    public static void registerUser(CreateUserRequest user) {
        given()
                .body(user)
                .when()
                .post(TestData.ENDPOINT_REGISTER);
    }

    public static void deletedUser(RegisteredUserResponse user) {
        given()
                .header("authorization", user.getAccessToken())
                .body(user)
                .when()
                .delete(TestData.ENDPOINT_USER)
                .then().log().all();
    }

    public static RegisteredUserResponse getRegisteredUser(LoginUserRequest user) {
        return given()
                .body(user)
                .when()
                .post(TestData.ENDPOINT_LOGIN)
                .then()
                .extract().as(RegisteredUserResponse.class);
    }

    public static String getIngredientIdByIndex(int index) {
        return given()
                .get(TestData.ENDPOINT_INGREDIENTS)
                .jsonPath()
                .get("data[" + index + "]._id");
    }

    public static void createOrder(RegisteredUserResponse user) {
        List<String> ingredients = new ArrayList<>(Arrays.asList(Utils.getIngredientIdByIndex(0), Utils.getIngredientIdByIndex(1)));
        CreateOrderRequest order = new CreateOrderRequest(ingredients);
        given()
                .header("authorization", user.getAccessToken())
                .and()
                .body(order)
                .when()
                .post(TestData.ENDPOINT_ORDERS);
    }
}
