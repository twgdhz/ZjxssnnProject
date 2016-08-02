package com.zjxfood.application;

import android.app.Service;
import android.os.Vibrator;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.widget.TextView;

import com.baidu.location.LocationClient;
import com.baidu.location.service.LocationService;
import com.baidu.location.service.WriteLog;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.zjxfood.route.LocationApplication;

public class MyApplication extends MultiDexApplication {
	
	public LocationClient mLocationClient;
    public LocationApplication.MyLocationListener mMyLocationListener;

    public TextView mLocationResult,logMsg;
    public TextView trigger,exit;
    public Vibrator mVibrator;
    public double latitude;
	public double longitude;
	public String mCityStr,mProvince;
	public static final String action = "main.location.broadcast";

    public LocationService locationService;
	@Override
	public void onCreate() {
		super.onCreate();
		MultiDex.install(this);
		ImageLoaderConfiguration configuration = ImageLoaderConfiguration
				.createDefault(this);
		ImageLoader.getInstance().init(configuration);
//		Thread.setDefaultUncaughtExceptionHandler(this);

        super.onCreate();
        locationService = new LocationService(getApplicationContext());
		mVibrator =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
		WriteLog.getInstance().init(); // 初始化日志
//        mLocationClient = new LocationClient(this.getApplicationContext());
//        mMyLocationListener = new MyLocationListener();
//        mLocationClient.registerLocationListener(mMyLocationListener);
//        mVibrator =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);

	}

//    /**
//     * 实现实时位置回调监听
//     */
//    public class MyLocationListener implements BDLocationListener {
//
//        @Override
//        public void onReceiveLocation(BDLocation location) {
//            //Receive Location
//            StringBuffer sb = new StringBuffer(256);
//            sb.append("time : ");
//            sb.append(location.getTime());
//            sb.append("\nerror code : ");
//            sb.append(location.getLocType());
//            sb.append("\nlatitude : ");
//            sb.append(location.getLatitude());
//            latitude = location.getLatitude();
//            sb.append("\nlontitude : ");
//            sb.append(location.getLongitude());
//            longitude = location.getLongitude();
//            sb.append("\nradius : ");
//            sb.append(location.getRadius());
//            if (location.getLocType() == BDLocation.TypeGpsLocation){// GPS定位结果
//                sb.append("\nspeed : ");
//                sb.append(location.getSpeed());// 单位：公里每小时
//                sb.append("\nsatellite : ");
//                sb.append(location.getSatelliteNumber());
//                sb.append("\nheight : ");
//                sb.append(location.getAltitude());// 单位：米
//                sb.append("\ndirection : ");
//                sb.append(location.getDirection());
//                sb.append("\naddr : ");
//                sb.append(location.getAddrStr());
//                sb.append("\ndescribe : ");
//                sb.append("gps定位成功");
//
//            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation){// 网络定位结果
//                sb.append("\naddr : ");
//                sb.append(location.getAddrStr());
//                mCityStr = location.getCity();
//                mProvince = location.getProvince();
//                //运营商信息
//                sb.append("\noperationers : ");
//                sb.append(location.getOperators());
//                sb.append("\ndescribe : ");
//                sb.append("网络定位成功");
//            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
//                sb.append("\ndescribe : ");
//                sb.append("离线定位成功，离线定位结果也是有效的");
//            } else if (location.getLocType() == BDLocation.TypeServerError) {
//                sb.append("\ndescribe : ");
//                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
//            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
//                sb.append("\ndescribe : ");
//                sb.append("网络不同导致定位失败，请检查网络是否通畅");
//            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
//                sb.append("\ndescribe : ");
//                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
//            }
//
//            logMsg(sb.toString());
//            Log.i("BaiduLocationApiDem", sb.toString());
//           // mLocationClient.setEnableGpsRealTimeTransfer(true);
//        }
//    }
//
//
//    /**
//     * 显示请求字符串
//     * @param str
//     */
//    public void logMsg(String str) {
//        try {
//            if (mLocationResult != null)
//                mLocationResult.setText(str);
//            Constants.lat = latitude;
//            Constants.longt = longitude;
//            Intent intent = new Intent(action);
//            intent.putExtra("data", "2");
//            intent.putExtra("province", mProvince);
//            intent.putExtra("city", mCityStr);
//            sendBroadcast(intent);
//            Log.i("定位成功", "latitude="+latitude+"=====longitude="+longitude+"====city"+mCityStr);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

}
