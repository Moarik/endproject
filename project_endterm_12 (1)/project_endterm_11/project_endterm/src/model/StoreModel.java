package model;

import observer.Observer;
import java.util.ArrayList;
import java.util.List;

public class StoreModel {
    private List<Product> cart = new ArrayList<>();
    private List<List<Product>> orderHistory = new ArrayList<>();
    private OrderStatus orderStatus = OrderStatus.CREATED;
    private List<Observer> observers = new ArrayList<>();
    private boolean discountApplied = false;

    public void addProductToCart(Product product) {
        if (!discountApplied) {
            cart.add(new Product(product.getName(), product.getPrice(), product.getCategory()));
        } else {
            cart.add(product);
        }
        notifyObservers("Товар " + product.getName() + " добавлен в корзину.");
    }

    public List<Product> getCart() {
        return cart;
    }

    public List<List<Product>> getOrderHistory() {
        return orderHistory;
    }

    public void checkout() {
        orderHistory.add(new ArrayList<>(cart));
        cart.clear();
        setOrderStatus(OrderStatus.COMPLETED);
        notifyObservers("Заказ завершен и добавлен в историю.");
    }

    public void applyDiscount(double percentage) {
        for (Product product : cart) {
            product.applyDiscount(percentage);
        }
        discountApplied = true;
        setOrderStatus(OrderStatus.DISCOUNT_APPLIED);
        notifyObservers("Скидка " + percentage + "% применена к корзине.");
    }

    public void setOrderStatus(OrderStatus status) {
        orderStatus = status;
        notifyObservers();
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    private void notifyObservers() {
        for (Observer observer : observers) {
            observer.update("");
        }
    }

    private void notifyObservers(String message) {
        for (Observer observer : observers) {
            observer.update(message);
        }
    }

    public double getTotalPrice() {
        double total = 0;
        for (Product product : cart) {
            total += product.getPrice();
        }
        return total;
    }
}
