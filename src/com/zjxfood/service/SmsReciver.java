package com.zjxfood.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

public class SmsReciver extends BroadcastReceiver{
	@Override
	public void onReceive(Context arg0, Intent intent) {
		Bundle bundle = intent.getExtras();  
        SmsMessage msg = null;  
        if (null != bundle) {  
            Object[] smsObj = (Object[]) bundle.get("pdus");  
            for (Object object : smsObj) {  
                msg = SmsMessage.createFromPdu((byte[]) object);  
            Date date = new Date(msg.getTimestampMillis());//时间  
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
                String receiveTime = format.format(date);  
                System.out.println("number:" + msg.getOriginatingAddress()  
                + "   body:" + msg.getDisplayMessageBody() + "  time:"  
                        + msg.getTimestampMillis());  
                  Log.i("短信内容", "number:" + msg.getOriginatingAddress()  
                + "   body:" + msg.getDisplayMessageBody() + "  time:"  
                        + msg.getTimestampMillis());
                //在这里写自己的逻辑  
                if (msg.getOriginatingAddress().equals("10690278225431")) {  
                    Log.i("短信内容", msg.getDisplayMessageBody()+"============true=========="); 
                }  
                  
            }  
        }  
	}

}
