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

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.project.util.ScreenUtils;
import com.zjxfood.adapter.JbBannerAdapter;
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
 * Created by Administrator on 2016/7/18.
 * 新金币商城
 */
public class NewJbMallActivity extends AppActivity implements View.OnClickListener{

    private TextView mTitleText;
    private ImageView mLeftImage,mRightImage;
    private MyGridViewScroll mMyGridView;
    private ArrayList<HashMap<String, Object>> mMallLists,mIconList,mAdvertisementTopList,mProList2;
    private NewCashMallAdapter mGridAdapter;
    private HorizontalListView mHorizonList;
    private NewCashTypeAdapter mTypeAdapter;
    private List<View> mViewList;
    private JbBannerAdapter mViewPagerAdapter;
    private ViewPager mBannerViewPager;
    private LinearLayout mLrLayout;
    private ImageView mSearchImage;
    private RelativeLayout mTypeLayout1,mTypeLayout2,mTypeLayout3,mTypeLayout4;
    private TextView mTypeText1,mTypeText2,mTypeText3,mTypeText4;
    private ArrayList<HashMap<String,Object>> newList;
    private ArrayList<TextView> mViews;
    private ImageView mMoreImage;
    private CashMoreMallPopup mMorePopup;
    private ScheduledExecutorService scheduledExecutorService;
    private int currentItem = 0; // 当前图片的索引号
    private ImageView mBackImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_jb_mall_layout);
        setImmerseLayout(findViewById(R.id.head_layout));
        mViewList =  new ArrayList<View>();
        init();
        mViews = new ArrayList<TextView>();
        mViews.add(mTypeText1);
        mViews.add(mTypeText2);
        mViews.add(mTypeText3);
        mViews.add(mTypeText4);
        // 获取顶端banner
        getBanner();
        //商城精选
        getMallList();
        getIcon();
        //获取左右两张图片
        getBannerLr();
//        mBitmapUtils.display(mLeftImage,"http://img.zjxssnn.com/banner/07/dpcx.png");
//        mBitmapUtils.display(mRightImage,"http://img.zjxssnn.com/banner/07/wzth.png");
    }

    private void init(){
        mTitleText = (TextView) findViewById(R.id.gift_center_info_text);
        mTitleText.setText("金币商城");
        mMyGridView = (MyGridViewScroll) findViewById(R.id.new_jb_mall_grid_view);
        mHorizonList = (HorizontalListView) findViewById(R.id.new_jb_mall_horizon_list);
        mBannerViewPager = (ViewPager) findViewById(R.id.new_jb_mall_viewpager);
        ViewGroup.LayoutParams layoutParams = mBannerViewPager.getLayoutParams();
        layoutParams.height = (int) (ScreenUtils.getScreenWidth(getApplicationContext())*0.51);
        mBannerViewPager.setLayoutParams(layoutParams);
        mLeftImage = (ImageView) findViewById(R.id.new_jb_left_image);
        mRightImage = (ImageView) findViewById(R.id.new_jb_right_image);
        mLrLayout = (LinearLayout) findViewById(R.id.new_jb_lr_layout);
        layoutParams = mLrLayout.getLayoutParams();
        layoutParams.height = (int) (ScreenUtils.getScreenWidth(getApplicationContext())*0.3);
        mLrLayout.setLayoutParams(layoutParams);
        mSearchImage = (ImageView) findViewById(R.id.mall_title_search_image);
        mTypeLayout1 = (RelativeLayout) findViewById(R.id.jb_lr_layout);
        mTypeLayout2 = (RelativeLayout) findViewById(R.id.jb_nb_layout);
        mTypeLayout3 = (RelativeLayout) findViewById(R.id.jb_nn_layout);
        mTypeLayout4 = (RelativeLayout) findViewById(R.id.jb_tc_layout);
        mTypeText1 = (TextView) findViewById(R.id.new_jb_type1);
        mTypeText2 = (TextView) findViewById(R.id.new_jb_type2);
        mTypeText3 = (TextView) findViewById(R.id.new_jb_type3);
        mTypeText4 = (TextView) findViewById(R.id.new_jb_type4);
        mMoreImage = (ImageView) findViewById(R.id.new_jb_more_image);
        mBackImage = (ImageView) findViewById(R.id.new_cash_title_back_image);

        mBackImage.setOnClickListener(this);
        mSearchImage.setOnClickListener(this);
        mMoreImage.setOnClickListener(this);
        mTypeLayout1.setOnClickListener(this);
        mTypeLayout2.setOnClickListener(this);
        mTypeLayout3.setOnClickListener(this);
        mTypeLayout4.setOnClickListener(this);
        mLeftImage.setOnClickListener(this);
        mRightImage.setOnClickListener(this);
    }

    //商城精选
    private void getMallList() {
        StringBuilder sb = new StringBuilder();
        sb.append("uid=" + Constants.mId + "&shop=2" + "&userBuyLevel=1"+"&top=10");
        XutilsUtils.get(Constants.getMallLikes, sb,
                new RequestCallBack<String>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                    }
                    @Override
                    public void onSuccess(ResponseInfo<String> res) {
                        try {
                            mMallLists = Constants.getJsonArrayByData(res.result);
                            Log.i("商城精选", mMallLists + "=================");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (mMallLists != null && mMallLists.size() > 0) {
                            handler.sendEmptyMessageDelayed(1, 0);
                        }
                    }
                });
    }
    //获取分类
    private void getIcon() {
        StringBuilder sb = new StringBuilder();
        sb.append("pid=0"+"&giftTypeCode=2");
        XutilsUtils.get(Constants.getIconList, sb,
                new RequestCallBack<String>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                    }
                    @Override
                    public void onSuccess(ResponseInfo<String> res) {
                        Log.i("banner", res.result+"==========");
                        try {
                            mIconList = Constants.getJsonArrayByData(res.result);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.i("mIconList", mIconList+"==========");
                        if(mIconList!=null && mIconList.size()>0){
                            handler.sendEmptyMessageDelayed(2, 0);
                        }
                    }
                });
    }
    // 获取首页顶端图片
    private void getBanner() {
        StringBuilder sb = new StringBuilder();
        sb.append("positionCode=6&top=5");
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
                            handler.sendEmptyMessageDelayed(3, 0);
                        }
                    }
                });
    }


    AdapterView.OnItemClickListener onItenClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            intent.setClass(getApplicationContext(), MallActivity.class);
            if(mIconList.get(i).get("code")!=null) {
                bundle.putString("code", mIconList.get(i).get("code").toString());
            }
            bundle.putString("group", "2");
            if(mIconList.get(i).get("name")!=null) {
                bundle.putString("titleName", mIconList.get(i).get("name").toString());
            }
            bundle.putString("shouCode","2");
            bundle.putString("type", "ssjb");
            bundle.putString("proportion","1");
            bundle.putString("typeName", "ssjb");
            if (mIconList.get(i).get("Id") != null) {
                bundle.putString("pid", mIconList.get(i).get("Id").toString());
            }
            intent.putExtras(bundle);
            startActivity(intent);
        }
    };
    //获取左右图片
    private void getBannerLr() {
        StringBuilder sb = new StringBuilder();
        sb.append("positionCode=11&top=5");
        XutilsUtils.get(Constants.getAdListByPosition, sb,
                new RequestCallBack<String>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                    }
                    @Override
                    public void onSuccess(ResponseInfo<String> res) {
                        Log.i("banner", res.result+"==========");
                        try {
                            mProList2 = Constants.getJsonArrayByData(res.result);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(mProList2!=null && mProList2.size()>0){
                            handler.sendEmptyMessageDelayed(4, 0);
                        }
                    }
                });
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    mGridAdapter = new NewCashMallAdapter(getApplicationContext(),
                            mMallLists,"ssjb");
                    mMyGridView.setAdapter(mGridAdapter);
                    break;
                case 2:
