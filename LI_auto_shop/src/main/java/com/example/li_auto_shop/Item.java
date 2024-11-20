package com.example.li_auto_shop;

public class Item {
    // region Variables
    private final String id, brand, model, path;
    private final double price;
    private final int on_hand, reorder_level;
    // endregion
    public Item(String id, String brand, String model, double price,
                int on_hand, int reorder_level, String path) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.price = price;
        this.on_hand = on_hand;
        this.reorder_level = reorder_level;
        this.path = path;
    }
    // region Getters
    public String getId() {
        return id;
    }
    public String getBrand() {
        return brand;
    }
    public String getModel() {
        return model;
    }
    public double getPrice() {
        return price;
    }
    public int getOn_hand() {
        return on_hand;
    }
    public int getReorder_level() {
        return reorder_level;
    }
    public String getPath() {
        return path;
    }
    // endregion
}