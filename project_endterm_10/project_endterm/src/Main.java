import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

// Модель интернет-магазина с расширенной логикой корзины и заказов
class StoreModel {
    private List<Product> cart = new ArrayList<>();
    private List<Product> orderHistory = new ArrayList<>();
    private OrderStatus orderStatus = OrderStatus.CREATED;
    private List<Observer> observers = new ArrayList<>();
    private boolean discountApplied = false; // Флаг для отслеживания скидки

    // Добавление товара в корзину
    public void addProductToCart(Product product) {
        // Если скидка не применена, добавляем товар без скидки
        if (!discountApplied) {
            cart.add(new Product(product.getName(), product.getPrice(), product.getCategory())); // Копируем товар без скидки
        } else {
            cart.add(product); // Если скидка применена, добавляем товар с скидкой
        }
        notifyObservers("Товар " + product.getName() + " добавлен в корзину.");
    }

    // Получение корзины и истории заказов
    public List<Product> getCart() {
        return cart;
    }

    public List<Product> getOrderHistory() {
        return orderHistory;
    }

    // Оформление заказа с перемещением товаров в историю заказов
    public void checkout() {
        orderHistory.addAll(cart); // Перемещаем товары в историю заказов
        cart.clear(); // Очищаем корзину
        setOrderStatus(OrderStatus.COMPLETED);
        notifyObservers("Заказ завершен и добавлен в историю.");
    }

    // Применение скидки ко всем товарам в корзине
    public void applyDiscount(double percentage) {
        // Применяем скидку только к товарам в корзине
        for (Product product : cart) {
            product.applyDiscount(percentage);
        }
        discountApplied = true; // Скидка применена
        setOrderStatus(OrderStatus.DISCOUNT_APPLIED);
        notifyObservers("Скидка " + percentage + "% применена к корзине.");
    }

    // Управление статусом заказа
    public void setOrderStatus(OrderStatus status) {
        orderStatus = status;
        notifyObservers();
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    // Подписка и уведомление
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    private void notifyObservers() {
    }

    private void notifyObservers(String message) {
        for (Observer observer : observers) {
            observer.update(message);
        }
    }

    // Вычисление общей цены товаров в корзине
    public double getTotalPrice() {
        double total = 0;
        for (Product product : cart) {
            total += product.getPrice();
        }
        return total;
    }
}

// Интерфейс наблюдателя
interface Observer {
    void update(String message);
}

// Представление для магазина с расширенным интерфейсом
class StoreView extends JFrame implements Observer {
    private List<Product> availableProducts = new ArrayList<>();
    private StoreModel model;

    private JPanel productsPanel = new JPanel();
    private JTextArea cartArea = new JTextArea(10, 30);
    private JTextArea historyArea = new JTextArea(10, 30);
    private JLabel statusLabel = new JLabel("Добро пожаловать в интернет-магазин!");
    private JLabel totalPriceLabel = new JLabel("Общая цена: 0 тг.");
    private JButton discountButton = new JButton("Применить скидку");
    private JButton purchaseButton = new JButton("Приобрести");

    public StoreView(StoreModel model) {
        this.model = model;
        model.addObserver(this);

        setTitle("Интернет-магазин");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 600);
        setLocationRelativeTo(null);

        // Инициализация готовых товаров
        initializeProducts();

        // Панель для интерфейса
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.LIGHT_GRAY);

        // Панель для отображения доступных товаров
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(availableProducts.size(), 2, 10, 10));
        mainPanel.setBackground(Color.WHITE);
        for (Product product : availableProducts) {
            JButton productButton = new JButton("Добавить " + product.getName() + " - " + product.getPrice() + " тг.");
            productButton.addActionListener(e -> model.addProductToCart(product));
            mainPanel.add(productButton);
        }

        // Панель для отображения корзины и истории заказов
        JPanel historyPanel = new JPanel();
        historyPanel.setLayout(new GridLayout(2, 1, 10, 10));
        historyPanel.setBackground(Color.WHITE);
        historyPanel.add(new JScrollPane(cartArea));
        historyPanel.add(new JScrollPane(historyArea));

        // Панель с текстом статуса
        JPanel statusPanel = new JPanel();
        statusPanel.setBackground(Color.LIGHT_GRAY);
        statusPanel.add(statusLabel);

        // Панель с общей ценой (перемещаем наверх)
        JPanel totalPricePanel = new JPanel();
        totalPricePanel.setBackground(Color.LIGHT_GRAY);
        totalPricePanel.add(totalPriceLabel);

        // Панель с кнопками (увеличиваем их размер и выравниваем)
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER)); // Центрируем кнопки
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(discountButton);  // Кнопка "Применить скидку"
        buttonPanel.add(purchaseButton);  // Кнопка "Приобрести"

        // Увеличиваем размер кнопок
        discountButton.setPreferredSize(new Dimension(200, 50));
        purchaseButton.setPreferredSize(new Dimension(200, 50));

        // Добавление всех панелей
        panel.add(statusPanel, BorderLayout.NORTH);
        panel.add(totalPricePanel, BorderLayout.NORTH); // Общая цена вверху
        panel.add(mainPanel, BorderLayout.CENTER);
        panel.add(historyPanel, BorderLayout.EAST);
        panel.add(buttonPanel, BorderLayout.SOUTH); // Кнопки размещены внизу

        add(panel);

        // Добавление слушателя для кнопки "Приобрести"
        purchaseButton.addActionListener(e -> {
            model.checkout(); // Оформляем заказ
        });

        // Добавление слушателя для кнопки "Применить скидку"
        discountButton.addActionListener(e -> {
            // Здесь можно задать конкретный процент скидки
            model.applyDiscount(10); // Применяем скидку 10%
        });
    }

    private void initializeProducts() {
        availableProducts.add(new Product("Телевизор", 150000, "Электроника"));
        availableProducts.add(new Product("Смартфон", 125000, "Электроника"));
        availableProducts.add(new Product("Ноутбук", 225000, "Электроника"));
        availableProducts.add(new Product("Кофеварка", 35000, "Бытовая техника"));
        availableProducts.add(new Product("Холодильник", 100000, "Бытовая техника"));
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

        StringBuilder historyText = new StringBuilder("История заказов:\n");
        for (Product product : model.getOrderHistory()) {
            historyText.append(product.getName()).append(" - ")
                    .append(product.getPrice()).append(" тг.\n");
        }
        historyArea.setText(historyText.toString());

        // Обновляем информацию о общей цене
        double totalPrice = model.getTotalPrice();
        totalPriceLabel.setText("Общая цена: " + totalPrice + " тг.");

        statusLabel.setText("Статус: " + model.getOrderStatus() + ". " + message);
    }
}

// Контроллер для управления действиями и взаимодействием между моделью и представлением
class StoreController {
    private StoreModel model;
    private StoreView view;

    public StoreController(StoreModel model, StoreView view) {
        this.model = model;
        this.view = view;
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

    // Применение скидки
    public void applyDiscount(double percentage) {
        this.price -= this.price * (percentage / 100);
    }
}

// Статус заказа
enum OrderStatus {
    CREATED, DISCOUNT_APPLIED, COMPLETED
}

// Основной класс для запуска программы
public class Main {
    public static void main(String[] args) {
        StoreModel model = new StoreModel();
        StoreView view = new StoreView(model);
        StoreController controller = new StoreController(model, view);

        view.setVisible(true);
    }
}
