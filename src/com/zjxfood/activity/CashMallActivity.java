package com.zjxfood.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.project.util.BitmapUtilSingle;
import com.project.util.DensityUtils;
import com.project.util.ScreenUtils;
import com.zjxfood.adapter.MainViewPagerAdapter;
import com.zjxfood.adapter.MallListGridAdapter;
import com.zjxfood.application.ExitApplication;
import com.zjxfood.common.Constants;
import com.zjxfood.http.XutilsUtils;
import com.zjxfood.toast.MyToast;
import com.zjxfood.view.MyGridViewScroll;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/6/15.
 * 现金商城
 */
public class CashMallActivity extends AppActivity implements View.OnClickListener {

    private TextView mTitleText;
    private ArrayList<HashMap<String, Object>> mAdvertisementList, mCashList2, mCashList3, mMallLists;
    private List<View> mViewList;
    private ImageView mViewImage;
    private BitmapUtils mBitmapUtils;
    private MyToast mToast;
    private MainViewPagerAdapter mViewPagerAdapter;
    private ViewPager mTopPager;
    private boolean isExit = false;
    private LinearLayout mMainLayout, mBuyBillLayout, mMyLayout,
            mMallIndexLayout, mCarsLayout;
    private ImageView mTtImage, mLeftImage, mRightImage;
    private MallListGridAdapter mGridAdapter;
    private MyGridViewScroll mGridView;
    private ImageView mSearchImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mall_cash_layout);
        setImmerseLayout(findViewById(R.id.head_layout));
        mBitmapUtils = BitmapUtilSingle
                .getBitmapInstance(getApplicationContext());
        mToast = new MyToast(getApplicationContext());
        init();
        getBanner();
        getBanner2();
        getBanner3();
        getMallList();
    }

    private void init() {
        mSearchImage = (ImageView) findViewById(R.id.mall_title_search_image);
        mMainLayout = (LinearLayout) findViewById(R.id.main_home_menu_layout);
        mBuyBillLayout = (LinearLayout) findViewById(R.id.main_buy_menu_layout);
        mMyLayout = (LinearLayout) findViewById(R.id.main_my_menu_layout);
        mMallIndexLayout = (LinearLayout) findViewById(R.id.my_gift_menu_layout);
        mCarsLayout = (LinearLayout) findViewById(R.id.main_cars_layout);
        ImageView icon = (ImageView) mMallIndexLayout.findViewById(R.id.my_gift_menu_image);
        TextView textView = (TextView) mMallIndexLayout.findViewById(R.id.my_gift_menu_text);
        icon.setImageResource(R.drawable.gift_png2);
        textView.setTextColor(getResources().getColor(R.color.main_menu_chose));
        mTtImage = (ImageView) findViewById(R.id.cash_tt_image);
        ViewGroup.LayoutParams params = mTtImage.getLayoutParams();
        params.height = DensityUtils.dp2px(getApplicationContext(), 70);
        mTtImage.setLayoutParams(params);
        mLeftImage = (ImageView) findViewById(R.id.cash_mall_left);
        mRightImage = (ImageView) findViewById(R.id.cash_mall_right);
        mGridView = (MyGridViewScroll) findViewById(R.id.cash_mall_graid);


        mTitleText = (TextView) findViewById(R.id.gift_center_info_text);
        mTitleText.setText("现金商城");
        mTopPager = (ViewPager) findViewById(R.id.cash_mall_top_pager);
        params = mTopPager.getLayoutParams();
        params.height = (int) (ScreenUtils.getScreenWidth(getApplicationContext()) * 0.32);

        mSearchImage.setOnClickListener(this);
        mMainLayout.setOnClickListener(this);
        mBuyBillLayout.setOnClickListener(this);
        mMyLayout.setOnClickListener(this);
        mMallIndexLayout.setOnClickListener(this);
        mCarsLayout.setOnClickListener(this);
        mTtImage.setOnClickListener(this);
        mLeftImage.setOnClickListener(this);
        mRightImage.setOnClickListener(this);

    }

    //天天特价
    private void getMallList() {
        StringBuilder sb = new StringBuilder();
        sb.append("uid=" + Constants.mId + "&shop=3" + "&userBuyLevel=1"+"&top=10");
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
                            handler.sendEmptyMessageDelayed(4, 0);
                        }
                    }
                });
    }

    //获取banner
    private void getBanner() {
        StringBuilder sb = new StringBuilder();
        sb.append("positionCode=5&top=4");
        XutilsUtils.get(Constants.getAdListByPosition, sb,
                new RequestCallBack<String>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> res) {
                        Log.i("tag", res.result + "=================");
                        try {
                            mAdvertisementList = Constants.getJsonArrayByData(res.result);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.i("mAdvertisementList", mAdvertisementList + "==========");
                        if (mAdvertisementList != null && mAdvertisementList.size() > 0) {
                            handler.sendEmptyMessageDelayed(1, 0);
                        }
                    }
                });
    }

    //获取中部广告
    private void getBanner2() {
        StringBuilder sb = new StringBuilder();
        sb.append("positionCode=8&top=5");
        XutilsUtils.get(Constants.getAdListByPosition, sb,
                new RequestCallBack<String>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> res) {
                        Log.i("中部图片", res.result + "=================");
                        try {
                            mCashList2 = Constants.getJsonArrayByData(res.result);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.i("mCashList2", mCashList2 + "==========");
                        if (mCashList2 != null && mCashList2.size() > 0) {
                            handler.sendEmptyMessageDelayed(2, 0);
                        }
                    }
                });
    }

    //获取左右两边图片
    private void getBanner3() {
        StringBuilder sb = new StringBuilder();
        sb.append("positionCode=12&top=5");
        XutilsUtils.get(Constants.getAdListByPosition, sb,
                new RequestCallBack<String>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> res) {
                        Log.i("左右两边图片", res.result + "=================");
                        try {
                            mCashList3 = Constants.getJsonArrayByData(res.result);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.i("mCashList3", mCashList3 + "==========");
                        if (mCashList3 != null && mCashList3.size() > 0) {
                            handler.sendEmptyMessageDelayed(3, 0);
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
                    mViewList = new ArrayList<View>();
                    LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
                    View view;
                    for (int i = 0; i < mAdvertisementList.size(); i++) {
                        view = inflater.inflate(R.layout.mall_detail_viewpager_item, null);
                        mViewImage = (ImageView) view
                                .findViewById(R.id.mall_detail_viewpager_image);
                        if (!Constants.isNull(mAdvertisementList.get(i).get("Images"))) {
                            mBitmapUtils.display(mViewImage, mAdvertisementList.get(i).get("Images").toString());
                        }
                        mViewList.add(view);
                    }
                    mViewPagerAdapter = new MainViewPagerAdapter(getApplicationContext(),
                            mViewList, mAdvertisementList);
                    mTopPager.setAdapter(mViewPagerAdapter);
                    break;
                case 2:
                    if (mCashList2 != null && mCashList2.size() > 0 && mCashList2.get(0).get("Images") != null) {

                        mBitmapUtils.display(mTtImage, mCashList2.get(0).get("Images").toString());
                    }
                    break;
                case 3:
                    if (mCashList3 != null && mCashList3.size() > 0) {
                        if (mCashList3.get(0).get("Images") != null) {
                            mBitmapUtils.display(mLeftImage, mCashList3.get(0).get("Images").toString());
                        }
                        if (mCashList3.get(1).get("Images") != null) {
                            mBitmapUtils.display(mRightImage, mCashList3.get(1).get("Images").toString());
                        }
                    }
                    break;
                case 4:
                    mGridAdapter = new MallListGridAdapter(getApplicationContext(),
                            mMallLists,"xj");
                    mGridView.setAdapter(mGridAdapter);
                    break;
            }
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                exit();
                // finish();
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
            handler.sendEmptyMessageDelayed(0, 2000);
        } else {
            ExitApplication.getInstance().exit();
        }
    }

    @Override
    public void onClick(View v) {
        int version = Integer.valueOf(android.os.Build.VERSION.SDK);
        Intent intent = new Intent();
        Bundle bundle;
        switch (v.getId()) {
            //搜索
            case R.id.mall_title_search_image:
                intent.setClass(getApplicationContext(), MallSearchActivity.class);
                bundle = new Bundle();
                bundle.putString("group","");
                bundle.putString("type","xj");
                bundle.putString("shouCode","3");
                bundle.putString("proportion","");
                bundle.putString("typeName","xj");
                intent.putExtras(bundle);
                startActivity(intent);
                break;

            case R.id.main_home_menu_layout:
//                intent.setClass(getApplicationContext(), MainActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                startActivity(intent);
//                if (version > 5) {
//                    overridePendingTransition(R.animator.activity_zoomin, R.animator.activity_zoomout);
//                }
                break;
//购物车
            case R.id.main_cars_layout:
//
                break;
            case R.id.main_buy_menu_layout:
                intent.setClass(getApplicationContext(), BuyBillActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                if (version > 5) {
                    overridePendingTransition(R.animator.activity_zoomin, R.animator.activity_zoomout);
                }
//			finish();
                break;
            //我的
            case R.id.main_my_menu_layout:
//			intent.setClass(getApplicationContext(), MyActivity.class);
                intent.setClass(getApplicationContext(), MyNewActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                if (version > 5) {
                    overridePendingTransition(R.animator.activity_zoomin, R.animator.activity_zoomout);
                }
//			finish();
                break;
            case R.id.my_gift_menu_layout:
                dissMallPop(CashMallActivity.this, mMallIndexLayout);
                break;
            //天天特价
            case R.id.cash_tt_image:
                intent.setClass(getApplicationContext(), TtActivity.class);
                bundle = new Bundle();
                bundle.putString("type", "xj_tt");
                bundle.putString("typeName", "xj");
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            //五折
            case R.id.cash_mall_left:
                intent.setClass(getApplicationContext(), TtActivity.class);
                bundle = new Bundle();
                bundle.putString("type", "xj_left");
                bundle.putString("typeName", "xj");
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            //特惠
            case R.id.cash_mall_right:
                intent.setClass(getApplicationContext(), TtActivity.class);
                bundle = new Bundle();
                bundle.putString("type", "xj_right");
                bundle.putString("typeName", "xj");
                intent.putExtras(bundle);
                startActivity(intent);
                break;
        }
    }
}
