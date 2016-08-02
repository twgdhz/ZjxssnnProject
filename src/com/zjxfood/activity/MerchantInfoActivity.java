package com.zjxfood.activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.project.util.DensityUtils;
import com.project.util.Utils;
import com.umeng.analytics.MobclickAgent;
import com.zjxfood.adapter.MallDetailViewPagerAdapter;
import com.zjxfood.application.ExitApplication;
import com.zjxfood.common.Constants;
import com.zjxfood.http.AsyUtils;
import com.zjxfood.route.RoutePlanningActivity;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 商家详情页面
 * 
 * @author zjx
 * 
 */
public class MerchantInfoActivity extends AppActivity implements OnClickListener {

	private ImageView[] mImageViewIds;// 小圆点ImageView数组
	private static final int IMAGE_COUNT = 5;// 小圆点个数
	// 立即支付
	private Button mPayBtn;
	// 返利
	// private TextView mRebateExplain;
	// 评论
	private LinearLayout mEvaluationListLayout;
	// 标题栏
	private RelativeLayout mHeadLayout;
	// 返回
	private ImageView mBackImage;
	// 查看所有评论
	private Button mSeeAllEvaluation;
	// 导航
	private RelativeLayout mNavigation;
	private Bundle mBundle;
	// 商家名称、地址、电话、介绍、id、图片
	private String merchantName, merchantAddress, merchantPhone,
			merchantIntroduction, mId, mLogoimage, merchantId, plstart,
			verifyState = "",mIscurrency;
	private TextView mMerchantAddress;
	private TextView mMerchantName;
	private TextView mMerchantPhone;
	private TextView mMerchantIntroduction;
	private ArrayList<String> mImagePath;
	// 返利
	private String flnum;
	private FrameLayout mMerchantHeadLayout;
	private ArrayList<HashMap<String, Object>> mMerchantEvalList;
	private TextView mEvalName, mEvalDate, mEvalContent;
	private View mNotEvalView;
	private TextView mVerifyState;
	private Button mReserveBtn;
	private ViewPager mViewPager;
	private List<View> mViewList;
	private ImageView mViewImage;
	private int currentItem = 0;
	private MallDetailViewPagerAdapter mViewPagerAdapter;
	private ArrayList<String> mPathList;
	private ScheduledExecutorService scheduledExecutorService;
	private BitmapUtils mBitmapUtils;
	private LinearLayout mGallertImageLayout;
	private PopupWindow mPayWindow;
	private TextView mOkText, mCancelText;
	private EditText mPriceText;
	private String mOrderCount;
	private String longlat;
	private String mIsparking;
	private String mCurrencybackbl;
	private String mIsStop = "true";//是否支持余额支付
	private TextView mIsparkingText;
	private TextView mRenqiText,mRenjunText,mJuliText,mSsbFlText;
	private String mRenqi="0",mRenjun="0",mJuli="0";
	// private String source =
	// "\t\t<font color='white'>恭喜你已获得</font><font color='#FFA800'>20</font><font color='white'>币</color>";
	private TextView mDaohangjuText;
	private TextView mOrderValueText,mSsbText;
	private TextView mTitleText;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.merchant_info_layout);
		setImmerseLayout(findViewById(R.id.head_layout));
		ViewUtils.inject(this);
		mBitmapUtils = new BitmapUtils(this);
		ExitApplication.getInstance().addActivity(this);
		ExitApplication.getInstance().addMyCommodityActivity(this);
		initView();

		int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
		int total = (int) (Runtime.getRuntime().totalMemory() / 1024);
		mImagePath = new ArrayList<String>();
		Log.d("TAG", "Max memory is " + maxMemory + "KB" + "---------" + total);
		// 获取传递过来的商家信息
		mBundle = getIntent().getExtras();
		if (mBundle != null) {
			merchantName = mBundle.getString("merchantName");
			merchantAddress = mBundle.getString("merchantAddress");
			merchantPhone = mBundle.getString("Phone");
			merchantIntroduction = mBundle.getString("Introduction");
			mId = mBundle.getString("Userid");
			flnum = mBundle.getString("Flnum");
			mLogoimage = mBundle.getString("Logoimage");
			merchantId = mBundle.getString("Id");
			plstart = mBundle.getString("plstar");
			verifyState = mBundle.getString("verifyState");
			mIscurrency = mBundle.getString("iscurrency");
			mOrderCount = mBundle.getString("ordercount");
			longlat = mBundle.getString("location");
			mIsparking = mBundle.getString("Isparking");
			mIsStop = mBundle.getString("istop");
			if(mBundle.getString("money")!=null){
				mRenjun = mBundle.getString("money");
			}
			if(mBundle.getString("ordercount")!=null){
				mRenqi = mBundle.getString("ordercount");
			}
			if(mBundle.getString("km")!=null){
				mJuli = mBundle.getString("km");
			}
			if(mBundle.getString("currencybackbl")!=null){
				mCurrencybackbl = mBundle.getString("currencybackbl");
			}
			
			Log.i("mIscurrency", mIscurrency+"============");
			for (int i = 0, j = 1; i < 5; i++, j++) {
				if (mBundle.getString("Images" + j) != null
						&& !(mBundle.getString("Images" + j).equals(""))) {
					mImagePath.add(mBundle.getString("Images" + j));
				}
			}
		}
		if(mIsparking!=null){
			if(mIsparking.equals("0")){
				mIsparkingText.setText("不提供");
			}else if(mIsparking.equals("1")){
				mIsparkingText.setText("免费提供");
			}else if(mIsparking.equals("2")){
				mIsparkingText.setText("有偿提供");
			}
		}
		
		String source = "\t\t消费即可获得" + flnum + "%返现，全场通用。不与店内其他活动同享";
		Spannable styledText = new SpannableString(source);
		styledText.setSpan(new TextAppearanceSpan(this, R.style.stylePan), 8,
				12, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

		handler.sendEmptyMessageDelayed(2, 100);
		mMerchantAddress.setText(merchantAddress);
		mMerchantName.setText(merchantName);
		mMerchantPhone.setText(merchantPhone);
		mMerchantIntroduction.setText("\t\t" + merchantIntroduction);
//		if(mRenjun!=null){
//			mRenjunText.setText("人均消费：￥"+mRenjun);
//		}
//		if(mRenqi!=null){
//			mRenqiText.setText("人气："+mRenqi);
		Double oldPrice = Double.parseDouble(mRenjun);
			mOrderValueText.setText("已成交订单："+mRenqi+"笔，人均消费：￥"+new java.text.DecimalFormat("0.00").format(oldPrice)+"元");
//		}
		if(mJuli!=null){
//			mJuliText.setText("距离："+mJuli+"km");
			mDaohangjuText.setText("导航("+mJuli+"km)");
		}
//		if(flnum!=null){
			mSsbText.setText("金币返利："+flnum.split("\\.")[0]+"%");
//		}
		

		Log.i("verifyState", verifyState + "=========verifyState===========");

		if (verifyState.equals("1")) {
			mVerifyState.setText("正常营业");
		} else if (verifyState.equals("0")) {
			mVerifyState.setText("即将上线");
		}
		mGallertImageLayout.removeAllViews();
		if (mImagePath != null && mImagePath.size() > 0) {
			mImageViewIds = new ImageView[mImagePath.size()];
			for (int i = 0; i < mImagePath.size(); i++) {
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
	}

	// 初始化控件
	private void initView() {
		mPayBtn = (Button) findViewById(R.id.merchange_info_immediate_pay_btn);
		mGallertImageLayout = (LinearLayout) findViewById(R.id.merchant_title_gallery_image_layout);

		mEvaluationListLayout = (LinearLayout) findViewById(R.id.merchant_introduction_evaluation_list);
		mHeadLayout = (RelativeLayout) findViewById(R.id.merchant_title_id);
//		mBackImage = (ImageView) mHeadLayout.findViewById(R.id.title_back_image);
		mBackImage = (ImageView) findViewById(R.id.title_back_image);
		mTitleText = (TextView) findViewById(R.id.title_text);
		mTitleText.setText("商家主页");
		mSeeAllEvaluation = (Button) findViewById(R.id.merchant_introduction_evaluation_all_btn);
		mNavigation = (RelativeLayout) findViewById(R.id.merchant_introduction_navigation_right_layout);
		mMerchantAddress = (TextView) findViewById(R.id.merchant_introduction_navigation_address_show);
		mMerchantName = (TextView) findViewById(R.id.merchant_name_text);
		mMerchantPhone = (TextView) findViewById(R.id.merchant_introduction_phone_show);
		mMerchantIntroduction = (TextView) findViewById(R.id.merchant_introduction_text_show1);
		mNotEvalView = findViewById(R.id.merchant_introduction_evaluation_view);
		mMerchantHeadLayout = (FrameLayout) findViewById(R.id.merchant_top_layout);
		mVerifyState = (TextView) findViewById(R.id.merchant_introduction_jijiang_text);
		mReserveBtn = (Button) findViewById(R.id.reserve_order_btn);
		mViewPager = (ViewPager) findViewById(R.id.merchant_info_view_pager);
		FrameLayout.LayoutParams layoutParams = (android.widget.FrameLayout.LayoutParams) mViewPager
				.getLayoutParams();
		layoutParams.height = DensityUtils.dp2px(getApplicationContext(), 180);
		mViewPager.setLayoutParams(layoutParams);
		mIsparkingText = (TextView) findViewById(R.id.merchant_introduction_phone_right_text);
		mRenjunText = (TextView) findViewById(R.id.merchant_introduce_renjun_layout);
		mRenqiText = (TextView) findViewById(R.id.merchant_introduce_renqi_layout);
		mJuliText = (TextView) findViewById(R.id.merchant_introduce_juli_layout);
		mSsbFlText = (TextView) findViewById(R.id.merchant_introduce_ssbfl_layout);
		mDaohangjuText = (TextView) findViewById(R.id.merchant_introduction_navigation_right_text);
		mOrderValueText = (TextView) findViewById(R.id.merchant_order_content_text);
		mSsbText = (TextView) findViewById(R.id.merchant_ssb_content_text);

		mPayBtn.setOnClickListener(this);
		mBackImage.setOnClickListener(this);
		mSeeAllEvaluation.setOnClickListener(this);
		mNavigation.setOnClickListener(this);
		mMerchantHeadLayout.setOnClickListener(this);
		mMerchantPhone.setOnClickListener(this);
		mReserveBtn.setOnClickListener(this);
	}

	// 添加评论显示
	@SuppressLint("InflateParams")
	private void addView(LinearLayout layout, int i) {
		LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
		View view = inflater.inflate(R.layout.evaluation_list_item, null);
		mEvalName = (TextView) view.findViewById(R.id.evaluation_name_text);
		mEvalDate = (TextView) view.findViewById(R.id.evaluation_date_text);
		mEvalContent = (TextView) view
				.findViewById(R.id.evaluation_content_text);
		String evalName = Utils.splitePhone(mMerchantEvalList.get(i)
				.get("username").toString());
		mEvalName.setText(evalName);
		mEvalDate.setText(mMerchantEvalList.get(i).get("create_time")
				.toString());
		mEvalContent.setText(mMerchantEvalList.get(i).get("pltext").toString());
		layout.addView(view);
	}

	OnItemSelectedListener mItemSelectedListener = new OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			int pos = position % IMAGE_COUNT;
			mImageViewIds[pos].setImageDrawable(getBaseContext().getResources()
					.getDrawable(R.drawable.ic_dot_fouc));
			if (pos > 0) {
				mImageViewIds[pos - 1].setImageDrawable(getBaseContext()
						.getResources().getDrawable(R.drawable.ic_dot_true));
			}
			if (pos < (IMAGE_COUNT - 1)) {
				mImageViewIds[pos + 1].setImageDrawable(getBaseContext()
						.getResources().getDrawable(R.drawable.ic_dot_true));
			}
			if (pos == 0) {
				mImageViewIds[IMAGE_COUNT - 1]
						.setImageDrawable(getBaseContext().getResources()
								.getDrawable(R.drawable.ic_dot_true));
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}
	};

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				mViewPager.setCurrentItem(currentItem);// 切换当前显示的图片
				break;
			case 2:
				mViewList = new ArrayList<View>();
				LayoutInflater inflater = LayoutInflater
						.from(getApplicationContext());
				View view;
				mPathList = new ArrayList<String>();
				for (int i = 0; i < mImagePath.size(); i++) {
					view = inflater.inflate(
							R.layout.mall_detail_viewpager_item, null);
					mViewImage = (ImageView) view
							.findViewById(R.id.mall_detail_viewpager_image);
					mBitmapUtils.display(mViewImage, mImagePath.get(i));
					mViewList.add(view);
					mPathList.add(mImagePath.get(i));
				}

				mViewPagerAdapter = new MallDetailViewPagerAdapter(
						getApplicationContext(), mViewList, mPathList);
				mViewPager.setAdapter(mViewPagerAdapter);
				mViewPager.setOnPageChangeListener(new MyPageChangeListener());
				handler.sendEmptyMessageDelayed(4, 0);
				getEvaluation();
				break;
			case 3:
				if (mMerchantEvalList != null && mMerchantEvalList.size() > 0) {
					for (int i = 0; i < mMerchantEvalList.size(); i++) {
						addView(mEvaluationListLayout, i);
					}
					mNotEvalView.setVisibility(View.VISIBLE);
				} else {
					mNotEvalView.setVisibility(View.GONE);
					addNotView(mEvaluationListLayout);
				}
				break;
			case 4:
				scheduledExecutorService = Executors
						.newSingleThreadScheduledExecutor();
				// 当Activity显示出来后，每两秒钟切换一次图片显示
				scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(),
						1, 3, TimeUnit.SECONDS);
				break;
			case 5:

				break;
			}
		};
	};

	private void addNotView(LinearLayout layout) {
		LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
		View view = inflater.inflate(R.layout.merchant_eval_not_layout, null);
		layout.removeAllViews();
		layout.addView(view);
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.merchange_info_immediate_pay_btn:
			if (Constants.onLine == 1) {
				if (verifyState.equals("1")) {
					LayoutInflater inflater = LayoutInflater
							.from(getApplicationContext());
					View view = inflater.inflate(R.layout.merchant_price_popup,
							null);
					mOkText = (TextView) view
							.findViewById(R.id.merchant_ok_text);
					mCancelText = (TextView) view
							.findViewById(R.id.merchant_ok_cancel);
					mPriceText = (EditText) view
							.findViewById(R.id.merchant_price_edit);
					mPayWindow = new PopupWindow(view,
							LayoutParams.MATCH_PARENT,
							LayoutParams.MATCH_PARENT, false);
					mPayWindow.setBackgroundDrawable(new BitmapDrawable());
					mPayWindow.setOutsideTouchable(true);
					mPayWindow.setFocusable(true);
					ColorDrawable dw = new ColorDrawable(0xb0000000);
					// 设置SelectPicPopupWindow弹出窗体的背景
					mPayWindow.setBackgroundDrawable(dw);
					// 设置SelectPicPopupWindow弹出窗体动画效果
					mPayWindow.setAnimationStyle(R.style.AnimTop_miss);
					mPayWindow.showAtLocation(mBackImage, Gravity.CENTER, 0, 0);
					mOkText.setOnClickListener(clickListener);
					mCancelText.setOnClickListener(clickListener);
				} else {
					Toast.makeText(getApplicationContext(), "商家未上线，暂时不能支付！",
							Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(getApplicationContext(), "请先登录用户帐号！",
						Toast.LENGTH_SHORT).show();
			}
			break;

		case R.id.title_back_image:

			System.gc();
			finish();
			break;
		case R.id.merchant_introduction_evaluation_all_btn:
			intent.setClass(getApplicationContext(), AllEvaluationActiviy.class);
			Bundle bundle2 = new Bundle();
			bundle2.putString("Id", merchantId);
			intent.putExtras(bundle2);
			startActivity(intent);
			break;
		case R.id.merchant_introduction_navigation_right_layout:
			if(longlat!=null){
			intent.setClass(getApplicationContext(), RoutePlanningActivity.class);
//			intent.setClass(getApplicationContext(), GPSNaviActivity.class);
			Bundle bundle = new Bundle();
//			bundle.putString("address", merchantAddress);
			
			bundle.putString("longlat", longlat);
			
			intent.putExtras(bundle);
			startActivity(intent);
			}else{
				Toast.makeText(getApplicationContext(), "该商家暂时没有坐标", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.merchant_top_layout:
			break;
		case R.id.merchant_introduction_phone_show:
			intent.setAction("android.intent.action.CALL");

			intent.setData(Uri.parse("tel:"
					+ mMerchantPhone.getText().toString()));

			startActivity(intent);
			break;
		case R.id.reserve_order_btn:
			// intent.setClass(getApplicationContext(),
			// ReserveOrderActivity.class);
			// startActivity(intent);
			Toast.makeText(getApplicationContext(), "敬请期待", Toast.LENGTH_SHORT)
					.show();
			break;
		}
	}

	OnClickListener clickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.merchant_ok_text:
				
				if (!mPriceText.getText().toString().equals("")) {
					if(isNumber(mPriceText.getText().toString())){
					// 转换输入的金额
					Double oldPrice = Double.parseDouble(mPriceText.getText()
							.toString());
					String oldPrices = new java.text.DecimalFormat("#.00")
							.format(oldPrice);
					if (Float.parseFloat((mPriceText.getText().toString())) >= 1) {
						Intent intent = new Intent();
						intent.setClass(getApplicationContext(),
								CheckOutActivity.class);
						Bundle mBundle = new Bundle();
						mBundle.putString("mId", mId);
						mBundle.putString("flag", "1");// 扫描
						mBundle.putString("merchantName", merchantName);
						mBundle.putString("flnum", flnum);
						mBundle.putString("Logoimage", mLogoimage);
						mBundle.putString("MallOrMerchant", "merchant");
						mBundle.putString("plstar", plstart);
						mBundle.putString("iscurrency", mIscurrency);
						mBundle.putString("istop", mIsStop);
//						mBundle.putString("iscurrency", "1");
						mBundle.putString("price", mPriceText.getText()
								.toString());
						mBundle.putString("ordercount",mOrderCount);
						intent.putExtras(mBundle);
						startActivity(intent);
						if (mPayWindow != null && mPayWindow.isShowing()) {
							mPayWindow.dismiss();
						}
					}else {
						Toast.makeText(getApplicationContext(), "金额必须大于1元以上！",
								Toast.LENGTH_SHORT).show();
					}
					}else {
						Toast.makeText(getApplicationContext(), "请输入有效的金额！",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(getApplicationContext(), "请输入金额！",
							Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.merchant_ok_cancel:
				if (mPayWindow != null && mPayWindow.isShowing()) {
					mPayWindow.dismiss();
				}
				break;
			}
		}
	};

	BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context arg0, Intent arg1) {
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			System.gc();
			finish();
			break;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void getEvaluation() {
		RequestParams params = new RequestParams();
		params.put("mid", merchantId);
		params.put("page", "1");
		params.put("pagesize", "20");
		AsyUtils.get(Constants.getEvaluationById2, params,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONArray response) {
						mMerchantEvalList = Constants.getJsonArray(response
								.toString());
						handler.sendEmptyMessageDelayed(3, 0);
						super.onSuccess(response);
					}
				});
	}

	private class MyPageChangeListener implements OnPageChangeListener {
		private int oldPosition = 0;

		public void onPageSelected(int position) {
			if (mImageViewIds != null && mImageViewIds.length > 0) {
				currentItem = position;
				mImageViewIds[oldPosition].setImageDrawable(getBaseContext()
						.getResources().getDrawable(R.drawable.ic_dot_true));
//				Log.i("position", position + "==============");
				if (position < mImageViewIds.length) {
					mImageViewIds[position]
							.setImageDrawable(getBaseContext().getResources()
									.getDrawable(R.drawable.ic_dot_fouc));
				}
				oldPosition = position;
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
	}

	private class ScrollTask implements Runnable {
		public void run() {
			synchronized (mViewPager) {
				currentItem = (currentItem + 1) % mViewList.size();
				handler.sendEmptyMessageDelayed(1, 0); // 通过Handler切换图片
			}
		}
	}

	@Override
	protected void onDestroy() {
		System.gc();
		scheduledExecutorService.shutdown();
		super.onDestroy();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}
	
	private boolean isNumber(String str){
		Boolean strResult = str.matches("-?[0-9]+.*[0-9]*");
	    return strResult;
	}

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
}
