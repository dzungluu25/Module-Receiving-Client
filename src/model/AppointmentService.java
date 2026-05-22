package model;

import java.io.Serializable;

public class AppointmentService implements Serializable{
    private int id;
    private int quantity;
    private double price;
    private double totalAmount;
    private Service service;
    private Staff staff;

    public AppointmentService() {
        super();
    }

    public AppointmentService(int id, int quantity, double price, double totalAmount, Service service, Staff staff) {
        super();
        this.id = id;
        this.quantity = quantity;
        this.price = price;
        this.totalAmount = totalAmount;
        this.service = service;
        this.staff = staff;
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

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }
}
