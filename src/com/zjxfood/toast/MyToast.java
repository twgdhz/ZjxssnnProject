package com.zjxfood.toast;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class MyToast {
	Context mContext;  
    Toast mToast;  
  
    public MyToast(Context context) {  
        mContext = context;  
  
        mToast = Toast.makeText(context, "", Toast.LENGTH_SHORT);  
//        mToast.setGravity(17, 0, -30);//居中显示
        mToast.setGravity(Gravity.BOTTOM, 0, 80);
    }  
  
    public void show(int resId, int duration) {  
        show(mContext.getText(resId), duration);  
    }  
  
    public void show(CharSequence s, int duration) {  
        mToast.setDuration(duration);  
        mToast.setText(s);  
        mToast.show();  
    }  
  
    public void cancel() {  
        mToast.cancel();  
    }  
}
