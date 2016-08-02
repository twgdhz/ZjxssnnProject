package com.zjxfood.model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/7/28.
 */
public class CarPrice {
    private String userId;
    private ArrayList<HashMap<String,Object>> products;
    private String useCurrency;

    public String getUserId() {
        return userId;
    }

    public ArrayList<HashMap<String, Object>> getProducts() {
        return products;
    }

    public String getUseCurrency() {
        return useCurrency;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setProducts(ArrayList<HashMap<String, Object>> products) {
        this.products = products;
    }

    public void setUseCurrency(String useCurrency) {
        this.useCurrency = useCurrency;
    }
}
