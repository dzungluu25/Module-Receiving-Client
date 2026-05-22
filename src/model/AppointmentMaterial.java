package model;

import java.io.Serializable;

public class AppointmentMaterial implements Serializable{
    private int id;
    private int quantity;
    private double price;
    private double totalAmount;
    private Material material;

    public AppointmentMaterial() {
        super();
    }

    public AppointmentMaterial(int id, int quantity, double price, double totalAmount, Material material) {
        super();
        this.id = id;
        this.quantity = quantity;
        this.price = price;
        this.totalAmount = totalAmount;
        this.material = material;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }
}
