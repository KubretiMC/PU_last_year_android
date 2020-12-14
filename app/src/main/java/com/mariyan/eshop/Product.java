package com.mariyan.eshop;
import java.math.BigDecimal;
import java.util.ArrayList;

public class Product {
    private Integer id;
    private String name;
    private BigDecimal cost;

    public static ArrayList<Product> list = new ArrayList<>();

    public Product(int id, String name, BigDecimal cost) {
        this.id = id;
        this.name = name;
        this.cost = cost;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getCost() {
        return cost;
    }


}
