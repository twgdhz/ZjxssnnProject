package com.zjxfood.interfaces;

import java.util.ArrayList;
import java.util.HashMap;
public class FavoriteFaceImpl {
	private FavoriteInterface mInterface;

	public FavoriteInterface getmInterface() {
		return mInterface;
	}

	public void setmInterface(FavoriteInterface mInterface) {
		this.mInterface = mInterface;
	}
	public void doList(HashMap<String, String> map){
		mInterface.onclick(map);
	}
}
