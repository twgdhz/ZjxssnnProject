package com.zjxfood.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.umeng.analytics.MobclickAgent;
import com.zjxfood.adapter.FragmentPagAdapter;
import com.zjxfood.adapter.IconAdapter;
import com.zjxfood.adapter.MainViewPagerAdapter;
import com.zjxfood.adapter.MallListGridAdapter;
import com.zjxfood.application.ExitApplication;
import com.zjxfood.common.Constants;
import com.zjxfood.fragment.MallFragment1;
import com.zjxfood.http.XutilsUtils;
import com.zjxfood.toast.MyToast;
import com.zjxfood.view.MyGridViewScroll;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MallIndexActivity extends AppActivity implements
		OnClickListener {

	private ViewPager mViewPager,mImagePager;
	private FragmentPagAdapter mPagerAdapter;// 类型选择适配器
	private ArrayList<Fragment> mFragments;
	private MainViewPagerAdapter mViewPagerAdapter;
	private List<View> mViewList;
	//	private int[] mImages = { R.drawable.mall_index_banner_1,
//			R.drawable.mall_index_banner_2 };
	private ImageView mViewImage;
	private int currentItem = 0; // 当前图片的索引号
	private ScheduledExecutorService scheduledExecutorService;
	//	private ImageView[] mImageViewIds;// 小圆点ImageView数组
	private MyGridViewScroll mGridView;
	private MallListGridAdapter mGridAdapter;
	private ArrayList<HashMap<String, Object>> mLikeList;
	private LinearLayout mMainLayout, mBuyBillLayout, mMyLayout,
			mMallIndexLayout,mCarsLayout;
	private boolean isExit = false;
	private MyToast mToast;
	private ImageView mSearchImage;
	private ImageView mFanyeImage;
	private ArrayList<HashMap<String, Object>> mAdvertisementList,mIconList;
	private BitmapUtils mBitmapUtils;
	private ImageView mXinchunImage;
	private MyGridViewScroll mIconGrid;
	private IconAdapter mIconAdapter;
	private ImageView mBackImage;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mall_index_layout);
		setImmerseLayout(findViewById(R.id.head_layout));
		ExitApplication.getInstance().addMyActivity(this);
		ExitApplication.getInstance().addActivity(this);
		init();
		mBitmapUtils = BitmapUtilSingle
				.getBitmapInstance(getApplicationContext());
		mToast = new MyToast(getApplicationContext());

		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		// 当Activity显示出来后，每两秒钟切换一次图片显示
		scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 1,5,
				TimeUnit.SECONDS);

		getMallList();
		getBanner();
		getIcon();

	}

	private void init() {
		mMainLayout = (LinearLayout) findViewById(R.id.main_home_menu_layout);
		mBuyBillLayout = (LinearLayout) findViewById(R.id.main_buy_menu_layout);
		mMyLayout = (LinearLayout) findViewById(R.id.main_my_menu_layout);
		mMallIndexLayout = (LinearLayout) findViewById(R.id.my_gift_menu_layout);
		ImageView icon = (ImageView) mMallIndexLayout.findViewById(R.id.my_gift_menu_image);
		TextView textView = (TextView) mMallIndexLayout.findViewById(R.id.my_gift_menu_text);
		icon.setImageResource(R.drawable.gift_png2);
		textView.setTextColor(getResources().getColor(R.color.main_menu_chose));
		mCarsLayout = (LinearLayout) findViewById(R.id.main_cars_layout);
		mIconGrid = (MyGridViewScroll) findViewById(R.id.mall_ssb_grid);
		mViewPager = (ViewPager) findViewById(R.id.mall_ssb_viewpager);
		mBackImage = (ImageView) findViewById(R.id.gift_center_back_info_image);

//		mViewPager = (ViewPager) findViewById(R.id.mall_index_viewpager);
//		LayoutParams params = mViewPager.getLayoutParams();
//		params.height = DensityUtils.dp2px(getApplicationContext(), 190);
//		mViewPager.setLayoutParams(params);

		mImagePager = (ViewPager) findViewById(R.id.mall_index_title_viewpager);
		ViewGroup.LayoutParams layoutParams = mImagePager
				.getLayoutParams();
//		layoutParams.height = DensityUtils.dp2px(getApplicationContext(), 140);
		layoutParams.height = (int) (ScreenUtils.getScreenWidth(getApplicationContext())*0.32);
		mImagePager.setLayoutParams(layoutParams);
		mGridView = (MyGridViewScroll) findViewById(R.id.mall_index_grid_view);
		mFanyeImage = (ImageView) findViewById(R.id.mall_index_fragment_dian_image);
		mSearchImage = (ImageView) findViewById(R.id.mall_title_search_image);
		mXinchunImage = (ImageView) findViewById(R.id.mall_xin_chun_image);
		layoutParams = mXinchunImage.getLayoutParams();
		layoutParams.height = DensityUtils.dp2px(getApplicationContext(),80);
		mXinchunImage.setLayoutParams(layoutParams);
		mMainLayout.setOnClickListener(this);
		mBuyBillLayout.setOnClickListener(this);
		mMyLayout.setOnClickListener(this);
		mMallIndexLayout.setOnClickListener(this);
		mSearchImage.setOnClickListener(this);
		mXinchunImage.setOnClickListener(this);
		mCarsLayout.setOnClickListener(this);
		mBackImage.setOnClickListener(this);
	}

	OnPageChangeListener mPageChangeListener = new OnPageChangeListener() {
		@Override
		public void onPageSelected(int position) {
			switch (position) {
				case 0:
					mFanyeImage.setImageResource(R.drawable.fanye_dian);
					break;
				case 1:
					mFanyeImage.setImageResource(R.drawable.fanye_dian2);
					break;
			}
		}
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}
		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	};

	private class ScrollTask implements Runnable {
		public void run() {
			synchronized (mImagePager) {
				currentItem = (currentItem + 1) % mViewList.size();
				handler.sendEmptyMessageDelayed(1, 0); // 通过Handler切换图片
			}
		}
	}
	//获取分类
	private void getIcon() {
		StringBuilder sb = new StringBuilder();
		sb.append("pid=0"+"&giftTypeCode=1");
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
							handler.sendEmptyMessageDelayed(4, 0);
						}
					}
				});
	}


	//	private void getLikes(){
