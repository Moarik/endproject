import model.StoreModel;
import view.StoreView;
import controller.StoreController;

public class Main {
    public static void main(String[] args) {
        StoreModel model = new StoreModel();
        StoreView view = new StoreView(model);  // Передаем модель в представление
        StoreController controller = new StoreController(model, view);

        // Инициализация товаров и отображение интерфейса
        controller.initializeProducts();
        view.setVisible(true);
    }
}
