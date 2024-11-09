package model;

import java.util.ArrayList;
import java.util.List;

public class StoreModel {
    private List<Product> availableProducts;
    private List<Product> cart;

    public StoreModel() {
        this.availableProducts = new ArrayList<>();
        this.cart = new ArrayList<>();
    }

    // Метод для добавления товара в корзину
    public void addProductToCart(Product product) {
        cart.add(product);
    }

    // Метод для оформления заказа
    public void checkout() {
        // Логика оформления заказа (например, очистка корзины после оформления)
        cart.clear();
    }

    // Метод для применения скидки ко всем товарам в корзине
    public void applyDiscount(double discount) {
        for (Product product : cart) {
            double newPrice = product.getPrice() - (product.getPrice() * discount / 100);
            product.setPrice(newPrice);
        }
        System.out.println("Скидка в " + discount + "% применена к товарам в корзине");
    }

    // Метод для вычисления общей суммы корзины
    public double calculateTotalPrice() {
        double total = 0;
        for (Product product : cart) {
            total += product.getPrice();
        }
        return total;
    }

    // Прочие методы для работы с доступными товарами
    public void addProductToAvailableProducts(Product product) {
        availableProducts.add(product);
    }

    // Геттеры
    public List<Product> getAvailableProducts() {
        return availableProducts;
    }

    public List<Product> getCart() {
        return cart;
    }
}
