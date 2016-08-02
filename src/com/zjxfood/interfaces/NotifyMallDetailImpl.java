package com.zjxfood.interfaces;

public class NotifyMallDetailImpl {
	private NotifyMallDetailInterface listener;

	public void setListener(NotifyMallDetailInterface listener) {
		this.listener = listener;
	} 
	public void doNotify() {  
        listener.notifyScrollBottom();
    } 
	
	public void doIng(){
		listener.notifyScrollBottom();
	}
}
