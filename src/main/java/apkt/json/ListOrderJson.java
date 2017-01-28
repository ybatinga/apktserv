package apkt.json;

import apkt.backingbean.AuthAux;
import apkt.model.Order;
import java.util.List;

public class ListOrderJson {
    
    private Long userId;
    private Order order;
    private String currencyCode;
    private String listType;
    private List<Order> orderList;    
    private AuthAux authAux;

    public ListOrderJson() {
    }

    public ListOrderJson(Order order, String listType) {
        this.order = order;
        this.listType = listType;
    }

    public ListOrderJson(List<Order> orderList) {
        this.orderList = orderList;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }
    
    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public String getListType() {
        return listType;
    }

    public void setListType(String listType) {
        this.listType = listType;
    }

    public List<Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }

    public AuthAux getAuthAux() {
        return authAux;
    }

    public void setAuthAux(AuthAux authAux) {
        this.authAux = authAux;
    }
}