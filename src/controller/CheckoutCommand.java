package controller;

import model.StoreModel;

public class CheckoutCommand implements Command {
    private StoreModel model;

    public CheckoutCommand(StoreModel model) {
        this.model = model;
    }

    @Override
    public void execute() {
        model.checkout();
    }
}
