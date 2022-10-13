import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;

public class UpdateUserDataTest {

    private static CreateUserRequest user;

    @Before
    public void setup() {
        Specifications.installSpecs(Specifications.reqSpec(TestData.URL));
        user = new CreateUserRequest(TestData.USER_EMAIL, TestData.USER_PASS, TestData.USER_NAME);
    }

    @Test
    public void updateUserNameSuccessTest() {
        Utils.registerUser(user);
        RegisteredUserResponse registeredUser = Utils.getRegisteredUser(user);
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
        Utils.deletedUser(registeredUser);
    }

    @Test
    public void updateUserEmailSuccessTest() {
        Utils.registerUser(user);
        RegisteredUserResponse registeredUser = Utils.getRegisteredUser(user);
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
        Utils.deletedUser(registeredUser);
    }

    @Test
    public void updateUserNameUnauthorizedErrorTest() {
        Utils.registerUser(user);
        RegisteredUserResponse registeredUser = Utils.getRegisteredUser(user);
        registeredUser.getUser().setName(TestData.USER_NEW_NAME);
        Response response = given()
                .body(registeredUser.getUser())
                .when()
                .patch(TestData.ENDPOINT_USER);
        Assert.assertEquals(401, response.getStatusCode());
        Assert.assertEquals(false, response.jsonPath().get("success"));
        Assert.assertEquals(TestData.ERROR_MESSAGE_UNAUTHORIZED, response.jsonPath().get("message"));
        Utils.deletedUser(registeredUser);
    }

    @Test
    public void updateUserEmailUnauthorizedErrorTest() {
        Utils.registerUser(user);
        RegisteredUserResponse registeredUser = Utils.getRegisteredUser(user);
        registeredUser.getUser().setEmail(TestData.USER_NEW_EMAIL);
        Response response = given()
                .body(registeredUser.getUser())
                .when()
                .patch(TestData.ENDPOINT_USER);
        Assert.assertEquals(401, response.getStatusCode());
        Assert.assertEquals(false, response.jsonPath().get("success"));
        Assert.assertEquals(TestData.ERROR_MESSAGE_UNAUTHORIZED, response.jsonPath().get("message"));
        Utils.deletedUser(registeredUser);
    }
}
