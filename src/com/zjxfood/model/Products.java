package com.zjxfood.model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/7/27.
 */
public class Products {
    private String addressId;
    private String memo;
    private String userId;
    private String useCurrency;
    private ArrayList<HashMap<String,Object>> products;

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUseCurrency(String useCurrency) {
        this.useCurrency = useCurrency;
    }

    public void setProducts(ArrayList<HashMap<String, Object>> products) {
        this.products = products;
    }

    public String getAddressId() {
        return addressId;
    }

    public String getMemo() {
        return memo;
    }

    public String getUserId() {
        return userId;
    }

    public String getUseCurrency() {
        return useCurrency;
    }

    public ArrayList<HashMap<String, Object>> getProducts() {
        return products;
    }
}
