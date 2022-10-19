import Requests.RegisterUserRequest;
import Responses.RegisteredUserResponse;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;

public class RegisterUserTest {
    private static RegisterUserRequest user;
    private static RegisteredUserResponse registeredUser;

    @Before
    public void setup() {
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
    public void registerUserSuccessTest() {
        registeredUser = given()
                .body(user)
                .when()
                .post(TestData.ENDPOINT_REGISTER)
                .then()
                .statusCode(200)
                .extract()
                .as(RegisteredUserResponse.class);
        Assert.assertEquals(true, registeredUser.getSuccess());
        Assert.assertEquals(user.getEmail(), registeredUser.getUser().getEmail());
        Assert.assertEquals(user.getName(), registeredUser.getUser().getName());
    }

    @Test
    public void registerExistUserErrorTest() {
        registeredUser = Utils.registerUser(user);
        Response response = given()
                .body(user)
                .when()
                .post(TestData.ENDPOINT_REGISTER);
        Assert.assertEquals(403, response.statusCode());
        Assert.assertEquals(false, response.jsonPath().get("success"));
        Assert.assertEquals(TestData.ERROR_MESSAGE_EXIST_USER, response.jsonPath().get("message"));
    }

    @Test
    public void registerUserWithoutEmailErrorTest() {
        user.setEmail("");
        Response response = given()
                .body(user)
                .when()
                .post(TestData.ENDPOINT_REGISTER);
        Assert.assertEquals(403, response.statusCode());
        Assert.assertEquals(false, response.jsonPath().get("success"));
        Assert.assertEquals(TestData.ERROR_MESSAGE_REQUIRED_DATA, response.jsonPath().get("message"));
    }

    @Test
    public void registerUserWithoutPasswordErrorTest() {
        user.setPassword("");
        Response response = given()
                .body(user)
                .when()
                .post(TestData.ENDPOINT_REGISTER);
        Assert.assertEquals(403, response.statusCode());
        Assert.assertEquals(false, response.jsonPath().get("success"));
        Assert.assertEquals(TestData.ERROR_MESSAGE_REQUIRED_DATA, response.jsonPath().get("message"));
    }

    @Test
    public void registerUserWithoutNameErrorTest() {
        user.setName("");
        Response response = given()
                .body(user)
                .when()
                .post(TestData.ENDPOINT_REGISTER);
        Assert.assertEquals(403, response.statusCode());
        Assert.assertEquals(false, response.jsonPath().get("success"));
        Assert.assertEquals(TestData.ERROR_MESSAGE_REQUIRED_DATA, response.jsonPath().get("message"));
    }
}