//                    mTypeAdapter = new NewCashTypeAdapter(getApplicationContext(),mIconList);
//                    mHorizonList.setAdapter(mTypeAdapter);
//                    mHorizonList.setOnItemClickListener(onItenClick);
                    for (int i = 0; i < mIconList.size(); i++) {
                        if (mIconList.get(i).get("name") != null && i < mViews.size()) {
                            mViews.get(i).setText(mIconList.get(i).get("name").toString());
                        }
                    }
                    break;
                case 3:
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
                    mViewPagerAdapter = new JbBannerAdapter(
                            getApplicationContext(), mViewList,
                            mAdvertisementTopList);
                    mBannerViewPager.setAdapter(mViewPagerAdapter);

                    break;
                case 4:
                    if(mProList2!=null && mProList2.size()>0){
                        if(mProList2.get(0).get("Images")!=null){
                            mBitmapUtils.display(mLeftImage, mProList2.get(0).get("Images").toString());
                        }
                        if(mProList2.get(1).get("Images")!=null){
                            mBitmapUtils.display(mRightImage, mProList2.get(1).get("Images").toString());
                        }
                    }
                    break;
                case 5:
                    mBannerViewPager.setCurrentItem(currentItem);// 切换当前显示的图片
                    break;
            }
        }
    };

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        Bundle bundle;
        switch (view.getId()){
            //返回
            case R.id.new_cash_title_back_image:
            finish();
            break;
            case R.id.mall_title_search_image:
                intent.setClass(getApplicationContext(), MallSearchActivity.class);
                bundle = new Bundle();
                bundle.putString("group", "2");
                bundle.putString("type","ssjb");
                bundle.putString("shouCode","2");
                bundle.putString("proportion","1");
                bundle.putString("typeName","ssjb");
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            //下拉更多
            case R.id.new_jb_more_image:
                Log.i("下拉更多",mIconList.size()+"=========");
                if(mIconList!=null && mIconList.size()>4) {
                    newList = new ArrayList<HashMap<String, Object>>();
                    for(int i=0;i<mIconList.size();i++){
                        if(i>3){
                            newList.add(mIconList.get(i));
                        }
                    }
                    int a = newList.size()%5;
                    for(int j=0;j<a;j++){
                        HashMap<String,Object> map = new HashMap<String, Object>();
                        newList.add(map);
                    }
                    mMorePopup = new CashMoreMallPopup(NewJbMallActivity.this, newList, onItenClick);
                    mMorePopup.showAsDropDown(mTypeLayout1);
                }
                break;
            //分类1
            case R.id.jb_lr_layout:
                bundle = new Bundle();
                if(mIconList!=null && mIconList.size()>0) {
                    intent.setClass(getApplicationContext(), MallActivity.class);
                    if (mIconList.get(0).get("code") != null) {
                        bundle.putString("code", mIconList.get(0).get("code").toString());
                    }
                    bundle.putString("group", "2");
                    if (mIconList.get(0).get("name") != null) {
                        bundle.putString("titleName", mIconList.get(0).get("name").toString());
                    }
                    bundle.putString("shouCode", "2");
                    bundle.putString("type", "ssjb");
                    bundle.putString("proportion", "1");
                    bundle.putString("typeName", "ssjb");
                    if (mIconList.get(0).get("Id") != null) {
                        bundle.putString("pid", mIconList.get(0).get("Id").toString());
                    }
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                break;
            //分类2
            case R.id.jb_nb_layout:
                bundle = new Bundle();
                if(mIconList!=null && mIconList.size()>1) {
                    intent.setClass(getApplicationContext(), MallActivity.class);
                    if (mIconList.get(1).get("code") != null) {
                        bundle.putString("code", mIconList.get(1).get("code").toString());
                    }
                    bundle.putString("group", "2");
                    if (mIconList.get(1).get("name") != null) {
                        bundle.putString("titleName", mIconList.get(1).get("name").toString());
                    }
                    bundle.putString("shouCode", "2");
                    bundle.putString("type", "ssjb");
                    bundle.putString("proportion", "1");
                    bundle.putString("typeName", "ssjb");
                    if (mIconList.get(1).get("Id") != null) {
                        bundle.putString("pid", mIconList.get(1).get("Id").toString());
                    }
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                break;
            //分类3
            case R.id.jb_nn_layout:
                bundle = new Bundle();
                if(mIconList!=null && mIconList.size()>2) {
                    intent.setClass(getApplicationContext(), MallActivity.class);
                    if (mIconList.get(2).get("code") != null) {
                        bundle.putString("code", mIconList.get(2).get("code").toString());
                    }
                    bundle.putString("group", "2");
                    if (mIconList.get(2).get("name") != null) {
                        bundle.putString("titleName", mIconList.get(2).get("name").toString());
                    }
                    bundle.putString("shouCode", "2");
                    bundle.putString("type", "ssjb");
                    bundle.putString("proportion", "1");
                    bundle.putString("typeName", "ssjb");
                    if (mIconList.get(2).get("Id") != null) {
                        bundle.putString("pid", mIconList.get(2).get("Id").toString());
                    }
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                break;
            //分类4
            case R.id.jb_tc_layout:
                bundle = new Bundle();
                if(mIconList!=null && mIconList.size()>3) {
                    intent.setClass(getApplicationContext(), MallActivity.class);
                    if (mIconList.get(3).get("code") != null) {
                        bundle.putString("code", mIconList.get(3).get("code").toString());
                    }
                    bundle.putString("group", "2");
                    if (mIconList.get(3).get("name") != null) {
                        bundle.putString("titleName", mIconList.get(3).get("name").toString());
                    }
                    bundle.putString("shouCode", "2");
                    bundle.putString("type", "ssjb");
                    bundle.putString("proportion", "1");
                    bundle.putString("typeName", "ssjb");
                    if (mIconList.get(3).get("Id") != null) {
                        bundle.putString("pid", mIconList.get(3).get("Id").toString());
                    }
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                break;
            //大牌尝鲜
            case R.id.new_jb_left_image:
                bundle = new Bundle();
                intent.setClass(getApplicationContext(), MallActivity.class);
                bundle.putString("type", "jb_dp");
                bundle.putString("typeName", "ssjb");
                bundle.putString("shouCode", "2");
                bundle.putString("group", "2");
                bundle.putString("proportion", "1");
                bundle.putString("titleName", "大牌尝鲜");
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            //品质生活
            case R.id.new_jb_right_image:
                intent.setClass(getApplicationContext(), MallActivity.class);
                bundle = new Bundle();
                bundle.putString("type", "jb_wzth");
                bundle.putString("typeName", "ssjb");
                bundle.putString("shouCode", "2");
                bundle.putString("group", "2");
                bundle.putString("proportion", "1");
                bundle.putString("orderby", "5");
                bundle.putString("titleName", "品质生活");
                intent.putExtras(bundle);
                startActivity(intent);
                break;
        }
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
        if (scheduledExecutorService != null) {
            scheduledExecutorService.shutdown();
        }
        super.onStop();
    }
    private class ScrollTask implements Runnable {
        public void run() {
            synchronized (mBannerViewPager) {
                currentItem = (currentItem + 1) % mViewList.size();
                handler.sendEmptyMessageDelayed(5, 0); // 通过Handler切换图片
            }
        }
    }
}
