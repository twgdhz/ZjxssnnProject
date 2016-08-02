package com.zjxfood.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
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
import com.zjxfood.adapter.FragmentPagAdapter;
import com.zjxfood.adapter.IconAdapter;
import com.zjxfood.adapter.MainViewPagerAdapter;
import com.zjxfood.adapter.MallListGridAdapter;
import com.zjxfood.common.Constants;
import com.zjxfood.fragment.MallFragment1;
import com.zjxfood.http.XutilsUtils;
import com.zjxfood.toast.MyToast;
import com.zjxfood.view.MyGridViewScroll;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/6/12.
 * 食尚币比例商城
 */
public class MallProActivity extends AppActivity implements View.OnClickListener{
    private ViewPager mImagePager;
    private ArrayList<HashMap<String, Object>> mAdvertisementList,mProList1,mProList2,mMallLists,mIconList;
    private List<View> mViewList;
    private ImageView mViewImage;
    private BitmapUtils mBitmapUtils;
    private MainViewPagerAdapter mViewPagerAdapter;
    private LinearLayout mMainLayout, mBuyBillLayout, mMyLayout,
            mMallIndexLayout,mCarsLayout;
    private MyToast mToast;
    private boolean isExit = false;
    private Bundle mBundle;
    private String mProportion="",mTypeCode="";
    private ImageView mTjImage,mLeftImage,mRightImage;
    private MallListGridAdapter mGridAdapter;
    private MyGridViewScroll mGridView;
    private IconAdapter mIconAdapter;
    private GridView mIconGrid;
    private TextView mTitleText;
    private ViewPager mViewPager;
    private ArrayList<Fragment> mFragments;
    private FragmentPagAdapter mPagerAdapter;// 类型选择适配器
    private ImageView mSearchImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mall_proportion_index_layout);
        mBundle = getIntent().getExtras();
        if(mBundle!=null){
            mProportion = mBundle.getString("proportion");
            mTypeCode = mBundle.getString("typeCode");
        }
        init();
        if(mProportion.equals("1")){
            mTitleText.setText("食尚金币v1会员区");
        }else if(mProportion.equals("2")){
            mTitleText.setText("金币商城");
        }else if(mProportion.equals("3")){
            mTitleText.setText("食尚金币v3会员区");
        }
        mToast = new MyToast(getApplicationContext());
        mBitmapUtils = BitmapUtilSingle
                .getBitmapInstance(getApplicationContext());
        getBanner();
        getBanner2();
        getBanner3();
        getMallList();
        getIcon();
        // 设置两页菜单
