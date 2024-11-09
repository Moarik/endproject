import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

// Модель интернет-магазина с расширенной логикой корзины
class StoreModel {
    private List<Product> cart = new ArrayList<>();
    private List<Observer> observers = new ArrayList<>();

    // Добавление товара в корзину
    public void addProductToCart(Product product) {
        cart.add(product);
        notifyObservers("Товар " + product.getName() + " добавлен в корзину.");
    }

    // Получение корзины
    public List<Product> getCart() {
        return cart;
    }

    private void notifyObservers() {
    }

    // Подписка и уведомление
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    private void notifyObservers(String message) {
        for (Observer observer : observers) {
            observer.update(message);
        }
    }
}

// Интерфейс наблюдателя
interface Observer {
    void update(String message);
}

// Представление для магазина с расширенным интерфейсом
class StoreView extends JFrame implements Observer {
    private JButton addButton = new JButton("Добавить товар");
    private JTextArea cartArea = new JTextArea(10, 30);
    private JLabel statusLabel = new JLabel("Добро пожаловать в интернет-магазин!");
    private StoreModel model;

    public StoreView(StoreModel model) {
        this.model = model;
        model.addObserver(this);

        setTitle("Интернет-магазин");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 600);
        setLocationRelativeTo(null);

        // Панель для интерфейса
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.LIGHT_GRAY);

        // Панель для отображения корзины
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(1, 1, 10, 10));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.add(new JScrollPane(cartArea));

        // Панель с кнопками
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 1, 10, 10));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(addButton);

        // Панель с текстом статуса
        JPanel statusPanel = new JPanel();
        statusPanel.setBackground(Color.LIGHT_GRAY);
        statusPanel.add(statusLabel);

        panel.add(statusPanel, BorderLayout.NORTH);
        panel.add(mainPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        add(panel);
    }

    // Слушатель для кнопки добавления товара
    public void setAddButtonListener(ActionListener listener) {
        addButton.addActionListener(listener);
    }

    // Обновление интерфейса при изменении состояния
    @Override
    public void update(String message) {
        StringBuilder cartText = new StringBuilder("Корзина:\n");
        for (Product product : model.getCart()) {
            cartText.append(product.getName()).append(" - ")
                    .append(product.getPrice()).append(" тг.\n");
        }
        cartArea.setText(cartText.toString());

        statusLabel.setText("Статус: " + message);
    }
}

// Контроллер для управления действиями и взаимодействием между моделью и представлением
class StoreController {
    private StoreModel model;
    private StoreView view;

    public StoreController(StoreModel model, StoreView view) {
        this.model = model;
        this.view = view;

        view.setAddButtonListener(e -> {
            Product product = ProductFactory.getInstance().createProduct("Товар A", 100, "Электроника");
            model.addProductToCart(product);
        });
    }
}

// Фабрика товаров с поддержкой категорий
class ProductFactory {
    private static ProductFactory instance;

    private ProductFactory() {}

    public static synchronized ProductFactory getInstance() {
        if (instance == null) {
            instance = new ProductFactory();
        }
        return instance;
    }

    public Product createProduct(String name, double price, String category) {
        return new Product(name, price, category);
    }
}

// Базовый класс товара с категорией
class Product {
    private String name;
    private double price;
    private String category;

    public Product(String name, double price, String category) {
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }
}

// Основной класс запуска
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            StoreModel model = new StoreModel();
            StoreView view = new StoreView(model);
            new StoreController(model, view);
            view.setVisible(true); // Показать интерфейс
        });
    }
}
