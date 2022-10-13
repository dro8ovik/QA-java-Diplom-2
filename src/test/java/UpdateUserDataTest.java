import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;

public class UpdateUserDataTest {

    private static final String ERROR_MESSAGE_UNAUTHORIZED = "You should be authorised";

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
        registeredUser.getUser().setName("Jules");
        Response response = given()
                .header("authorization", registeredUser.getAccessToken())
                .and()
                .body(registeredUser.getUser())
                .when()
                .patch(TestData.USER_DATA_ENDPOINT);
        Assert.assertEquals(200, response.getStatusCode());
        Assert.assertEquals(true, response.jsonPath().get("success"));
        Assert.assertEquals(user.getEmail(), response.jsonPath().get("user.email"));
        Assert.assertEquals("Jules", response.jsonPath().get("user.name"));
        Utils.deletedUser(registeredUser);
    }

    @Test
    public void updateUserEmailSuccessTest() {
        Utils.registerUser(user);
        RegisteredUserResponse registeredUser = Utils.getRegisteredUser(user);
        registeredUser.getUser().setEmail("jules.winnfield@gmail.com");
        Response response = given()
                .header("authorization", registeredUser.getAccessToken())
                .and()
                .body(registeredUser.getUser())
                .when()
                .patch(TestData.USER_DATA_ENDPOINT);
        Assert.assertEquals(200, response.getStatusCode());
        Assert.assertEquals(true, response.jsonPath().get("success"));
        Assert.assertEquals(user.getName(), response.jsonPath().get("user.name"));
        Assert.assertEquals("jules.winnfield@gmail.com", response.jsonPath().get("user.email"));
        Utils.deletedUser(registeredUser);
    }

    @Test
    public void updateUserNameUnauthorizedErrorTest() {
        Utils.registerUser(user);
        RegisteredUserResponse registeredUser = Utils.getRegisteredUser(user);
        registeredUser.getUser().setName("Jules");
        Response response = given()
                .body(registeredUser.getUser())
                .when()
                .patch(TestData.USER_DATA_ENDPOINT);
        Assert.assertEquals(401, response.getStatusCode());
        Assert.assertEquals(false, response.jsonPath().get("success"));
        Assert.assertEquals(ERROR_MESSAGE_UNAUTHORIZED, response.jsonPath().get("message"));
        Utils.deletedUser(registeredUser);
    }

    @Test
    public void updateUserEmailUnauthorizedErrorTest() {
        Utils.registerUser(user);
        RegisteredUserResponse registeredUser = Utils.getRegisteredUser(user);
        registeredUser.getUser().setEmail("jules.winnfield@gmail.com");
        Response response = given()
                .body(registeredUser.getUser())
                .when()
                .patch(TestData.USER_DATA_ENDPOINT);
        Assert.assertEquals(401, response.getStatusCode());
        Assert.assertEquals(false, response.jsonPath().get("success"));
        Assert.assertEquals(ERROR_MESSAGE_UNAUTHORIZED, response.jsonPath().get("message"));
        Utils.deletedUser(registeredUser);
    }
}
