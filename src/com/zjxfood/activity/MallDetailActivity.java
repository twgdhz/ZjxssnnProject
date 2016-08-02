package com.zjxfood.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.appkefu.lib.interfaces.KFAPIs;
import com.appkefu.lib.service.KFMainService;
import com.appkefu.lib.service.KFXmppManager;
import com.appkefu.lib.utils.KFLog;
import com.appkefu.smack.util.StringUtils;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.project.util.ScreenUtils;
import com.tencent.tauth.Tencent;
import com.umeng.analytics.MobclickAgent;
import com.zjxfood.adapter.MallDetailGridAdapter;
import com.zjxfood.adapter.MallDetailViewPagerAdapter;
import com.zjxfood.adapter.MallParameterAdapter;
import com.zjxfood.application.ExitApplication;
import com.zjxfood.common.Constants;
import com.zjxfood.fragment.MallParameterFragment;
import com.zjxfood.http.AsyUtils;
import com.zjxfood.http.XutilsUtils;
import com.zjxfood.interfaces.NotifyMallDetailImpl;
import com.zjxfood.interfaces.NotifyMallDetailInterface;
import com.zjxfood.popupwindow.JhPopupWindow;
import com.zjxfood.popupwindow.MallSizePopupWindow;
import com.zjxfood.popupwindow.ServiceCommitmentPopupWindow;
import com.zjxfood.qq.Util;
import com.zjxfood.view.ScrollViewContainer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 商城详情页面
 * @author zjx
 *
 */
public class MallDetailActivity extends FragmentActivity implements
		OnClickListener {
	private LinearLayout mCheckSizeLayout;// 查看颜色尺码
	private LinearLayout mMallIntroductionLayout;// 产品介绍
	private LinearLayout mAlertLayout;
	private MallDetailGridAdapter mGridAdapter;
	private Bundle mBundle;
	private static String price, title, yf, LoginImage = "", sale, dhnumber,
			money, giftId, id = "";// 价格、内容、运费、图片路径、销量、商品Id
	private TextView mMallName;// 商品名称
	private TextView mMarketValueText;// 市场价
	private TextView mMemberPriceText;// 会员价
	private TextView mKuaidiText;// 快递
	private TextView mStockText;// 库存
	private TextView mSalesText;// 销量
	private TextView mUserName;// 收货人姓名
	private TextView mUserPhone;// 收货人电话
	private TextView mUserAddress;// 收货人地址
	private ArrayList<HashMap<String, Object>> mDefaulAddressList;
	private Button mModifyAddressBtn;
	private String addressId = "";// 收货地址Id
	// private String userName = "", userPhone = "", userAddress = "";
	private String otherYf = "0";// 额外运费
	private Button mConfirmBtn;
	private String mShmoney = "0";
	private PopupWindow mJhPopupWindow;
	private String mJhMoney = "";// 激活金额
	private static String mSizeId = "";
	private String sizeId = "";// 选择中的尺码
	private ArrayList<HashMap<String, Object>> mSizeList;// 商品尺码list
	private ImageView mMallTitleImage;
//	private ArrayList<HashMap<String, Object>> mDetailList;// 商品详情list
	private ArrayList<String> mArrayBitmaps;
	private ImageView mBackImage;
	private Button mSizeChoseBtn;
	private int mSizePosition;
	private String mOldPosition;
	private boolean isConfirmBuy = false;
	private static ArrayList<String> mImageList;
	private ViewPager mViewPager;
	private List<View> mViewList;

	private ImageView mViewImage;
	private MallDetailViewPagerAdapter mViewPagerAdapter;
	private int currentItem = 0; // 当前图片的索引号
	private ScheduledExecutorService scheduledExecutorService;
	// private MallSizePopupWindow mSizePopupWindow;
	private View view;
	private BitmapUtils mBitmapUtils;
	// private boolean isJhPup = true;
	private static String sizeString;
	private TextView mServiceCommitmentText;
	private ServiceCommitmentPopupWindow mCommitmentPopupWindow;
	private int data = 0;
	private JhPopupWindow mJhPupWindow;
	private LinearLayout mContactCustomerLayout;// 联系客户
	public static String mAppid;
	public static Tencent mTencent;
//	private LinearLayout mQqLayout1, mQqLayout2;
	private LinearLayout mGallertImageLayout;
	private ImageView[] mImageViewIds;// 小圆点ImageView数组
	private HashMap<String, Object> mIsFavoriteMap;
	private HashMap<String, Object> mAddFavoriteMap;
	private HashMap<String, Object> mDeleteFavoriteMap;
	private ImageView mFavoriteImage;
	private TextView mFavoriteText;
	private RelativeLayout mFavoriteLayout;
	private boolean isFavorite = true;
	private HashMap<String, Object> mIsUserBuyMap;
	private TextView mGraphicDetails, mProductParameters;
	private View mGraphicView, mProductView;
//	private LinearLayout mProductLayout;
	private WebView mWebView;
	private LinearLayout mGraphiContentLayout, mProductContentLayout;
//	private MyScrollListView mParameterList;
	private ArrayList<HashMap<String, Object>> mParamterArrayList;
	private MallParameterAdapter mParamterAdapter;
	private Button mCommoditySpecificationsBtn;
	private MallSizePopupWindow mSizePopupWindow;
	private NotifyMallDetailImpl mNotifyMallDetailImpl;
	// private MallBuyWindow mBuyWindow;
//	private RelativeLayout mBottomLayout;
	public static String mFlag = "1";
	private TextView mShowText;
	private WebView mDetailWeb;
	private ListView mScrollListView;
	private LinearLayout mDetailLayout,mWebLayout,mParameterLayout;
	private View mDetailView,mWebviewView,mParameterView;
	private ScrollView mScrollViewContainer;
	private LinearLayout mParameterConLayout,mWebConLayout;
	private TextView mParameterShow;
//	private ScrollView mParameterScroll;
	private TextView mTypeText1,mTypeText2,mTypeText3;
//	private String mLoginImage;
	private HashMap<String,Object> mShMaps;
	private static String mTypeName;
	private TextView mSjName,mBlText;
	private HashMap<String,Object> mDetailMap,mCarsDetailMap;
	private TextView mTitleText;
	private String mMallDetail = "商品信息";
	private RelativeLayout mKefuLayout;
	private  HashMap<String,Object> mCarsList;
	private TextView mGuigeText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mall_detail_new_layout);

		setImmerseLayout(findViewById(R.id.mall_detail_id));
		mBitmapUtils = new BitmapUtils(getApplicationContext());
		ExitApplication.getInstance().addActivity(this);
		ExitApplication.getInstance().addMallActivity(this);
		mFlag = "1";
		init();
		mNotifyMallDetailImpl = new NotifyMallDetailImpl();
		mNotifyMallDetailImpl.setListener(mInterface);
		checkTencentInstance();
		mBundle = getIntent().getExtras();
		if (mBundle != null) {
			if (mBundle.getString("flag").equals("1")) {
				mTypeName = mBundle.getString("typeName");
				sizeString = null;
				mSizeId = "";
				mImageList = new ArrayList<String>();
				price = mBundle.getString("Price");
				title = mBundle.getString("Title");
				yf = mBundle.getString("Yf");
				Log.i("运费",yf+"====================");
				dhnumber = mBundle.getString("Dhnumber");

				LoginImage = mBundle.getString("Image1");
				id = mBundle.getString("Id");
				sale = mBundle.getString("sale");
				money = mBundle.getString("money");
				giftId = mBundle.getString("giftId");
				if (mBundle.getString("Image1") != null
						&& !(mBundle.getString("Image1").equals(""))) {
					mImageList.add(LoginImage);
				}
				if (!(mBundle.getString("titleImage1").equals(""))) {
					mImageList.add(mBundle.getString("titleImage1"));
				}
				if (!(mBundle.getString("titleImage2").equals(""))) {
					mImageList.add(mBundle.getString("titleImage2"));
				}
				if (!(mBundle.getString("titleImage3").equals(""))) {
					mImageList.add(mBundle.getString("titleImage3"));
				}
			}
		}
		mViewList = new ArrayList<View>();
		LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
		if (mImageList != null && mImageList.size() > 0) {
			mGallertImageLayout.removeAllViews();
			mImageViewIds = new ImageView[mImageList.size()];
			for (int i = 0; i < mImageList.size(); i++) {
				view = inflater.inflate(R.layout.mall_detail_viewpager_item,
						null);
				mViewImage = (ImageView) view
						.findViewById(R.id.mall_detail_viewpager_image);
				mBitmapUtils.display(mViewImage, mImageList.get(i));
				mViewList.add(view);

				ImageView imageView = new ImageView(getApplicationContext());
				imageView.setImageResource(R.drawable.ic_dot_true);
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);
				lp.setMargins(0, 0, 10, 0);
				imageView.setLayoutParams(lp);
				mGallertImageLayout.addView(imageView);
				mImageViewIds[i] = imageView;
			}
		}
		if (mImageViewIds != null && mImageViewIds.length > 0) {
			mImageViewIds[0].setImageDrawable(getBaseContext().getResources()
					.getDrawable(R.drawable.ic_dot_fouc));
		}
		mViewPagerAdapter = new MallDetailViewPagerAdapter(
				getApplicationContext(), mViewList, mImageList);
		mViewPager.setAdapter(mViewPagerAdapter);
		mViewPager.setOnPageChangeListener(new MyPageChangeListener());
		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		// 当Activity显示出来后，每两秒钟切换一次图片显示
		scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 1, 3,
				TimeUnit.SECONDS);
		// getMallSize();
		// mExecutorService.execute(sizeRun);
		getMallDetail();
		setResource();
		Log.i("sizeString", sizeString + "===========" + mSizeId);
