package com.zjxfood.route;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.AMapNaviViewListener;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.autonavi.tbt.TrafficFacilityInfo;
import com.project.util.TTSController;
import com.zjxfood.application.ExitApplication;
import com.zjxfood.common.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建时间：11/11/15 11:02
 * 项目名称：newNaviDemo
 *
 * @author lingxiang.wang
 * @email lingxiang.wang@alibaba-inc.com
 * 类说明：
 */

public class BaseActivity extends Activity implements AMapNaviListener, AMapNaviViewListener {

    AMapNaviView naviView;
    AMapNavi aMapNavi;
    TTSController ttsManager;
    NaviLatLng endLatlng = new NaviLatLng(39.925846, 116.432765);
    NaviLatLng startLatlng = new NaviLatLng(Constants.lat, Constants.longt);
    
    List<NaviLatLng> startList = new ArrayList<NaviLatLng>();
    List<NaviLatLng> endList = new ArrayList<NaviLatLng>();
    List<NaviLatLng> wayPointList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        ExitApplication.getInstance().addActivity(this);
        //为了尽最大可能避免内存泄露问题，建议这么写
        ttsManager = TTSController.getInstance(getApplicationContext());
        ttsManager.init();
        ttsManager.startSpeaking();

        //为了尽最大可能避免内存泄露问题，建议这么写
        aMapNavi = AMapNavi.getInstance(getApplicationContext());
        aMapNavi.setAMapNaviListener(this);
        aMapNavi.setAMapNaviListener(ttsManager);
        aMapNavi.setEmulatorNaviSpeed(150);

    }
    
    @Override
    protected void onResume() {
        super.onResume();
        naviView.onResume();
        startList.add(startLatlng);
        endList.add(endLatlng);
    }

    @Override
    protected void onPause() {
        super.onPause();
        naviView.onPause();

//        仅仅是停止你当前在说的这句话，一会到新的路口还是会再说的
        ttsManager.stopSpeaking();
//
//        停止导航之后，会触及底层stop，然后就不会再有回调了，但是讯飞当前还是没有说完的半句话还是会说完
//        aMapNavi.stopNavi();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        naviView.onDestroy();
        aMapNavi.destroy();
        ttsManager.destroy();
    }

    @Override
    public void onInitNaviFailure() {
    }

    @Override
    public void onInitNaviSuccess() {
        aMapNavi.calculateDriveRoute(startList, endList, wayPointList, 5);
    }

    @Override
    public void onStartNavi(int type) {

    }

    @Override
    public void onTrafficStatusUpdate() {

    }

    @Override
    public void onLocationChange(AMapNaviLocation location) {

    }

    @Override
    public void onGetNavigationText(int type, String text) {

    }

    @Override
    public void onEndEmulatorNavi() {
    }

    @Override
    public void onArriveDestination() {
    }

    @Override
    public void onCalculateRouteSuccess() {
        aMapNavi.startNavi(AMapNavi.EmulatorNaviMode);
    }

    @Override
    public void onCalculateRouteFailure(int errorInfo) {
    }

    @Override
    public void onReCalculateRouteForYaw() {

    }

    @Override
    public void onReCalculateRouteForTrafficJam() {

    }

    @Override
    public void onArrivedWayPoint(int wayID) {

    }

    @Override
    public void onGpsOpenStatus(boolean enabled) {
    }

    @Override
    public void onNaviSetting() {
    }

    @Override
    public void onNaviMapMode(int isLock) {

    }

    @Override
    public void onNaviCancel() {
        finish();
    }


    @Override
    public void onNaviTurnClick() {

    }

    @Override
    public void onNextRoadClick() {

    }


    @Override
    public void onScanViewButtonClick() {
    }

    @Deprecated
    @Override
    public void onNaviInfoUpdated(AMapNaviInfo naviInfo) {
    }

    @Override
    public void onNaviInfoUpdate(NaviInfo naviinfo) {
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
    public void showLaneInfo(AMapLaneInfo[] laneInfos, byte[] laneBackgroundInfo, byte[] laneRecommendedInfo) {

    }

    @Override
    public void hideLaneInfo() {

    }


    @Override
    public void onLockMap(boolean isLock) {
    }

    @Override
    public boolean onNaviBackClick() {
        return false;
    }


}
