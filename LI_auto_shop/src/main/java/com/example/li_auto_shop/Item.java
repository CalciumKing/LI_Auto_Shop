package com.example.li_auto_shop;
public class Item {
    // region Variables
    private String id;
    private String vehicle_type;
    private String year;
    private String make;
    private String model;
    private double price;
    private int quantity_in_stock;
    private int reorder_level;
    // endregion
    public Item(String id, String vehicle_type, String year,
                String make, String model, double price,
                int quantity_in_stock, int reorder_level) {
        this.id = id;
        this.vehicle_type = vehicle_type;
        this.year = year;
        this.make = make;
        this.model = model;
        this.price = price;
        this.quantity_in_stock = quantity_in_stock;
        this.reorder_level = reorder_level;
    }
    // region Getters
    public String getId() {
        return id;
    }
    public String getVehicle_type() {
        return vehicle_type;
    }
    public String getYear() {
        return year;
    }
    public String getMake() {
        return make;
    }
    public String getModel() {
        return model;
    }
    public double getPrice() {
        return price;
    }
    public int getQuantity_in_stock() {
        return quantity_in_stock;
    }
    public int getReorder_level() {
        return reorder_level;
    }
    // endregion
    // region Setters
    public void setId(String id) {
        this.id = id;
    }
    public void setVehicle_type(String vehicle_type) {
        this.vehicle_type = vehicle_type;
    }
    public void setYear(String year) {
        this.year = year;
    }
    public void setMake(String make) {
        this.make = make;
    }
    public void setModel(String model) {
        this.model = model;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public void setQuantity_in_stock(int quantity_in_stock) {
        this.quantity_in_stock = quantity_in_stock;
    }
    public void setReorder_level(int reorder_level) {
        this.reorder_level = reorder_level;
    }
    // endregion
}