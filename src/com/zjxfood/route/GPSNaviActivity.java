package com.zjxfood.route;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;

import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.model.NaviLatLng;
import com.project.util.Utils;
import com.zjxfood.activity.R;
import com.zjxfood.application.ExitApplication;
import com.zjxfood.common.Constants;

/**
 * 创建时间：11/11/15 18:50 项目名称：newNaviDemo
 * 
 * @author lingxiang.wang
 * @email lingxiang.wang@alibaba-inc.com 类说明：
 */

public class GPSNaviActivity extends BaseActivity implements OnClickListener {

	private Bundle mBundle;
	private Double[] mLonglat;
	private String longlat;
//	private Button mMoniBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ExitApplication.getInstance().addRouteActivity(this);
		setContentView(R.layout.activity_basic_navi);
//		mMoniBtn = (Button) findViewById(R.id.navi_moni_btn);
		
		mBundle = getIntent().getExtras();
		
		naviView = (AMapNaviView) findViewById(R.id.navi_view);
        naviView.onCreate(savedInstanceState);
        startLatlng = new NaviLatLng(Constants.lat, Constants.longt);
        if (mBundle != null) {
			if (mBundle.getString("longlat") != null) {
				longlat = mBundle.getString("longlat");
				Log.i("开始导航3","============开始导航=============="+ mBundle.getString("longlat"));
				mLonglat = Utils.getLalon(mBundle.getString("longlat"));
				endLatlng = new NaviLatLng(mLonglat[1], mLonglat[0]);
			}
		}
        naviView.setAMapNaviViewListener(this);
//		mMoniBtn.setOnClickListener(this);
	}
	@Override
    public void onCalculateRouteSuccess() {
        aMapNavi.startNavi(AMapNavi.GPSNaviMode);
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
		case R.id.navi_moni_btn:
//			intent.setClass(getApplicationContext(), CustomDriveWayViewActivity.class);
//			Bundle bundle = new Bundle();
//			bundle.putDouble("lat", mLonglat[1]);
//			bundle.putDouble("lng", mLonglat[0]);
//			intent.putExtras(bundle);
//			startActivity(intent);
//			finish();
//			naviView.onDestroy();
//			aMapNavi.destroy();
//			ttsManager.destroy();
			break;

		default:
			break;
		}
	}
}