//		StringBuilder sb = new StringBuilder();
//		if (Constants.mId.equals("")) {
//			sb.append("uid=0");
//		} else {
//			sb.append("uid="+ Constants.mId);
//		}
//		XutilsUtils.get(Constants.getguessyoulike2, sb, new RequestCallBack<String>() {
//			@Override
//			public void onFailure(HttpException arg0, String arg1) {}
//
//			@Override
//			public void onSuccess(ResponseInfo<String> res) {
//				try {
//					mLikeList = Constants.getJsonArrayByData(res.result);
//					if (mLikeList != null && mLikeList.size() > 0) {
//						handler.sendEmptyMessageDelayed(2, 0);
//					}
//				} catch (JSONException e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}
//猜你喜欢
	private void getMallList() {
		StringBuilder sb = new StringBuilder();
		sb.append("uid=" + Constants.mId + "&shop=1" + "&buyLevel=1"+"&top=10");
		XutilsUtils.get(Constants.getMallLikes, sb,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
					}
					@Override
					public void onSuccess(ResponseInfo<String> res) {

						try {
							mLikeList = Constants.getJsonArrayByData(res.result);
							Log.i("猜你喜欢", mLikeList + "=================");
						} catch (JSONException e) {
							e.printStackTrace();
						}
						if (mLikeList != null && mLikeList.size() > 0) {
							handler.sendEmptyMessageDelayed(2, 0);
						}
					}
				});
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
				case 0:
					isExit = false;
					break;
				case 1:
					mImagePager.setCurrentItem(currentItem);
					break;

				case 2:
					mGridAdapter = new MallListGridAdapter(getApplicationContext(),
							mLikeList,"ssb");
					mGridView.setAdapter(mGridAdapter);
					break;
				case 3:
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
					break;
				case 4:
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
					mFragments.add(new MallFragment1(list1,"ssb","1","","ssb"));
					if(list2.size()>0){
						mFragments.add(new MallFragment1(list2,"ssb","1","","ssb"));
					}
					mPagerAdapter = new FragmentPagAdapter(getSupportFragmentManager(),
							mFragments);
					mViewPager.setAdapter(mPagerAdapter);
					mViewPager.setOnPageChangeListener(mPageChangeListener);
					break;
			}
		};
	};

	AdapterView.OnItemClickListener onItemClickListener2 = new AdapterView.OnItemClickListener() {
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
			bundle.putString("type","ssb");
			bundle.putString("shouCode","1");
			intent.putExtras(bundle);
			startActivity(intent);
		}
	};

	@Override
	public void onClick(View v) {
		int version = Integer.valueOf(android.os.Build.VERSION.SDK);
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		switch (v.getId()) {
			//返回
			case R.id.gift_center_back_info_image:
				finish();
				break;
			//新春礼包
			case R.id.mall_xin_chun_image:
				bundle = new Bundle();
				intent.setClass(getApplicationContext(), MallActivity.class);
				bundle.putString("group", "J");
				bundle.putString("titleName", "新春礼包");
				intent.putExtras(bundle);
				startActivity(intent);
				break;
			//搜索
			case R.id.mall_title_search_image:
				intent.setClass(getApplicationContext(), MallSearchActivity.class);
				bundle = new Bundle();
				bundle.putString("group","");
				bundle.putString("type","ssb");
				bundle.putString("shouCode","1");
				bundle.putString("proportion","");
				bundle.putString("typeName","ssb");
				intent.putExtras(bundle);
				startActivity(intent);
				break;
			case R.id.main_home_menu_layout:
//				intent.setClass(getApplicationContext(), MainActivity.class);
//				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//				intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//				startActivity(intent);
//				if(version>5){
//					overridePendingTransition(R.animator.activity_zoomin, R.animator.activity_zoomout);
//				}
				break;
//购物车
			case R.id.main_cars_layout:
//
				break;
			case R.id.main_buy_menu_layout:
				intent.setClass(getApplicationContext(), BuyBillActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				if(version>5){
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
				if(version>5){
					overridePendingTransition(R.animator.activity_zoomin, R.animator.activity_zoomout);
				}
//			finish();
				break;
			case R.id.my_gift_menu_layout:
				dissMallPop(MallIndexActivity.this,mMallIndexLayout);
				break;
		}
	}

//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		switch (keyCode) {
//			case KeyEvent.KEYCODE_BACK:
//				exit();
//				// finish();
//				break;
//			default:
//				break;
//		}
//		return false;
//	}
//
//	private void exit() {
//		if (!isExit) {
//			isExit = true;
//			mToast.show("再按一次退出程序", 1);
//			handler.sendEmptyMessageDelayed(0, 2000);
//		} else {
//			ExitApplication.getInstance().exit();
//		}
//	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mToast.cancel();
		MobclickAgent.onPause(this);
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);
	}
	//获取顶端图片
	private void getBanner() {
		StringBuilder sb = new StringBuilder();
		sb.append("positionCode=2&top=4");
		XutilsUtils.get(Constants.getAdListByPosition, sb,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
					}
					@Override
					public void onSuccess(ResponseInfo<String> res) {
						try {
							mAdvertisementList = Constants.getJsonArrayByData(res.result);
						} catch (JSONException e) {
							e.printStackTrace();
						}
						Log.i("mAdvertisementList", mAdvertisementList+"==========");
						if(mAdvertisementList!=null && mAdvertisementList.size()>0){
							handler.sendEmptyMessageDelayed(3, 0);
						}
					}
				});
	}
}
