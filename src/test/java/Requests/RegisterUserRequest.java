package Requests;

public class RegisterUserRequest extends LoginUserRequest {
    private String name;

    public RegisterUserRequest() {
    }

    public RegisterUserRequest(String email, String password, String name) {
        super(email, password);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
