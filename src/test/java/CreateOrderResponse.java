public class CreateOrderResponse {

    private String name;
    private Boolean success;
    private Order order;

    public CreateOrderResponse() {
    }

    public CreateOrderResponse(String name, Boolean success, Order order) {
        this.name = name;
        this.success = success;
        this.order = order;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
