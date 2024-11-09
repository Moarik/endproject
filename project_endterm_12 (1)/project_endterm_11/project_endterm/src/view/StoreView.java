package view;

import model.Product;
import model.StoreModel;
import model.OrderStatus;
import observer.Observer;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class StoreView extends JFrame implements Observer {
    private List<Product> availableProducts;
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

        initializeProducts();

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.LIGHT_GRAY);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(availableProducts.size(), 2, 10, 10));
        mainPanel.setBackground(Color.WHITE);
        for (Product product : availableProducts) {
            JButton productButton = new JButton("Добавить " + product.getName() + " - " + product.getPrice() + " тг.");
            productButton.addActionListener(e -> model.addProductToCart(product));
            mainPanel.add(productButton);
        }

        JPanel historyPanel = new JPanel();
        historyPanel.setLayout(new GridLayout(2, 1, 10, 10));
        historyPanel.setBackground(Color.WHITE);
        historyPanel.add(new JScrollPane(cartArea));
        historyPanel.add(new JScrollPane(historyArea));

        JPanel statusPanel = new JPanel();
        statusPanel.setBackground(Color.LIGHT_GRAY);
        statusPanel.add(statusLabel);

        JPanel totalPricePanel = new JPanel();
        totalPricePanel.setBackground(Color.LIGHT_GRAY);
        totalPricePanel.add(totalPriceLabel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(discountButton);
        buttonPanel.add(purchaseButton);

        discountButton.setPreferredSize(new Dimension(200, 50));
        purchaseButton.setPreferredSize(new Dimension(200, 50));

        panel.add(statusPanel, BorderLayout.NORTH);
        panel.add(totalPricePanel, BorderLayout.NORTH);
        panel.add(mainPanel, BorderLayout.CENTER);
        panel.add(historyPanel, BorderLayout.EAST);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        add(panel);

        purchaseButton.addActionListener(e -> model.checkout());
        discountButton.addActionListener(e -> model.applyDiscount(10));
    }

    private void initializeProducts() {
        availableProducts = List.of(
                new Product("Телевизор", 150000, "Электроника"),
                new Product("Смартфон", 125000, "Электроника"),
                new Product("Ноутбук", 225000, "Электроника"),
                new Product("Кофеварка", 35000, "Бытовая техника"),
                new Product("Холодильник", 100000, "Бытовая техника")
        );
    }

    @Override
    public void update(String message) {
        StringBuilder cartText = new StringBuilder("Корзина:\n");
        for (Product product : model.getCart()) {
            cartText.append(product.getName()).append(" - ")
                    .append(product.getPrice()).append(" тг.\n");
        }
        cartArea.setText(cartText.toString());

        StringBuilder historyText = new StringBuilder("История заказов:\n");
        for (List<Product> order : model.getOrderHistory()) {
            for (Product product : order) {
                historyText.append(product.getName()).append(" - ")
                        .append(product.getPrice()).append(" тг.\n");
            }
            historyText.append("\n");
        }
        historyArea.setText(historyText.toString());

        totalPriceLabel.setText("Общая цена: " + model.getTotalPrice() + " тг.");
        statusLabel.setText("Статус заказа: " + model.getOrderStatus());
    }
}
