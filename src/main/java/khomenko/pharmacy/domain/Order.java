package khomenko.pharmacy.domain;

import java.util.List;

public class Order {

    private List<OrderItem> items;

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }
}
