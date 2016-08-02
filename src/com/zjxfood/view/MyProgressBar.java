package com.zjxfood.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

public class MyProgressBar extends View{
	// 创建画笔  
    Paint p = new Paint();  
   
	public MyProgressBar(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		 p.setColor(Color.RED);// 设置红色  
		//画圆角矩形  
        p.setStyle(Paint.Style.FILL);//充满  
        p.setColor(Color.LTGRAY);  
        p.setAntiAlias(true);// 设置画笔的锯齿效果  
        canvas.drawText("画圆角矩形:", 10, 260, p);  
        RectF oval3 = new RectF(80, 260, 200, 300);// 设置个新的长方形  
        canvas.drawRoundRect(oval3, 20, 15, p);//第二个参数是x半径，第三个参数是y半径  
	}

}
