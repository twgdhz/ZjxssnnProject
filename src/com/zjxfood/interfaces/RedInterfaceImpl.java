package com.zjxfood.interfaces;

import java.util.HashMap;

public class RedInterfaceImpl {
	private RedClickInterface clickInterface;

	public RedClickInterface getClickInterface() {
		return clickInterface;
	}

	public void setClickInterface(RedClickInterface clickInterface) {
		this.clickInterface = clickInterface;
	}
	public void doClick(int p,String s){
		clickInterface.onclick(p,s);
	}
}
