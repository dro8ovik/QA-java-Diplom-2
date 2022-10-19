import Requests.RegisterUserRequest;
import Responses.RegisteredUserResponse;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;

public class LoginUserTest{
    private static RegisterUserRequest user;
    private static RegisteredUserResponse registeredUser;


    @Before
    public void setup(){
        Utils.setReqSpec(TestData.HOST_URL, ContentType.JSON);
        user = new RegisterUserRequest(TestData.USER_EMAIL, TestData.USER_PASS, TestData.USER_NAME);
        Utils.cleanTestUserData(user);
        registeredUser = Utils.registerUser(user);
    }

    @After
    public void teardown() {
        if (registeredUser != null)
            Utils.deleteUser(registeredUser);
    }

    @Test
    public void loginUserSuccessTest() {
        RegisteredUserResponse loginUser = given()
                .body(user)
                .when()
                .post(TestData.ENDPOINT_LOGIN)
                .then()
                .statusCode(200)
                .extract().as(RegisteredUserResponse.class);
        Assert.assertEquals(true, loginUser.getSuccess());
        Assert.assertEquals(user.getEmail(), loginUser.getUser().getEmail());
        Assert.assertEquals(user.getName(), loginUser.getUser().getName());
    }

    @Test
    public void loginUserIncorrectLoginErrorTest() {
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
