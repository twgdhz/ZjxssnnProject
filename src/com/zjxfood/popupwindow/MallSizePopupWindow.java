package com.zjxfood.popupwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.zjxfood.activity.R;
import com.zjxfood.adapter.MallDetailGridAdapter;

public class MallSizePopupWindow extends PopupWindow {

	private View mMenuView;
	private ImageView mXImage;
	private GridView mGridView;
	private Button mConfirmBtn;

	public MallSizePopupWindow(Activity context,OnItemClickListener itemsOnClick,MallDetailGridAdapter adapter,OnClickListener clickListener) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.popup_mall_detail_color_size_layout, null);
		mXImage = (ImageView) mMenuView.findViewById(R.id.pop_mall_detail_x);
		mGridView = (GridView) mMenuView.findViewById(R.id.popup_mall_detail_grid);
		mConfirmBtn = (Button) mMenuView.findViewById(R.id.popup_mall_bottom_confirm_btn);

		mGridView.setAdapter(adapter);
		mGridView.setOnItemClickListener(itemsOnClick);
		mConfirmBtn.setOnClickListener(clickListener);
		mXImage.setOnClickListener(clickListener);
		//设置SelectPicPopupWindow的View
		this.setContentView(mMenuView);
		//设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.FILL_PARENT);
		//设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
//		this.setHeight(DensityUtils.dp2px(context, 300));
		//设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		//设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.AnimBottom);
		//实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0xb0000000);
//		ColorDrawable dw = new ColorDrawable(R.color.touming);
		//设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
		//mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
		mMenuView.setOnTouchListener(new OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event) {
				
				int height = mMenuView.findViewById(R.id.popup_mall_detail_content_layout).getTop();
				int y=(int) event.getY();
				if(event.getAction()==MotionEvent.ACTION_UP){
					if(y<height){
						dismiss();
					}
				}				
				return true;
			}
		});

	}

}
