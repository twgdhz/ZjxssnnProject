package com.zjxfood.popupwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.project.util.DensityUtils;
import com.zjxfood.activity.R;
import com.zjxfood.adapter.MallChoseTypeAdapter;

public class MallChosePopup extends PopupWindow {
//	private Button btn_take_photo, btn_pick_photo, btn_cancel;
	private View mMenuView;
	private ListView mListView;
	private ImageView mBottomImage;
//	private TextView btn_take_photo, btn_pick_photo, btn_cancel;

	public MallChosePopup(Activity context, AdapterView.OnItemClickListener itemClickListener, MallChoseTypeAdapter adapter) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.popup_mall_chose_layout,null);
		mListView = (ListView) mMenuView.findViewById(R.id.lv_pop_right);
		mBottomImage = (ImageView) mMenuView.findViewById(R.id.iv2_pop_right);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mBottomImage.getLayoutParams());
		params.setMargins(DensityUtils.dp2px(context,215),0,0,0);
		mBottomImage.setLayoutParams(params);
//		mBottomImage.setPadding(DensityUtils.dp2px(context,20),0,0,0);

		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(itemClickListener);
//		btn_take_photo = (TextView) mMenuView.findViewById(R.id.head_pupup_btn_take_photo);
//		btn_pick_photo = (TextView) mMenuView.findViewById(R.id.head_pupup_btn_pick_photo);
//		btn_cancel = (TextView) mMenuView.findViewById(R.id.head_pupup_btn_cancel);
		//取消按钮
//		btn_cancel.setOnClickListener(new OnClickListener() {
//			public void onClick(View v) {
//				//销毁弹出框
//				dismiss();
//			}
//		});
		//设置按钮监听
//		btn_pick_photo.setOnClickListener(itemsOnClick);
//		btn_take_photo.setOnClickListener(itemsOnClick);
		//设置SelectPicPopupWindow的View
		this.setContentView(mMenuView);
		//设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.FILL_PARENT);

		//设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.FILL_PARENT);
		//设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		//设置SelectPicPopupWindow弹出窗体动画效果
//		this.setAnimationStyle(R.style.AnimBottom);
		//实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0x20000000);
		//设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
		//mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
		mMenuView.setOnTouchListener(new OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event) {
				int height = mMenuView.findViewById(R.id.lv_pop_right).getTop();
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
