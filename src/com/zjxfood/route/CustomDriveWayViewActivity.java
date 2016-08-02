package com.zjxfood.route;

/**
 * 创建时间：11/10/15 16:07
 * 项目名称：newNaviDemo
 *
 * @author lingxiang.wang
 * @email lingxiang.wang@alibaba-inc.com
 * 类说明：
 */

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;

import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.AMapNaviViewOptions;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.project.util.Utils;
import com.zjxfood.activity.R;
import com.zjxfood.application.ExitApplication;
import com.zjxfood.common.Constants;

public class CustomDriveWayViewActivity extends BaseActivity implements
		AMapNaviListener, OnClickListener {
//	private DriveWayView myDriveWayView;
	private Bundle mBundle;
	private double lat, lng;
//	private Button mShishiBtn;
	private Double[] mLonglat;
	private String longlat;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ExitApplication.getInstance().addRouteActivity(this);
		// 为了能最快的看到效果
		mBundle = getIntent().getExtras();
		if (mBundle != null) {
			if (mBundle.getString("longlat") != null) {
				longlat = mBundle.getString("longlat");
				mLonglat = Utils.getLalon(mBundle.getString("longlat"));
			}
			lat = mLonglat[1];
			lng = mLonglat[0];
			Log.i("模拟导航", lat + "========" + lng);
		}
		startLatlng = new NaviLatLng(Constants.lat, Constants.longt);
		endLatlng = new NaviLatLng(lat, lng);
//		startLatlng = new NaviLatLng(39.92458861111111, 116.43543861111111);
		setContentView(R.layout.activity_custom_drive_way_view);
//		mShishiBtn = (Button) findViewById(R.id.navi_shishi_btn);
		naviView = (AMapNaviView) findViewById(R.id.custom_navi_view);
		naviView.onCreate(savedInstanceState);

//		myDriveWayView = (DriveWayView) findViewById(R.id.myDriveWayView);

		// 设置布局完全不可见
		AMapNaviViewOptions viewOptions = naviView.getViewOptions();
		viewOptions.setLayoutVisible(false);
//		mShishiBtn.setOnClickListener(this);
	}

	@Override
	public void showLaneInfo(AMapLaneInfo[] laneInfos,
			byte[] laneBackgroundInfo, byte[] laneRecommendedInfo) {

		// 你可以使用我们的DriveWayView来自定义位置
//		myDriveWayView.loadDriveWayBitmap(laneBackgroundInfo,
//				laneRecommendedInfo);
//		myDriveWayView.invalidate();
//		myDriveWayView.setVisibility(View.VISIBLE);

		// or
		// 只接收数据，自行绘制属于你的道路选择view
		// 下面是解释

		Log.d("解释：", "当前车道数量为" + laneInfos.length + "条");
		for (int i = 0; i < laneInfos.length; i++) {
			AMapLaneInfo info = laneInfos[i];
			Log.d("解释：", "该条车道的类型为" + info.getLaneTypeIdHexString());
			// 你将收到两位字符
			// 第一位表示背景
			// 第二位表示当前推荐的方向（如果不推荐则为F）
			// 请看drawable-hpdi，里面有一些图
			// 其中，从0 - E，各自代表

			// 直行0
			// 左转1
			// 左转，直行2
			// 右转3
			// 右转和直行4
			// 左转调头5
			// 左转和右转6
			// 直行，左转，右转 7
			// 右转调头8
			// 直行，左转调头9
			// 直行，右转调头A
			// 左转和左转调头B
			// 右转和右转掉头C
			// 。。。

			// 所以（以下三图均存在）
			// 如果00，说明该车道为直行且推荐直行
			// 如果0F，说明该车道为直行，但不推荐
			// 如果20，说明该车道为左转直行车道，推荐直行
			// 以此类推
		}

	}

	@Override
	public void hideLaneInfo() {
		Log.i("hideLaneInfo", "========hideLaneInfo===========");
//		myDriveWayView.setVisibility(View.INVISIBLE);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), RoutePlanningActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("longlat", longlat);
			intent.putExtras(bundle);
			startActivity(intent);
			break;
		default:
			break;
		}
		return false;
	}
	
	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.navi_shishi_btn:
			// intent.setClass(getApplicationContext(),
			// CustomDriveWayViewActivity.class);
			// Bundle bundle = new Bundle();
			// bundle.putString("longlat", lng+","+lat);
			// intent.putExtras(bundle);
			// startActivity(intent);
			// finish();
			break;

		default:
			break;
		}
	}
}
