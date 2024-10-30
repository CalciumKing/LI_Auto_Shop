package com.example.li_auto_shop;

public class Item {
    // region Variables
    private final String id, vehicle_type, year, make, model;
    private final double price;
    private final int quantity_in_stock, reorder_level;
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
}