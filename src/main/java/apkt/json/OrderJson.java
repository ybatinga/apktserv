package apkt.json;

import apkt.backingbean.AuthAux;
import apkt.model.Order;

public class OrderJson {
    private Order order;
    private AuthAux authAux;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
    
    public AuthAux getAuthAux() {
        return authAux;
    }

    public void setAuthAux(AuthAux authAux) {
        this.authAux = authAux;
    }
}
