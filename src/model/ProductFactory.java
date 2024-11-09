package model;

public class ProductFactory {
    private static ProductFactory instance;

    private ProductFactory() {}

    public static synchronized ProductFactory getInstance() {
        if (instance == null) {
            instance = new ProductFactory();
        }
        return instance;
    }

    public Product createProduct(String name, double price, String category) {
        return new Product(name, price, category);  // Создание товара с тремя параметрами
    }

    // Пример метода для создания всех товаров (массив)
    public Product[] createAllProducts() {
        Product tv = createProduct("Телевизор", 150000, "Электроника");
        Product phone = createProduct("Смартфон", 125000, "Электроника");
        Product laptop = createProduct("Ноутбук", 225000, "Электроника");
        Product coffeeMachine = createProduct("Кофеварка", 35000, "Бытовая техника");
        Product refrigerator = createProduct("Холодильник", 100000, "Бытовая техника");

        return new Product[] {tv, phone, laptop, coffeeMachine, refrigerator};
    }
}
