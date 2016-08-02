package com.zjxfood.popupwindow;

import java.io.InputStream;
import org.apache.http.util.EncodingUtils;
import com.zjxfood.activity.R;
import com.zjxfood.interfaces.MyInterfaceImpl;
import com.zjxfood.view.Crop_Canvas;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.PopupWindow;

public class PhotoPopupWindow extends PopupWindow {


//	private Button btn_take_photo, btn_pick_photo, btn_cancel;
	private View mMenuView;
//	private Crop_Canvas mCanvas=null;
	private Context mContext;
	private ImageView mConfirmImage,mExitImage;
	private Bitmap mBitmap;
	private Crop_Canvas canvas;
	private MyInterfaceImpl mClick;
	
	public PhotoPopupWindow(Activity context,Bitmap bitmap,Crop_Canvas mCanvas,MyInterfaceImpl myClick) {
		super(context);
		mContext = context;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mClick = myClick;
		mMenuView = inflater.inflate(R.layout.photo_cut_layout, null);
		mConfirmImage = (ImageView) mMenuView.findViewById(R.id.confirm_images);
		mExitImage = (ImageView) mMenuView.findViewById(R.id.exit_images);
		mCanvas = (Crop_Canvas) mMenuView.findViewById(R.id.my_popup_cut_canvas);
		mCanvas.setBitmap(bitmap);
		canvas = mCanvas;
//		mBitmap = mCanvas.getSubsetBitmap();
//		mContentText.setMovementMethod(ScrollingMovementMethod.getInstance());
//		mCanvas.setImageBitmap(bitmap);
		
		mConfirmImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				dismiss();
				mClick.doClick(canvas.getSubsetBitmap());
			}
		});
		//设置按钮监听
		mExitImage.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				//销毁弹出框
				dismiss();
			}
		});
		//设置SelectPicPopupWindow的View
		this.setContentView(mMenuView);
		//设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.FILL_PARENT);
		//设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.MATCH_PARENT);
		//设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		//设置SelectPicPopupWindow弹出窗体动画效果
//		this.setAnimationStyle(R.style.AnimTop);
		//实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		//设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
		//mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
		mMenuView.setOnTouchListener(new OnTouchListener() {
			//
			public boolean onTouch(View v, MotionEvent event) {
//				int height = mMenuView.findViewById(R.id.popup_cut_photo_content_layout).getTop();
//				int y=(int) event.getY();
//				if(event.getAction()==MotionEvent.ACTION_UP){
//					if(y<height){
//						dismiss();
//					}
//				}
				return true;
			}
		});
	}
	//读取协议文件
		public String getFromAssets(String fileName) {
			String result = "";
			try {
				InputStream in = mContext.getResources().getAssets().open(fileName);
				// 获取文件的字节数
				int lenght = in.available();
				// 创建byte数组
				byte[] buffer = new byte[lenght];
				// 将文件中的数据读到byte数组中
				in.read(buffer);
				result = EncodingUtils.getString(buffer, "UTF-8");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}
}
