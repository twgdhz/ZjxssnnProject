package com.zjxfood.interfaces;

import java.util.ArrayList;
import java.util.HashMap;

import android.graphics.Bitmap;

public interface MyInterface {
	public void onclick(Bitmap bitmap);
	public void updateOrder(HashMap<String, ArrayList<HashMap<String, Object>>> map,float price);
}
