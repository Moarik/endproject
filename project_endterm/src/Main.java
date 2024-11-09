import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

// Модель интернет-магазина: управляет данными о товарах и заказах
class StoreModel {
    private List<Product> cart = new ArrayList<>();
    private List<Observer> observers = new ArrayList<>();

    // Добавление товара в корзину
    public void addProductToCart(Product product) {
        cart.add(product);
        notifyObservers();
    }

    // Получение списка товаров в корзине
    public List<Product> getCart() {
        return cart;
    }

    // Подписка на уведомления
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    // Уведомление всех подписчиков об изменениях
    private void notifyObservers() {
        for (Observer observer : observers) {
            observer.update();
        }
    }
}

// Интерфейс наблюдателя (Observer Pattern)
interface Observer {
    void update();
}

// Представление GUI для магазина
class StoreView extends JFrame implements Observer {
    private JButton addButton = new JButton("Добавить товар");
    private JTextArea cartArea = new JTextArea(10, 30);
    private StoreModel model;

    public StoreView(StoreModel model) {
        this.model = model;
        model.addObserver(this);

        setTitle("Интернет-магазин");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(new JScrollPane(cartArea), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);
        add(panel);
    }

    // Слушатель событий для кнопки добавления товара
    public void setAddButtonListener(ActionListener listener) {
        addButton.addActionListener(listener);
    }

    // Обновление интерфейса при изменении состояния
    @Override
    public void update() {
        StringBuilder cartText = new StringBuilder("Корзина:\n");
        for (Product product : model.getCart()) {
            cartText.append(product.getName()).append(" - ")
                    .append(product.getPrice()).append(" тг.\n");
        }
        cartArea.setText(cartText.toString());
    }
}

// Контроллер: управление действиями и взаимодействием между моделью и представлением
class StoreController {
    private StoreModel model;
    private StoreView view;

    public StoreController(StoreModel model, StoreView view) {
        this.model = model;
        this.view = view;

        view.setAddButtonListener(e -> {
            Product product = ProductFactory.createProduct("Товар A", 100);
            model.addProductToCart(product);
        });
    }
}

// Шаблон Singleton для фабрики товаров
class ProductFactory {
    private static ProductFactory instance;

    private ProductFactory() {}

    public static synchronized ProductFactory getInstance() {
        if (instance == null) {
            instance = new ProductFactory();
        }
        return instance;
    }

    public static Product createProduct(String name, double price) {
        return new Product(name, price);
    }
}

// Базовый класс товара
class Product {
    private String name;
    private double price;

    public Product(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }
}

// Основной класс запуска
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            StoreModel model = new StoreModel();
            StoreView view = new StoreView(model);
            StoreController controller = new StoreController(model, view);

            view.setVisible(true); // Показать интерфейс
        });
    }
}
