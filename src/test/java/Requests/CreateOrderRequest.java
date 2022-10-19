package Requests;

import java.util.ArrayList;
import java.util.List;

public class CreateOrderRequest {

    private List<String> ingredients = new ArrayList<>();

    public CreateOrderRequest() {
    }

    public CreateOrderRequest(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }
}
