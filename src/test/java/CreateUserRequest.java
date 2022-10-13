public class CreateUserRequest extends LoginUserRequest{
    private String name;

    public CreateUserRequest() {
    }

    public CreateUserRequest(String email, String password, String name) {
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
