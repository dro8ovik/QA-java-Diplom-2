import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;

public class LoginUserTest {
    private static CreateUserRequest user;

    @Before
    public void setup() {
        Specifications.installSpecs(Specifications.reqSpec(TestData.URL));
        user = new CreateUserRequest(TestData.USER_EMAIL, TestData.USER_PASS, TestData.USER_NAME);
    }

    @Test
    public void loginUserSuccessTest() {
        Utils.registerUser(user);
        RegisteredUserResponse registeredUser = given()
                .body(user)
                .when()
                .post(TestData.ENDPOINT_LOGIN)
                .then()
                .statusCode(200)
                .extract().as(RegisteredUserResponse.class);
        Assert.assertEquals(true, registeredUser.getSuccess());
        Assert.assertEquals(user.getEmail(), registeredUser.getUser().getEmail());
        Assert.assertEquals(user.getName(), registeredUser.getUser().getName());
        Utils.deletedUser(registeredUser);
    }

    @Test
    public void loginUserIncorrectLoginErrorTest() {
        Utils.registerUser(user);
        user.setEmail("");
        Response response = given()
                .body(user)
                .when()
                .post(TestData.ENDPOINT_LOGIN);
        Assert.assertEquals(401, response.statusCode());
        Assert.assertEquals(false, response.jsonPath().get("success"));
        Assert.assertEquals(TestData.ERROR_MESSAGE_INCORRECT_USER_DATA, response.jsonPath().get("message"));
    }

    @Test
    public void loginUserIncorrectPasswordErrorTest() {
        Utils.registerUser(user);
        user.setPassword("");
        Response response = given()
                .body(user)
                .when()
                .post(TestData.ENDPOINT_LOGIN);
        Assert.assertEquals(401, response.statusCode());
        Assert.assertEquals(false, response.jsonPath().get("success"));
        Assert.assertEquals(TestData.ERROR_MESSAGE_INCORRECT_USER_DATA, response.jsonPath().get("message"));
    }


}
