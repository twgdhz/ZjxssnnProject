package com.zjxfood.interfaces;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/7/29.
 */
public class CarsListImpl implements CarsListInterface{
    private CarsListInterface listInterface;

    public CarsListInterface getListInterface() {
        return listInterface;
    }

    public void setListInterface(CarsListInterface listInterface) {
        this.listInterface = listInterface;
    }

    @Override
    public void notifyList(HashMap<String, Object> map,String key,String status,int i,ArrayList<HashMap<String, Object>> list) {
        listInterface.notifyList(map,key,status,i,list);
    }

    @Override
    public void alertPop(String addressId) {
        listInterface.alertPop(addressId);
    }

    @Override
    public void startAppkf(String str) {
     listInterface.startAppkf(str);
    }
}