//		if (sizeString != null && !(mSizeId.equals(""))) {
//			mSizeChoseBtn.setText(sizeString);
//			mSizeChoseBtn.setVisibility(View.VISIBLE);
//		}
		IntentFilter filter = new IntentFilter(NewAddressActivity.action);
		// filter.addAction(NewAddressActivity.action);
		filter.addAction(MallParameterFragment.action);
		filter.addAction(ScrollViewContainer.action);
		registerReceiver(mBroadcastReceiver, filter);
		if (Constants.onLine == 1) {
			isFavorite();
		}
		getMallValue();
//		getSizeHttp();
		SharedPreferences sp;
		if(mTypeName.equals("ssb")){
			sp = getApplicationContext().getSharedPreferences("食尚币商城购买须知", MODE_PRIVATE);
			if (sp != null && sp.getString("Content", "") != null
					&& !sp.getString("Content", "").toString().equals("")) {
				mShowText.setText(sp.getString("Content", ""));
			}
		}else{
			sp = getApplicationContext().getSharedPreferences("非食尚币商城购物须知", MODE_PRIVATE);
			if (sp != null && sp.getString("Content", "") != null
					&& !sp.getString("Content", "").toString().equals("")) {
				mShowText.setText(sp.getString("Content", ""));
			}
		}

		sp = getApplicationContext().getSharedPreferences("商品参数", MODE_PRIVATE);
		if(sp!=null && sp.getString("Content", "")!=null && !sp.getString("Content", "").toString().equals("")){
			mParameterShow.setText(sp.getString("Content", ""));
		}
		//
	}

	private void init() {
		mBlText = (TextView) findViewById(R.id.mall_detail_member_price_value_text3);
		mSjName = (TextView) findViewById(R.id.mall_detail_member_price_value_text2);
		mCheckSizeLayout = (LinearLayout) findViewById(R.id.mall_detail_color_layout);
		mAlertLayout = (LinearLayout) findViewById(R.id.mall_detail_not_log_alert_view);
		mMallName = (TextView) findViewById(R.id.mall_detail_name_text);
		mMarketValueText = (TextView) findViewById(R.id.mall_detail_market_value);
		mMemberPriceText = (TextView) findViewById(R.id.mall_detail_member_price_value_text);
		mKuaidiText = (TextView) findViewById(R.id.mall_detail_express_value);
		mStockText = (TextView) findViewById(R.id.mall_detail_stock);
		mSalesText = (TextView) findViewById(R.id.mall_detail_sales);
		mUserName = (TextView) findViewById(R.id.mall_detail_receiving_name);
		mUserPhone = (TextView) findViewById(R.id.mall_detail_receiving_phone);
		mUserAddress = (TextView) findViewById(R.id.mall_detail_receiving_address);
		mModifyAddressBtn = (Button) findViewById(R.id.mall_detail_receiving_modity_btn);
		mMallIntroductionLayout = (LinearLayout) findViewById(R.id.mall_detail_introduction_layout);
		mConfirmBtn = (Button) findViewById(R.id.mall_detail_confirm_buy_btn);
		mMallTitleImage = (ImageView) findViewById(R.id.mall_detail_title_image);
//		mBackImage = (ImageView) findViewById(R.id.title_back_image);
		mBackImage = (ImageView) findViewById(R.id.title_back_image);
		mTitleText = (TextView) findViewById(R.id.title_text);
		mTitleText.setText("商品详情");
		mSizeChoseBtn = (Button) findViewById(R.id.mall_detail_size_chose_btn);
		mServiceCommitmentText = (TextView) findViewById(R.id.srvice_commitment_text);
		mGallertImageLayout = (LinearLayout) findViewById(R.id.mall_detail_view_image_layout);
		mViewPager = (ViewPager) findViewById(R.id.mall_detail_view_pager);
		mContactCustomerLayout = (LinearLayout) findViewById(R.id.mall_detail_contact_customer_layout);
//		mQqLayout1 = (LinearLayout) findViewById(R.id.mall_contact_list_layout);
//		mQqLayout2 = (LinearLayout) findViewById(R.id.mall_contact_list_layout2);
		mFavoriteImage = (ImageView) findViewById(R.id.mall_detail_favorite_image);
		mFavoriteText = (TextView) findViewById(R.id.mall_detail_favorite_text);
		mFavoriteLayout = (RelativeLayout) findViewById(R.id.mall_detail_favorite_layout);

		mGraphicDetails = (TextView) findViewById(R.id.mall_introduction_graphic_details);
		mProductParameters = (TextView) findViewById(R.id.mall_introduction_product_parameters);
		mGraphicView = findViewById(R.id.mall_introduction_graphic_details_view);
		mProductView = findViewById(R.id.mall_introduction_product_parameters_view);
//		mGraphiLayout = (LinearLayout) findViewById(R.id.mall_detail_introduction_head_child_layout1);
//		mProductLayout = (LinearLayout) findViewById(R.id.mall_detail_introduction_head_child_layout2);
		mWebView = (WebView) findViewById(R.id.mall_introduction_webview);
		mGraphiContentLayout = (LinearLayout) findViewById(R.id.bottom_scroll_graphic_details);
		mProductContentLayout = (LinearLayout) findViewById(R.id.bottom_scroll_product_parameters);
//		mParameterList = (MyScrollListView) findViewById(R.id.mall_detail_parameter_list);
		mCommoditySpecificationsBtn = (Button) findViewById(R.id.mall_detail_commodity_specifications_btn);
//		mBottomLayout = (RelativeLayout) findViewById(R.id.mall_detail_bottom_layout);
		mShowText = (TextView) findViewById(R.id.mall_detail_buy_purchase_show);
		
		mDetailLayout = (LinearLayout) findViewById(R.id.mall_content_detail_layout);
		mWebLayout = (LinearLayout) findViewById(R.id.mall_content_left_layout);
		mParameterLayout = (LinearLayout) findViewById(R.id.mall_content_right_layout);
		mDetailView = findViewById(R.id.mall_content_detail_view);
		mWebviewView = findViewById(R.id.mall_content_left_view);
		mParameterView = findViewById(R.id.mall_content_right_view);
		mScrollViewContainer = (ScrollView) findViewById(R.id.mall_detail_content_scroll);
		mDetailWeb = (WebView) findViewById(R.id.mall_detail_webview);
		mScrollListView = (ListView) findViewById(R.id.mall_detail_parameter_lists);
		mParameterConLayout = (LinearLayout) findViewById(R.id.mall_detail_parameter_layout);
		mParameterShow = (TextView) findViewById(R.id.mall_detail_parameter_show);
		mWebConLayout = (LinearLayout) findViewById(R.id.mall_detail_webview_layout);
//		mParameterScroll = (ScrollView) findViewById(R.id.mall_scroll_detail_parameter_layout);
		mTypeText1 = (TextView) findViewById(R.id.mall_content_detail_text);
		mTypeText2 = (TextView) findViewById(R.id.mall_content_left_text);
		mTypeText3 = (TextView) findViewById(R.id.mall_content_right_text);
		mKefuLayout = (RelativeLayout) findViewById(R.id.mall_detail_kefu_layout);
		mGuigeText = (TextView) findViewById(R.id.mall_detail_guige_text);
		
		ViewGroup.LayoutParams layoutParams = (ViewGroup.LayoutParams) mViewPager
				.getLayoutParams();
		layoutParams.height = ScreenUtils
				.getScreenWidth(getApplicationContext());
		mViewPager.setLayoutParams(layoutParams);

		mDetailLayout.setOnClickListener(this);
		mWebLayout.setOnClickListener(this);
		mParameterLayout.setOnClickListener(this);
		
//		mBottomLayout.setOnClickListener(this);
		mCommoditySpecificationsBtn.setOnClickListener(this);
//		mGraphiLayout.setOnClickListener(this);
//		mProductLayout.setOnClickListener(this);
		// mSizeLayout.setOnClickListener(this);

		mFavoriteLayout.setOnClickListener(this);
//		mQqLayout1.setOnClickListener(this);
//		mQqLayout2.setOnClickListener(this);
		mContactCustomerLayout.setOnClickListener(this);
		mServiceCommitmentText.setOnClickListener(this);
		mBackImage.setOnClickListener(this);
		mCheckSizeLayout.setOnClickListener(this);
		mAlertLayout.setOnClickListener(this);
		mModifyAddressBtn.setOnClickListener(this);
		mMallIntroductionLayout.setOnClickListener(this);
		mConfirmBtn.setOnClickListener(this);
		mKefuLayout.setOnClickListener(this);
	}

	private void setResource() {
		mMallName.setText(title);
		if (money != null) {
			Double moneys = Double.parseDouble(money + "");
			mMarketValueText.setText("￥"
					+ new java.text.DecimalFormat("#.00").format(moneys) + "");
		}
		Log.i("设置价格","========================="+price+"==="+mTypeName);
		mMarketValueText.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); // 中间横线
		if (price != null) {

			Double pri = Double.parseDouble(price);
			if(mTypeName!=null){
				if(mTypeName.equals("xj")){
					mMemberPriceText.setText("￥"+new java.text.DecimalFormat("#.00")
							.format(pri) + "");
					mSjName.setVisibility(View.GONE);
				}else if(mTypeName.equals("ssb")){
					mMemberPriceText.setText(new java.text.DecimalFormat("#.00")
							.format(pri) + "");
					mSjName.setVisibility(View.VISIBLE);
					mSjName.setText("食尚币");
				}else if(mTypeName.equals("ssjb")){
					mMemberPriceText.setText("￥"+new java.text.DecimalFormat("#.00")
							.format(pri) + "");
					mSjName.setVisibility(View.GONE);
				}
			}

		}
		mSalesText.setText("销量：" + sale);
		mStockText.setText("库存：" + dhnumber + "件");
		String yfs = Float.parseFloat(yf) + "";

		Double Yf = Double.parseDouble(yfs + "");
		mKuaidiText.setText(new java.text.DecimalFormat("#.00").format(Yf)
				+ "元");
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		Bundle mBundle = new Bundle();
		switch (v.getId()) {
		//商品详情
		case R.id.mall_content_detail_layout:
			mDetailView.setVisibility(View.VISIBLE);
			mWebviewView.setVisibility(View.GONE);
			mParameterView.setVisibility(View.GONE);
			mScrollViewContainer.setVisibility(View.VISIBLE);
			mWebConLayout.setVisibility(View.GONE);
			mParameterConLayout.setVisibility(View.GONE);
			mTypeText1.setTextColor(getResources().getColor(R.color.back));
			mTypeText2.setTextColor(getResources().getColor(R.color.my_user_decription2));
			mTypeText3.setTextColor(getResources().getColor(R.color.my_user_decription2));
			break;
			//图文详情
		case R.id.mall_content_left_layout:
			mDetailView.setVisibility(View.GONE);
			mWebviewView.setVisibility(View.VISIBLE);
			mParameterView.setVisibility(View.GONE);
			mScrollViewContainer.setVisibility(View.GONE);
			mWebConLayout.setVisibility(View.VISIBLE);
			mParameterConLayout.setVisibility(View.GONE);
			mTypeText1.setTextColor(getResources().getColor(R.color.my_user_decription2));
			mTypeText2.setTextColor(getResources().getColor(R.color.back));
			mTypeText3.setTextColor(getResources().getColor(R.color.my_user_decription2));
			break;
			//商品参数
		case R.id.mall_content_right_layout:
			mDetailView.setVisibility(View.GONE);
			mWebviewView.setVisibility(View.GONE);
			mParameterView.setVisibility(View.VISIBLE);
			mScrollViewContainer.setVisibility(View.GONE);
			mWebConLayout.setVisibility(View.GONE);
			mParameterConLayout.setVisibility(View.VISIBLE);
			mTypeText1.setTextColor(getResources().getColor(R.color.my_user_decription2));
			mTypeText2.setTextColor(getResources().getColor(R.color.my_user_decription2));
			mTypeText3.setTextColor(getResources().getColor(R.color.back));
			break;

		// 商品规格
		case R.id.mall_detail_commodity_specifications_btn:
			int version = Integer.valueOf(android.os.Build.VERSION.SDK);
			intent.setClass(getApplicationContext(), NewCarsActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("flag","car");
			intent.putExtras(bundle);
//			intent.setClass(getApplicationContext(), NewMallActivity.class);
//			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
//			if (version > 5) {
//				overridePendingTransition(R.animator.activity_zoomin, R.animator.activity_zoomout);
//			}
//			if (mSizeList != null && mSizeList.size() > 0) {
//				try{
//				mGridAdapter = new MallDetailGridAdapter(
//						getApplicationContext(), mSizeList);
//					if(null != mOldPosition) {
//						mGridAdapter.setSeclection(Integer.parseInt(mOldPosition));
//					}
//				mSizePopupWindow = new MallSizePopupWindow(
//						MallDetailActivity.this, mOnItemClickListener,
//						mGridAdapter, itemsOnClick);
//				mSizePopupWindow.showAtLocation(MallDetailActivity.this
//						.findViewById(R.id.mall_detail_id), Gravity.BOTTOM
//						| Gravity.CENTER_HORIZONTAL, 0, 0);
//				}catch(Exception e){
//					e.printStackTrace();
//				}
//			} else {
//				Toast.makeText(getApplicationContext(), "该商品没有规格可选！",
//						Toast.LENGTH_SHORT).show();
//			}
			break;
		case R.id.mall_detail_introduction_head_child_layout1:
			mGraphicView.setVisibility(View.VISIBLE);
			mProductView.setVisibility(View.GONE);
			mGraphicDetails.setTextColor(getResources().getColor(R.color.red));
			mProductParameters.setTextColor(getResources().getColor(
					R.color.service_provider_color));
			mGraphiContentLayout.setVisibility(View.VISIBLE);
			mProductContentLayout.setVisibility(View.GONE);
			break;
		case R.id.mall_detail_introduction_head_child_layout2:
			mGraphicView.setVisibility(View.GONE);
			mProductView.setVisibility(View.VISIBLE);
			mGraphicDetails.setTextColor(getResources().getColor(
					R.color.service_provider_color));
			mProductParameters.setTextColor(getResources()
					.getColor(R.color.red));
			mGraphiContentLayout.setVisibility(View.GONE);
			mProductContentLayout.setVisibility(View.VISIBLE);
			break;
		case R.id.mall_detail_introduction_head_child_layout3:
			// mBottomPager.setCurrentItem(2);
			mGraphicView.setVisibility(View.GONE);
			mProductView.setVisibility(View.GONE);
			// mSizeView.setVisibility(View.VISIBLE);
			mGraphicDetails.setTextColor(getResources().getColor(
					R.color.service_provider_color));
			mProductParameters.setTextColor(getResources().getColor(
					R.color.service_provider_color));
			// mSizeText.setTextColor(getResources().getColor(R.color.red));
			break;
		// 收藏
		case R.id.mall_detail_favorite_layout:
			Log.i("isFavorite", isFavorite + "===========");
			if (Constants.onLine == 1) {
				if (!isFavorite) {
					addFavorite();
				} else {
					cancelFavorite();
				}
			} else {
				Toast.makeText(getApplicationContext(), "请先登录帐号",
						Toast.LENGTH_SHORT).show();
			}
			break;
		// 联系QQ1
		case R.id.mall_detail_kefu_layout:
			startChat();
//			String uin = "2924007010";
//			if (!"".equals(uin)) {
//				int ret = mTencent.startWPAConversation(
//						MallDetailActivity.this, uin, "");
//				if (ret != 0) {
//					Toast.makeText(getApplicationContext(),
//							"start WPA conversation failed. error:" + ret,
//							Toast.LENGTH_LONG).show();
//				}
//			}
			break;
		// 联系QQ2
		case R.id.mall_contact_list_layout2:
//			uin = "3289142544";
//			if (!"".equals(uin)) {
//				int ret = mTencent.startWPAConversation(
//						MallDetailActivity.this, uin, "");
//				if (ret != 0) {
//					Toast.makeText(getApplicationContext(),
//							"start WPA conversation failed. error:" + ret,
//							Toast.LENGTH_LONG).show();
//				}
//			}
			break;
		// 选择商品规格
		case R.id.mall_detail_contact_customer_layout:
			if (mSizeList != null && mSizeList.size() > 0) {
				try{
					mGridAdapter = new MallDetailGridAdapter(
							getApplicationContext(), mSizeList);
					if(null != mOldPosition) {
						mGridAdapter.setSeclection(Integer.parseInt(mOldPosition));
					}
					mSizePopupWindow = new MallSizePopupWindow(
							MallDetailActivity.this, mOnItemClickListener,
							mGridAdapter, itemsOnClick);
					mSizePopupWindow.showAtLocation(MallDetailActivity.this
							.findViewById(R.id.mall_detail_id), Gravity.BOTTOM
							| Gravity.CENTER_HORIZONTAL, 0, 0);
				}catch(Exception e){
					e.printStackTrace();
				}
			} else {
				Toast.makeText(getApplicationContext(), "该商品没有规格可选！",
						Toast.LENGTH_SHORT).show();
			}
			break;
		// 服务承诺
		case R.id.srvice_commitment_text:
//			mCommitmentPopupWindow = new ServiceCommitmentPopupWindow(
//					MallDetailActivity.this);
//			mCommitmentPopupWindow.showAtLocation(mViewPager, Gravity.CENTER,
//					0, 0);
			break;
		// 点击标题图片放大显示
		case R.id.mall_detail_title_image:

			break;
		// 选择颜色、尺码
		case R.id.mall_detail_color_layout:
//			handler.sendEmptyMessageDelayed(6, 0);
			break;

		case R.id.mall_detail_not_log_alert_view:
			break;
		// 填写/更换地址
		case R.id.mall_detail_receiving_modity_btn:
			if (Constants.onLine == 1) {
				if (mModifyAddressBtn.getText().equals("去填写")) {
					intent.setClass(getApplicationContext(),
							NewAddressActivity.class);
					bundle = new Bundle();
					bundle.putString("flag", "1");
					intent.putExtras(bundle);
					startActivity(intent);
				} else if (mModifyAddressBtn.getText().equals("修改")) {
					intent.setClass(getApplicationContext(),
							NewAddressManageActivity.class);
					bundle = new Bundle();
					bundle.putString("flag", "1");
					intent.putExtras(bundle);
					startActivity(intent);
				}
			} else {
				Toast.makeText(getApplicationContext(), "请先登录帐号！",
						Toast.LENGTH_SHORT).show();
			}
			break;
		// 产品介绍
		case R.id.mall_detail_introduction_layout:
			if (mDetailMap != null && mDetailMap.size() > 0) {
				if (mDetailMap.get("detail") != null) {
					intent.setClass(getApplicationContext(),
							MallDetailContentActivity.class);
					mBundle = new Bundle();
					mBundle.putString("detail", mDetailMap
							.get("detail").toString());
					mBundle.putString("gid", giftId);
					intent.putExtras(mBundle);
					startActivity(intent);
				} else {
					Toast.makeText(getApplicationContext(), "该商品暂时没有更多介绍！",
							Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(getApplicationContext(), "该商品暂时没有更多介绍！",
						Toast.LENGTH_SHORT).show();
			}
			break;
		// 确认购买 加入购物车
		case R.id.mall_detail_confirm_buy_btn:
//			if(Constants.onLine==1 && !Constants.mId.equals("0")) {
				// 判断是否有可选择的尺码规格
				boolean isBuy = true;
				if (mSizeList != null && mSizeList.size() > 0) {
					if (mSizeId.equals("")) {
						isBuy = false;
					} else {
						isBuy = true;
					}
				}
				if(mSizeList!=null && mSizeList.size()>0) {
					if (isConfirmBuy) {
						if (isBuy) {
							mCarsList = Constants.mCarsMap.get(mSizeList.get(mSizePosition).get("Id").toString());
							Log.i("尺码情况", mCarsDetailMap.get("chima") + "========" + mSizePosition + "====尺码ID：" + mCarsDetailMap);
							if (mCarsList == null) {
								mCarsList = new HashMap<>();
								mCarsList = mCarsDetailMap;
								mCarsList.put("quantity", 1);
							} else {
								int a = Integer.parseInt(mCarsList.get("quantity").toString());
								a++;
								mCarsList.put("quantity", a);
							}
							Log.i("尺码情况2", mSizeList.get(mSizePosition).get("Id").toString() + "========" + mCarsList);
							Constants.mCarsMap.put(mSizeList.get(mSizePosition).get("Id").toString(), mCarsList);
							Toast.makeText(getApplicationContext(), "加入购物车成功", Toast.LENGTH_SHORT).show();
							SharedPreferences sp = getApplication().getSharedPreferences("cars", Context.MODE_PRIVATE);
							SharedPreferences.Editor editor = sp.edit();
							editor.putString("cars", com.alibaba.fastjson.JSONObject.toJSON(Constants.mCarsMap).toString());
							editor.commit();
//							for (Map.Entry<String, HashMap<String, Object>> entry : Constants.mCarsMap.entrySet()) {
//								editor.putString(entry.getKey(), entry.getValue().toString());
//							}
						} else {
							Toast.makeText(getApplicationContext(), "请选择商品规格", Toast.LENGTH_SHORT).show();
						}
					} else {
						Toast.makeText(getApplicationContext(), "网速较慢，请稍后再试", Toast.LENGTH_SHORT).show();
					}
				}else {
					mCarsDetailMap = new HashMap<String, Object>();
					if(mDetailMap!=null && mDetailMap.size()>0) {
						mCarsDetailMap.put("ProductName", mDetailMap.get("Title"));
						mCarsDetailMap.put("ProductId", mDetailMap.get("Id"));
						mCarsDetailMap.put("Price", mDetailMap.get("BuyingPrice"));
						mCarsDetailMap.put("Image", mDetailMap.get("Image"));
						mCarsDetailMap.put("chima", "");
						mCarsDetailMap.put("isCheck", "true");
						mCarsDetailMap.put("isTrue", "true");
						mCarsDetailMap.put("userId", Constants.mId);
						mCarsDetailMap.put("AttrId", mDetailMap.get("Id"));
						mCarsDetailMap.put("Yf", mDetailMap.get("Yf"));
						mCarsDetailMap.put("Dhnumber", mDetailMap.get("Dhnumber"));
						mCarsDetailMap.put("titleImage1", mDetailMap.get("image1"));
						mCarsDetailMap.put("titleImage2", mDetailMap.get("image2"));
						mCarsDetailMap.put("titleImage3", mDetailMap.get("image3"));
						mCarsDetailMap.put("Salenumber", mDetailMap.get("Salenumber"));
						mCarsDetailMap.put("Money", mDetailMap.get("Money"));
						mCarsDetailMap.put("Content", mDetailMap.get("Content"));
						if(mDetailMap.get("GiftType").toString().equals("1")){
							mCarsDetailMap.put("typeName", "ssb");
						}else if(mDetailMap.get("GiftType").toString().equals("2")){
							mCarsDetailMap.put("typeName", "ssjb");
						}else if(mDetailMap.get("GiftType").toString().equals("3")){
							mCarsDetailMap.put("typeName", "xj");
						}


						mCarsList = Constants.mCarsMap.get(mDetailMap.get("Id"));
						if (mCarsList == null) {
							mCarsList = new HashMap<>();
							mCarsList = mCarsDetailMap;
							mCarsList.put("quantity", 1);
						} else {
							int a = Integer.parseInt(mCarsList.get("quantity").toString());
							a++;
							mCarsList.put("quantity", a);
						}
						Constants.mCarsMap.put(mDetailMap.get("Id")+"", mCarsList);
						Toast.makeText(getApplicationContext(), "加入购物车成功", Toast.LENGTH_SHORT).show();
						SharedPreferences sp = getApplication().getSharedPreferences("cars", Context.MODE_PRIVATE);
						SharedPreferences.Editor editor = sp.edit();
						editor.putString("cars", com.alibaba.fastjson.JSONObject.toJSON(Constants.mCarsMap).toString());
						editor.commit();
					}
//					Toast.makeText(getApplicationContext(), "该商品没有规格", Toast.LENGTH_SHORT).show();
				}
//			}else {
//				Toast.makeText(getApplicationContext(), "请先登录帐号", Toast.LENGTH_SHORT).show();
//			}
//			Log.i("加入购物车",isConfirmBuy+"==="+isBuy+"==================="+Constants.mCarsMap.get(mSizeList.get(mSizePosition).get("Id").toString()).get(0).get("chima"));
			break;
		// 返回
		case R.id.title_back_image:
			ExitApplication.getInstance().finishMall();
			break;
		}
	}

	// Grid监听事件
	OnItemClickListener mOnItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			mOldPosition = position+"";
			mSizePosition = position;
			mGridAdapter.setSeclection(position);
			mGridAdapter.notifyDataSetChanged();
			// mSizeId = sizeId;
			if (mSizeList != null && mSizeList.size() > 0) {
				if (!Constants.isNull(mSizeList.get(position).get("Id"))) {
					sizeId = mSizeList.get(position).get("Id").toString();
				}
			}
			Log.i("mSizePosition", "=======确认==mSizePosition====="+position);
		}
	};
	// 获取额外运费
	private void getOtherYf() {
		RequestParams params = new RequestParams();
		params.put("addressid", addressId);
		AsyUtils.get(Constants.getOtherYf2, params,
				new AsyncHttpResponseHandler() {
					@Override
					@Deprecated
					public void onSuccess(String content) {
						Log.i("content", content
								+ "===========content==========");
						otherYf = content;
						handler.sendEmptyMessageDelayed(2, 0);
						super.onSuccess(content);
					}
				});
	}

	// 获取用户食尚币
	private void getUserShmoney() {
		RequestParams params = new RequestParams();
		params.put("username", Constants.mUserName);
		AsyUtils.get(Constants.getUserShmoney2, params,
				new AsyncHttpResponseHandler() {
					@Override
					@Deprecated
					public void onSuccess(String content) {
						Log.i("用户食尚币", content + "=====================");
						mShmoney = content;
						handler.sendEmptyMessageDelayed(3, 0);
						super.onSuccess(content);
					}
				});
	}
