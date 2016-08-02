package com.zjxfood.interfaces;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/7/29.
 */
public interface CarsListInterface {
    public void notifyList(HashMap<String,Object> map, String key, String status, int i, ArrayList<HashMap<String,Object>> list);
    public void alertPop(String addressId);
    public void startAppkf(String str);
}
