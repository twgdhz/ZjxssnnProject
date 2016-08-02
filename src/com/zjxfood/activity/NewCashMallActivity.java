package com.zjxfood.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.project.util.BitmapUtilSingle;
import com.project.util.ScreenUtils;
import com.zjxfood.adapter.MainViewPagerAdapter;
import com.zjxfood.adapter.NewCashMallAdapter;
import com.zjxfood.adapter.NewCashTypeAdapter;
import com.zjxfood.common.Constants;
import com.zjxfood.http.XutilsUtils;
import com.zjxfood.popupwindow.CashMoreMallPopup;
import com.zjxfood.view.HorizontalListView;
import com.zjxfood.view.MyGridViewScroll;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 新现金商城
 * Created by Administrator on 2016/7/13.
 */
public class NewCashMallActivity extends AppActivity implements View.OnClickListener {

    private ViewPager mBannerViewPager;
    private ArrayList<HashMap<String, Object>> mAdvertisementTopList, mMallLists, mMoreLists, mIconList,mCjzxLists;
    private BitmapUtils mBitmapUtils;
    List<View> mViewList;
    private MainViewPagerAdapter mViewPagerAdapter;
    private MyGridViewScroll mMyGridView;
    private NewCashMallAdapter mGridAdapter;
    private ScheduledExecutorService scheduledExecutorService;
    private int currentItem = 0; // 当前图片的索引号
    private ImageView mBackImage;
    private ImageView mDownImage;
    private CashMoreMallPopup mMorePopup;
    private LinearLayout mTopTypeLayout;
    //    private ScrollView mScrollView;
    private RelativeLayout mTypeLayout1, mTypeLayout2, mTypeLayout3, mTypeLayout4;
    private ImageView mSearchImage;
    private ImageView mCjzxImage, mTttjImage, mWzcxImage, mCzthImage, mFcdpImage;
    private HorizontalListView mHorizonList;
    private NewCashTypeAdapter mTypeAdapter;
    private LinearLayout mCjzxLayout;
    private TextView mTypeText1, mTypeText2, mTypeText3, mTypeText4;
    private ArrayList<TextView> mViews;
    private ArrayList<HashMap<String,Object>> newList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_cash_mall_layout);
        setImmerseLayout(findViewById(R.id.head_layout));
        mBitmapUtils = BitmapUtilSingle
                .getBitmapInstance(getApplicationContext());
        mBitmapUtils
                .configDefaultLoadFailedImage(R.drawable.main_paimai_zhanwei);
        init();
        mViews = new ArrayList<TextView>();
        mViews.add(mTypeText1);
        mViews.add(mTypeText2);
        mViews.add(mTypeText3);
        mViews.add(mTypeText4);
        mViewList = new ArrayList<View>();
        // 获取顶端banner
        getBanner();
        //商城精选
        getMallList();
        getIcon();
        // 获取五张图片
        getcjzx();
    }

    private void init() {
        mBannerViewPager = (ViewPager) findViewById(R.id.new_cash_mall_viewpager);
        ViewGroup.LayoutParams layoutParams = mBannerViewPager
                .getLayoutParams();
        layoutParams.height = (int) (ScreenUtils.getScreenWidth(getApplicationContext()) * 0.51);
        mBannerViewPager.setLayoutParams(layoutParams);
        mMyGridView = (MyGridViewScroll) findViewById(R.id.new_mall_index_grid_view);
        mBackImage = (ImageView) findViewById(R.id.new_cash_title_back_image);
        mDownImage = (ImageView) findViewById(R.id.new_cash_more_image);
        mTopTypeLayout = (LinearLayout) findViewById(R.id.new_cash_top_layout);
        mTypeLayout1 = (RelativeLayout) findViewById(R.id.cash_lr_layout);
        mTypeLayout2 = (RelativeLayout) findViewById(R.id.cash_nb_layout);
        mTypeLayout3 = (RelativeLayout) findViewById(R.id.cash_nn_layout);
        mTypeLayout4 = (RelativeLayout) findViewById(R.id.cash_tc_layout);
        mSearchImage = (ImageView) findViewById(R.id.mall_title_search_image);
        mCjzxLayout = (LinearLayout) findViewById(R.id.new_cash_czjx_layout);
        ViewGroup.LayoutParams params = mCjzxLayout.getLayoutParams();
        params.height = (int) (ScreenUtils.getScreenWidth(getApplicationContext()) * 0.45);
        mCjzxLayout.setLayoutParams(params);
        mCjzxImage = (ImageView) findViewById(R.id.cjzx_image);
        mTttjImage = (ImageView) findViewById(R.id.tttj_image);
        mWzcxImage = (ImageView) findViewById(R.id.wzcx_image);
        mCzthImage = (ImageView) findViewById(R.id.czth_image);
        mFcdpImage = (ImageView) findViewById(R.id.fcdp_image);
        mHorizonList = (HorizontalListView) findViewById(R.id.new_cash_mall_horizon_list);
        mTypeText1 = (TextView) findViewById(R.id.new_cash_type1);
        mTypeText2 = (TextView) findViewById(R.id.new_cash_type2);
        mTypeText3 = (TextView) findViewById(R.id.new_cash_type3);
        mTypeText4 = (TextView) findViewById(R.id.new_cash_type4);


        mBackImage.setOnClickListener(this);
        mDownImage.setOnClickListener(this);
        mSearchImage.setOnClickListener(this);

        mTypeLayout1.setOnClickListener(this);
        mTypeLayout2.setOnClickListener(this);
        mTypeLayout3.setOnClickListener(this);
        mTypeLayout4.setOnClickListener(this);

        mCjzxImage.setOnClickListener(this);
        mTttjImage.setOnClickListener(this);
        mWzcxImage.setOnClickListener(this);
        mCzthImage.setOnClickListener(this);
        mFcdpImage.setOnClickListener(this);
    }

    // 获取首页顶端图片
    private void getBanner() {
        StringBuilder sb = new StringBuilder();
        sb.append("positionCode=5&top=5");
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

    //商城精选
    private void getMallList() {
        StringBuilder sb = new StringBuilder();
        sb.append("uid=" + Constants.mId + "&shop=3" + "&userBuyLevel=1" + "&top=10");
        XutilsUtils.get(Constants.getMallLikes, sb,
                new RequestCallBack<String>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> res) {
                        try {
                            mMallLists = Constants.getJsonArrayByData(res.result);
                            Log.i("猜你喜欢", mMallLists + "=================");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (mMallLists != null && mMallLists.size() > 0) {
                            handler.sendEmptyMessageDelayed(2, 0);
                        }
                    }
                });
    }

    //获取分类
    private void getIcon() {
        StringBuilder sb = new StringBuilder();
        sb.append("pid=0" + "&giftTypeCode=3");
        XutilsUtils.get(Constants.getIconList, sb,
                new RequestCallBack<String>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> res) {
                        Log.i("banner", res.result + "==========");
                        try {
                            mIconList = Constants.getJsonArrayByData(res.result);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.i("mIconList", mIconList + "==========");
                        if (mIconList != null && mIconList.size() > 0) {
                            handler.sendEmptyMessageDelayed(4, 0);
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
                            Log.i("获取五张图片",
                                    res.result + "======获取五张图片======");
                            mCjzxLists = Constants
                                    .getJsonArrayByData(res.result);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (mCjzxLists != null
                                && mCjzxLists.size() > 0) {
                            handler.sendEmptyMessageDelayed(5, 0);
                        }
                    }
                });
    }
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
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
                    mGridAdapter = new NewCashMallAdapter(getApplicationContext(),
                            mMallLists, "xj");
                    mMyGridView.setAdapter(mGridAdapter);
                    break;
                case 3:
                    mBannerViewPager.setCurrentItem(currentItem);// 切换当前显示的图片
                    break;
                case 4:
                    for (int i = 0; i < mIconList.size(); i++) {
                        if (mIconList.get(i).get("name") != null && i < mViews.size()) {
                            mViews.get(i).setText(mIconList.get(i).get("name").toString());
                        }
                    }
                    break;
                case 5:
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

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        Bundle bundle;
        switch (view.getId()) {
            //厂家直销
            case R.id.cjzx_image:
//                intent.setClass(getApplicationContext(), MallActivity.class);
//                bundle.putString("code", "CJ");
//                bundle.putString("group", "3");
//                bundle.putString("titleName", "厂家直销");
//                bundle.putString("shouCode", "3");
//                bundle.putString("proportion", "");
//                bundle.putString("type", "xj");
//                bundle.putString("typeName", "xj");
//                intent.putExtras(bundle);
//                startActivity(intent);

                if(mCjzxLists!=null  && mCjzxLists.size()>0 && mCjzxLists.get(0).get("AndroidForm")!=null) {
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
                                bundle.putString("shouCode", "3");
                                bundle.putString("group", "3");
                                bundle.putString("proportion", "");
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
                                bundle.putString("shouCode", "3");
                                bundle.putString("group", "3");
                                bundle.putString("proportion", "");
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
                                bundle.putString("shouCode", "3");
                                bundle.putString("group", "3");
                                bundle.putString("proportion", "");
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
                if(mCjzxLists!=null && mCjzxLists.size()>0  && mCjzxLists.get(4).get("AndroidForm")!=null) {
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
                                bundle.putString("shouCode", "3");
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
            //搜索
            case R.id.mall_title_search_image:
                intent.setClass(getApplicationContext(), MallSearchActivity.class);
                bundle = new Bundle();
                bundle.putString("group", "3");
                bundle.putString("type", "xj");
                bundle.putString("shouCode", "3");
                bundle.putString("proportion", "");
                bundle.putString("typeName", "xj");
                intent.putExtras(bundle);
                startActivity(intent);
                break;

            case R.id.new_cash_title_back_image:
                finish();
                break;
            //下拉更多
            case R.id.new_cash_more_image:
                if(mIconList!=null && mIconList.size()>4) {
                    newList = new ArrayList<HashMap<String, Object>>();
                    for(int i=0;i<mIconList.size();i++){
                        if(i>3){
                            newList.add(mIconList.get(i));
                        }
                    }
                    int a = newList.size()%5;
                    Log.i("下拉更多",newList.size()+"=========");
                    for(int j=0;j<a-1;j++){
                        HashMap<String,Object> map = new HashMap<String, Object>();
                        newList.add(map);
                    }
                    mMorePopup = new CashMoreMallPopup(NewCashMallActivity.this, newList, onItemClickListener);
                    mMorePopup.showAsDropDown(mTopTypeLayout);
                }
                break;
            //分类1
            case R.id.cash_lr_layout:
//                intent.setClass(getApplicationContext(), NewCashListActivity.class);
                bundle = new Bundle();
//                bundle.putString("type", "xj");
//                bundle.putString("typeName", "xj");
//                intent.putExtras(bundle);
//                startActivity(intent);
                if (mIconList != null && mIconList.size() > 0) {
                    intent.setClass(getApplicationContext(), MallActivity.class);
                    if (mIconList.get(0).get("code") != null) {
                        bundle.putString("code", mIconList.get(0).get("code").toString());
                    }
                    bundle.putString("group", "3");
                    if (mIconList.get(0).get("name") != null) {
                        bundle.putString("titleName", mIconList.get(0).get("name").toString());
                    }
                    bundle.putString("shouCode", "3");
                    bundle.putString("proportion", "");
                    bundle.putString("type", "xj");
                    bundle.putString("typeName", "xj");
                    if (mIconList.get(0).get("Id") != null) {
                        bundle.putString("pid", mIconList.get(0).get("Id").toString());
                    }
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                break;
            //分类2
            case R.id.cash_nb_layout:
                bundle = new Bundle();
                if (mIconList != null && mIconList.size() > 1) {
                    intent.setClass(getApplicationContext(), MallActivity.class);
                    if (mIconList.get(1).get("code") != null) {
                        bundle.putString("code", mIconList.get(1).get("code").toString());
                    }
                    bundle.putString("group", "3");
                    if (mIconList.get(1).get("name") != null) {
                        bundle.putString("titleName", mIconList.get(1).get("name").toString());
                    }
                    bundle.putString("shouCode", "3");
                    bundle.putString("proportion", "");
                    bundle.putString("type", "xj");
                    bundle.putString("typeName", "xj");
                    if (mIconList.get(1).get("Id") != null) {
                        bundle.putString("pid", mIconList.get(1).get("Id").toString());
                    }
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                break;
            //分类3
            case R.id.cash_nn_layout:
                bundle = new Bundle();
                if (mIconList != null && mIconList.size() > 2) {
                    intent.setClass(getApplicationContext(), MallActivity.class);
                    if (mIconList.get(2).get("code") != null) {
                        bundle.putString("code", mIconList.get(2).get("code").toString());
                    }
                    bundle.putString("group", "3");
                    if (mIconList.get(2).get("name") != null) {
                        bundle.putString("titleName", mIconList.get(2).get("name").toString());
                    }
                    bundle.putString("shouCode", "3");
                    bundle.putString("proportion", "");
                    bundle.putString("type", "xj");
                    bundle.putString("typeName", "xj");
                    if (mIconList.get(2).get("Id") != null) {
                        bundle.putString("pid", mIconList.get(2).get("Id").toString());
                    }
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                break;
            //分类4
            case R.id.cash_tc_layout:
                bundle = new Bundle();
                if (mIconList != null && mIconList.size() > 3) {
                    intent.setClass(getApplicationContext(), MallActivity.class);
                    if (mIconList.get(3).get("code") != null) {
                        bundle.putString("code", mIconList.get(3).get("code").toString());
                    }
                    bundle.putString("group", "3");
                    if (mIconList.get(3).get("name") != null) {
                        bundle.putString("titleName", mIconList.get(3).get("name").toString());
                    }
                    bundle.putString("shouCode", "3");
                    bundle.putString("proportion", "");
                    bundle.putString("type", "xj");
                    bundle.putString("typeName", "xj");
                    if (mIconList.get(3).get("Id") != null) {
                        bundle.putString("pid", mIconList.get(3).get("Id").toString());
                    }
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                break;
        }
    }

    private class ScrollTask implements Runnable {
        public void run() {
            synchronized (mBannerViewPager) {
                currentItem = (currentItem + 1) % mViewList.size();
                handler.sendEmptyMessageDelayed(3, 0); // 通过Handler切换图片
            }
        }
    }

    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            if(newList!=null && newList.size()>0) {

                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                intent.setClass(getApplicationContext(), MallActivity.class);
                if (newList.get(i).get("code") != null) {
                    bundle.putString("code", newList.get(i).get("code").toString());
                }
                bundle.putString("group", "3");
                if (newList.get(i).get("name") != null) {
                    bundle.putString("titleName", newList.get(i).get("name").toString());
                }
                bundle.putString("shouCode", "3");
                bundle.putString("proportion", "");
                bundle.putString("type", "xj");
                bundle.putString("typeName", "xj");
                if (mIconList.get(i).get("Id") != null) {
                    bundle.putString("pid", mIconList.get(i).get("Id").toString());
                }
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }
    };

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
        if (scheduledExecutorService != null) {
            scheduledExecutorService.shutdown();
        }
        super.onStop();
    }
}
