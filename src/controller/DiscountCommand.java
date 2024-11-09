package controller;

import model.StoreModel;

public class DiscountCommand implements Command {
    private StoreModel model;
    private double discount;

    public DiscountCommand(StoreModel model, double discount) {
        this.model = model;
        this.discount = discount;
    }

    @Override
    public void execute() {
        model.applyDiscount(discount);
    }
}
