package model;

import java.io.Serializable;

public class Material implements Serializable{
    private int id;
    private String name;
    private String unit;
    private float price;
    private String des;

    public Material(){
        super();

    }

    public Material(int id, String name, String unit, float price, String des){
        super();
        this.id = id;
        this.name = name;
        this.unit = unit;
        this.price = price;
        this.des = des; 
    }

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


}
