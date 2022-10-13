import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;

import static io.restassured.RestAssured.given;

public class RegisterUserTest {
    private static final String ERROR_MESSAGE_REQUIRED_DATA = "Email, password and name are required fields";
    private static final String ERROR_MESSAGE_EXIST_USER = "User already exists";
    private static CreateUserRequest user;

    @Before
    public void setup() {
        Specifications.installSpecs(Specifications.reqSpec(TestData.URL));
        user = new CreateUserRequest(TestData.USER_EMAIL, TestData.USER_PASS, TestData.USER_NAME);
    }

    @Test
    public void registerUserSuccessTest() {
        user.setEmail(LocalDateTime.now().getNano() + "@gmail.com");
        RegisteredUserResponse registeredUser = given()
                .body(user)
                .when()
                .post(TestData.REGISTER_USER_ENDPOINT)
                .then()
                .statusCode(200)
                .extract()
                .as(RegisteredUserResponse.class);
        Assert.assertEquals(true, registeredUser.getSuccess());
        Assert.assertEquals(user.getEmail(), registeredUser.getUser().getEmail());
        Assert.assertEquals(user.getName(), registeredUser.getUser().getName());
        Utils.deletedUser(registeredUser);
    }

    @Test
    public void registerExistUserErrorTest() {
        Utils.registerUser(user);
        Response response = given()
                .body(user)
                .when()
                .post(TestData.REGISTER_USER_ENDPOINT);
        Assert.assertEquals(403, response.statusCode());
        Assert.assertEquals(false, response.jsonPath().get("success"));
        Assert.assertEquals(ERROR_MESSAGE_EXIST_USER, response.jsonPath().get("message"));
    }

    @Test
    public void registerUserWithoutEmailErrorTest() {
        user.setEmail("");
        Response response = given()
                .body(user)
                .when()
                .post(TestData.REGISTER_USER_ENDPOINT);
        Assert.assertEquals(403, response.statusCode());
        Assert.assertEquals(false, response.jsonPath().get("success"));
        Assert.assertEquals(ERROR_MESSAGE_REQUIRED_DATA, response.jsonPath().get("message"));
    }

    @Test
    public void registerUserWithoutPasswordErrorTest() {
        user.setPassword("");
        Response response = given()
                .body(user)
                .when()
                .post(TestData.REGISTER_USER_ENDPOINT);
        Assert.assertEquals(403, response.statusCode());
        Assert.assertEquals(false, response.jsonPath().get("success"));
        Assert.assertEquals(ERROR_MESSAGE_REQUIRED_DATA, response.jsonPath().get("message"));
    }

    @Test
    public void registerUserWithoutNameErrorTest() {
        user.setName("");
        Response response = given()
                .body(user)
                .when()
                .post(TestData.REGISTER_USER_ENDPOINT);
        Assert.assertEquals(403, response.statusCode());
        Assert.assertEquals(false, response.jsonPath().get("success"));
        Assert.assertEquals(ERROR_MESSAGE_REQUIRED_DATA, response.jsonPath().get("message"));
    }
}
