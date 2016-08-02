package com.zjxfood.activity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.project.util.DensityUtils;
import com.project.util.ScreenUtils;
import com.umeng.analytics.MobclickAgent;
import com.zjxfood.application.ExitApplication;
import com.zjxfood.common.Constants;
import com.zjxfood.popupwindow.MallChosePopup;
import com.zjxfood.toast.MyToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 买单
 * @author zjx
 *
 */
public class BuyBillActivity extends AppActivity implements OnClickListener{

	private LinearLayout mBottomLayout;
//	private ImageView mScanningImage;
	//	private LinearLayout mGiftCenterLayout;
	private boolean isExit = false;
	private MyToast mToast;
	private TextView mShowText;
	private String ss = "";
	private ImageView mBuyImage1,mBuyImage2,mBuyBtn;
	private ImageView mCarsImage;
	private MallChosePopup mChosePopup;
	private LinearLayout mMainLayout,mBuyLayout,mCarLayout,mMyLayout;
	private ImageView mBuyImage;
//	private View mMainView;
	private TextView mBottomText;
	private ImageView mCarImage;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.buy_the_bill_layout);
		mToast = new MyToast(getApplicationContext());
		ExitApplication.getInstance().addMyActivity(this);
		setImmerseLayout(findViewById(R.id.new_buy_bille_layout));
		init();
		clickFlag = "buy";
		SharedPreferences sp = getApplicationContext()
				.getSharedPreferences("二维码结账说明", MODE_PRIVATE);
		if(sp!=null && sp.getString("Content", "")!=null && !sp.getString("Content", "").toString().equals("")){
			mShowText.setText(sp.getString("Content", ""));
		}
		ArrayList<HashMap<String,Object>> mList = new ArrayList<>();
		sp = getApplication().getSharedPreferences("cars", Context.MODE_PRIVATE);
		String result = sp.getString("cars",null);
		if(result!=null && result.length()>0) {
			Constants.mCarsMap = Constants.getJsonObjectToMap(result);
			for (Map.Entry<String, HashMap<String, Object>> entry : Constants.mCarsMap.entrySet()) {
				Log.i("key", "Key = " + entry.getKey() + ", Value = " + entry.getValue());
				mList.add(entry.getValue());
			}
			if (mList != null && mList.size() > 0) {
				Bitmap bitmap2 = ((BitmapDrawable) mCarImage.getDrawable()).getBitmap();
				mCarImage.setImageBitmap(Constants.createCarsBitmap(bitmap2, mList.size(), getApplicationContext()));
			}
		}
	}
	private void init(){
		mCarImage = (ImageView) findViewById(R.id.new_car_menu_image);
		mBottomLayout = (LinearLayout) findViewById(R.id.buy_bill_bottom_menu_layout);
		mShowText = (TextView) findViewById(R.id.qr_code_way_detail_text);
		mBuyImage1 = (ImageView) findViewById(R.id.qr_new_code_image);
		mBuyImage2 = (ImageView) findViewById(R.id.qr_new_code_image2);
		mBuyBtn = (ImageView) findViewById(R.id.qr_new_code_btn);
		LayoutParams params = mBuyImage1.getLayoutParams();
		params.width = ScreenUtils.getScreenWidth(getApplicationContext());
		params.height = (int) (ScreenUtils.getScreenHeight(getApplicationContext())*0.49)-DensityUtils.dp2px(getApplicationContext(), 40);
		mBuyImage1.setLayoutParams(params);
		params = mBuyImage2.getLayoutParams();
		params.width = ScreenUtils.getScreenWidth(getApplicationContext());
		params.height = ScreenUtils.getScreenHeight(getApplicationContext())/3-DensityUtils.dp2px(getApplicationContext(), 60);
		mBuyImage2.setLayoutParams(params);

		params = mBuyBtn.getLayoutParams();
		params.width = (int) (ScreenUtils.getScreenWidth(getApplicationContext())*0.77);
		params.height = (int) (ScreenUtils.getScreenHeight(getApplicationContext())*0.05);
		mBuyBtn.setLayoutParams(params);

		mMainLayout = (LinearLayout) findViewById(R.id.new_main_home_menu_layout);

		mBuyLayout = (LinearLayout) findViewById(R.id.new_main_buy_menu_layout);
		mBuyImage = (ImageView) findViewById(R.id.new_main_buy_menu_image);
//		mMainView = findViewById(R.id.new_main_main_view2);
//		mMainView.setVisibility(View.VISIBLE);
		mBuyLayout.setBackgroundColor(getResources().getColor(R.color.main_color13));
		mBuyImage.setImageDrawable(getResources().getDrawable(R.drawable.new_main_pay_hover));

		mCarLayout = (LinearLayout) findViewById(R.id.new_my_buy_car);
		mMyLayout = (LinearLayout) findViewById(R.id.new_main_my_menu_layout);
		mBottomText = (TextView) findViewById(R.id.new_main_buy_menu_text);
		mBottomText.setTextColor(getResources().getColor(R.color.white));

		mMainLayout.setOnClickListener(click);
		mBuyLayout.setOnClickListener(click);
		mCarLayout.setOnClickListener(click);
		mMyLayout.setOnClickListener(click);
		mBuyBtn.setOnClickListener(this);
//		mScanningImage.setOnClickListener(this);
//		mGiftCenterLayout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		int version = Integer.valueOf(android.os.Build.VERSION.SDK);
		Intent intent = new Intent();
		switch (v.getId()) {
//		case R.id.main_home_menu_layout:
//			intent.setClass(getApplicationContext(), MainActivity.class);
//			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//			startActivity(intent);
//			if(version>5){
//				overridePendingTransition(R.animator.activity_zoomin, R.animator.activity_zoomout);
//			}
////			ExitApplication.getInstance().finishMy();
//			break;
//		//我的
//		case R.id.main_my_menu_layout:
////			intent.setClass(getApplicationContext(), MyActivity.class);
//			intent.setClass(getApplicationContext(), MyNewActivity.class);
//			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			startActivity(intent);
//			if(version>5){
//				overridePendingTransition(R.animator.activity_zoomin, R.animator.activity_zoomout);
//			}
////			finish();
//			break;
//		//购物车
//			case R.id.main_cars_layout:
//
//				break;
			case R.id.qr_new_code_btn:
				//扫描二维码
				intent.setClass(getApplicationContext(), ZxingActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("flag", "1");
				intent.putExtras(bundle);
				startActivity(intent);
				break;
//		case R.id.my_gift_menu_layout:
//			dissMallPop(BuyBillActivity.this,mBottomLayout);
//			break;
		}
	}

	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
				case 0:
					isExit = false;
					break;

				default:
					break;
			}
		};
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
			case KeyEvent.KEYCODE_BACK:
				exit();
				break;
			default:
				break;
		}
		return false;
	}

	private void exit() {
		if (!isExit) {
			isExit = true;
			mToast.show("再按一次退出程序", 1);
//            Toast.makeText(getApplicationContext(), "再按一次退出程序",
//                    Toast.LENGTH_SHORT).show();
			// 利用handler延迟发送更改状态信息
			handler.sendEmptyMessageDelayed(0, 2000);
		} else {
			System.gc();
			ExitApplication.getInstance().exit();
//            System.exit(0);
		}
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		System.gc();
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mToast.cancel();
		MobclickAgent.onPause(this);
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);

	}
}
