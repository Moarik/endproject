import model.StoreModel;
import view.StoreView;

public class OnlineStoreApp {
    public static void main(String[] args) {
        StoreModel model = new StoreModel();
        StoreView view = new StoreView(model);
        view.setVisible(true);
    }
}