//	private void getAllSource() {
//		StringBuilder sb = new StringBuilder();
//		sb.append("UserId="+Constants.mId+"&currencyType=4");
//		XutilsUtils.get(Constants.getGoldShmony, sb,
//				new RequestCallBack<String>() {
//					@Override
//					public void onFailure(HttpException arg0, String arg1) {
//					}
//					@Override
//					public void onSuccess(ResponseInfo<String> res) {
//						Log.i("用户食尚币", res.result + "=====================");
//						mShMaps = Constants.getJsonObjectByData(res.result);
//						if(mShMaps!=null && mShMaps.size()>0){
//							if(mShMaps.get("AvailableBalance")!=null) {
//								mShmoney = mShMaps.get("AvailableBalance").toString();
//							}
//							handler.sendEmptyMessageDelayed(3,0);
//						}
//					}
//				});
//	}
	// 获取激活金额
	private void getJhMoney() {
		RequestParams params = new RequestParams();
		params.put("uid", Constants.mId);
		AsyUtils.get(Constants.getJhMoney2, params,
				new AsyncHttpResponseHandler() {
					@Override
					@Deprecated
					public void onSuccess(String content) {
						Log.i("激活金额", content + "=====================");
						mJhMoney = content;
						if (content.length() > 10) {// 获取的数据不为金额
							handler.sendEmptyMessageDelayed(5, 0);
						} else {
							handler.sendEmptyMessageDelayed(4, 0);
						}

						super.onSuccess(content);
					}

					@Override
					@Deprecated
					public void onFailure(Throwable error, String content) {
						handler.sendEmptyMessageDelayed(5, 0);
						super.onFailure(error, content);
					}
				});
	}

	private void getMallDetail() {
		StringBuilder sb = new StringBuilder();
		sb.append("id=" + id+"&userid="+Constants.mId);
		XutilsUtils.get(Constants.getMallDetail3, sb,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						isConfirmBuy = false;
					}
					@Override
					public void onSuccess(ResponseInfo<String> res) {
						Log.i("商品详细信息",res.result+"=======================");
						mDetailMap = Constants.getJsonObjectByData(res.result);
						String mMallType = "";
						if(mDetailMap!=null && mDetailMap.size()>0 && mDetailMap.get("GiftType")!=null){
							if(mDetailMap.get("GiftType").toString().equals("1")){
								mMallType = "食尚币商品";
							}else if(mDetailMap.get("GiftType").toString().equals("2")){
								mMallType = "金币商品";
							}else if(mDetailMap.get("GiftType").toString().equals("3")){
								mMallType = "激活商品";
							}
						}

						mMallDetail = "<img border=\\\"0\" src=\""+mDetailMap.get("Image")+"\" />  <p>商品类型："+mMallType+"</p>  <p>商品名称："+mDetailMap.get("Title")+"</p>  <p>商品价格："+mDetailMap.get("Money")+",运费："+mDetailMap.get("Yf")+"</p>  <p>用户账号："+Constants.mUserName+"</p>  <p>用户等级："+Constants.UserLevelMemo+"</p>";
						Log.i("订单信息显示",mMallDetail+"=======================");
						if(mDetailMap!=null && mDetailMap.size()>0 && mDetailMap.get("KC")!=null){
							mSizeList = Constants.getJsonArray(mDetailMap.get("KC").toString());
							isConfirmBuy = true;
							if(mSizeList!=null){
								if(mSizeList.size()<=0){
//									mCommoditySpecificationsBtn.setVisibility(View.GONE);
									mContactCustomerLayout.setVisibility(View.GONE);
								}
							}else{
//								mCommoditySpecificationsBtn.setVisibility(View.GONE);
								mContactCustomerLayout.setVisibility(View.GONE);
							}
						}
						handler.sendEmptyMessageDelayed(7, 0);
					}
				});
	}

	Handler handler = new Handler() {
		Intent intent = new Intent();

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				if (mDefaulAddressList != null && mDefaulAddressList.size() > 0) {
					Log.i("mModifyAddressBtn", "=======2=========="
							+ mDefaulAddressList);
					if (mDefaulAddressList.get(0).get("Id").toString()
							.equals("null")) {
						return;
					}
					mUserName.setText("收货人姓名："
							+ mDefaulAddressList.get(0).get("Realname")
									.toString());
					mUserPhone.setText("收货人电话："
							+ mDefaulAddressList.get(0).get("Mobile")
									.toString());
					mUserAddress.setText("收货人地址："
							+ mDefaulAddressList.get(0).get("Address")
									.toString());
					mModifyAddressBtn.setText("修改");
					addressId = mDefaulAddressList.get(0).get("Id").toString();
					getOtherYf();
				} else {
					Log.i("mModifyAddressBtn", "=======3==========");
					mModifyAddressBtn.setText("去填写");
				}
				break;

			case 2:
				String money = Float.parseFloat(yf) + Float.parseFloat(otherYf)
						+ "";

				Double Yf = Double.parseDouble(money + "");
				mKuaidiText.setText(new java.text.DecimalFormat("#.00")
						.format(Yf) + "元");
				break;
			case 3:
				// 判断是否有可选择的尺码规格
				boolean isBuy = true;
				if (mSizeList != null && mSizeList.size() > 0) {
					if (mSizeId.equals("")) {
						isBuy = false;
					} else {
						isBuy = true;
					}
				}
				Constants.mShMoney = mShmoney;
				String shmoneys =(mMemberPriceText.getText()
						.toString()).replaceAll("￥","");
				if (isConfirmBuy) {
					if (Constants.onLine == 1 && !(Constants.mId.equals(""))) {
//						Log.i("用户食尚币",shmoneys+"======================");
//						Toast.makeText(getApplicationContext(),mShmoney+"=================",Toast.LENGTH_SHORT).show();
//						if (Float.parseFloat(mShmoney) > Float
//								.parseFloat(shmoneys)) {
							if (isBuy) {
								if (mKuaidiText.getText().toString() != null) {
									intent.setClass(getApplicationContext(),
											MallChoseAddressActivity.class);
									Bundle bundle = new Bundle();
									bundle.putString("flag", "1");
									bundle.putString("yf",
											Float.parseFloat(yf) + "");

									bundle.putString("merchantName", title);
									// 商家ID 商城页面随便传一个来充数
									bundle.putString("mId", "52913");
									bundle.putString("type", "gift");
									bundle.putString("flum", "0");
									bundle.putString("LoginImage", LoginImage);
									bundle.putString("mallOrMerchant", "mall");
									// bundle.putString("address", addressId);
									bundle.putString("giftId", giftId);
									bundle.putString("sizeId", mSizeId);
									bundle.putString("money", price);
									bundle.putString("sizeStr", sizeString);
									bundle.putString("typeName", mTypeName);
									intent.putExtras(bundle);
									startActivity(intent);
								}
							} else {
								Toast.makeText(getApplicationContext(),
										"请选择商品规格！", Toast.LENGTH_SHORT).show();
							}
//						} else {
//							Toast.makeText(getApplicationContext(), "食尚币不足！",
//									Toast.LENGTH_SHORT).show();
//						}

					} else {
						Toast.makeText(getApplicationContext(), "请先登录账户！",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(getApplicationContext(), "数据加载失败！",
							Toast.LENGTH_SHORT).show();
				}
				break;
			// 跳转到激活页面
			case 4:
				Bundle bundle = new Bundle();
				// 跳转到支付方式选择页面
				bundle.putString("price", mJhMoney);
				bundle.putString("merchantName", "食尚男女");
				bundle.putString("mId", Constants.mId);
				bundle.putString("LoginImage", "");
				bundle.putString("flum", "");
				bundle.putString("type", "");
				intent.putExtras(bundle);
				intent.setClass(getApplicationContext(), JHPayWayActivity.class);
				startActivity(intent);
				break;
			case 5:
				Toast.makeText(getApplicationContext(), "激活失败，请重新登录！",
						Toast.LENGTH_SHORT).show();

				break;
			case 6:
				if (mSizeList != null && mSizeList.size() > 0) {
					// mGridAdapter = new MallDetailGridAdapter(
					// getApplicationContext(), mSizeList);
					// mSizePopupWindow = new MallSizePopupWindow(
					// MallDetailActivity.this, mOnItemClickListener,
					// mGridAdapter, itemsOnClick);
					// mSizePopupWindow.showAtLocation(MallDetailActivity.this
					// .findViewById(R.id.mall_detail_id), Gravity.BOTTOM
					// | Gravity.CENTER_HORIZONTAL, 0, 0);

				} else {
					Toast.makeText(getApplicationContext(), "该商品没有规格可选！",
							Toast.LENGTH_SHORT).show();
				}
				break;
			case 7:

				if (mDetailMap != null && mDetailMap.size() > 0) {
					if(mDetailMap.get("detail")!=null){
					mDetailWeb.loadDataWithBaseURL(null, mDetailMap
							.get("detail").toString(), "text/html", "utf-8", null);
					}
						if (mDetailMap.get("GoldMoneyMemo") != null) {
							mBlText.setText(mDetailMap.get("GoldMoneyMemo")
									.toString());
						}
					mArrayBitmaps = new ArrayList<String>();
					if (mDetailMap.get("image") != null) {
						mArrayBitmaps.add(mDetailMap.get("image")
								.toString());
					}
					if (mDetailMap.get("image1") != null) {
						mArrayBitmaps.add(mDetailMap.get("image1")
								.toString());
					}
					if (mDetailMap.get("image2") != null) {
						mArrayBitmaps.add(mDetailMap.get("image2")
								.toString());
					}
					if (mDetailMap.get("image3") != null) {
						mArrayBitmaps.add(mDetailMap.get("image3")
								.toString());
					}
					if (mDetailMap.get("image") != null) {
						mBitmapUtils.display(mMallTitleImage, mDetailMap
								.get("image").toString());
					} else if (mDetailMap.get("image1") != null) {
						mBitmapUtils.display(mMallTitleImage, mDetailMap
								.get("image1").toString());
					} else if (mDetailMap.get("image2") != null) {
						mBitmapUtils.display(mMallTitleImage, mDetailMap
								.get("image2").toString());
					} else if (mDetailMap.get("image3") != null) {
						mBitmapUtils.display(mMallTitleImage, mDetailMap
								.get("image3").toString());
					}
				}
				break;
			case 8:
				isConfirmBuy = true;
				break;
			case 9:
				// mTitleGallery.setSelection(index);
				break;
			case 10:
				mViewPager.setCurrentItem(currentItem);// 切换当前显示的图片
				break;
			case 11:
				mViewPagerAdapter = new MallDetailViewPagerAdapter(
						getApplicationContext(), mViewList, mImageList);
				mViewPager.setAdapter(mViewPagerAdapter);
				mViewPager.setOnPageChangeListener(new MyPageChangeListener());
				break;
			case 12:
				if (mIsFavoriteMap != null && mIsFavoriteMap.size() > 0) {
					if (mIsFavoriteMap.get("Code") != null
							&& mIsFavoriteMap.get("Code").equals("200")) {
						if (!Constants.isNull(mIsFavoriteMap.get("Data"))) {
							if (mIsFavoriteMap.get("Data").toString()
									.equals("false")) {
								mFavoriteImage
										.setImageResource(R.drawable.icon_shoucangqian);
								mFavoriteText.setText("收藏");
								isFavorite = false;
							} else if (mIsFavoriteMap.get("Data").toString()
									.equals("true")) {
								mFavoriteImage
										.setImageResource(R.drawable.icon_shoucanghou);
								mFavoriteText.setText("已收藏");
								isFavorite = true;
							}
						}
					}
				}
				break;
			case 13:
				if (mAddFavoriteMap != null && mAddFavoriteMap.size() > 0) {
					Toast.makeText(getApplicationContext(),
							mAddFavoriteMap.get("Message").toString(),
							Toast.LENGTH_SHORT).show();
					if (!Constants.isNull(mAddFavoriteMap.get("Code"))
							&& mAddFavoriteMap.get("Code").equals("200")) {
						mFavoriteImage
								.setImageResource(R.drawable.icon_shoucanghou);
						mFavoriteText.setText("已收藏");
						isFavorite = true;
					}
				} else {
					Toast.makeText(getApplicationContext(), "添加收藏失败",
							Toast.LENGTH_SHORT).show();
				}
				break;
			case 14:
				if (mDeleteFavoriteMap != null && mDeleteFavoriteMap.size() > 0) {
					Toast.makeText(getApplicationContext(),
							mDeleteFavoriteMap.get("Message").toString(),
							Toast.LENGTH_SHORT).show();
					if (!Constants.isNull(mDeleteFavoriteMap.get("Code"))
							&& mDeleteFavoriteMap.get("Code").equals("200")) {
						mFavoriteImage
								.setImageResource(R.drawable.icon_shoucangqian);
						mFavoriteText.setText("收藏");
						isFavorite = false;
					}
				} else {
					Toast.makeText(getApplicationContext(), "取消收藏失败",
							Toast.LENGTH_SHORT).show();
				}
				break;
			case 15:
				if (mIsUserBuyMap != null && mIsUserBuyMap.size() > 0) {
					if (!Constants.isNull(mIsUserBuyMap.get("Data"))
							&& mIsUserBuyMap.get("Data").toString()
									.equals("true")) {
						intent.setClass(getApplicationContext(),
								MallPayWayActivity.class);
						bundle = new Bundle();
						bundle.putString(
								"price",
								Float.parseFloat(yf)
										+ Float.parseFloat(otherYf) + "");
						bundle.putString("merchantName", title);
						bundle.putString("mId", "52913");
						bundle.putString("type", "gift");
						bundle.putString("flum", "0");
						bundle.putString("LoginImage", LoginImage);
						bundle.putString("mallOrMerchant", "mall");
						bundle.putString("address", addressId);
						bundle.putString("giftId", giftId);
						bundle.putString("sizeId", mSizeId);
						intent.putExtras(bundle);
						startActivity(intent);

					} else {
						try{
						mJhPupWindow = new JhPopupWindow(
								MallDetailActivity.this, mJhClick,
								mIsUserBuyMap.get("Message").toString());
						mJhPupWindow.showAtLocation(mMallName, Gravity.CENTER,
								0, 0);
						}catch (Exception e) {
							e.printStackTrace();
						}
					}
				} else {
					try{
					mJhPupWindow = new JhPopupWindow(MallDetailActivity.this,
							mJhClick, "");
					mJhPupWindow
							.showAtLocation(mMallName, Gravity.CENTER, 0, 0);
					}catch (Exception e) {
						e.printStackTrace();
					}
				}
				break;
			case 16:
				
				if (mParamterArrayList != null && mParamterArrayList.size() > 0) {
					Log.i("商品参数", "=============商品参数===============");
					mParamterAdapter = new MallParameterAdapter(
							getApplicationContext(), mParamterArrayList);
					mScrollListView.setAdapter(mParamterAdapter);
				}
				break;
			}
		};
	};

	// 为弹出窗口实现监听类
	private OnClickListener itemsOnClick = new OnClickListener() {
		public void onClick(View v) {
			mSizePopupWindow.dismiss();
			switch (v.getId()) {
			case R.id.pop_mall_detail_x:
				break;
			case R.id.popup_mall_bottom_confirm_btn:
//				mSizeChoseBtn.setVisibility(View.VISIBLE);
				mSizeId = sizeId;
				if (!Constants.isNull(mSizeList.get(mSizePosition).get("yf"))) {
					yf = mSizeList.get(mSizePosition).get("yf").toString();
				}
				if (!Constants
						.isNull(mSizeList.get(mSizePosition).get("price"))) {
					price = mSizeList.get(mSizePosition).get("price")
							.toString();
				}
				if (!Constants
						.isNull(mSizeList.get(mSizePosition).get("money"))) {
					money = mSizeList.get(mSizePosition).get("money")
							.toString();
				}
				if (!Constants.isNull(mSizeList.get(mSizePosition)
						.get("amount"))) {
					dhnumber = mSizeList.get(mSizePosition).get("amount")
							.toString();
				}
				if (!Constants
						.isNull(mSizeList.get(mSizePosition).get("color"))
						&& !Constants.isNull(mSizeList.get(mSizePosition).get(
								"chima"))) {
					sizeString = mSizeList.get(mSizePosition).get("color")
							.toString()
							+ "-"
							+ mSizeList.get(mSizePosition).get("chima")
									.toString();
					Log.i("选择尺码", mSizeList.get(mSizePosition).get("chima")
							+ "==========="+sizeString+"============="+mSizePosition);
				}
				mGuigeText.setText(sizeString);
//				mSizeChoseBtn.setText(sizeString);
				Log.i("mSizeId", mSizeId + "=======确认=======");
				mCarsDetailMap = new HashMap<String, Object>();
				mCarsDetailMap.put("ProductName",mDetailMap.get("Title"));
				mCarsDetailMap.put("ProductId",mDetailMap.get("Id"));
				mCarsDetailMap.put("Price",mDetailMap.get("BuyingPrice"));
				mCarsDetailMap.put("Image",mDetailMap.get("Image"));
				mCarsDetailMap.put("chima",sizeString);
				mCarsDetailMap.put("isTrue", "true");
				mCarsDetailMap.put("userId",Constants.mId);
				mCarsDetailMap.put("isCheck","true");
				mCarsDetailMap.put("AttrId",mSizeList.get(mSizePosition).get("Id")
						.toString());
				mCarsDetailMap.put("Yf", mDetailMap.get("Yf"));
				mCarsDetailMap.put("Dhnumber", mDetailMap.get("Dhnumber"));
				mCarsDetailMap.put("titleImage1", mDetailMap.get("image1"));
				mCarsDetailMap.put("titleImage2", mDetailMap.get("image2"));
				mCarsDetailMap.put("titleImage3", mDetailMap.get("image3"));
				mCarsDetailMap.put("Salenumber", mDetailMap.get("Salenumber"));
				mCarsDetailMap.put("Money", mDetailMap.get("Money"));
				mCarsDetailMap.put("Content", mDetailMap.get("Content"));
				if(mDetailMap.get("GiftType").toString().equals("1")){
					mCarsDetailMap.put("typeName", "ssb");
				}else if(mDetailMap.get("GiftType").toString().equals("2")){
					mCarsDetailMap.put("typeName", "ssjb");
				}else if(mDetailMap.get("GiftType").toString().equals("3")){
					mCarsDetailMap.put("typeName", "xj");
				}

//				mDetailMap.put("chima",sizeString);
//				mDetailMap.put("chimaId",mSizeList.get(mSizePosition).get("Id")
//						.toString());
				Double pri = Double.parseDouble(money);
				mMarketValueText.setText("￥" + pri + "");
				mMarketValueText.getPaint().setFlags(
						Paint.STRIKE_THRU_TEXT_FLAG); // 中间横线
//				mMemberPriceText.setText(price);

				pri = Double.parseDouble(price);
				if(mTypeName!=null){
					if(mTypeName.equals("xj")){
						mMemberPriceText.setText("￥"+new java.text.DecimalFormat("#.00")
								.format(pri) + "");
						mSjName.setVisibility(View.GONE);
					}else if(mTypeName.equals("ssb")){
						mMemberPriceText.setText(new java.text.DecimalFormat("#.00")
								.format(pri) + "");
						mSjName.setVisibility(View.VISIBLE);
						mSjName.setText("食尚币");
					}else if(mTypeName.equals("ssjb")){
						mMemberPriceText.setText("￥"+new java.text.DecimalFormat("#.00")
								.format(pri) + "");
						mSjName.setVisibility(View.GONE);
					}
				}

				mStockText.setText("库存：" + dhnumber + "件");
				getOtherYf();
				break;
			default:
				break;
			}
		}
	};

	// 去激活
	OnClickListener mJhClick = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			if(mJhPupWindow!=null && mJhPupWindow.isShowing()){
			mJhPupWindow.dismiss();
			}
			if (!(Constants.mId.equals(""))) {
				getJhMoney();
			} else {
				Toast.makeText(getApplicationContext(), "激活失败，请重新登录帐号！",
						Toast.LENGTH_SHORT).show();
			}
		}
	};

	// 取消激活提示框
	OnClickListener mCancelJhClick = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			mAlertLayout.setVisibility(View.GONE);
			if(mJhPopupWindow!=null && mJhPopupWindow.isShowing()){
			mJhPopupWindow.dismiss();
			}
		}
	};

