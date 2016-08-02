package com.zjxfood.view;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

public class TiltTextView extends TextView {
	private static final String NAMESPACE = "http://schemas.android.com/apk/res/android";
	private static final String ATTR_ROTATE = "rotate";
	private static final int DEFAULTVALUE_DEGREES = 45;
	private int degrees;

	public TiltTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		degrees = attrs.getAttributeIntValue(NAMESPACE, ATTR_ROTATE,
				DEFAULTVALUE_DEGREES);
	}

	@SuppressLint("DrawAllocation") @Override
	protected void onDraw(Canvas canvas) {
		canvas.rotate(degrees, getMeasuredWidth() / 2, getMeasuredHeight() / 2);
		super.onDraw(canvas);
	}

}
