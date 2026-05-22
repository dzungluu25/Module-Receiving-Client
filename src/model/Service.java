package model;

import java.io.Serializable;

public class Service implements Serializable{
    private int id;
    private String name;
    private String unit;
    private float price;
    private String des;

    public Service(){
        super();

    }

    public Service(int id, String name, String unit, float price, String des){
        super();
        this.id = id;
        this.name = name;
        this.unit = unit;
        this.price = price;
        this.des = des; 
    }
    // Getter and Setter
    public int getId(){
        return this.id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getName(){
        return this.name;
    }
    
    public void setName(String name){
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}
