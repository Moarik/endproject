package controller;

import model.Product;
import model.ProductFactory;
import model.StoreModel;
import view.StoreView;

public class StoreController {
    private StoreModel model;
    private StoreView view;

    public StoreController(StoreModel model, StoreView view) {
        this.model = model;
        this.view = view;
    }

    // Метод для инициализации доступных товаров
    public void initializeProducts() {
        // Получаем экземпляр фабрики
        ProductFactory factory = ProductFactory.getInstance();

        // Массив всех доступных товаров
        Product[] availableProducts = factory.createAllProducts();

        // Добавляем товары в модель
        for (Product product : availableProducts) {
            model.addProductToAvailableProducts(product);
        }

        // Обновляем представление с новыми товарами
        view.updateProductButtons(model.getAvailableProducts());
    }
}
