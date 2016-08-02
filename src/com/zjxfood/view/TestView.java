package com.zjxfood.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.project.util.ScreenUtils;

/**
 * Created by Administrator on 2016/5/23.
 */
public class TestView extends View {
    private Paint mPaint1,mPaint2,mPaint;
    private int mViewWidth=0;
    private LinearGradient mLinearGradient;
    private Matrix mGradientMatrix;
    private int length;
    private int mCircleXY;
    private float mRadius;
    private RectF mArcRectF;

    public TestView(Context context) {
        super(context);
    }
    public TestView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint1 = new Paint();
        mPaint1.setColor(Color.BLUE);
        mPaint1.setStyle(Paint.Style.FILL);
        mPaint2 = new Paint();
        mPaint2.setStyle(Paint.Style.FILL);
        mPaint2.setColor(Color.YELLOW);

        length = ScreenUtils.getScreenWidth(context);
        mCircleXY = length/2;
        mRadius = (float) (length*0.5/2);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//       canvas.drawRect(0,0,getMeasuredWidth(),getMeasuredHeight(),mPaint1);
//        //绘制内层矩形
//        canvas.drawRect(10,10,getMeasuredWidth()-10,getMeasuredHeight()-10,mPaint2);
//        canvas.save();
//        //绘制前平移10个像素
//        canvas.translate(10,0);
//        super.onDraw(canvas);
//        canvas.restore();
//        mArcRectF = new RectF((float)(length*0.1),(float)(length*0.1),(float)(length*0.9),(float)(length*0.9));
        //绘制圆
        canvas.drawCircle(mCircleXY,mCircleXY,mRadius,mPaint1);
        //绘制弧线
//        canvas.drawArc(mArcRectF,270,);
        //绘制文字
//        canvas.drawText("hello world",0,10,mCircleXY,mCircleXY+(10/4),mPaint2);
        canvas.drawRect(100,100,100,100,mPaint1);
//        for(int i=0;i<10;i++){
//            canvas.drawRect(10,10,10,10,mPaint2);
//        }
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
//        if(mViewWidth==0){
//            mViewWidth = getMeasuredWidth();
//            if(mViewWidth>0){
//                mPaint = new Paint();
//                mLinearGradient = new LinearGradient(0,0,mViewWidth,0,new int[]{Color.BLUE,0xffffffff,Color.BLUE},null, Shader.TileMode.CLAMP);
//                mPaint.setShader(mLinearGradient);
//                mGradientMatrix = new Matrix();
//            }
//        }
    }
}
