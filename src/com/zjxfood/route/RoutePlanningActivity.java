package com.zjxfood.route;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviPath;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.navi.view.RouteOverLay;
import com.autonavi.tbt.TrafficFacilityInfo;
import com.project.util.TTSController;
import com.project.util.Utils;
import com.zjxfood.activity.R;
import com.zjxfood.application.ExitApplication;
import com.zjxfood.common.Constants;

import java.util.ArrayList;

/**
 * 创建时间：11/11/15 17:32 项目名称：newNaviDemo
 * 
 * @author lingxiang.wang
 * @email lingxiang.wang@alibaba-inc.com 类说明：
 */

public class RoutePlanningActivity extends Activity implements
		AMapNaviListener, View.OnClickListener, LocationSource,
		AMapLocationListener, OnCheckedChangeListener {

	// 驾车线路：路线规划、模拟导航、实时导航按钮
	private Button mDriveRouteButton;
	// 步行线路：路线规划、模拟导航、实时导航按钮
	private Button mFootRouteButton;
	// 地图和导航资源
	private MapView mMapView;
	private AMap mAMap;

	// 起点终点坐标
	private NaviLatLng mNaviStart = new NaviLatLng(Constants.lat,
			Constants.longt);
	// private NaviLatLng mNaviStart;
	// private NaviLatLng mNaviEnd = new NaviLatLng(39.983456, 116.3154950);
	private NaviLatLng mNaviEnd;
	// 起点终点列表
	private ArrayList<NaviLatLng> mStartPoints = new ArrayList<NaviLatLng>();
	private ArrayList<NaviLatLng> mEndPoints = new ArrayList<NaviLatLng>();

	// 规划线路
	private RouteOverLay mRouteOverLay;
	private TTSController ttsManager;
	private AMapNavi aMapNavi;
	private Bundle mBundle;
	private Double[] mLonglat;
	private OnLocationChangedListener mListener;
	private AMapLocationClient mlocationClient;
	private AMapLocationClientOption mLocationOption;
	private RadioGroup mGPSModeGroup;
	private Button mShishiBtn, mMoniBtn;
	private String mLnglat;
	private int n = 1;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ExitApplication.getInstance().addActivity(this);
		ExitApplication.getInstance().addRouteActivity(this);
		mBundle = getIntent().getExtras();
		if (mBundle != null) {
			if (mBundle.getString("longlat") != null) {
				mLonglat = Utils.getLalon(mBundle.getString("longlat"));
				mLnglat = mBundle.getString("longlat");
				if(mLonglat!=null && mLonglat[1]!=null && mLonglat[0]!=null){
				mNaviEnd = new NaviLatLng(mLonglat[1], mLonglat[0]);
				}
				
			}
		}
		ttsManager = TTSController.getInstance(this);
		ttsManager.init();

		aMapNavi = AMapNavi.getInstance(this);
		aMapNavi.setAMapNaviListener(this);
		aMapNavi.setAMapNaviListener(ttsManager);
		aMapNavi.setEmulatorNaviSpeed(150);

		setContentView(R.layout.activity_route_planning);
		initView(savedInstanceState);
		// 默认显示驾车路线
		calculateDriveRoute();
	}

	// 初始化View
	private void initView(Bundle savedInstanceState) {

		mDriveRouteButton = (Button) findViewById(R.id.car_navi_route);
		mFootRouteButton = (Button) findViewById(R.id.foot_navi_route);
		mShishiBtn = (Button) findViewById(R.id.shishi_navi_route);
		mMoniBtn = (Button) findViewById(R.id.moni_navi_route);

		mDriveRouteButton.setOnClickListener(this);
		mFootRouteButton.setOnClickListener(this);
		mShishiBtn.setOnClickListener(this);
		mMoniBtn.setOnClickListener(this);

		mMapView = (MapView) findViewById(R.id.mapview);
		mMapView.onCreate(savedInstanceState);
		if (mAMap == null) {
			mAMap = mMapView.getMap();
			setUpMap();
		}

		// mAMap = mMapView.getMap();
		mRouteOverLay = new RouteOverLay(mAMap, null);
		mGPSModeGroup = (RadioGroup) findViewById(R.id.route_gps_radio_group);
		mGPSModeGroup.setOnCheckedChangeListener(this);
	}

	/**
	 * 设置一些amap的属性
	 */
	private void setUpMap() {
		mAMap.setLocationSource(this);// 设置定位监听
		mAMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
		mAMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
		// 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
		mAMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
	}

	// 计算驾车路线
	private void calculateDriveRoute() {
		mNaviStart = parseEditText(mNaviStart.getLatitude() + ","
				+ mNaviStart.getLongitude());
		mNaviEnd = parseEditText(mNaviEnd.getLatitude() + ","
				+ mNaviEnd.getLongitude());
		mStartPoints.clear();
		mEndPoints.clear();
		mStartPoints.add(mNaviStart);
		mEndPoints.add(mNaviEnd);

		boolean isSuccess = aMapNavi.calculateDriveRoute(mStartPoints,
				mEndPoints, null, AMapNavi.DrivingDefault);
		if (!isSuccess) {
			showToast("路线计算失败,检查参数情况");
		}
	}

	private NaviLatLng parseEditText(String text) {
		try {
			double latD = Double.parseDouble(text.split(",")[0]);
			double lonD = Double.parseDouble(text.split(",")[1]);
			return new NaviLatLng(latD, lonD);
		} catch (Exception e) {
			Toast.makeText(this, "e:" + e, Toast.LENGTH_SHORT).show();
			Toast.makeText(this, "格式:[lat],[lon]", Toast.LENGTH_SHORT).show();
		}
		return null;
	}

	// 计算步行路线
	private void calculateFootRoute() {
		Log.i("步行路线", "====================");
		mNaviStart = parseEditText(mNaviStart.getLatitude() + ","
				+ mNaviStart.getLongitude());
		mNaviEnd = parseEditText(mNaviEnd.getLatitude() + ","
				+ mNaviEnd.getLongitude());
		boolean isSuccess = aMapNavi.calculateWalkRoute(mNaviStart, mNaviEnd);
		if (!isSuccess) {
			showToast("路线计算失败,检查参数情况");
		}
	}

	private void showToast(String message) {
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}

	// -------------------------Button点击事件和返回键监听事件---------------------------------
	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.car_navi_route:
			calculateDriveRoute();
			break;
		case R.id.foot_navi_route:
			calculateFootRoute();
			break;
		// 实时导航
		case R.id.shishi_navi_route:
			intent.setClass(getApplicationContext(), GPSNaviActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("longlat", mLnglat);
			intent.putExtras(bundle);
			startActivity(intent);
			finish();
			break;
		// 模拟导航
		case R.id.moni_navi_route:
			intent.setClass(getApplicationContext(),
					CustomDriveWayViewActivity.class);
			bundle = new Bundle();
			bundle.putString("longlat", mLnglat);
			intent.putExtras(bundle);
			startActivity(intent);
			finish();
			break;
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			ExitApplication.getInstance().finishRoute();
//			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	// --------------------导航监听回调事件-----------------------------
	@Override
	public void onArriveDestination() {

	}

	@Override
	public void onArrivedWayPoint(int arg0) {

	}

	@Override
	public void onCalculateRouteFailure(int arg0) {
		showToast("路径规划出错" + arg0);
	}

	@Override
	public void onCalculateRouteSuccess() {
		AMapNaviPath naviPath = aMapNavi.getNaviPath();
		if (naviPath == null) {
			return;
		}
		// 获取路径规划线路，显示到地图上
		mRouteOverLay.setRouteInfo(naviPath);
		mRouteOverLay.addToMap();
	}

	@Override
	public void onEndEmulatorNavi() {

	}

	@Override
	public void onGetNavigationText(int arg0, String arg1) {

	}

	@Override
	public void onGpsOpenStatus(boolean arg0) {

	}

	@Override
	public void onInitNaviFailure() {

	}

	@Override
	public void onInitNaviSuccess() {

	}

	@Override
	public void onLocationChange(AMapNaviLocation arg0) {

	}

	@Override
	public void onNaviInfoUpdated(AMapNaviInfo arg0) {

	}

	@Override
	public void onReCalculateRouteForTrafficJam() {

	}

	@Override
	public void onReCalculateRouteForYaw() {

	}

	@Override
	public void onStartNavi(int arg0) {

	}

	@Override
	public void onTrafficStatusUpdate() {

	}

	// ------------------生命周期重写函数---------------------------

	@Override
	public void onResume() {
		super.onResume();
		mMapView.onResume();
		mStartPoints.add(mNaviStart);
		mEndPoints.add(mNaviEnd);
	}

	@Override
	public void onPause() {
		super.onPause();
		if(mMapView!=null){
		mMapView.onPause();
		}
		if(aMapNavi!=null){
		aMapNavi.destroy();
		}
//		ttsManager.destroy();
	}
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mMapView.onSaveInstanceState(outState);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if(mMapView!=null){
		mMapView.onDestroy();
		}
		if(aMapNavi!=null){
		aMapNavi.destroy();
		}
		if(ttsManager!=null){
		ttsManager.destroy();
		}
		if (null != mlocationClient) {
			mlocationClient.onDestroy();
		}
	}
	
	@Override
	public void onNaviInfoUpdate(NaviInfo arg0) {

	}

	@Override
	public void OnUpdateTrafficFacility(TrafficFacilityInfo trafficFacilityInfo) {

	}

	@Override
	public void showCross(AMapNaviCross aMapNaviCross) {

	}

	@Override
	public void hideCross() {

	}

	@Override
	public void showLaneInfo(AMapLaneInfo[] aMapLaneInfos, byte[] bytes,
			byte[] bytes1) {

	}

	@Override
	public void hideLaneInfo() {

	}

	@Override
	public void onLocationChanged(AMapLocation amapLocation) {
		if (mListener != null && amapLocation != null) {
			if (amapLocation != null && amapLocation.getErrorCode() == 0) {
				
				// mLocationErrText.setVisibility(View.GONE);
				mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
				Double geoLat = amapLocation.getLatitude();
				Double geoLng = amapLocation.getLongitude();

				if (n == 1) {
					mAMap.moveCamera(CameraUpdateFactory.zoomTo(16));
				}
				n++;
				Log.i("自动定位成功", geoLat + "=====================" + geoLng+"==="+amapLocation.getLocationType());
//				Bundle locBundle = amapLocation.getExtras();
			} else {
				String errText = "定位失败," + amapLocation.getErrorCode() + ": "
						+ amapLocation.getErrorInfo();
				Log.e("AmapErr", errText);
				// mLocationErrText.setVisibility(View.VISIBLE);
				// mLocationErrText.setText(errText);
			}
		}
	}

	@Override
	public void activate(OnLocationChangedListener listener) {
		mListener = listener;
		if (mlocationClient == null) {
			mlocationClient = new AMapLocationClient(this);
			mLocationOption = new AMapLocationClientOption();
			// 设置定位监听
			mlocationClient.setLocationListener(this);
			// 设置为高精度定位模式
			mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
			// 设置定位参数
			mlocationClient.setLocationOption(mLocationOption);
			// 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
			// 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
			// 在定位结束后，在合适的生命周期调用onDestroy()方法
			// 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
			mlocationClient.startLocation();
		}
	}

	@Override
	public void deactivate() {
		mListener = null;
		if (mlocationClient != null) {
			mlocationClient.stopLocation();
			mlocationClient.onDestroy();
		}
		mlocationClient = null;
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.gps_locate_button:
			// 设置定位的类型为定位模式
			mAMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
			break;
		case R.id.gps_follow_button:
			// 设置定位的类型为 跟随模式
			mAMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_FOLLOW);
			break;
		case R.id.gps_rotate_button:
			// 设置定位的类型为根据地图面向方向旋转
			mAMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_ROTATE);
			break;
		}
	}
	
}
