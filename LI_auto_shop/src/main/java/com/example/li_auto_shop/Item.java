package com.example.li_auto_shop;

public class Item {
    // region Variables
    private String id, brand, model, path;
    private double price;
    private int on_hand, reorder_level;
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
    // region Getters and Setters
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getBrand() {
        return brand;
    }
    public void setBrand(String brand) {
        this.brand = brand;
    }
    public String getModel() {
        return model;
    }
    public void setModel(String model) {
        this.model = model;
    }
    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public int getOn_hand() {
        return on_hand;
    }
    public void setOn_hand(int on_hand) {
        this.on_hand = on_hand;
    }
    public int getReorder_level() {
        return reorder_level;
    }
    public void setReorder_level(int reorder_level) {
        this.reorder_level = reorder_level;
    }
    // endregion
}