//	public ArrayList<HashMap<String, Object>> getJsonLists(String json)
//			throws JSONException {
//		JSONObject jsonObject = new JSONObject(json);
//		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
//		HashMap<String, Object> map = new HashMap<String, Object>();
//		map.put("Id", jsonObject.getString("Id"));
//		map.put("Address", jsonObject.getString("Address"));
//		map.put("Realname", jsonObject.getString("Realname"));
//		map.put("Mobile", jsonObject.getString("Mobile"));
//		map.put("Cityid", jsonObject.getString("Cityid"));
//
//		map.put("Provinceid", jsonObject.getString("Provinceid"));
//		map.put("Areaid", jsonObject.getString("Areaid"));
//		map.put("Status", jsonObject.getString("Status"));
//
//		map.put("Create_time", jsonObject.getString("Create_time"));
//		map.put("Use_time", jsonObject.getString("Use_time"));
//		map.put("Userid", jsonObject.getString("Userid"));
//
//		map.put("Cityname", jsonObject.getString("Cityname"));
//		map.put("Provincename", jsonObject.getString("Provincename"));
//		map.put("Areaname", jsonObject.getString("Areaname"));
//		map.put("Yf", jsonObject.getString("Yf"));
//		list.add(map);
//		return list;
//	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			ExitApplication.getInstance().finishMall();
			break;
		default:
			break;
		}
		return false;
	};

	private class MyPageChangeListener implements OnPageChangeListener {
		private int oldPosition = 0;

		public void onPageSelected(int position) {
			try{
			if (mImageViewIds != null && mImageViewIds.length > 0) {
				currentItem = position;
				mImageViewIds[oldPosition].setImageDrawable(getBaseContext()
						.getResources().getDrawable(R.drawable.ic_dot_true));
				mImageViewIds[position].setImageDrawable(getBaseContext()
						.getResources().getDrawable(R.drawable.ic_dot_fouc));

				oldPosition = position;
			}
			}catch(Exception e){
				e.printStackTrace();
			}
		}

		public void onPageScrollStateChanged(int arg0) {
		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		IntentFilter intentFilter = new IntentFilter();
		//监听网络连接变化情况
		intentFilter.addAction(KFMainService.ACTION_XMPP_CONNECTION_CHANGED);
		//监听消息
		intentFilter.addAction(KFMainService.ACTION_XMPP_MESSAGE_RECEIVED);
		//工作组在线状态
		intentFilter.addAction(KFMainService.ACTION_XMPP_WORKGROUP_ONLINESTATUS);
		registerReceiver(mXmppreceiver, intentFilter);
	}

	@Override
	protected void onStop() {

		System.gc();
		super.onStop();
		unregisterReceiver(mXmppreceiver);
	}

	private class ScrollTask implements Runnable {
		public void run() {
			synchronized (mViewPager) {
				currentItem = (currentItem + 1) % mViewList.size();
				handler.sendEmptyMessageDelayed(10, 0); // 通过Handler切换图片
			}
		}
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(mBroadcastReceiver);
		super.onDestroy();
		Util.release();
	}

	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			LayoutInflater inflater = LayoutInflater
					.from(getApplicationContext());
			for (int i = 0; i < mImageList.size(); i++) {
				view = inflater.inflate(R.layout.mall_detail_viewpager_item,
						null);
				mViewImage = (ImageView) view
						.findViewById(R.id.mall_detail_viewpager_image);
				mBitmapUtils.display(mViewImage, mImageList.get(i));
				mViewList.add(view);
			}
			handler.sendEmptyMessageDelayed(11, 0);
		}
	};

	BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context arg0, Intent intent) {
			data = Integer.parseInt(intent.getExtras().getString("data"));
			switch (data) {
			case 1:
				mUserName.setText("收货人姓名："
						+ intent.getExtras().getString("userName"));

				mUserPhone.setText("收货人电话："
						+ intent.getExtras().getString("userPhone"));

				mUserAddress.setText("收货人地址："
						+ intent.getExtras().getString("userAddress"));
				addressId = intent.getExtras().getString("addressId");
				getOtherYf();
				break;

			case 2:
				mSizeId = intent.getExtras().getString("sizeId");
				mSizeList = (ArrayList<HashMap<String, Object>>) intent
						.getExtras().getSerializable("list");
				mSizePosition = intent.getExtras().getInt("mSizePosition");
				if (mSizeList != null && mSizeList.size() > 0) {
					yf = mSizeList.get(mSizePosition).get("yf").toString();
					price = mSizeList.get(mSizePosition).get("price")
							.toString();
					money = mSizeList.get(mSizePosition).get("money")
							.toString();
					dhnumber = mSizeList.get(mSizePosition).get("amount")
							.toString();
					sizeString = mSizeList.get(mSizePosition).get("color")
							.toString()
							+ "-"
							+ mSizeList.get(mSizePosition).get("chima")
									.toString();
//					mSizeChoseBtn.setText(sizeString);
					Log.i("mSizeId", mSizeId + "=======确认=======");

					Double pri = Double.parseDouble(price);
					mMarketValueText.setText("￥" + pri + "");
					mMarketValueText.getPaint().setFlags(
							Paint.STRIKE_THRU_TEXT_FLAG); // 中间横线
					mMemberPriceText.setText(money);
					mStockText.setText("库存：" + dhnumber + "件");
					getOtherYf();
				}
				Log.i("data", mSizeList.get(mSizePosition).get("chima")
						+ "==========确认尺码============" + mSizeId);
				break;
			case 3:
				Log.i("data", "滚动到底了==============");
				mGraphicView.setVisibility(View.GONE);
				mProductView.setVisibility(View.VISIBLE);
				mGraphicDetails.setTextColor(getResources().getColor(
						R.color.service_provider_color));
				mProductParameters.setTextColor(getResources().getColor(
						R.color.red));
				mGraphiContentLayout.setVisibility(View.GONE);
				mProductContentLayout.setVisibility(View.VISIBLE);
				break;
			}
		}
	};

	protected void checkTencentInstance() {
		MallDetailActivity.mAppid = "222222";
		MallDetailActivity.mTencent = Tencent.createInstance(
				MallDetailActivity.mAppid, MallDetailActivity.this);
	}

	// 检测是否已经收藏该商品
	private void isFavorite() {
		RequestParams params = new RequestParams();
		params.put("uid", Constants.mId);
		params.put("giftid", giftId);
		AsyUtils.get(Constants.isFavorite, params,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONObject response) {
						mIsFavoriteMap = Constants.getJsonObject(response
								.toString());

						Log.i("mFavoriteMap", mIsFavoriteMap
								+ "=========response===========");
						handler.sendEmptyMessageDelayed(12, 0);
						super.onSuccess(response);
					}
				});
	}

	// 添加商品收藏
	private void addFavorite() {
		RequestParams params = new RequestParams();
		params.put("uid", Constants.mId);
		params.put("giftid", giftId);
		AsyUtils.get(Constants.addFavorite, params,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONObject response) {

						mAddFavoriteMap = Constants.getJsonObject(response
								.toString());

						Log.i("mAddFavoriteMap", mAddFavoriteMap
								+ "=========response===========");
						handler.sendEmptyMessageDelayed(13, 0);
						super.onSuccess(response);
					}
				});
	}

	// 取消商品收藏
	private void cancelFavorite() {
		RequestParams params = new RequestParams();
		params.put("uid", Constants.mId);
		params.put("giftid[]", giftId);
		AsyUtils.get(Constants.deleteFavorite2, params,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONObject response) {
						Log.i("response", response + "====================");

						mDeleteFavoriteMap = Constants.getJsonObject(response
								.toString());

						handler.sendEmptyMessageDelayed(14, 0);
						super.onSuccess(response);
					}
				});
	}

	// 获取商品详情 产品参数
	private void getMallValue() {
		Log.i("getMallValue", "==============");
		RequestParams params = new RequestParams();
		params.put("gid", giftId);
		AsyUtils.get(Constants.getMallValue, params,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONObject response) {
						Log.i("商品参数", response + "=============");
						try {
							mParamterArrayList = Constants
									.getJsonArrayByData(response.toString());
						} catch (JSONException e) {
							e.printStackTrace();
						}
						handler.sendEmptyMessageDelayed(16, 0);
						super.onSuccess(response);
					}
				});
	}


	// 检测滚动到底切换到产品参数
	private NotifyMallDetailInterface mInterface = new NotifyMallDetailInterface() {
		@Override
		public void notifyScrollBottom() {
		}
	};
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
	protected void setImmerseLayout(View view) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			//透明状态栏
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//			//透明导航栏
//			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

			int statusBarHeight = getStatusBarHeight(this.getBaseContext());
			view.setPadding(0, statusBarHeight, 0, 0);
		}
	}
	/**
	 * 用于获取状态栏的高度。 使用Resource对象获取（推荐这种方式）
	 *
	 * @return 返回状态栏高度的像素值。
	 */
	public static int getStatusBarHeight(Context context) {
		int result = 0;
		int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen",
				"android");
		if (resourceId > 0) {
			result = context.getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}
	//监听：连接状态、即时通讯消息、客服在线状态
	private BroadcastReceiver mXmppreceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			//监听：连接状态
			if (action.equals(KFMainService.ACTION_XMPP_CONNECTION_CHANGED))//监听链接状态
			{
				updateStatus(intent.getIntExtra("new_state", 0));
			}//监听：即时通讯消息
			else if (action.equals(KFMainService.ACTION_XMPP_MESSAGE_RECEIVED))//监听消息
			{
				String body = intent.getStringExtra("body");
				String from = StringUtils.parseName(intent.getStringExtra("from"));
//				KFSLog.d("body:"+body+" from:"+from);
			}//客服工作组在线状态
			else if (action.equals(KFMainService.ACTION_XMPP_WORKGROUP_ONLINESTATUS)) {
				String onlineStatus = intent.getStringExtra("onlinestatus");
				KFLog.d("客服工作组:" + onlineStatus);//online：在线；offline: 离线
				if (onlineStatus.equals("online")) {
					KFLog.d("online");
				} else {
					KFLog.d("offline");
				}
			}
		}
	};
	//根据监听到的连接变化情况更新界面显示
	private void updateStatus(int status) {
		switch (status) {
			case KFXmppManager.CONNECTED:
//				mTitle.setText("微客服(客服Demo)");
				//在成功建立连接之后, 查询客服工作组在线状态，返回结果在BroadcastReceiver中返回
				KFAPIs.checkKeFuIsOnlineAsync("wgdemo", MallDetailActivity.this);
				break;
			case KFXmppManager.DISCONNECTED:
//				mTitle.setText("微客服(客服Demo)(未连接)");
				break;
			case KFXmppManager.CONNECTING:
//				mTitle.setText("微客服(客服Demo)(登录中...)");
				break;
			case KFXmppManager.DISCONNECTING:
//				mTitle.setText("微客服(客服Demo)(登出中...)");
				break;
			case KFXmppManager.WAITING_TO_CONNECT:
			case KFXmppManager.WAITING_FOR_NETWORK:
//				mTitle.setText("微客服(客服Demo)(登录中)");
				break;
			default:
				throw new IllegalStateException();
		}
	}
	// 2.咨询人工客服
