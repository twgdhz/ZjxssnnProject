package com.zjxfood.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.location.service.LocationService;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.project.util.BitmapUtilSingle;
import com.project.util.DensityUtils;
import com.project.util.ScreenUtils;
import com.zjxfood.adapter.CommodityListAdapter;
import com.zjxfood.adapter.MainViewPagerAdapter;
import com.zjxfood.adapter.NewJbMallGridAdapter;
import com.zjxfood.application.ExitApplication;
import com.zjxfood.application.MyApplication;
import com.zjxfood.common.Constants;
import com.zjxfood.http.AsyUtils;
import com.zjxfood.http.XutilsUtils;
import com.zjxfood.popupwindow.NoticePopupWindow;
import com.zjxfood.popupwindow.ServiceCommitmentPopupWindow;
import com.zjxfood.toast.MyToast;
import com.zjxfood.update.UpdateVersionService;
import com.zjxfood.view.MyGridViewScroll;
import com.zjxfood.view.MyScrollListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2016/7/11.
 */
public class NewMainActivity extends AppActivity implements View.OnClickListener,Runnable{


    private ViewFlipper mViewFlipper;
    private ArrayList<HashMap<String,Object>> mGgHeadlList;
    private LinearLayout mMainLayout,mBuyLayout,mCarLayout,mMyLayout;
    private ImageView mMainImage;
    private View mMainView;
    private TextView mBottomText;
    private ViewPager mBannerViewPager;
    private ArrayList<HashMap<String, Object>> mAdvertisementTopList,mCjzxLists;
    private BitmapUtils mBitmapUtils;
    private MainViewPagerAdapter mViewPagerAdapter;
    private ScheduledExecutorService scheduledExecutorService;
    private int currentItem = 0; // 当前图片的索引号
    List<View> mViewList;
    private LinearLayout mXjLayout,mSsLayout,mJbLayout,mZcLayout;
    private MyScrollListView mMyScrollListView;
    private static String mCityStr = "0", mCurrentProvince = "0";// 当前省、城市
    public static String mCityCode = "0", mProvinceCode = "0";// 省编码、城市编码
    private ArrayList<HashMap<String, Object>> mMerList;
    public static boolean isLocation = true;
    private LocationClient mLocationClient;
    private LocationClientOption.LocationMode tempMode = LocationClientOption.LocationMode.Hight_Accuracy;
    public LocationService locationService;
    private TextView mCityText;
    private Bundle mBundles;
    public double latitude,longitude;
    public static boolean isCity = false;
    private boolean isFirst = true;
    public static boolean isGetCity = true;
    private UpdateVersionService updateVersionService;
    private ArrayList<HashMap<String, Object>> mNoticeList;
    private NoticePopupWindow mNoticePopupWindow;
    private PopupWindow mBeautySalonPopup;
    private RelativeLayout mBeautyContentLayout;
    private TextView mUserMerchantName, mUserMerchantContent,
            mUserMerchantPhone, mUserMerchantAddress;
    private ImageView mBeautyPopCanleImage;
    private Button mCheckDetailsBtn;
    private ArrayList<HashMap<String, Object>> mUserMerchantList;
    private boolean isExit = false;
    private MyToast mToast;
    private RelativeLayout mCityLayout;
    public static boolean isClick = true;
    private CommodityListAdapter mTuijianAdapter;
    private String[] mCitys;
    private TextView mMerchantMoreText;
    private LinearLayout mMsLayout,mLrLayout,mKtvLayout,mXxLayout,mQtLayout;
    private RelativeLayout mSearchLayout;
    public static String userName;
    private HashMap<String,Object> mWxMap1,mWxMap2,mLogMap;
    private ArrayList<HashMap<String, Object>> mMallLists,mSettingList;
    private String mProportion="2";
    private NewJbMallGridAdapter mGridAdapter;
//    private NewCashMallAdapter mGridAdapter;
    private MyGridViewScroll mMyGridView;
    private ImageView mCjzxImage,mTttjImage,mWzcxImage,mCzthImage,mFcdpImage;
    private TextView mGyjbText;
    private LinearLayout mCjzLayout;
    private ServiceCommitmentPopupWindow mCommitmentPopupWindow;
    private LinearLayout mActLayout;
    public static ImageView mCarImage;
    private ImageView mJhImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_main_layout);
        mToast = new MyToast(getApplicationContext());
        setImmerseLayout(findViewById(R.id.new_main_layout));
        init();
        // 判断是否有网络  // 判断是否有网络
        // 判断是否有网络
        if (!(isNetworkConnected(getApplicationContext()))
                && !(isWifiConnected(getApplicationContext()))) {
            Toast.makeText(getApplicationContext(), "网络不可用，请检查后重试",
                    Toast.LENGTH_SHORT).show();
        }
        mViewList =  new ArrayList<View>();
        mBitmapUtils = BitmapUtilSingle
                .getBitmapInstance(getApplicationContext());
        mBitmapUtils
                .configDefaultLoadFailedImage(R.drawable.main_paimai_zhanwei);
        clickFlag = "main";
        mGgHeadlList = new ArrayList<HashMap<String, Object>>();
        for(int i=0;i<10;i++){
            HashMap<String,Object> map = new HashMap<String, Object>();
            map.put("Title","注册即送70元现金"+i);
            mGgHeadlList.add(map);
        }
        //获取公告
        mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(this,
                R.animator.push_main_up_in));
        mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this,
                R.animator.push_main_up_out));

        // 获取选择的城市地址
        mBundles = getIntent().getExtras();
        Log.i("当前城市",isLocation+"===================="+mCityStr+"===="+mBundles);
        if (mBundles != null) {
            Log.i("当前城市",isLocation+"======0=============="+mCityStr+"===="+mBundles);
            if(mBundles.getString("cityId")!=null) {
                mCityText.setText(mBundles.getString("city"));
                mCityCode = mBundles.getString("cityId");
                choseCity();
            }else{
                handler.sendEmptyMessageDelayed(3, 0);
                if (mCityStr != null) {
                    mCityText.setText(mCityStr);
                }
            }
            // 获取首页推荐商家
        getRecommendMerchant();
        } else {
            Log.i("当前城市",isLocation+"=========1==========="+mCityStr);
            if (isLocation) {
                locationService = ((MyApplication) getApplication()).locationService;
                locationService.registerListener(mListener);
                //注册监听
                int type = getIntent().getIntExtra("from", 0);
                if (type == 0) {
                    locationService.setLocationOption(locationService.getDefaultLocationClientOption());
                } else if (type == 1) {
                    locationService.setLocationOption(locationService.getOption());
                }
                locationService.start();// 定位SDK
            } else {
                Log.i("当前城市","===================="+mCityStr);
                handler.sendEmptyMessageDelayed(3, 0);
                if (mCityStr != null) {
                    mCityText.setText(mCityStr);
                }
            }
            // 获取首页推荐商家
            getRecommendMerchant();
        }
        // 判断是否自动登录
        if (Constants.onLine != 1) {
            if (isAutoLogin()) {
                if(Constants.unionid==null){
                    Log.i("auto", "===============自动登录===========" + isAutoLogin());
                    autologin();
                }else{
                    Log.i("auto", "===============微信自动登录===========" + isAutoLogin());
                    wxAutologin();
                }
            }
        }

        // 获取顶端banner
        getBanner();
        //金币商城精选
        getMallList();
        //公告
        getBannerMiddel();
        // 获取说明
        getSetting();
        // 获取五张图片
        getcjzx();
        ArrayList<HashMap<String,Object>> mList = new ArrayList<>();
        SharedPreferences sp = getApplication().getSharedPreferences("cars", Context.MODE_PRIVATE);
        Constants.mCarsMap = Constants.getJsonObjectToMap(sp.getString("cars",null));
        for (Map.Entry<String, HashMap<String,Object>> entry : Constants.mCarsMap.entrySet()) {
            Log.i("key", "Key = " + entry.getKey() + ", Value = " + entry.getValue());
            mList.add(entry.getValue());
        }
        if(mList!=null && mList.size()>0) {
            Bitmap bitmap2 = ((BitmapDrawable) mCarImage.getDrawable()).getBitmap();
            mCarImage.setImageBitmap(Constants.createCarsBitmap(bitmap2,mList.size(), getApplicationContext()));
        }
    }

    private void init(){
        mJhImage = (ImageView) findViewById(R.id.jh_image);
        mCarImage = (ImageView) findViewById(R.id.new_car_menu_image);
        mViewFlipper = (ViewFlipper) findViewById(R.id.main_gonggao_flipper);
        mMainLayout = (LinearLayout) findViewById(R.id.new_main_home_menu_layout);
        mMainImage = (ImageView) findViewById(R.id.new_main_home_menu_image);
        mMainView = findViewById(R.id.new_main_main_view);
        mMainLayout.setBackgroundColor(getResources().getColor(R.color.main_color13));
        mMainImage.setImageDrawable(getResources().getDrawable(R.drawable.new_main_home_hover));
//        mMainView.setVisibility(View.VISIBLE);
        mBuyLayout = (LinearLayout) findViewById(R.id.new_main_buy_menu_layout);
        mCarLayout = (LinearLayout) findViewById(R.id.new_my_buy_car);
        mMyLayout = (LinearLayout) findViewById(R.id.new_main_my_menu_layout);
        mBottomText = (TextView) findViewById(R.id.new_main_home_menu_text);
        mBottomText.setTextColor(getResources().getColor(R.color.white));
        mBannerViewPager = (ViewPager) findViewById(R.id.new_main_title_viewpager);
        ViewGroup.LayoutParams layoutParams = mBannerViewPager
                .getLayoutParams();
        layoutParams.height = (int) (ScreenUtils.getScreenWidth(getApplicationContext())*0.51);
        mBannerViewPager.setLayoutParams(layoutParams);
        mXjLayout = (LinearLayout) findViewById(R.id.main_xj_layout);
        mSsLayout = (LinearLayout) findViewById(R.id.main_ss_layout);
        mJbLayout = (LinearLayout) findViewById(R.id.main_jb_layout);
        mZcLayout = (LinearLayout) findViewById(R.id.main_zc_layout);
        mMyScrollListView = (MyScrollListView) findViewById(R.id.new_main_tjsj_list);
        mCityText = (TextView) findViewById(R.id.main_city_text);

        mMerchantMoreText = (TextView) findViewById(R.id.new_main_more_merchant);
        mMsLayout = (LinearLayout) findViewById(R.id.new_main_ms_layout);
        mLrLayout = (LinearLayout) findViewById(R.id.new_main_lr_layout);
        mKtvLayout = (LinearLayout) findViewById(R.id.new_main_ktv_layout);
        mXxLayout = (LinearLayout) findViewById(R.id.new_main_xx_layout);
        mQtLayout = (LinearLayout) findViewById(R.id.new_main_qt_layout);
        mCityLayout = (RelativeLayout) findViewById(R.id.new_main_city_layout);
        mSearchLayout = (RelativeLayout) findViewById(R.id.new_main_search_layout);
        ViewGroup.LayoutParams params = mSearchLayout.getLayoutParams();
        params.width = (int) (ScreenUtils.getScreenWidth(getApplicationContext())*0.72);
        params.height = (int) (ScreenUtils.getScreenWidth(getApplicationContext())*0.085);
        mSearchLayout.setLayoutParams(params);
        params = mCityLayout.getLayoutParams();
        params.width = (int) (ScreenUtils.getScreenWidth(getApplicationContext())*0.21);
        params.height = (int) (ScreenUtils.getScreenWidth(getApplicationContext())*0.085);
        mCityLayout.setLayoutParams(params);
        mCjzLayout = (LinearLayout) findViewById(R.id.new_main_cjzx_layout);
        params = mCjzLayout.getLayoutParams();
        params.height = (int) (ScreenUtils.getScreenWidth(getApplicationContext())*0.45);
        mCjzLayout.setLayoutParams(params);
        mMyGridView = (MyGridViewScroll) findViewById(R.id.new_mall_index_grid_view);
        mCjzxImage = (ImageView) findViewById(R.id.cjzx_image);
        mActLayout = (LinearLayout) findViewById(R.id.new_main_act);

        mActLayout.setOnClickListener(this);
        mTttjImage = (ImageView) findViewById(R.id.tttj_image);
        mWzcxImage = (ImageView) findViewById(R.id.wzcx_image);
        mCzthImage = (ImageView) findViewById(R.id.czth_image);
        mFcdpImage = (ImageView) findViewById(R.id.fcdp_image);
        mGyjbText = (TextView) findViewById(R.id.new_main_guanyujb);

        mCjzxImage.setOnClickListener(this);
        mTttjImage.setOnClickListener(this);
        mWzcxImage.setOnClickListener(this);
        mCzthImage.setOnClickListener(this);
        mFcdpImage.setOnClickListener(this);

        mMainLayout.setOnClickListener(click);
        mBuyLayout.setOnClickListener(click);
        mCarLayout.setOnClickListener(click);
        mMyLayout.setOnClickListener(click);
        mXjLayout.setOnClickListener(this);
        mSsLayout.setOnClickListener(this);
        mJbLayout.setOnClickListener(this);
        mZcLayout.setOnClickListener(this);
        mCityLayout.setOnClickListener(this);
        mMerchantMoreText.setOnClickListener(this);

        mMsLayout.setOnClickListener(this);
        mLrLayout.setOnClickListener(this);
        mKtvLayout.setOnClickListener(this);
        mXxLayout.setOnClickListener(this);
        mQtLayout.setOnClickListener(this);
        mSearchLayout.setOnClickListener(this);
        mGyjbText.setOnClickListener(this);
    }

    // 获取首页顶端图片
    private void getBanner() {
        StringBuilder sb = new StringBuilder();
        sb.append("positionCode=0&top=5");
        XutilsUtils.get(Constants.getAdListByPosition, sb,
                new RequestCallBack<String>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        Log.i("onFailure", arg1 + "======获取首页顶端图片======");
                    }
                    @Override
                    public void onSuccess(ResponseInfo<String> res) {
                        try {
                            Log.i("mAdvertisementTopList",
                                    res.result + "======获取首页顶端图片======");
                            mAdvertisementTopList = Constants
                                    .getJsonArrayByData(res.result);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (mAdvertisementTopList != null
                                && mAdvertisementTopList.size() > 0) {
                            handler.sendEmptyMessageDelayed(1, 0);
                        }
                    }
                });
    }
    // 获取五个图片
    private void getcjzx() {
        StringBuilder sb = new StringBuilder();
        sb.append("positionCode=13&top=5");
        XutilsUtils.get(Constants.getAdListByPosition, sb,
                new RequestCallBack<String>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                    }
                    @Override
                    public void onSuccess(ResponseInfo<String> res) {
                        try {
                            Log.i("获取首页五张图片",
                                    res.result + "======获取首页五张图片======");
                            mCjzxLists = Constants
                                    .getJsonArrayByData(res.result);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (mCjzxLists != null
                                && mCjzxLists.size() > 0) {
                            handler.sendEmptyMessageDelayed(12, 0);
                        }
                    }
                });
    }
    // 获取首页推荐商家
    private void getRecommendMerchant() {
        Log.i("getRecommendMerchant", "========获取首页推荐商家===========");
        StringBuilder sb = new StringBuilder();
        if (mCityCode != null && !mCityCode.equals("")) {
            sb.append("page=1" + "&pagesize=8" + "&orderby=4" + "&name="
                    + "&groupid=0" + "&cityid=" + mCityCode + "&areaid=0"
                    + "&x=" + Constants.longt + "&y=" + Constants.lat
                    + "&istj=true");

        } else {
            sb.append("page=1" + "&pagesize=15" + "&orderby=4" + "&name="
                    + "&groupid=0" + "&cityid=0" + "&areaid=0" + "&x="
                    + Constants.longt + "&y=" + Constants.lat + "&istj=true");
        }
        XutilsUtils.get(Constants.getMerchant2, sb,
                new RequestCallBack<String>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                    }
                    @Override
                    public void onSuccess(ResponseInfo<String> res) {
                        Log.i("getRecommendMerchant", "========获取首页推荐商家==========="+res.result);
                        mMerList = Constants.getJsonArray(res.result);
                        if (mMerList != null && mMerList.size() > 0) {
                            handler.sendEmptyMessageDelayed(6, 0);
                        }
                    }
                });
    }
    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        Bundle bundle;
        switch (view.getId()){
            //活动中心
            case R.id.new_main_act:
                intent.setClass(getApplicationContext(), MainListActivity.class);
                startActivity(intent);
                break;
            //厂家直销
            case R.id.cjzx_image:
//                intent.setClass(getApplicationContext(), MallActivity.class);
//                bundle = new Bundle();
//                    bundle.putString("code", "CJ");
//                bundle.putString("group", "3");
//                bundle.putString("titleName", "厂家直销");
//                bundle.putString("shouCode","3");
//                bundle.putString("proportion","");
//                bundle.putString("type", "xj");
//                bundle.putString("typeName", "xj");
//                intent.putExtras(bundle);
//                startActivity(intent);
                if(mCjzxLists!=null && mCjzxLists.size()>0 && mCjzxLists.get(0).get("AndroidForm")!=null) {
                    if(mCjzxLists.get(0).get("AndroidForm").toString().equals("h5")){
                        intent.setClass(getApplicationContext(), WebActivity.class);
                        bundle = new Bundle();
                        if(mCjzxLists.get(0).get("Title")!=null) {
                            bundle.putString("title", mCjzxLists.get(0).get("Title").toString());
                        }
                        if(mCjzxLists.get(0).get("LinkUrl")!=null) {
                            bundle.putString("url", mCjzxLists.get(0).get("LinkUrl").toString());
                        }
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }else {
                        if (mCjzxLists.get(0).get("AndroidForm").toString().equals("MallActivity")) {
                            try {
                                intent.setClass(getApplicationContext(), Class.forName("com.zjxfood.activity." + mCjzxLists.get(0).get("AndroidForm")));
                                bundle = new Bundle();
                                bundle.putString("code", "CJ");
                                bundle.putString("group", "3");
                                bundle.putString("titleName", "厂家直销");
                                bundle.putString("shouCode", "3");
                                bundle.putString("proportion", "");
                                bundle.putString("type", "xj");
                                bundle.putString("typeName", "xj");
                                intent.putExtras(bundle);
                                startActivity(intent);
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), "请下载最新版本", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
                break;
            //天天特价
            case R.id.tttj_image:
                if(mCjzxLists!=null  && mCjzxLists.size()>0 && mCjzxLists.get(1).get("AndroidForm")!=null) {
                    if(mCjzxLists.get(1).get("AndroidForm").toString().equals("h5")){
                        intent.setClass(getApplicationContext(), WebActivity.class);
                        bundle = new Bundle();
                        if(mCjzxLists.get(1).get("Title")!=null) {
                            bundle.putString("title", mCjzxLists.get(1).get("Title").toString());
                        }
                        if(mCjzxLists.get(1).get("LinkUrl")!=null) {
                            bundle.putString("url", mCjzxLists.get(1).get("LinkUrl").toString());
                        }
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }else {
                        if (mCjzxLists.get(1).get("AndroidForm").toString().equals("MallActivity")) {
                            try {
                                intent.setClass(getApplicationContext(), Class.forName("com.zjxfood.activity." + mCjzxLists.get(1).get("AndroidForm")));
                                bundle = new Bundle();
                                bundle.putString("type", "xj_tt");
                                bundle.putString("typeName", "xj");
                                bundle.putString("shouCode","3");
                                bundle.putString("group", "3");
                                bundle.putString("proportion","");
                                bundle.putString("titleName", "天天特价");
                                intent.putExtras(bundle);
                                startActivity(intent);
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), "请下载最新版本", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
                break;
            //超值特惠
            case R.id.czth_image:
                if(mCjzxLists!=null  && mCjzxLists.size()>0 && mCjzxLists.get(2).get("AndroidForm")!=null) {
                    if(mCjzxLists.get(2).get("AndroidForm").toString().equals("h5")){
                        intent.setClass(getApplicationContext(), WebActivity.class);
                        bundle = new Bundle();
                        if(mCjzxLists.get(2).get("Title")!=null) {
                            bundle.putString("title", mCjzxLists.get(2).get("Title").toString());
                        }
                        if(mCjzxLists.get(2).get("LinkUrl")!=null) {
                            bundle.putString("url", mCjzxLists.get(2).get("LinkUrl").toString());
                        }
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }else {
                        if (mCjzxLists.get(2).get("AndroidForm").toString().equals("MallActivity")) {
                            try {
                                intent.setClass(getApplicationContext(), Class.forName("com.zjxfood.activity." + mCjzxLists.get(2).get("AndroidForm")));
                                bundle = new Bundle();
                                bundle.putString("type", "xj_czth");
                                bundle.putString("typeName", "xj");
                                bundle.putString("shouCode","3");
                                bundle.putString("group", "3");
                                bundle.putString("proportion","");
                                bundle.putString("titleName", "超值特惠");
                                intent.putExtras(bundle);
                                startActivity(intent);
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), "请下载最新版本", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
                break;
            //五折畅想
            case R.id.wzcx_image:
                if(mCjzxLists!=null  && mCjzxLists.size()>0 && mCjzxLists.get(3).get("AndroidForm")!=null) {
                    if(mCjzxLists.get(3).get("AndroidForm").toString().equals("h5")){
                        intent.setClass(getApplicationContext(), WebActivity.class);
                        bundle = new Bundle();
                        if(mCjzxLists.get(3).get("Title")!=null) {
                            bundle.putString("title", mCjzxLists.get(3).get("Title").toString());
                        }
                        if(mCjzxLists.get(3).get("LinkUrl")!=null) {
                            bundle.putString("url", mCjzxLists.get(3).get("LinkUrl").toString());
                        }
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }else {
                        if (mCjzxLists.get(3).get("AndroidForm").toString().equals("MallActivity")) {
                            try {
                                intent.setClass(getApplicationContext(), Class.forName("com.zjxfood.activity." + mCjzxLists.get(3).get("AndroidForm")));
                                bundle = new Bundle();
                                bundle.putString("type", "xj_wzcx");
                                bundle.putString("typeName", "xj");
                                bundle.putString("shouCode","3");
                                bundle.putString("group", "3");
                                bundle.putString("proportion","");
                                bundle.putString("titleName", "五折畅想");
                                intent.putExtras(bundle);
                                startActivity(intent);
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), "请下载最新版本", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
                break;

            //非常大牌
            case R.id.fcdp_image:
                if(mCjzxLists!=null  && mCjzxLists.size()>0 && mCjzxLists.get(4).get("AndroidForm")!=null) {
                    if(mCjzxLists.get(4).get("AndroidForm").toString().equals("h5")){
                        intent.setClass(getApplicationContext(), WebActivity.class);
                        bundle = new Bundle();
                        if(mCjzxLists.get(4).get("Title")!=null) {
                            bundle.putString("title", mCjzxLists.get(4).get("Title").toString());
                        }
                        if(mCjzxLists.get(4).get("LinkUrl")!=null) {
                            bundle.putString("url", mCjzxLists.get(4).get("LinkUrl").toString());
                        }
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }else {
                        if (mCjzxLists.get(4).get("AndroidForm").toString().equals("MallActivity")) {
                            try {
                                intent.setClass(getApplicationContext(), Class.forName("com.zjxfood.activity." + mCjzxLists.get(4).get("AndroidForm")));
                                bundle = new Bundle();
                                bundle.putString("type", "xj_fcdp");
                                bundle.putString("typeName", "xj");
                                bundle.putString("titleName", "非常大牌");
                                bundle.putString("code", "DP");
                                bundle.putString("group", "3");
                                bundle.putString("shouCode","3");
                                intent.putExtras(bundle);
                                startActivity(intent);
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), "请下载最新版本", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
                break;
            //关于金币
            case R.id.new_main_guanyujb:
                intent.setClass(getApplicationContext(), WebActivity.class);
                bundle = new Bundle();
                bundle.putString("title","关于金币");
                bundle.putString("url", Constants.guanyuJb);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            //搜索
            case R.id.new_main_search_layout:
                intent.setClass(getApplicationContext(), SearchActivity.class);
                startActivity(intent);
                break;
            //美食
            case R.id.new_main_ms_layout:
                intent.setClass(NewMainActivity.this, CommodityListActivity.class);
                bundle = new Bundle();
                bundle.putString("type", "中餐");
                bundle.putString("id", "1");
                bundle.putString("cityId", NewMainActivity.mCityCode);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            //丽人
            case R.id.new_main_lr_layout:
                intent.setClass(NewMainActivity.this, CommodityListActivity.class);
                bundle = new Bundle();
                bundle.putString("type", "丽人");
                bundle.putString("id", "10");
                bundle.putString("cityId", NewMainActivity.mCityCode);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            //ktv
            case R.id.new_main_ktv_layout:
                intent.setClass(NewMainActivity.this, CommodityListActivity.class);
                bundle = new Bundle();
                bundle.putString("type", "Ktv");
                bundle.putString("id", "9");
                bundle.putString("cityId", NewMainActivity.mCityCode);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            //休闲
            case R.id.new_main_xx_layout:
                if (NewMainActivity.isClick) {
                    intent.setClass(NewMainActivity.this, CommodityListActivity.class);
                    bundle = new Bundle();
                    bundle.putString("type", "浴足");
                    bundle.putString("id", "14");
                    bundle.putString("cityId", NewMainActivity.mCityCode);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                break;
            //其他
            case R.id.new_main_qt_layout:
                intent.setClass(NewMainActivity.this, CommodityListActivity.class);
                bundle = new Bundle();
                bundle.putString("type", "其他");
                bundle.putString("id", "16");
                bundle.putString("cityId", NewMainActivity.mCityCode);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            //现金商城
            case R.id.main_xj_layout:
                intent.setClass(getApplicationContext(), NewCashMallActivity.class);
                startActivity(intent);
                break;
            //食尚币商城
            case R.id.main_ss_layout:
                intent.setClass(getApplicationContext(), MallIndexActivity.class);
                startActivity(intent);

                break;
            //金币商城
            case R.id.main_jb_layout:
                intent.setClass(getApplicationContext(), NewJbMallActivity.class);
                startActivity(intent);
//                intent.setClass(getApplicationContext(), MallProActivity.class);
//                bundle = new Bundle();
//                bundle.putString("proportion","2");
//                bundle.putString("typeCode","2");
//                intent.putExtras(bundle);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
                break;
            //众筹商城
            case R.id.main_zc_layout:
                mCommitmentPopupWindow = new ServiceCommitmentPopupWindow(
                        NewMainActivity.this,"暂未开放，敬请期待");
                mCommitmentPopupWindow.showAtLocation(mBuyLayout, Gravity.CENTER,
                        0, 0);
                break;
            //选择城市
            case R.id.new_main_city_layout:
//                if (NewMainActivity.isClick) {
                    if (!(mCityText.getText().toString().equals(""))
                            && !(mCityText.getText().toString().equals("刷新"))) {
                        intent.setClass(getApplicationContext(),
                                ChoseCityActivity.class);
                        startActivity(intent);
                    } else if (mCityText.getText().toString().equals("刷新")) {
                        Log.i("isLocation", "正在刷新=======================");
                        handler.postDelayed(this, 8000);// 设置超过8秒还没有定位到就停止定位
                    }
                break;
            //推荐商家更多
            case R.id.new_main_more_merchant:
                intent.setClass(getApplicationContext(),
                        CommodityListActivity.class);
                bundle = new Bundle();
                bundle.putString("type", "中餐");
                bundle.putString("id", "1");
                bundle.putString("istj", "true");
                bundle.putString("cityId", mCityCode);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void run() {
        Log.i("isLocation", "========请求超时============" + isLocation);
        Toast.makeText(getApplicationContext(),"当前网络较慢",Toast.LENGTH_SHORT).show();
    }

    private class ScrollTask implements Runnable {
        public void run() {
            synchronized (mBannerViewPager) {
                currentItem = (currentItem + 1) % mViewList.size();
                handler.sendEmptyMessageDelayed(2, 0); // 通过Handler切换图片
            }
        }
    }


    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    isExit = false;
                    break;
                case 1:
                    LayoutInflater inflater = LayoutInflater
                            .from(getApplicationContext());
                    View view;
                    ImageView mViewImage;

                    for (int i = 0; i < mAdvertisementTopList.size(); i++) {
                        view = inflater.inflate(
                                R.layout.mall_detail_viewpager_item, null);
                        mViewImage = (ImageView) view
                                .findViewById(R.id.mall_detail_viewpager_image);

                        if (mAdvertisementTopList.get(i).get("Images") != null) {
                            mBitmapUtils.display(mViewImage, mAdvertisementTopList
                                    .get(i).get("Images").toString());
                        }
                        mViewList.add(view);
                    }
                    mViewPagerAdapter = new MainViewPagerAdapter(
                            getApplicationContext(), mViewList,
                            mAdvertisementTopList);
                    mBannerViewPager.setAdapter(mViewPagerAdapter);
                    break;
                case 2:
                    mBannerViewPager.setCurrentItem(currentItem);// 切换当前显示的图片
                    break;
                case 3:
                    isCity = true;
                    break;
                case 4:
                    Log.i("mBeautySalonPopup", "===========美容院提示框2=============="+mNoticeList);
                    try {
                        if (mNoticePopupWindow == null) {
                            if (mNoticeList != null && mNoticeList.size() > 0) {
                                Log.i("mBeautySalonPopup", "===========美容院提示框3=============="+mNoticeList);
                                mNoticePopupWindow = new NoticePopupWindow(
                                        NewMainActivity.this, mNoticeList);
                                mNoticePopupWindow.showAtLocation(mMainLayout,
                                        Gravity.CENTER, 0, 0);
                            }
                        }
                    } catch (Exception e) {
                    }
                    getBeautyHttp();
                    break;
                case 5:
                    Log.i("mBeautySalonPopup", "============美容院提示框4============");
                    if (mBeautySalonPopup == null) {
                        try {
                            inflater = LayoutInflater
                                    .from(getApplicationContext());
                            view = inflater.inflate(
                                    R.layout.popup_main_upmerchant_layout, null);
                            mBeautyContentLayout = (RelativeLayout) view
                                    .findViewById(R.id.popup_main_beautysalon_layout);
                            ViewGroup.LayoutParams params = mBeautyContentLayout
                                    .getLayoutParams();
                            params.height = DensityUtils.dp2px(
                                    getApplicationContext(), 300);
                            mBeautyContentLayout.setLayoutParams(params);
                            mUserMerchantName = (TextView) view
                                    .findViewById(R.id.popup_sign_detail_content_text);
                            mUserMerchantContent = (TextView) view
                                    .findViewById(R.id.beauty_salon_content_text);
                            mUserMerchantPhone = (TextView) view
                                    .findViewById(R.id.beauty_salon_phone_text);
                            mUserMerchantAddress = (TextView) view
                                    .findViewById(R.id.beauty_salon_address_text);

                            mBeautyPopCanleImage = (ImageView) view
                                    .findViewById(R.id.popup_main_beauty_salon_xx);
                            mBeautyPopCanleImage
                                    .setOnClickListener(mBeautyCanleClick);
                            mCheckDetailsBtn = (Button) view
                                    .findViewById(R.id.beauty_salon_check_btn);
                            mBeautySalonPopup = new PopupWindow(view,
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.MATCH_PARENT, false);
                            mBeautySalonPopup
                                    .setBackgroundDrawable(new BitmapDrawable());
                            mBeautySalonPopup.setOutsideTouchable(true);
                            mBeautySalonPopup.setFocusable(true);
                            mBeautySalonPopup.showAtLocation(mCityText,
                                    Gravity.CENTER, 0, 0);

                            if (mUserMerchantList.get(0).get("merchantname") != null) {
                                mUserMerchantName.setText(mUserMerchantList.get(0)
                                        .get("merchantname").toString());
                            }
                            if (mUserMerchantList.get(0).get("introduction") != null) {
                                mUserMerchantContent.setText(mUserMerchantList
                                        .get(0).get("introduction").toString());
                            }
                            if (mUserMerchantList.get(0).get("phone") != null) {
                                mUserMerchantPhone.setText("电话："
                                        + mUserMerchantList.get(0).get("phone")
                                        .toString());
                            }
                            if (mUserMerchantList.get(0).get("address") != null) {
                                mUserMerchantAddress.setText("地址："
                                        + mUserMerchantList.get(0).get("address")
                                        .toString());
                            }
                            mCheckDetailsBtn.setOnClickListener(mBeautyDetailClick);
                            view.setOnTouchListener(new View.OnTouchListener() {
                                public boolean onTouch(View v, MotionEvent event) {
                                    int height = mBeautyContentLayout.getTop();
                                    int bottom = mBeautyContentLayout.getBottom();
                                    int y = (int) event.getY();
                                    if (event.getAction() == MotionEvent.ACTION_UP) {
                                        if (y < height || y > bottom) {
                                            mBeautySalonPopup.dismiss();
                                        }
                                    }
                                    return true;
                                }
                            });
                        } catch (Exception e) {
                        }
                    }
                    break;
                case 6:
                    if (mMerList != null && mMerList.size() > 0) {
                        mTuijianAdapter = new CommodityListAdapter(
                                getApplicationContext(), mMerList);
                        // mMerchantListAdapter = new MainMerchantListAdapter(
                        // getApplicationContext(), mMerList);
                        mMyScrollListView.setAdapter(mTuijianAdapter);
                        mMyScrollListView
                                .setOnItemClickListener(mMerchantListItemClick);
                    }
                    break;
                case 7:
                    if (Constants.mProvinceList != null
                            && Constants.mProvinceList.size() > 0) {
                        for (int i = 0; i < Constants.mProvinceList.size(); i++) {
                            if (Constants.mProvinceList.get(i).get("Provincename")
                                    .equals(mCurrentProvince)) {
                                mProvinceCode = Constants.mProvinceList.get(i)
                                        .get("Provinceid").toString();
                                getCity();
                            }
                        }
                    }
                    break;
                case 8:
                    if (Constants.mCityLists != null
                            && Constants.mCityLists.size() > 0) {
                        mCitys = new String[Constants.mCityLists.size()];
                        for (int i = 0; i < Constants.mCityLists.size(); i++) {
                            mCitys[i] = Constants.mCityLists.get(i).get("Cityname")
                                    .toString();
                        }
                        Log.i("citys", mCitys[0] + mCitys[1]
                                + "=======mCitys[0]==========");
                        mCityText.setText(mCityStr);
                        mCityText.setVisibility(View.VISIBLE);
                        for (int i = 0; i < Constants.mCityLists.size(); i++) {
                            if (Constants.mCityLists.get(i).get("Cityname")
                                    .equals(mCityStr)) {
                                mCityCode = Constants.mCityLists.get(i)
                                        .get("Cityid").toString();
                                getArea();
                            }
                        }
                        getRecommendMerchant();
                    }
                    break;
                case 9:
                    Log.i("自动登录信息", mLogMap+"==========自动登录=========");
                    if (mLogMap != null && mLogMap.size() > 0) {
                        try {
                            if (mLogMap.get("Id").equals("0")) {
                                Log.i("auto", "==========自动登录失败=========");
                                Constants.onLine = 0;
                            } else {
                                Constants.onLine = 1;
                                Constants.mPayPassword = mLogMap
                                        .get("Paypassword").toString();
                                Constants.mShMoney = mLogMap
                                        .get("Shmoney").toString();
                                Constants.mIsjh = mLogMap.get("Isjh")
                                        .toString();
                                Constants.UserLevelMemo = mLogMap.get("UserLevelMemo")
                                        .toString();
                                Constants.headPath = mLogMap
                                        .get("Avatar").toString();
                                Constants.mRealname = URLDecoder.decode(
                                        mLogMap.get("Realname")
                                                .toString(), "UTF-8");
                                Constants.UserLevelMemo = mLogMap.get("UserLevelMemo")
                                        .toString();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.i("auto", "==========自动登录失败=========");
                        Constants.onLine = 0;
                    }
                    break;
                case 10:
                    mGridAdapter = new NewJbMallGridAdapter(getApplicationContext(),
                            mMallLists,"ssjb");
                    mMyGridView.setAdapter(mGridAdapter);
                    break;
                case 11:
                    mViewFlipper.removeAllViews();
                    inflater = LayoutInflater.from(getApplicationContext());
                    if (mGgHeadlList.size() > 1) {
                    for (int i = 0; i < mGgHeadlList.size(); i++) {
                        View flipper = inflater.inflate(R.layout.new_main_flipper_layout,null);
                        TextView textView = (TextView) flipper.findViewById(R.id.main_flipper_text1);
                        RelativeLayout mLayout2 = (RelativeLayout) flipper.findViewById(R.id.new_main_flipper_layout2);
                        TextView textView2 = (TextView) flipper.findViewById(R.id.main_flipper_text2);
                        if (mGgHeadlList.get(i).get("Title") != null) {
                            textView.setText(mGgHeadlList.get(i).get("Title")
                                    .toString());
                        }
                        if(!(i+1>=mGgHeadlList.size())) {
                            if (mGgHeadlList.get(i + 1).get("Title") != null) {
                                textView2.setText(mGgHeadlList.get(i + 1).get("Title")
                                        .toString());
                            }
                        }else{
                            mLayout2.setVisibility(View.GONE);
                        }
                        mViewFlipper.addView(flipper);
                    }
                        mViewFlipper.startFlipping();
                    }else{
                            View flipper = inflater.inflate(R.layout.new_main_flipper_layout,null);
                        RelativeLayout layout2 = (RelativeLayout) flipper.findViewById(R.id.new_main_flipper_layout2);
                            TextView textView = (TextView) flipper.findViewById(R.id.main_flipper_text1);
                            if (mGgHeadlList.get(0).get("Title") != null) {
                                textView.setText(mGgHeadlList.get(0).get("Title")
                                        .toString());
                            }
                        layout2.setVisibility(View.GONE);
                            mViewFlipper.addView(flipper);

                        mViewFlipper.startFlipping();
                    }
                    break;
                case 12:
                    if(mCjzxLists!=null && mCjzxLists.size()>0){
                        if(mCjzxLists.get(0).get("Images")!=null){
                            mBitmapUtils.display(mCjzxImage, mCjzxLists.get(0).get("Images").toString());
                        }
                        if(mCjzxLists.get(1).get("Images")!=null){
                            mBitmapUtils.display(mTttjImage, mCjzxLists.get(1).get("Images").toString());
                        }

                        if(mCjzxLists.get(2).get("Images")!=null){
                            mBitmapUtils.display(mCzthImage, mCjzxLists.get(2).get("Images").toString());
                        }
                        if(mCjzxLists.get(3).get("Images")!=null){
                            mBitmapUtils.display(mWzcxImage, mCjzxLists.get(3).get("Images").toString());
                        }
                        if(mCjzxLists.get(4).get("Images")!=null){
                            mBitmapUtils.display(mFcdpImage, mCjzxLists.get(4).get("Images").toString());
                        }
                    }
                    break;

            }
        }
    };
    // 获取上级美容院
    private void getBeautyHttp() {
        RequestParams params = new RequestParams();
        params.put("uid", Constants.mId);
        if (Constants.mId == null || Constants.mId.equals(""))
            return;
        AsyUtils.get(Constants.getUserMerchant2, params,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONArray response) {
                        Log.i("美容院信息",response.toString()+"=================");
                        mUserMerchantList = Constants.getJsonArray(response
                                .toString());
                        if (mUserMerchantList != null
                                && mUserMerchantList.size() > 0
                                && mUserMerchantList.get(0).get("address") != null) {
                            handler.sendEmptyMessageDelayed(5, 0);
                        }
                        super.onSuccess(response);
                    }
                });
    }
    @Override
    protected void onStart() {
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        // 当Activity显示出来后，每两秒钟切换一次图片显示
        scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 1, 5,
                TimeUnit.SECONDS);
        // flushAuction();
        super.onStart();
    }
    @Override
    protected void onStop() {
        // 当Activity不可见的时候停止切换
        if(scheduledExecutorService!=null) {
            scheduledExecutorService.shutdown();
        }
        if(locationService!=null){
            locationService.unregisterListener(mListener); //注销掉监听
            locationService.stop();
        }
        super.onStop();
    }
    /**
     * 判断网络是否打开
     *
     * @param context
     * @return
     */
    public boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager
                    .getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }
    /**
     * 判断wifi是否可用
     *
     * @param context
     * @return
     */
    public boolean isWifiConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWiFiNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWiFiNetworkInfo != null) {
                return mWiFiNetworkInfo.isAvailable();
            }
        }
        return false;
    }
    // 四个直辖市单独加载
    private void choseCity() {
        if (mCityText.getText().toString().equals("北京市")) {
            Constants.mAreaLists = new ArrayList<HashMap<String, Object>>();
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("Areaname", "直辖市");
            Constants.mAreaLists.add(map);
        } else if (mCityText.getText().toString().equals("上海市")) {
            Constants.mAreaLists = new ArrayList<HashMap<String, Object>>();
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("Areaname", "直辖市");
            Constants.mAreaLists.add(map);
        } else if (mCityText.getText().toString().equals("重庆市")) {
            Constants.mAreaLists = new ArrayList<HashMap<String, Object>>();
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("Areaname", "直辖市");
            Constants.mAreaLists.add(map);
        } else if (mCityText.getText().toString().equals("天津市")) {
            Constants.mAreaLists = new ArrayList<HashMap<String, Object>>();
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("Areaname", "直辖市");
            Constants.mAreaLists.add(map);
        } else {
            if (Constants.mAllCityList != null
                    && Constants.mAllCityList.size() > 0) {
                for (int i = 0; i < Constants.mAllCityList.size(); i++) {
                    if (Constants.mAllCityList.get(i).get("Cityname")
                            .equals(mCityText.getText().toString())) {
                        mCityCode = Constants.mAllCityList.get(i).get("Cityid")
                                .toString();
                        getArea();
                    }
                }
            }
        }
    }
    // 获取省
    private void getProvince() {
        RequestParams params = new RequestParams();
        AsyUtils.get(Constants.getProvince2, params,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONArray response) {
                        try {
                            Constants.mProvinceList = Constants.getJsonArray(response
                                    .toString());
                            Log.i("获取省",response.toString()+"=============="+ Constants.mProvinceList);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        handler.sendEmptyMessageDelayed(7, 0);
                        super.onSuccess(response);
                    }
                });
    }
    // 获取市
    private void getCity() {
        RequestParams params = new RequestParams();
        params.put("provinceid", mProvinceCode);
        AsyUtils.get(Constants.getCity2, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONArray response) {

                try {
//                    Constants.mCityLists = getCityList(response.toString());
                    Constants.mCityLists = Constants.getJsonArray(response.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessageDelayed(8, 0);
                super.onSuccess(response);
            }
        });
    }
    // 获取区
    private void getArea() {
        RequestParams params = new RequestParams();
        params.put("cityid", mCityCode);
        AsyUtils.get(Constants.getArea2, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONArray response) {
                try {
                    Log.i("Constants.mAreaLists", Constants.mAreaLists
                            + "=======Constants.mAreaLists=========");
//                    Constants.mAreaLists = getAreaList(response.toString());
                    Constants.mAreaLists = Constants.getJsonArray(response.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessageDelayed(3, 0);
                super.onSuccess(response);
            }
        });
    }
    private BDLocationListener mListener = new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation location) {
            // TODO Auto-generated method stub
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                StringBuffer sb = new StringBuffer(256);
                sb.append("time : ");
                /**
                 * 时间也可以使用systemClock.elapsedRealtime()方法 获取的是自从开机以来，每次回调的时间；
                 * location.getTime() 是指服务端出本次结果的时间，如果位置不发生变化，则时间不变
                 */
                sb.append(location.getTime());
                sb.append("\nerror code : ");
                sb.append(location.getLocType());
                sb.append("\nlatitude : ");
                sb.append(location.getLatitude());
                sb.append("\nlontitude : ");
                sb.append(location.getLongitude());
                sb.append("\nradius : ");
                sb.append(location.getRadius());
                sb.append("\nCountryCode : ");
                sb.append(location.getCountryCode());
                sb.append("\nCountry : ");
                sb.append(location.getCountry());
                sb.append("\ncitycode : ");
                sb.append(location.getCityCode());
                sb.append("\ncity : ");
                sb.append(location.getCity());
                sb.append("\nDistrict : ");
                sb.append(location.getDistrict());
                sb.append("\nStreet : ");
                sb.append(location.getStreet());
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append("\nDescribe: ");
                sb.append(location.getLocationDescribe());
                sb.append("\nDirection(not all devices have value): ");
                sb.append(location.getDirection());
                sb.append("\nPoi: ");
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                if (location.getPoiList() != null && !location.getPoiList().isEmpty()) {
                    for (int i = 0; i < location.getPoiList().size(); i++) {
                        Poi poi = (Poi) location.getPoiList().get(i);
                        sb.append(poi.getName() + ";");
                    }
                }
                if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                    sb.append("\nspeed : ");
                    sb.append(location.getSpeed());// 单位：km/h
                    sb.append("\nsatellite : ");
                    sb.append(location.getSatelliteNumber());
                    sb.append("\nheight : ");
                    sb.append(location.getAltitude());// 单位：米
                    sb.append("\ndescribe : ");
                    sb.append("gps定位成功");
                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                    // 运营商信息
                    sb.append("\noperationers : ");
                    sb.append(location.getOperators());
                    sb.append("\ndescribe : ");
                    sb.append("网络定位成功");
                    mCityStr = location.getCity();
                    mCurrentProvince = location.getProvince();
                } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                    sb.append("\ndescribe : ");
                    sb.append("离线定位成功，离线定位结果也是有效的");
                } else if (location.getLocType() == BDLocation.TypeServerError) {
                    sb.append("\ndescribe : ");
                    sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                    sb.append("\ndescribe : ");
                    sb.append("网络不同导致定位失败，请检查网络是否通畅");
                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                    sb.append("\ndescribe : ");
                    sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
                }
                logMsg(sb.toString());
            }
        }
    };
    /**
     * 显示请求字符串
     * @param str
     */
    public void logMsg(String str) {
        try {
            Constants.lat = latitude;
            Constants.longt = longitude;
            if (isFirst) {
                mCityText.setText(mCityStr);

                isFirst = false;
                isLocation = false;
                //获取省
                getProvince();
                isGetCity = false;
                new Thread(updateRun).start();
                SharedPreferences sp = getApplicationContext()
                        .getSharedPreferences("city", MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("city", mCityStr);
                editor.commit();
                // 获取上级服务商
                Log.i("mBeautySalonPopup",
                        "===========美容院提示框0==============");
                getNoticeHttp();
            }
            Log.i("定位成功", "latitude="+latitude+"=====longitude="+longitude+"====city"+mCityStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // 自动更新
    Runnable updateRun = new Runnable() {
        @Override
        public void run() {
            Looper.prepare();
            updateVersionService = new UpdateVersionService(
                    Constants.UPDATEVERSIONXMLPATH, NewMainActivity.this);// 创建更新业务对象
            updateVersionService.checkUpdate();// 调用检查更新的方法,如果可以更新.就更新.不能更新就提示已经是最新的版本了
            Looper.loop();
        }
    };
    // 获取平台公告
    private void getNoticeHttp() {
        RequestParams params = new RequestParams();
        AsyUtils.get(Constants.getnotice2, params,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        try {
                            mNoticeList = Constants.getJsonArrayByData(response
                                    .toString());
                            // if (mNoticeList != null) {
                            handler.sendEmptyMessageDelayed(4, 0);

                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        super.onSuccess(response);
                    }
                });
    }

    // 取消上级服务商提示框
    View.OnClickListener mBeautyCanleClick = new View.OnClickListener() {
        @Override
        public void onClick(View arg0) {
//            mAlertLayout.setVisibility(View.GONE);
            if(mBeautySalonPopup!=null && mBeautySalonPopup.isShowing()) {
                mBeautySalonPopup.dismiss();
            }
        }
    };

    View.OnClickListener mBeautyDetailClick = new View.OnClickListener() {
        @Override
        public void onClick(View arg0) {

            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString("userMerchantName",
                    mUserMerchantList.get(0).get("merchantname").toString());
            if (mUserMerchantList.get(0).get("introduction") != null) {
                bundle.putString("userMerchantContent", mUserMerchantList
                        .get(0).get("introduction").toString());
            } else {

                bundle.putString("userMerchantContent", "");
            }
            if (mUserMerchantList.get(0).get("phone") != null) {
                bundle.putString("userMerchantPhone", mUserMerchantList.get(0)
                        .get("phone").toString());
            } else {
                bundle.putString("userMerchantPhone", "");
            }
            if (mUserMerchantList.get(0).get("address") != null) {
                bundle.putString("userMerchantAddress", mUserMerchantList
                        .get(0).get("address").toString());
            } else {
                bundle.putString("userMerchantAddress", "");
            }
            intent.putExtras(bundle);
            intent.setClass(getApplicationContext(),
                    ServiceProviderActivity.class);
            startActivity(intent);
//            mAlertLayout.setVisibility(View.GONE);
            mBeautySalonPopup.dismiss();
        }
    };
    AdapterView.OnItemClickListener mMerchantListItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                long arg3) {
            Intent intent = new Intent();
            if (mMerList != null && mMerList.size() > 0) {
                Bundle bundle = new Bundle();
                if (mMerList.get(position).get("Merchantname") != null) {
                    bundle.putString("merchantName", mMerList.get(position)
                            .get("Merchantname").toString());
                }
                if (mMerList.get(position).get("Logoimage") != null) {
                    bundle.putString("Logoimage",
                            mMerList.get(position).get("Logoimage").toString());
                }
                if (mMerList.get(position).get("Address") != null) {
                    bundle.putString("merchantAddress", mMerList.get(position)
                            .get("Address").toString());
                }
                if (mMerList.get(position).get("Phone") != null) {
                    bundle.putString("Phone",
                            mMerList.get(position).get("Phone").toString());
                }
                if (mMerList.get(position).get("Introduction") != null) {
                    bundle.putString("Introduction", mMerList.get(position)
                            .get("Introduction").toString());
                }
                if (mMerList.get(position).get("Userid") != null) {
                    bundle.putString("Userid",
                            mMerList.get(position).get("Userid").toString());
                }
                if (mMerList.get(position).get("Id") != null) {
                    bundle.putString("Id", mMerList.get(position).get("Id")
                            .toString());
                }
                if (mMerList.get(position).get("Flnum") != null) {
                    bundle.putString("Flnum",
                            mMerList.get(position).get("Flnum").toString());
                }
                if (mMerList.get(position).get("Images1") != null) {
                    bundle.putString("Images1",
                            mMerList.get(position).get("Images1").toString());
                }
                if (mMerList.get(position).get("Images2") != null) {
                    bundle.putString("Images2",
                            mMerList.get(position).get("Images2").toString());
                }
                if (mMerList.get(position).get("Images3") != null) {
                    bundle.putString("Images3",
                            mMerList.get(position).get("Images3").toString());
                }
                if (mMerList.get(position).get("Images4") != null) {
                    bundle.putString("Images4",
                            mMerList.get(position).get("Images4").toString());
                }
                if (mMerList.get(position).get("Images5") != null) {
                    bundle.putString("Images5",
                            mMerList.get(position).get("Images5").toString());
                }
                if (mMerList.get(position).get("plstar") != null) {
                    bundle.putString("plstar",
                            mMerList.get(position).get("plstar").toString());
                } else {
                    bundle.putString("plstar", "0");
                }
                if (mMerList.get(position).get("verifyState") != null) {
                    bundle.putString("verifyState",
                            mMerList.get(position).get("verifyState")
                                    .toString());
                }
                if (mMerList.get(position).get("iscurrency") != null) {
                    bundle.putString("iscurrency",
                            mMerList.get(position).get("iscurrency").toString());
                }
                if (mMerList.get(position).get("location") != null) {
                    bundle.putString("location",
                            mMerList.get(position).get("location").toString());
                }
                if (mMerList.get(position).get("ordercount") != null) {
                    bundle.putString("ordercount",
                            mMerList.get(position).get("ordercount").toString());
                }
                if (mMerList.get(position).get("Isparking") != null) {
                    bundle.putString("Isparking",
                            mMerList.get(position).get("Isparking").toString());
                }
                if (mMerList.get(position).get("money") != null) {
                    bundle.putString("money",
                            mMerList.get(position).get("money").toString());
                }
                if (mMerList.get(position).get("km") != null) {
                    bundle.putString("km",
                            mMerList.get(position).get("km").toString());
                }
                if (mMerList.get(position).get("currencybackbl") != null) {
                    bundle.putString("currencybackbl",
                            mMerList.get(position).get("currencybackbl").toString());
                }
                if (mMerList.get(position).get("istop") != null) {
                    bundle.putString("istop",
                            mMerList.get(position).get("istop").toString());
                }
                intent.putExtras(bundle);
            }
            // 跳转到商家详情界面
            intent.setClass(getApplicationContext(), MerchantInfoActivity.class);
            startActivity(intent);
        }
    };
    // 自动登录
    public boolean isAutoLogin() {
        SharedPreferences preferences = getSharedPreferences("MyAccounts",
                Activity.MODE_PRIVATE);
        if (preferences != null) {
            if(preferences.getString("flag", "").equals("1")) {
                Constants.mUserName = preferences.getString("userName", "");
                Constants.mPassWord = preferences.getString("pwd", "");
                Constants.mId = preferences.getString("Id", "");
                Constants.mUserCode = preferences.getString("userCode", "");
                Constants.mRealname = preferences.getString("Realname", "");
                Constants.headPath = preferences.getString("Avatar", "");
                Log.i("Constants.mUserName", Constants.mUserName
                        + "=======================");
                if (Constants.mUserName.equals("")) {
                    Constants.onLine = 0;
                    return false;
                } else {
                    userName = Constants.mUserName;
                    Constants.onLine = 1;
                    return true;
                }
            }else if(preferences.getString("flag", "").equals("2")){
                Constants.unionid = preferences.getString("unionid", "");
                return true;
            }
        }
        return false;
    }
    private void autologin() {
        StringBuilder sb = new StringBuilder();
        sb.append("username=" + Constants.mUserName + "&password="
                + Constants.mPassWord);
        XutilsUtils.get(Constants.log2, sb, new RequestCallBack<String>() {
            @Override
            public void onFailure(HttpException arg0, String arg1) {
                Log.i("自动登录失败", arg1 + "======onFailure=========");
            }
            @Override
            public void onSuccess(ResponseInfo<String> res) {
//					mAccountList = getJsonList(res.result);
                mLogMap = Constants.getJsonObject(res.result);

                handler.sendEmptyMessageDelayed(9, 0);
            }
        });
    }
    private void wxAutologin() {
        StringBuilder sb = new StringBuilder();
        sb.append("unionId=" +Constants.unionid);
        XutilsUtils.get(Constants.wxLog, sb, new RequestCallBack<String>() {
            @Override
            public void onFailure(HttpException arg0, String arg1) {
                Log.i("微信自动登录失败", arg1 + "======onFailure=========");
            }
            @Override
            public void onSuccess(ResponseInfo<String> res) {
                mWxMap1 = Constants.getJsonObject(res.result);
                if(mWxMap1!=null && mWxMap1.size()>0 && mWxMap1.get("Code")!=null){
                    if(!mWxMap1.get("Code").toString().equals("200")){
                    }else{
                        mWxMap2 = Constants.getJsonObjectByData(res.result);
                        Log.i("微信自动登录成功",mWxMap2+"====================");
                        try{
                            if (mWxMap2 != null && mWxMap2.size() > 0) {
                                if (mWxMap2.get("Id").equals("0")) {
                                    handler.sendEmptyMessageDelayed(2, 0);
                                } else {
                                    Constants.mUserName = mWxMap2.get("Username")
                                            .toString();
                                    Constants.mId = mWxMap2.get("Id").toString();
                                    Constants.mUserCode = mWxMap2.get("Usercode")
                                            .toString();
                                    Constants.mPayPassword = mWxMap2
                                            .get("Paypassword").toString();
                                    Constants.mShMoney = mWxMap2.get("Shmoney")
                                            .toString();
                                    Constants.mPassWord = "null";
                                    Constants.mFid = mWxMap2.get("Fid").toString();
                                    Constants.mIsjh = mWxMap2.get("Isjh")
                                            .toString();
                                    Constants.headPath = mWxMap2.get("Avatar")
                                            .toString();
                                    Constants.UserLevelMemo = mWxMap2.get("UserLevelMemo")
                                            .toString();
                                    if (!(mWxMap2.get("Realname").equals("null"))) {
                                        Constants.mRealname = URLDecoder.decode(mWxMap2
                                                        .get("Realname").toString(),
                                                "UTF-8");
                                    } else {
                                        Constants.mRealname = mWxMap2
                                                .get("Username").toString();
                                    }
                                    Constants.onLine = 1;
                                }
                            }
                        }catch (Exception e){}
                    }
                }
            }
        });
    }
    //金币商城精选
    private void getMallList() {
        StringBuilder sb = new StringBuilder();
        sb.append("uid="+Constants.mId+"&top=4"+"&shop=2");
        XutilsUtils.get(Constants.getMallJx, sb,
                new RequestCallBack<String>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                    }
                    @Override
                    public void onSuccess(ResponseInfo<String> res) {
                        try {
                            mMallLists = Constants.getJsonArrayByData(res.result);
                            Log.i("金币商城精选",mMallLists+"=================");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(mMallLists!=null && mMallLists.size()>0){
                            handler.sendEmptyMessageDelayed(10,0);
                        }
                    }
                });
    }

    private void getBannerMiddel() {
        StringBuilder sb = new StringBuilder();
        sb.append("positionCode=3&top=5");
        XutilsUtils.get(Constants.getAdListByPosition, sb,
                new RequestCallBack<String>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> res) {
                        Log.i("公告",res.result+"====================");
                        try {
                            mGgHeadlList = Constants
                                    .getJsonArrayByData(res.result);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (mGgHeadlList != null && mGgHeadlList.size() > 0) {
                            handler.sendEmptyMessageDelayed(11, 0);
                        }
                    }
                });
    }

    private void getSetting() {
        StringBuilder sb = new StringBuilder();
        sb.append("");
        XutilsUtils.get(Constants.getSetting, sb,
                new RequestCallBack<String>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                    }
                    @Override
                    public void onSuccess(ResponseInfo<String> res) {
                        mSettingList = Constants.getJsonArray(res.result);

                         Log.i("获取所有说明", res.result+"=================");
                        if (mSettingList != null && mSettingList.size() > 0) {
                            for (int i = 0; i < mSettingList.size(); i++) {
                                if (mSettingList.get(i).get("Title") != null
                                        && mSettingList.get(i).get("Content") != null) {
                                    SharedPreferences sp = getApplicationContext().getSharedPreferences(mSettingList.get(i)
                                            .get("Title").toString(),MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sp.edit();
                                    editor.putString("Title",
                                            mSettingList.get(i).get("Title")
                                                    .toString());
                                    editor.putString("id",
                                            mSettingList.get(i).get("Id")
                                                    .toString());
                                    editor.putString("Content", mSettingList
                                            .get(i).get("Content").toString());
                                    editor.commit();
                                }
                            }
                        }

                    }
                });
    }

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
    // 退出程序
    private void exit() {
        if (!isExit) {
            isExit = true;
            mToast.show("再按一次退出程序", 1);
            handler.sendEmptyMessageDelayed(0, 2000);
        } else {
            ExitApplication.getInstance().exit();
        }
    }
}

