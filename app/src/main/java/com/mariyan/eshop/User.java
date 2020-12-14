package com.mariyan.eshop;
import java.math.BigDecimal;
import java.util.ArrayList;

public class User {
    private Integer id;
    private String username;
    private String password;
    private BigDecimal cash;
    private String address;

    public static ArrayList<User> list = new ArrayList<>();

    public User(int id, String username, String password, BigDecimal cash, String address) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.cash = cash;
        this.address = address;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public BigDecimal getCash() { return cash; }

    public String getAddress() { return address; }


}
