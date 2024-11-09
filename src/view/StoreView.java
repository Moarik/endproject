package view;

import model.Product;
import model.StoreModel;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class StoreView extends JFrame {
    private StoreModel model;
    private JLabel totalPriceLabel;
    private JTextArea orderHistoryArea;
    private JTextArea cartArea;
    private JTextField discountField;
    private double appliedDiscount = 0;  // Храним текущую примененную скидку

    public StoreView(StoreModel model) {
        this.model = model;
        setTitle("Магазин");
        setSize(600, 600); // Увеличен размер окна для добавления корзины
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Панель с кнопками товаров
        JPanel productPanel = new JPanel();
        productPanel.setLayout(new GridLayout(0, 1)); // Вертикальная компоновка для кнопок товаров
        add(productPanel, BorderLayout.CENTER);

        // Панель для общей суммы
        JPanel totalPanel = new JPanel();
        totalPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        totalPriceLabel = new JLabel("Общая сумма: 0 тг.");
        totalPanel.add(totalPriceLabel);
        add(totalPanel, BorderLayout.NORTH); // Добавляем панель общей суммы

        // История заказов
        orderHistoryArea = new JTextArea();
        orderHistoryArea.setEditable(false);
        JScrollPane orderScrollPane = new JScrollPane(orderHistoryArea);
        add(orderScrollPane, BorderLayout.SOUTH);

        // Панель с корзиной
        cartArea = new JTextArea();
        cartArea.setEditable(false);
        JScrollPane cartScrollPane = new JScrollPane(cartArea);
        cartScrollPane.setBorder(BorderFactory.createTitledBorder("Корзина"));  // Заголовок для корзины
        add(cartScrollPane, BorderLayout.EAST); // Корзина будет справа

        // Панель с кнопками для применения скидки и покупки
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        // Поле для ввода скидки
        discountField = new JTextField(5);
        buttonPanel.add(new JLabel("Скидка (%):"));
        buttonPanel.add(discountField);

        // Кнопка для применения скидки
        JButton applyDiscountButton = new JButton("Применить скидку");
        applyDiscountButton.addActionListener(e -> applyDiscount());
        buttonPanel.add(applyDiscountButton);

        // Кнопка для оформления заказа
        JButton checkoutButton = new JButton("Приобрести");
        checkoutButton.addActionListener(e -> checkout());
        buttonPanel.add(checkoutButton);

        // Добавляем панель с кнопками
        add(buttonPanel, BorderLayout.SOUTH); // Панель с кнопками снизу

        updateProductButtons(model.getAvailableProducts());
    }

    // Метод для обновления кнопок с товарами
    public void updateProductButtons(List<Product> products) {
        JPanel productPanel = (JPanel) getContentPane().getComponent(0); // Получаем панель для кнопок
        productPanel.removeAll(); // Очищаем текущие кнопки

        // Для каждого товара создаем кнопку и добавляем её на панель
        for (Product product : products) {
            JButton productButton = new JButton("Добавить " + product.getName() + " - " + product.getPrice() + " тг.");
            productButton.addActionListener(e -> {
                // Добавляем товар в корзину
                model.addProductToCart(product);
                updateTotalPrice();
                updateCart(); // Обновляем список товаров в корзине с учётом скидки
                updateOrderHistory();
            });
            productPanel.add(productButton);
        }

        revalidate();
        repaint();
    }

    // Обновление общей цены
    public void updateTotalPrice() {
        double total = model.calculateTotalPrice();

        // Применяем скидку, если она есть
        if (appliedDiscount > 0) {
            total -= total * (appliedDiscount / 100); // Вычисляем цену со скидкой
        }

        totalPriceLabel.setText("Общая сумма: " + total + " тг.");
    }

    // Обновление истории заказов
    public void updateOrderHistory() {
        StringBuilder history = new StringBuilder("История заказов:\n");
        for (Product product : model.getCart()) {
            history.append(product.getName()).append(" - ").append(product.getPrice()).append(" тг.\n");
        }
        orderHistoryArea.setText(history.toString());
    }

    // Обновление списка товаров в корзине с учётом скидки
    public void updateCart() {
        StringBuilder cart = new StringBuilder();
        for (Product product : model.getCart()) {
            double discountedPrice = product.getPrice();

            // Если скидка применена, пересчитываем цену товара
            if (appliedDiscount > 0) {
                discountedPrice -= discountedPrice * (appliedDiscount / 100);
            }

            cart.append(product.getName()).append(" - ").append(String.format("%.2f", discountedPrice)).append(" тг.\n");
        }
        cartArea.setText(cart.toString());
    }

    // Метод для применения скидки
    private void applyDiscount() {
        try {
            double discount = Double.parseDouble(discountField.getText());
            if (discount < 0 || discount > 100) {
                JOptionPane.showMessageDialog(this, "Скидка должна быть в пределах от 0 до 100");
                return;
            }
            appliedDiscount = discount;  // Сохраняем скидку
            updateTotalPrice();
            updateCart(); // Обновляем корзину с учётом новой скидки
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Введите корректную скидку");
        }
    }

    // Метод для оформления заказа
    private void checkout() {
        model.checkout();
        appliedDiscount = 0;  // Сброс скидки после оформления заказа
        discountField.setText(""); // Очистить поле скидки
        updateTotalPrice();
        updateCart();
        updateOrderHistory();
        JOptionPane.showMessageDialog(this, "Заказ оформлен. Корзина очищена.");
    }
}
