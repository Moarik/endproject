package model;

public class CartFacade {
    private StoreModel storeModel;

    public CartFacade(StoreModel storeModel) {
        this.storeModel = storeModel;
    }

    public void addProduct(String name, double price, String category) {
        // Передаем все три аргумента: name, price, category
        Product product = ProductFactory.getInstance().createProduct(name, price, category);
        storeModel.addProductToCart(product);  // Добавляем товар в корзину
    }

    public void checkout() {
        storeModel.checkout();  // Оформляем заказ
    }
}
