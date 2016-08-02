package com.zjxfood.interfaces;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/7/18.
 */
public class MallTypeInterImpl implements MallTypeInterface{
    private MallTypeInterface listener;
    public void setListener(MallTypeInterface listener) {
        this.listener = listener;
    }
    public void onclick(int i, ArrayList<HashMap<String,Object>> list) {
        listener.onclick(i,list);
    }
}
