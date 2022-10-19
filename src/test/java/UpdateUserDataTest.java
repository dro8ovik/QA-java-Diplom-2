import Requests.RegisterUserRequest;
import Responses.RegisteredUserResponse;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;

public class UpdateUserDataTest {

    private static RegisterUserRequest user;
    private static RegisterUserRequest newUser;
    private static RegisteredUserResponse registeredUser;
    private static RegisteredUserResponse registeredNewUser;

    @Before
    public void setup() {
        Utils.setReqSpec(TestData.HOST_URL, ContentType.JSON);
        user = new RegisterUserRequest(TestData.USER_EMAIL, TestData.USER_PASS, TestData.USER_NAME);
        Utils.cleanTestUserData(user);
        registeredUser = Utils.registerUser(user);
    }

    @After
    public void teardown() {
        if (registeredUser != null)
            Utils.deleteUser(registeredUser);
        if (registeredNewUser != null)
            Utils.deleteUser(registeredNewUser);
    }

    @Test
    public void updateUserNameSuccessTest() {
        registeredUser.getUser().setName(TestData.USER_NEW_NAME);
        Response response = given()
                .header("authorization", registeredUser.getAccessToken())
                .and()
                .body(registeredUser.getUser())
                .when()
                .patch(TestData.ENDPOINT_USER);
        Assert.assertEquals(200, response.getStatusCode());
        Assert.assertEquals(true, response.jsonPath().get("success"));
        Assert.assertEquals(user.getEmail(), response.jsonPath().get("user.email"));
        Assert.assertEquals(TestData.USER_NEW_NAME, response.jsonPath().get("user.name"));
    }

    @Test
    public void updateUserEmailSuccessTest() {
        newUser = new RegisterUserRequest(TestData.USER_NEW_EMAIL, TestData.USER_PASS, TestData.USER_NEW_NAME);
        Utils.cleanTestUserData(newUser);
        registeredUser.getUser().setEmail(TestData.USER_NEW_EMAIL);
        Response response = given()
                .header("authorization", registeredUser.getAccessToken())
                .and()
                .body(registeredUser.getUser())
                .when()
                .patch(TestData.ENDPOINT_USER);
        Assert.assertEquals(200, response.getStatusCode());
        Assert.assertEquals(true, response.jsonPath().get("success"));
        Assert.assertEquals(user.getName(), response.jsonPath().get("user.name"));
        Assert.assertEquals(TestData.USER_NEW_EMAIL, response.jsonPath().get("user.email"));
    }

    @Test
    public void updateUserEmailExistErrorTest() {
        newUser = new RegisterUserRequest(TestData.USER_NEW_EMAIL, TestData.USER_PASS, TestData.USER_NEW_NAME);
        registeredNewUser = Utils.registerUser(newUser);
        registeredUser.getUser().setEmail(newUser.getEmail());
        Response response = given()
                .header("authorization", registeredUser.getAccessToken())
                .and()
                .body(registeredUser.getUser())
                .when()
                .patch(TestData.ENDPOINT_USER);
        Assert.assertEquals(403, response.getStatusCode());
        Assert.assertEquals(false, response.jsonPath().get("success"));
        Assert.assertEquals(TestData.ERROR_MESSAGE_EXIST_EMAIL, response.jsonPath().get("message"));
    }

    @Test
    public void updateUserNameUnauthorizedErrorTest() {
        registeredUser.getUser().setName(TestData.USER_NEW_NAME);
        Response response = given()
                .body(registeredUser.getUser())
                .when()
                .patch(TestData.ENDPOINT_USER);
        Assert.assertEquals(401, response.getStatusCode());
        Assert.assertEquals(false, response.jsonPath().get("success"));
        Assert.assertEquals(TestData.ERROR_MESSAGE_UNAUTHORIZED, response.jsonPath().get("message"));
    }

    @Test
    public void updateUserEmailUnauthorizedErrorTest() {
        registeredUser.getUser().setEmail(TestData.USER_NEW_EMAIL);
        Response response = given()
                .body(registeredUser.getUser())
                .when()
                .patch(TestData.ENDPOINT_USER);
        Assert.assertEquals(401, response.getStatusCode());
        Assert.assertEquals(false, response.jsonPath().get("success"));
        Assert.assertEquals(TestData.ERROR_MESSAGE_UNAUTHORIZED, response.jsonPath().get("message"));
    }
}
