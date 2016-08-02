package com.zjxfood.interfaces;

import java.util.ArrayList;
import java.util.HashMap;

import android.graphics.Bitmap;

public class MyInterfaceImpl {
	private MyInterface listener;

	public void setListener(MyInterface listener) {
		this.listener = listener;
	} 
	public void doClick(Bitmap bitmap) {  
        listener.onclick(bitmap);  
    } 
	
	public void doUpdateOrder(HashMap<String, ArrayList<HashMap<String, Object>>> map,float price){
		listener.updateOrder(map, price);
	}
}
