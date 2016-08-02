package com.zjxfood.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.appkefu.lib.interfaces.KFAPIs;
import com.lidroid.xutils.BitmapUtils;
import com.project.util.BitmapUtilSingle;
import com.project.util.DensityUtils;
import com.zjxfood.adapter.MallChoseTypeAdapter;
import com.zjxfood.application.ExitApplication;
import com.zjxfood.common.Constants;
import com.zjxfood.popupwindow.MallChosePopup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AppActivity extends FragmentActivity{
	private FrameLayout parentLinearLayout;
	private MallChosePopup mChosePopup;
	private MallChoseTypeAdapter mAdapter;
	private String[] malls;
	public String clickFlag = "";
	public BitmapUtils mBitmapUtils;
	private RelativeLayout mContentLayout;
	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		KFAPIs.visitorLogin(this);
		KFAPIs.loginWithUserID(Constants.mId, this);
		KFAPIs.setTagNickname(Constants.mUserName,getApplicationContext());
		ExitApplication.getInstance().addActivity(this);
		initContentView(R.layout.base_layout);
		mContentLayout = (RelativeLayout) findViewById(R.id.base_layout);
		mBitmapUtils = BitmapUtilSingle
				.getBitmapInstance(getApplicationContext());
		mBitmapUtils
				.configDefaultLoadFailedImage(R.drawable.main_paimai_zhanwei);
		malls = getResources().getStringArray(R.array.malls);
	};
	protected void setImmerseLayout(View view) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			//透明状态栏
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//			//透明导航栏
//			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

			int statusBarHeight = getStatusBarHeight(this.getBaseContext());
			view.setPadding(0, statusBarHeight, 0, 0);
		}
	}
	/**
	 * 用于获取状态栏的高度。 使用Resource对象获取（推荐这种方式）
	 *
	 * @return 返回状态栏高度的像素值。
	 */
	public static int getStatusBarHeight(Context context) {
		int result = 0;
		int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen",
				"android");
		if (resourceId > 0) {
			result = context.getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}
	public void initContentView(int layoutResID) {
		ViewGroup viewGroup = (ViewGroup) findViewById(android.R.id.content);
		viewGroup.removeAllViews();
		parentLinearLayout = new FrameLayout(this);
//		parentLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
		viewGroup.addView(parentLinearLayout);

	}

	public void dissMallPop(Activity context,View view){
		if(malls!=null && malls.length>0){
			mAdapter = new MallChoseTypeAdapter(getApplicationContext(),malls);

		mChosePopup = new MallChosePopup(context,onItemClickListener,mAdapter);
		mChosePopup.showAtLocation(view,
				Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL,0,  DensityUtils.dp2px(getApplicationContext(),50));
		}
	}

	AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
			Intent intent = new Intent();
			int version = Integer.valueOf(android.os.Build.VERSION.SDK);
			dissPop();
			Bundle bundle;
			switch (i){
				case 0:
					intent.setClass(getApplicationContext(), CashMallActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
					if(version>5){
						overridePendingTransition(R.animator.activity_zoomin, R.animator.activity_zoomout);
					}
					break;
				case 1:
					intent.setClass(getApplicationContext(), MallIndexActivity.class);

//					bundle = new Bundle();
//					bundle.putString("typeCode","2");
//					intent.putExtras(bundle);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
					if(version>5){
						overridePendingTransition(R.animator.activity_zoomin, R.animator.activity_zoomout);
					}
					break;
				case 2:
					intent.setClass(getApplicationContext(), MallProActivity.class);
					bundle = new Bundle();
					bundle.putString("proportion","1");
					bundle.putString("typeCode","2");
					intent.putExtras(bundle);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
					if(version>5){
						overridePendingTransition(R.animator.activity_zoomin, R.animator.activity_zoomout);
					}
					break;
				case 3:
					intent.setClass(getApplicationContext(), MallProActivity.class);
					bundle = new Bundle();
					bundle.putString("proportion","2");
					bundle.putString("typeCode","2");
					intent.putExtras(bundle);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
					if(version>5){
						overridePendingTransition(R.animator.activity_zoomin, R.animator.activity_zoomout);
					}
					break;
				case 4:
					intent.setClass(getApplicationContext(), MallProActivity.class);
					bundle = new Bundle();
					bundle.putString("proportion","3");
					bundle.putString("typeCode","2");
					intent.putExtras(bundle);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
					if(version>5){
						overridePendingTransition(R.animator.activity_zoomin, R.animator.activity_zoomout);
					}
					break;
				case 5:
//					intent.setClass(getApplicationContext(), MallProActivity.class);
//					bundle = new Bundle();
//					bundle.putString("proportion","4");
//					bundle.putString("typeCode","2.4");
//					intent.putExtras(bundle);
//					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//					startActivity(intent);
//					if(version>5){
//						overridePendingTransition(R.animator.activity_zoomin, R.animator.activity_zoomout);
//					}
					break;
			}
		}
	};

	private void dissPop(){
		if(mChosePopup!=null && mChosePopup.isShowing()){
			mChosePopup.dismiss();
		}
	}


	@Override
	public void setContentView(int layoutResID) {
		LayoutInflater.from(this).inflate(layoutResID, parentLinearLayout, true);
	}

	@Override
	public void setContentView(View view) {

		parentLinearLayout.addView(view);
	}

	@Override
	public void setContentView(View view, ViewGroup.LayoutParams params) {

		parentLinearLayout.addView(view, params);

	}

	View.OnClickListener click = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			Intent intent = new Intent();
			Bundle bundle ;
			int version = Integer.valueOf(android.os.Build.VERSION.SDK);
			switch (view.getId()){
				case R.id.new_main_home_menu_layout:
					Log.i("tag","首页点击");
					if(clickFlag.equals("main")){
					}else{
						intent.setClass(getApplicationContext(), NewMainActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
						startActivity(intent);
						if (version > 5) {
							overridePendingTransition(R.animator.activity_zoomin,
									R.animator.activity_zoomout);
						}
						changeCar();
					}
					break;
				case R.id.new_main_buy_menu_layout:
					Log.i("tag","买单");
					if(clickFlag.equals("buy")){

					}else {
						intent.setClass(getApplicationContext(), BuyBillActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
						if (version > 5) {
							overridePendingTransition(R.animator.activity_zoomin,
									R.animator.activity_zoomout);
						}
					}
					break;
				case R.id.new_my_buy_car:
					Log.i("tag","购物车");
					if(clickFlag.equals("car")){

					}else {
						intent.setClass(getApplicationContext(), NewCarsActivity.class);
//						intent.setClass(getApplicationContext(), NewMallActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
						if (version > 5) {
							overridePendingTransition(R.animator.activity_zoomin, R.animator.activity_zoomout);
						}
					}
					break;
				case R.id.new_main_my_menu_layout:
					Log.i("tag","我的");
					if(clickFlag.equals("my")){

					}else {
						intent.setClass(getApplicationContext(), MyNewActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
						if (version > 5) {
							overridePendingTransition(R.animator.activity_zoomin, R.animator.activity_zoomout);
						}
					}
					break;
			}
		}
	};

	private void changeCar(){
		ArrayList<HashMap<String,Object>> mList = new ArrayList<>();
		SharedPreferences sp = getApplication().getSharedPreferences("cars", Context.MODE_PRIVATE);
		String result = sp.getString("cars",null);
		if(result!=null && result.length()>0) {
			Constants.mCarsMap = Constants.getJsonObjectToMap(result);
			for (Map.Entry<String, HashMap<String, Object>> entry : Constants.mCarsMap.entrySet()) {
				Log.i("key", "Key = " + entry.getKey() + ", Value = " + entry.getValue());
				mList.add(entry.getValue());
			}
			Log.i("购物车数据", "==================" + mList);
			if (mList != null && mList.size() > 0) {
				Bitmap bitmap2 = ((BitmapDrawable) NewMainActivity.mCarImage.getDrawable()).getBitmap();
				NewMainActivity.mCarImage.setImageBitmap(Constants.createCarsBitmap(bitmap2, mList.size(), getApplicationContext()));
			}else{
				NewMainActivity.mCarImage.setImageResource(R.drawable.shopping_car_normal);
			}
		}else{
			NewMainActivity.mCarImage.setImageResource(R.drawable.shopping_car_normal);
		}
	}
}