//        mFragments = new ArrayList<Fragment>();
//        mFragments.add(new ProMallFragment());
//        mPagerAdapter = new FragmentPagAdapter(getSupportFragmentManager(),
//                mFragments);
//        mViewPager.setAdapter(mPagerAdapter);
//        mViewPager.setOnPageChangeListener(mPageChangeListener);
    }

    private void init(){
        mSearchImage = (ImageView) findViewById(R.id.mall_title_search_image);
        mViewPager = (ViewPager) findViewById(R.id.mall_pro_viewpager);
        mImagePager = (ViewPager) findViewById(R.id.mall_pro_title_viewpager);
        FrameLayout.LayoutParams layoutParams = (android.widget.FrameLayout.LayoutParams) mImagePager
                .getLayoutParams();
        layoutParams.height = (int) (ScreenUtils.getScreenWidth(getApplicationContext())*0.32);
        mImagePager.setLayoutParams(layoutParams);
        mMainLayout = (LinearLayout) findViewById(R.id.main_home_menu_layout);
        mBuyBillLayout = (LinearLayout) findViewById(R.id.main_buy_menu_layout);
        mMyLayout = (LinearLayout) findViewById(R.id.main_my_menu_layout);
        mMallIndexLayout = (LinearLayout) findViewById(R.id.my_gift_menu_layout);
        ImageView icon = (ImageView) mMallIndexLayout.findViewById(R.id.my_gift_menu_image);
        TextView textView = (TextView) mMallIndexLayout.findViewById(R.id.my_gift_menu_text);
        icon.setImageResource(R.drawable.gift_png2);
        textView.setTextColor(getResources().getColor(R.color.main_menu_chose));
        mCarsLayout = (LinearLayout) findViewById(R.id.main_cars_layout);
        mTjImage = (ImageView) findViewById(R.id.pro_mall_tj);
        ViewGroup.LayoutParams params = mTjImage.getLayoutParams();
        params.height = DensityUtils.dp2px(getApplicationContext(),70);
        mTjImage.setLayoutParams(params);
        mIconGrid = (GridView) findViewById(R.id.pro_grid);
        mTitleText = (TextView) findViewById(R.id.gift_center_info_text);

        mLeftImage = (ImageView) findViewById(R.id.pro_mall_left);
        mRightImage = (ImageView) findViewById(R.id.pro_mall_right);
        mGridView = (MyGridViewScroll) findViewById(R.id.pro_mall_graid);

        mSearchImage.setOnClickListener(this);
        mMainLayout.setOnClickListener(this);
        mBuyBillLayout.setOnClickListener(this);
        mMyLayout.setOnClickListener(this);
        mMallIndexLayout.setOnClickListener(this);
        mCarsLayout.setOnClickListener(this);
        mTjImage.setOnClickListener(this);
        mLeftImage.setOnClickListener(this);
        mRightImage.setOnClickListener(this);
    }
    //获取分类
    private void getIcon() {
        StringBuilder sb = new StringBuilder();
        sb.append("pid=0"+"&giftTypeCode="+mTypeCode+"&buylevel="+mProportion);
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
                            handler.sendEmptyMessageDelayed(5, 0);
                        }
                    }
                });
    }
    //获取顶端图片
    private void getBanner() {
        StringBuilder sb = new StringBuilder();
        sb.append("positionCode=6&top=5");
        XutilsUtils.get(Constants.getAdListByPosition, sb,
                new RequestCallBack<String>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                    }
                    @Override
                    public void onSuccess(ResponseInfo<String> res) {
                        Log.i("banner", res.result+"==========");
                        try {
                            mAdvertisementList = Constants.getJsonArrayByData(res.result);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.i("mAdvertisementList", mAdvertisementList+"==========");
                        if(mAdvertisementList!=null && mAdvertisementList.size()>0){
                            handler.sendEmptyMessageDelayed(1, 0);
                        }
                    }
                });
    }
    //获取中端图片
    private void getBanner2() {
        StringBuilder sb = new StringBuilder();
        sb.append("positionCode=9&top=5");
        XutilsUtils.get(Constants.getAdListByPosition, sb,
                new RequestCallBack<String>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                    }
                    @Override
                    public void onSuccess(ResponseInfo<String> res) {
                        Log.i("banner", res.result+"==========");
                        try {
                            mProList1 = Constants.getJsonArrayByData(res.result);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.i("mProList1", mProList1+"==========");
                        if(mProList1!=null && mProList1.size()>0){
                            handler.sendEmptyMessageDelayed(2, 0);
                        }
                    }
                });
    }
    //获取左右图片
    private void getBanner3() {
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
                            handler.sendEmptyMessageDelayed(3, 0);
                        }
                    }
                });
    }
    //个性推荐
    private void getMallList() {
        StringBuilder sb = new StringBuilder();
        sb.append("uid="+Constants.mId+"&shop=2"+"&userBuyLevel="+mProportion);
        XutilsUtils.get(Constants.getMallLikes, sb,
                new RequestCallBack<String>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                    }
                    @Override
                    public void onSuccess(ResponseInfo<String> res) {

                        try {
                            mMallLists = Constants.getJsonArrayByData(res.result);
                            Log.i("个性推荐",mMallLists+"=================");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(mMallLists!=null && mMallLists.size()>0){
                            handler.sendEmptyMessageDelayed(4,0);
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
                    mViewList = new ArrayList<View>();
                    LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
                    View view;
                    for (int i = 0; i < mAdvertisementList.size(); i++) {
                        view = inflater.inflate(R.layout.mall_detail_viewpager_item, null);
                        mViewImage = (ImageView) view
                                .findViewById(R.id.mall_detail_viewpager_image);
                        if(!Constants.isNull(mAdvertisementList.get(i).get("Images"))){
                            mBitmapUtils.display(mViewImage, mAdvertisementList.get(i).get("Images").toString());
                        }
                        mViewList.add(view);
                    }
                    mViewPagerAdapter = new MainViewPagerAdapter(getApplicationContext(),
                            mViewList,mAdvertisementList);
                    mImagePager.setAdapter(mViewPagerAdapter);
                    Log.i("tag","获取banner图片========================="+mViewList);
                    break;
                case 2:
                    if(mProList1!=null && mProList1.size()>0 && mProList1.get(0).get("Images")!=null) {

                        mBitmapUtils.display(mTjImage, mProList1.get(0).get("Images").toString());
                    }
                    break;
                case 3:
                    if(mProList2!=null && mProList2.size()>0){
                        if(mProList2.get(0).get("Images")!=null){
                            mBitmapUtils.display(mLeftImage, mProList2.get(0).get("Images").toString());
                        }
                        if(mProList2.get(1).get("Images")!=null){
                            mBitmapUtils.display(mRightImage, mProList2.get(1).get("Images").toString());
                        }
                    }
                    break;
                //个性推荐
                case 4:
                    mGridAdapter = new MallListGridAdapter(getApplicationContext(),
                            mMallLists,"ssjb");
                    mGridView.setAdapter(mGridAdapter);
                    break;
                case 5:
                    if(mIconList!=null){
                        if(mIconList.size()>=8){
                            mIconGrid.setVisibility(View.GONE);
                            mViewPager.setVisibility(View.VISIBLE);

                            // 设置两页菜单
                            mFragments = new ArrayList<Fragment>();

                            ArrayList<HashMap<String,Object>> list1 = new ArrayList<HashMap<String, Object>>();
                            ArrayList<HashMap<String,Object>> list2 = new ArrayList<HashMap<String, Object>>();
                            for (int i=0;i<mIconList.size();i++){
                                if(i<8){
                                    list1.add(mIconList.get(i));
                                }else{
                                    list2.add(mIconList.get(i));
                                }
                            }
                            mFragments.add(new MallFragment1(list1,"ssjb","2",mProportion,"ssjb"));
                            if(list2.size()>0){
                                mFragments.add(new MallFragment1(list2,"ssjb","2",mProportion,"ssjb"));
                            }
                            mPagerAdapter = new FragmentPagAdapter(getSupportFragmentManager(),
                                    mFragments);
                            mViewPager.setAdapter(mPagerAdapter);
//                            mViewPager.setOnPageChangeListener(mPageChangeListener);
                        }else{
                            mIconGrid.setVisibility(View.VISIBLE);
                            mViewPager.setVisibility(View.GONE);
                            mIconAdapter = new IconAdapter(getApplicationContext(),mIconList);
                            mIconGrid.setAdapter(mIconAdapter);
                            mIconGrid.setOnItemClickListener(onItemClickListener);
                        }
                    }

                    break;
            }
        }
    };

    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Bundle bundle;
            Intent intent = new Intent();
            intent.setClass(getApplicationContext(), MallActivity.class);
            bundle = new Bundle();
            if(mIconList.get(i).get("code")!=null) {
                bundle.putString("group", mIconList.get(i).get("code").toString());
            }
            if(mIconList.get(i).get("name")!=null) {
                bundle.putString("titleName", mIconList.get(i).get("name").toString());
            }
            bundle.putString("type","ssjb");
            bundle.putString("shouCode","2");
            bundle.putString("proportion",mProportion);
            bundle.putString("typeName","ssjb");
            intent.putExtras(bundle);
            startActivity(intent);
        }
    };

    @Override
    public void onClick(View v) {
        int version = Integer.valueOf(android.os.Build.VERSION.SDK);
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        switch (v.getId()){
            //搜索
            case R.id.mall_title_search_image:
                intent.setClass(getApplicationContext(), MallSearchActivity.class);
                bundle = new Bundle();
                bundle.putString("group","");
                bundle.putString("type","ssjb");
                bundle.putString("shouCode","2");
                bundle.putString("proportion",mProportion);
                bundle.putString("typeName","ssjb");
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            //首页
            case R.id.main_home_menu_layout:
//                intent.setClass(getApplicationContext(), MainActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                startActivity(intent);
//                if(version>5){
//                    overridePendingTransition(R.animator.activity_zoomin, R.animator.activity_zoomout);
//                }
                break;
            //我的
            case R.id.main_my_menu_layout:
                intent.setClass(getApplicationContext(), MyNewActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                if(version>5){
                    overridePendingTransition(R.animator.activity_zoomin, R.animator.activity_zoomout);
                }
                break;
            //商城
            case R.id.my_gift_menu_layout:
                dissMallPop(MallProActivity.this,mMallIndexLayout);
                break;
            //购物车
            case R.id.main_cars_layout:
//
                break;
            //买单
            case R.id.main_buy_menu_layout:
                intent.setClass(getApplicationContext(), BuyBillActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                if(version>5){
                    overridePendingTransition(R.animator.activity_zoomin, R.animator.activity_zoomout);
                }
                break;
            //特价专区
            case R.id.pro_mall_tj:
                intent.setClass(getApplicationContext(), TtActivity.class);
                bundle = new Bundle();
                bundle.putString("type", "jb_tj");
                bundle.putString("typeName", "ssjb");
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            //大牌尝鲜
            case R.id.pro_mall_left:
                intent.setClass(getApplicationContext(), TtActivity.class);
                bundle = new Bundle();
                bundle.putString("type", "jb_dp");
                bundle.putString("attr", mProportion);
                bundle.putString("typeName", "ssjb");
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            //品质生活
            case R.id.pro_mall_right:
                intent.setClass(getApplicationContext(), TtActivity.class);
                bundle = new Bundle();
                bundle.putString("type", "jb_pz");
                bundle.putString("attr", mProportion);
                bundle.putString("typeName", "ssjb");
                intent.putExtras(bundle);
                startActivity(intent);
                break;
        }
    }
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        switch (keyCode) {
//            case KeyEvent.KEYCODE_BACK:
//                exit();
//                break;
//            default:
//                break;
//        }
//        return false;
//    }
//    private void exit() {
//        if (!isExit) {
//            isExit = true;
//            mToast.show("再按一次退出程序", 1);
//            handler.sendEmptyMessageDelayed(0, 2000);
//        } else {
//            ExitApplication.getInstance().exit();
//            // System.exit(0);
//        }
//    }
}
