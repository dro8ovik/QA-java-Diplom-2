import static io.restassured.RestAssured.given;

public class Utils {

    public static void registerUser(CreateUserRequest user) {
        given()
                .body(user)
                .when()
                .post(TestData.REGISTER_USER_ENDPOINT);
    }

    public static void deletedUser(RegisteredUserResponse user) {
        given()
                .header("authorization", user.getAccessToken())
                .body(user)
                .when()
                .delete(TestData.USER_DATA_ENDPOINT)
                .then().log().all();
    }

    public static RegisteredUserResponse getRegisteredUser(LoginUserRequest user) {
        return given()
                .body(user)
                .when()
                .post(TestData.LOGIN_USER_ENDPOINT)
                .then()
                .extract().as(RegisteredUserResponse.class);
    }
}