//	private void startChat2() {
//		KFAPIs.startChat(this, "storeservices", // 1. 客服工作组ID(请务必保证大小写一致)，请在管理后台分配
//				"食尚客服", // 2. 会话界面标题，可自定义
//				mMallDetail, // 3. 设置为 null或者"" 将不发送此信息给客服
//				false, // 4. 是否显示自定义菜单,如果设置为显示,请务必首先在管理后台设置自定义菜单,
//				// 请务必至少分配三个且只分配三个自定义菜单,多于三个的暂时将不予显示
//				// 显示:true, 不显示:false
//				0, // 5. 默认显示消息数量
//				null, // 6. 使用默认客服头像
//				Constants.headPath, // 7. 使用默认用户头像
//				false, // 8. 默认机器人应答
//				false,  //9. 是否强制用户在关闭会话的时候 进行“满意度”评价， true:是， false:否
//				new KFCallBack() { // 10. 会话页面右上角回调函数, 如果想要保持默认，请设置为null
//
//					@Override
//					public Boolean useTopRightBtnDefaultAction() {
//						return true;
//					}
//
//					@Override
//					public void OnChatActivityTopRightButtonClicked() {
//						// TODO Auto-generated method stub
//						Log.d("KFMainActivity", "右上角回调接口调用");
////						Toast.makeText(MallDetailActivity.this, "右上角回调接口调用",
////								Toast.LENGTH_SHORT).show();
//						// 测试右上角回调接口调用
////						showTagList();
//					}
//
//					@Override
//					public void OnECGoodsImageViewClicked(String imageViewURL) {
//						// TODO Auto-generated method stub
//
//						Log.d("KFMainActivity", "OnECGoodsImageViewClicked");
//
//					}
//
//					@Override
//					public void OnECGoodsTitleDetailClicked(
//							String titleDetailString) {
//						// TODO Auto-generated method stub
//						Log.d("KFMainActivity", "OnECGoodsIntroductionClicked");
//
//					}
//
//					@Override
//					public void OnECGoodsPriceClicked(String priceString) {
//						// TODO Auto-generated method stub
//						Log.d("KFMainActivity", "OnECGoodsPriceClicked");
//
//					}
//					@Override
//					public void OnEcGoodsInfoClicked(String callbackId) {
//						// TODO Auto-generated method stub
//
//					}
//					/**
//					 * 用户点击会话页面下方“常见问题”按钮时，是否使用自定义action，如果返回true,
//					 * 则默认action将不起作用，会调用下方OnFaqButtonClicked函数
//					 */
//					public Boolean userSelfFaqAction(){
//						return false;
//					}
//
//					/**
//					 * 用户点击“常见问题”按钮时，自定义action回调函数接口
//					 */
//					@Override
//					public void OnFaqButtonClicked() {
//
//						Log.d("KFMainActivity", "OnFaqButtonClicked");
//					}
//
//				} // 10. 会话页面右上角回调函数
//		);
//	}
	// 1.咨询人工客服
	private void startChat() {
		//
		KFAPIs.startChat(this,
				"storeservices", // 1. 客服工作组ID(请务必保证大小写一致)，请在管理后台分配
				"食尚客服", // 2. 会话界面标题，可自定义
				mMallDetail, // 3. 附加信息，在成功对接客服之后，会自动将此信息发送给客服;
				// 如果不想发送此信息，可以将此信息设置为""或者null
				false, // 4. 是否显示自定义菜单,如果设置为显示,请务必首先在管理后台设置自定义菜单,
				// 请务必至少分配三个且只分配三个自定义菜单,多于三个的暂时将不予显示
				// 显示:true, 不显示:false
				5, // 5. 默认显示消息数量
				//修改SDK自带的头像有两种方式，1.直接替换appkefu_message_toitem和appkefu_message_fromitem.xml里面的头像，2.传递网络图片自定义
				null, //"http://47.90.33.185/PHP/XMPP/gyfd/chat/web/img/kefu-avatar.png",//6. 修改默认客服头像，如果想显示客服真实头像，设置此参数为null
				Constants.headPath,		//7. 修改默认用户头像, 如果不想修改默认头像，设置此参数为null
				false, // 8. 默认机器人应答
				false,  //9. 是否强制用户在关闭会话的时候 进行“满意度”评价， true:是， false:否
				null);
	}
}
