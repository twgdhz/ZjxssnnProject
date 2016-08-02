package com.zjxfood.activity;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.appkefu.lib.interfaces.KFAPIs;
import com.appkefu.lib.service.KFMainService;
import com.appkefu.lib.utils.KFLog;
import com.appkefu.smack.util.StringUtils;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.project.util.BitmapCompressionUtils;
import com.project.util.BitmapUtilSingle;
import com.project.util.ZoomBitmap;
import com.zjxfood.application.ExitApplication;
import com.zjxfood.common.Constants;
import com.zjxfood.http.XutilsUtils;
import com.zjxfood.indiana.MyIndianaOrderActivity;
import com.zjxfood.interfaces.MyInterface;
import com.zjxfood.interfaces.MyInterfaceImpl;
import com.zjxfood.popupwindow.HeadPopupWindow;
import com.zjxfood.popupwindow.PhotoPopupWindow;
import com.zjxfood.popupwindow.ServiceCommitmentPopupWindow;
import com.zjxfood.red.RedAwardActivity;
import com.zjxfood.toast.MyToast;
import com.zjxfood.view.Crop_Canvas;
import com.zjxfood.view.RoundRedImageView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * "我的"改版页面
 *
 * @author zjx
 *
 */
public class MyNewActivity extends AppActivity implements OnClickListener {

	//	private LinearLayout mMainLayout, mBuyLayout, mMallLayout,mCarsLayout;
	private Button mLogBtn;
	private TextView mUserName;
	//	private boolean isClick = true;
	private LinearLayout mLogLayout, mNoLogLayout;
	private RoundRedImageView mHeadImage;
	private BitmapUtils mBitmapUtils;
	private ArrayList<HashMap<String, Object>> mUsersNumbersList;
	private Button mCancelBtn;
	private TextView mMyCodeText;
	private LinearLayout mConsumerOrderLayout, mMallOrderLayout,
			mAuctionOrderLayout, mDuobaoOrderLayout;
	private LinearLayout mRedLayout, mDownCodeLayout,
			mCollectionLayout, mCashCouponLayout,
			mMyBalanceLayout, mMyUserLayout,
			mAccountLayout, mServiceProviderLayout;
	private PopupWindow mCurrencyPop;
	private TextView mCurrencyOkText, mCurrencyCancelText;
	private EditText mCurrencyEdit;
	private TextView mCurrencyShow;
	private TextView mSignShowText;
	private Button mSignBtn;
	private HeadPopupWindow mPicPopupWindow;
	private String localTempImgDir = "headPng";
	private String localTempImgFileName = Constants.mId + "ssnn.png";
	private Bitmap upbitmap;
	private PhotoPopupWindow mPhotoPopupWindow;
	private Crop_Canvas mCanvas;
	private MyInterfaceImpl mMyClick;
	private HashMap<String, Object> mHeadMap;
	private int x = 1;
	private RunTask mRunTask;
	private TextView mMyUserText,mMyTouristText;
	private boolean isExit = false;
	private MyToast mToast;
	private LinearLayout mMygoldLayout,mGwsjLayout,mWykdLayout;
	private TextView mUserGrade;
	private ImageView mUserJsImage;
	private ServiceCommitmentPopupWindow mCommitmentPopupWindow;
	private LinearLayout mMainLayout,mBuyLayout,mCarLayout,mMyLayout;
	private ImageView mMyImage;
	private View mMainView;
	private TextView mBottomText;
	private HashMap<String,Object> mUserClearMap;
	private LinearLayout mKefuLayout;
	private ImageView mCarImage;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ExitApplication.getInstance().addActivity(this);
		ExitApplication.getInstance().addMyActivity(this);
		setContentView(R.layout.my_new_layout);
		setImmerseLayout(findViewById(R.id.my_new_activity_id));
		init();
		clickFlag = "my";
		mToast = new MyToast(getApplicationContext());
		mBitmapUtils = BitmapUtilSingle
				.getBitmapInstance(getApplicationContext());
		mBitmapUtils
				.configDefaultLoadFailedImage(R.drawable.hongbao_faile);
		mMyClick = new MyInterfaceImpl();
		mMyClick.setListener(mInterface);
		// 判断是否为登录状态
		if (Constants.onLine == 0) {
			mUserJsImage.setVisibility(View.GONE);
			mLogLayout.setVisibility(View.GONE);
			mNoLogLayout.setVisibility(View.VISIBLE);
			mSignShowText.setVisibility(View.VISIBLE);
			mCancelBtn.setVisibility(View.GONE);
		} else if (Constants.onLine == 1) {
			mUserJsImage.setVisibility(View.VISIBLE);
			mCancelBtn.setVisibility(View.VISIBLE);
			mLogLayout.setVisibility(View.VISIBLE);
			mNoLogLayout.setVisibility(View.GONE);
//			isClick = true;
			mUserName.setText("我的推荐码:" + Constants.mUserCode);
			loadHead();
			mSignShowText.setVisibility(View.GONE);
			if (Constants.mRealname.equals("null")) {
				if (Constants.mIsjh.equals("1")) {
					mUserName.setText(Constants.mUserName + "（会员）");
				} else {
					mUserName.setText(Constants.mUserName + "（游客）");
					getUserClear();
				}
			} else {
				if (Constants.mIsjh.equals("1")) {
					mUserName.setText(Constants.mRealname + "（会员）");
				} else {
					mUserName.setText(Constants.mRealname + "（游客）");
					getUserClear();
				}
			}
			if(!Constants.UserLevelMemo.equals("")){
				mUserGrade.setText(Constants.UserLevelMemo);
			}
			if(!Constants.mUserCode.equals("") && !Constants.mUserCode.equals("null")){
				mMyCodeText.setText("推荐码："+Constants.mUserCode);
			}
		}
		ArrayList<HashMap<String,Object>> mList = new ArrayList<>();
		SharedPreferences sp = getApplication().getSharedPreferences("cars", Context.MODE_PRIVATE);
		String result = sp.getString("cars",null);
		if(result!=null && result.length()>0) {
			Constants.mCarsMap = Constants.getJsonObjectToMap(result);
			for (Map.Entry<String, HashMap<String, Object>> entry : Constants.mCarsMap.entrySet()) {
				Log.i("key", "Key = " + entry.getKey() + ", Value = " + entry.getValue());
				mList.add(entry.getValue());
			}
			if (mList != null && mList.size() > 0) {
				Bitmap bitmap2 = ((BitmapDrawable) mCarImage.getDrawable()).getBitmap();
				mCarImage.setImageBitmap(Constants.createCarsBitmap(bitmap2, mList.size(), getApplicationContext()));
			}
		}
	}
	private void getUserClear() {
		StringBuilder sb = new StringBuilder();
		sb.append("userid="+Constants.mId);

		XutilsUtils.get(Constants.getUserClear, sb,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
					}
					@Override
					public void onSuccess(ResponseInfo<String> res) {
						Log.i("获取游客剩余天数",res.result+"================");
						mUserClearMap = Constants.getJsonObject(res.result);
						if(mUserClearMap!=null && mUserClearMap.size()>0){
							handler.sendEmptyMessageDelayed(3,0);
						}
					}
				});
	}
	private void init() {
		mCarImage = (ImageView) findViewById(R.id.new_car_menu_image);
		mUserJsImage = (ImageView) findViewById(R.id.my_wenhao);
		mGwsjLayout = (LinearLayout) findViewById(R.id.my_new_buy_update_layout);
		mWykdLayout = (LinearLayout) findViewById(R.id.my_new_shop_layout);
		mMainLayout = (LinearLayout) findViewById(R.id.new_main_home_menu_layout);
		mBuyLayout = (LinearLayout) findViewById(R.id.new_main_buy_menu_layout);
		mCarLayout = (LinearLayout) findViewById(R.id.new_my_buy_car);
		mMyLayout = (LinearLayout) findViewById(R.id.new_main_my_menu_layout);
		mBottomText = (TextView) findViewById(R.id.new_main_my_menu_text);
		mBottomText.setTextColor(getResources().getColor(R.color.white));
		mKefuLayout = (LinearLayout) findViewById(R.id.my_new_kefu_layout);

		mMyImage = (ImageView) findViewById(R.id.new_main_my_menu_image);
		mMainView = findViewById(R.id.new_main_main_view4);
		mMyLayout.setBackgroundColor(getResources().getColor(R.color.main_color13));
		mMyImage.setImageDrawable(getResources().getDrawable(R.drawable.new_main_my_hover));
//		mMainView.setVisibility(View.VISIBLE);
//		mMallLayout = (LinearLayout) findViewById(R.id.my_gift_menu_layout);
//		ImageView icon = (ImageView) findViewById(R.id.main_my_menu_image);
//		TextView textView = (TextView) findViewById(R.id.main_my_menu_text);
//		icon.setImageResource(R.drawable.icon_tabbar_mine);
//		textView.setTextColor(getResources().getColor(R.color.main_menu_chose));
		mUserGrade = (TextView) findViewById(R.id.my_user_grade);

		mLogBtn = (Button) findViewById(R.id.my_new_no_log_btn);
		mUserName = (TextView) findViewById(R.id.my_new_log_name);
		mLogLayout = (LinearLayout) findViewById(R.id.my_new_log_btn_layout);
		mNoLogLayout = (LinearLayout) findViewById(R.id.my_new_no_log_btn_left_layout);
		mHeadImage = (RoundRedImageView) findViewById(R.id.my_head_xiaoguo_images);
		mCancelBtn = (Button) findViewById(R.id.my_cancel_btn);
		mMyCodeText = (TextView) findViewById(R.id.my_new_log_text);
		mConsumerOrderLayout = (LinearLayout) findViewById(R.id.my_new_consumption_order_layout);
		mMallOrderLayout = (LinearLayout) findViewById(R.id.my_new_mall_order_layout);
		mAuctionOrderLayout = (LinearLayout) findViewById(R.id.my_new_auction_order_layout);
		mDuobaoOrderLayout = (LinearLayout) findViewById(R.id.my_new_duobao_order_layout);
		mRedLayout = (LinearLayout) findViewById(R.id.my_new_ssred_layout);
//		mCarLayout = (LinearLayout) findViewById(R.id.my_new_car_layout);
		mDownCodeLayout = (LinearLayout) findViewById(R.id.my_new_qr_layout);
		mCollectionLayout = (LinearLayout) findViewById(R.id.my_new_collection_layout);
		mCashCouponLayout = (LinearLayout) findViewById(R.id.my_new_xjgwj_layout);
//		mCashCouponCz = (LinearLayout) findViewById(R.id.my_new_xjgwj_cz_layout);
		mMyBalanceLayout = (LinearLayout) findViewById(R.id.my_new_balance_layout);
//		mBalanceCz = (LinearLayout) findViewById(R.id.my_new_balance_cz_layout);
//		mJhLayout = (LinearLayout) findViewById(R.id.my_new_jh_layout);
		mMyUserLayout = (LinearLayout) findViewById(R.id.my_new_user_layout);
		mAccountLayout = (LinearLayout) findViewById(R.id.my_new_setting_layout);
		mServiceProviderLayout = (LinearLayout) findViewById(R.id.my_new_fuwu_layout);
		mSignShowText = (TextView) findViewById(R.id.my_new_content_title_sign);
		mSignBtn = (Button) findViewById(R.id.my_new_sign_btn);
		mMyUserText = (TextView) findViewById(R.id.my_new_user_text);
		mMyTouristText = (TextView) findViewById(R.id.my_new_user_text2);
		mMygoldLayout = (LinearLayout) findViewById(R.id.my_new_gold_layout);

		mKefuLayout.setOnClickListener(this);
		mUserJsImage.setOnClickListener(this);
		mSignBtn.setOnClickListener(this);
//		mJhLayout.setOnClickListener(this);
		mMyUserLayout.setOnClickListener(this);
		mAccountLayout.setOnClickListener(this);
		mServiceProviderLayout.setOnClickListener(this);
		mMygoldLayout.setOnClickListener(this);

		mGwsjLayout.setOnClickListener(this);
		mWykdLayout.setOnClickListener(this);
		mRedLayout.setOnClickListener(this);
//		mCarLayout.setOnClickListener(this);
		mDownCodeLayout.setOnClickListener(this);
		mCollectionLayout.setOnClickListener(this);
		mCashCouponLayout.setOnClickListener(this);
//		mCashCouponCz.setOnClickListener(this);
		mMyBalanceLayout.setOnClickListener(this);
//		mBalanceCz.setOnClickListener(this);

		mConsumerOrderLayout.setOnClickListener(this);
		mMallOrderLayout.setOnClickListener(this);
		mAuctionOrderLayout.setOnClickListener(this);
		mDuobaoOrderLayout.setOnClickListener(this);

		mCancelBtn.setOnClickListener(this);
		mLogBtn.setOnClickListener(this);
		mHeadImage.setOnClickListener(this);

		mMainLayout.setOnClickListener(click);
		mBuyLayout.setOnClickListener(click);
		mCarLayout.setOnClickListener(click);
		mMyLayout.setOnClickListener(click);
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		Bundle bundle ;
		SharedPreferences sp;
		int version = Integer.valueOf(android.os.Build.VERSION.SDK);
		switch (v.getId()) {
			//客服中心
			case R.id.my_new_kefu_layout:
					startChat();
				break;
			//会员介绍
			case R.id.my_wenhao:
				intent.setClass(getApplicationContext(), WebActivity.class);
				bundle = new Bundle();
				bundle.putString("title","会员介绍");
				bundle.putString("url", "http://mp.weixin.qq.com/s?__biz=MzIwMDQxNzU2OA==&mid=502297074&idx=1&sn=6025d2bc3f0801d4feb4e37fa4ec72e2&scene=23&srcid=0621eoz8qgxDDSsuoaCWjoJy#rd");
				intent.putExtras(bundle);
				startActivity(intent);
				break;
			//我要开店
			case R.id.my_new_shop_layout:
				mCommitmentPopupWindow = new ServiceCommitmentPopupWindow(
						MyNewActivity.this,"请下载食尚男女商户版后，使用个人版帐号密码登录即可。");
				mCommitmentPopupWindow.showAtLocation(mBuyLayout, Gravity.CENTER,
						0, 0);
//				Toast.makeText(getApplicationContext(),"请下载食尚男女商户版后，使用个人版帐号密码登录即可。",Toast.LENGTH_SHORT).show();
				break;
			//购物升级
			case R.id.my_new_buy_update_layout:
				intent.setClass(getApplicationContext(), NewCashMallActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				if(version>5){
					overridePendingTransition(R.animator.activity_zoomin, R.animator.activity_zoomout);
				}

				break;

			//我的金币
			case R.id.my_new_gold_layout:
				if (Constants.onLine == 1) {
					intent.setClass(getApplicationContext(), MyGoldActivity.class);

					startActivity(intent);
				} else {
					Toast.makeText(getApplicationContext(), "请登录后在操作！",
							Toast.LENGTH_SHORT).show();
				}
				break;
			//更换头像
			case R.id.my_head_xiaoguo_images:
				if (Constants.onLine == 1) {
					mPicPopupWindow = new HeadPopupWindow(MyNewActivity.this,
							itemsOnClick);
					mPicPopupWindow.showAtLocation(
							MyNewActivity.this.findViewById(R.id.my_new_activity_id),
							Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
//				isClick = false;
				} else {
					intent.setClass(getApplicationContext(),
							MyUserLogActivity.class);
					startActivity(intent);
					finish();
				}
				break;
			//签到
			case R.id.my_new_sign_btn:
				if (Constants.onLine == 1 && !(Constants.mId.equals(""))) {
					intent.setClass(getApplicationContext(), SignNewActivity.class);
					startActivity(intent);
				} else {
					Toast.makeText(getApplicationContext(), "请先登录用户帐号！",
							Toast.LENGTH_SHORT).show();
				}
				break;
			// 激活
//		case R.id.my_new_jh_layout:
//			if (Constants.onLine == 1) {
//				intent.setClass(getApplicationContext(), MyJhActivity.class);
//				startActivity(intent);
//			} else {
//				Toast.makeText(getApplicationContext(), "请登录后在操作！",
//						Toast.LENGTH_SHORT).show();
//			}
//			break;
			// 我的会员
			case R.id.my_new_user_layout:
				if (Constants.onLine == 1) {
					intent.setClass(getApplicationContext(), MyMemberActivity.class);

					startActivity(intent);
				} else {
					Toast.makeText(getApplicationContext(), "请登录后在操作！",
							Toast.LENGTH_SHORT).show();
				}
				break;
			// 我的账户
			case R.id.my_new_setting_layout:
				if (Constants.onLine == 1 && !(Constants.mId.equals(""))) {
					intent.setClass(getApplicationContext(), MyUserActivity.class);
					startActivity(intent);
				} else {
					Toast.makeText(getApplicationContext(), "请先登录用户帐号！",
							Toast.LENGTH_SHORT).show();
				}
				break;
			// 我的服务商
			case R.id.my_new_fuwu_layout:
				if (Constants.onLine == 1 && !(Constants.mId.equals(""))) {
					intent.setClass(getApplicationContext(),
							ServiceProviderActivity.class);
					startActivity(intent);
				} else {
					Toast.makeText(getApplicationContext(), "请先登录用户帐号！",
							Toast.LENGTH_SHORT).show();
				}
				break;
			// 食尚红包
			case R.id.my_new_ssred_layout:
				if (Constants.onLine == 1 && !(Constants.mId.equals(""))) {
					intent.setClass(getApplicationContext(), RedAwardActivity.class);
					startActivity(intent);
				} else {
					Toast.makeText(getApplicationContext(), "请先登录用户帐号！",
							Toast.LENGTH_SHORT).show();
				}
				break;
			// 2元购车
//		case R.id.my_new_car_layout:
//			if (Constants.onLine == 1 && !(Constants.mId.equals(""))) {
//				intent.setClass(getApplicationContext(),
//						CashMyIndianaOrderActivity.class);
//				startActivity(intent);
//			} else {
//				Toast.makeText(getApplicationContext(), "请先登录用户帐号！",
//						Toast.LENGTH_SHORT).show();
//			}
//			break;
			// 下载二维码
			case R.id.my_new_qr_layout:
				intent.setClass(getApplicationContext(), MyQrCodeActivity.class);
				startActivity(intent);
				break;
			// 我的收藏
			case R.id.my_new_collection_layout:
				if (Constants.onLine == 1 && !(Constants.mId.equals(""))) {
					intent.setClass(getApplicationContext(),
							MyFavoriteActivity.class);
					startActivity(intent);
				} else {
					Toast.makeText(getApplicationContext(), "请先登录用户帐号！",
							Toast.LENGTH_SHORT).show();
				}
				break;
			// 我的食尚币
			case R.id.my_new_xjgwj_layout:
				if (Constants.onLine == 1) {
//				intent.setClass(getApplicationContext(),
//						MyRestaurantBiActivity.class);
					intent.setClass(getApplicationContext(),
							MyShmoneyActivity.class);
					startActivity(intent);
				} else {
					Toast.makeText(getApplicationContext(), "请登录后在操作！",
							Toast.LENGTH_SHORT).show();
				}
				break;
//			break;
			// 我的余额
			case R.id.my_new_balance_layout:
				if (Constants.onLine == 1 && !(Constants.mId.equals(""))) {
//				intent.setClass(getApplicationContext(),
//						MyCurrencyActivity.class);
					intent.setClass(getApplicationContext(),
							MyNewCurrencyActivity.class);

					startActivity(intent);
				} else {
					Toast.makeText(getApplicationContext(), "请先登录用户帐号！",
							Toast.LENGTH_SHORT).show();
				}
				break;
			// 余额充值
//		case R.id.my_new_balance_cz_layout:
//			sp = getApplicationContext().getSharedPreferences("余额充值",
//					MODE_PRIVATE);
//
//			try {
//				LayoutInflater inflater = LayoutInflater
//						.from(getApplicationContext());
//				View view = inflater.inflate(R.layout.currency_cz_popup, null);
//				mCurrencyShow = (TextView) view
//						.findViewById(R.id.currency_show_title_show);
//				mCurrencyOkText = (TextView) view
//						.findViewById(R.id.currency_cz_ok);
//				mCurrencyCancelText = (TextView) view
//						.findViewById(R.id.currency_cz_cancel);
//				mCurrencyEdit = (EditText) view
//						.findViewById(R.id.display_popup_chujia_value);
//				mCurrencyPop = new PopupWindow(view, LayoutParams.MATCH_PARENT,
//						LayoutParams.MATCH_PARENT, false);
//				mCurrencyPop.setBackgroundDrawable(new BitmapDrawable());
//				mCurrencyPop.setOutsideTouchable(true);
//				mCurrencyPop.setFocusable(true);
//				ColorDrawable dw = new ColorDrawable(0xb0000000);
//				// 设置SelectPicPopupWindow弹出窗体的背景
//				mCurrencyPop.setBackgroundDrawable(dw);
//				// 设置SelectPicPopupWindow弹出窗体动画效果
//				mCurrencyPop.setAnimationStyle(R.style.AnimTop_miss);
//				mCurrencyPop
//						.showAtLocation(mCashCouponCz, Gravity.CENTER, 0, 0);
//				mCurrencyOkText.setOnClickListener(clickListener);
//				mCurrencyCancelText.setOnClickListener(clickListener);
//
//				if (sp != null && sp.getString("Content", "") != null
//						&& !sp.getString("Content", "").toString().equals("")) {
//					mCurrencyShow.setText(sp.getString("Content", ""));
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			break;
			// 消费订单
			case R.id.my_new_consumption_order_layout:
				if (Constants.onLine == 1 && !(Constants.mId.equals(""))) {
					intent.setClass(getApplicationContext(), MyOrderActivity.class);
					startActivity(intent);
				} else {
					Toast.makeText(getApplicationContext(), "请先登录用户帐号！",
							Toast.LENGTH_SHORT).show();
				}
				break;
			// 商城订单
			case R.id.my_new_mall_order_layout:
				if (Constants.onLine == 1 && !(Constants.mId.equals(""))) {
					intent.setClass(getApplicationContext(),
							MallOrderActivity.class);
//				intent.setClass(getApplicationContext(),
//						MallNewOrderActivity.class);
					startActivity(intent);
				} else {
					Toast.makeText(getApplicationContext(), "请登录后操作！",
							Toast.LENGTH_SHORT).show();
				}
				break;
			// 竞拍订单
			case R.id.my_new_auction_order_layout:
				if (Constants.onLine == 1 && !(Constants.mId.equals(""))) {
					intent.setClass(getApplicationContext(),
							MyAuctionActivity.class);
					startActivity(intent);
				} else {
					Toast.makeText(getApplicationContext(), "请先登录用户帐号！",
							Toast.LENGTH_SHORT).show();
				}
				break;
			// 夺宝订单
			case R.id.my_new_duobao_order_layout:
				if (Constants.onLine == 1 && !(Constants.mId.equals(""))) {
					intent.setClass(getApplicationContext(),
							MyIndianaOrderActivity.class);
					startActivity(intent);
				} else {
					Toast.makeText(getApplicationContext(), "请先登录用户帐号！",
							Toast.LENGTH_SHORT).show();
				}
				break;
			// 退出登录
			case R.id.my_cancel_btn:
				Constants.clearUserInfo();
				sp = getApplicationContext().getSharedPreferences("MyAccounts",
						MODE_PRIVATE);
				Editor editor = sp.edit();
				editor.clear();
				editor.commit();
				intent.setClass(getApplicationContext(), MyUserLogActivity.class);
				startActivity(intent);

				finish();
				break;
			//购物车
			case R.id.main_cars_layout:
//
				break;
			// 登录
			case R.id.my_new_no_log_btn:
				intent.setClass(getApplicationContext(), MyUserLogActivity.class);
				startActivity(intent);
				finish();
				break;
			// 首页
			case R.id.main_home_menu_layout:
//				intent.setClass(getApplicationContext(), MainActivity.class);
//				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//				intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//				startActivity(intent);
//				if (version > 5) {
//					overridePendingTransition(R.animator.activity_zoomin,
//							R.animator.activity_zoomout);
//				}
				break;
			// 买单
//		case R.id.main_buy_menu_layout:
//			intent.setClass(getApplicationContext(), BuyBillActivity.class);
//			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			startActivity(intent);
//			if (version > 5) {
//				overridePendingTransition(R.animator.activity_zoomin,
//						R.animator.activity_zoomout);
//			}
//			break;
			// 商城
//		case R.id.my_gift_menu_layout:
//			dissMallPop(MyNewActivity.this,mMallLayout);
//			intent.setClass(getApplicationContext(), MallIndexActivity.class);
//			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			startActivity(intent);
//			if (version > 5) {
//				overridePendingTransition(R.animator.activity_zoomin,
//						R.animator.activity_zoomout);
//			}
//			break;

		}
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
				case 1:
					if(mUsersNumbersList.get(0).get("jhnum")!=null){
						mMyUserText.setText("我的会员("+mUsersNumbersList.get(0).get("jhnum")+")");
					}
					if(mUsersNumbersList.get(0).get("nojhnum")!=null){
						mMyTouristText.setText("/游客("+mUsersNumbersList.get(0).get("nojhnum")+")");
					}
					break;

				case 2:
					loadHead();
					Toast.makeText(getApplicationContext(),
							mHeadMap.get("msg").toString(), Toast.LENGTH_SHORT)
							.show();
					break;
				//
				case 3:
					if(mUserClearMap.get("Code")!=null && mUserClearMap.get("Code").toString().equals("200")){
						if(mUserClearMap.get("Message")!=null) {
							mCommitmentPopupWindow = new ServiceCommitmentPopupWindow(
									MyNewActivity.this, mUserClearMap.get("Message").toString());
							mCommitmentPopupWindow.showAtLocation(mBuyLayout, Gravity.CENTER,
									0, 0);
						}
					}
					break;
			}
		};
	};

	private void getUsers() {
		StringBuilder sb = new StringBuilder();
		sb.append("uid=" + Constants.mId);
		XutilsUtils.get(Constants.getmyusernum2, sb,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
					}
					@Override
					public void onSuccess(ResponseInfo<String> res) {
						try {
							mUsersNumbersList = Constants
									.getJsonArrayByData(res.result);
						} catch (JSONException e) {
							e.printStackTrace();
						}
						Log.i("我的信息", mUsersNumbersList
								+ "======================");
						if(mUsersNumbersList!=null && mUsersNumbersList.size()>0){
							handler.sendEmptyMessageDelayed(1, 0);
						}
					}
				});
	}

	private void loadHead() {
		if (Constants.headPath != null && !(Constants.headPath.equals(""))) {
			Log.i("mHeadShowImage", Constants.headPath + "================");
			mBitmapUtils.display(mHeadImage, Constants.headPath);
		} else {
			mHeadImage.setImageResource(R.drawable.touxiang);
		}
	}

	// 消费币充值
	OnClickListener clickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			switch (v.getId()) {
				case R.id.currency_cz_ok:
					if (!mCurrencyEdit.getText().toString().equals("")) {
						intent.setClass(getApplicationContext(),
								CurrencyPayWayActivity.class);
						Bundle bundle = new Bundle();
						bundle.putString("price", mCurrencyEdit.getText()
								.toString());
						bundle.putString("merchantName", "xfb");
						intent.putExtras(bundle);
						startActivity(intent);
						if (mCurrencyPop != null && mCurrencyPop.isShowing()) {
							mCurrencyPop.dismiss();
						}
					}
					break;
				case R.id.currency_cz_cancel:
					if (mCurrencyPop != null && mCurrencyPop.isShowing()) {
						mCurrencyPop.dismiss();
					}
					break;
			}
		}
	};
	// 为弹出窗口实现监听类
	private OnClickListener itemsOnClick = new OnClickListener() {
		public void onClick(View v) {
			mPicPopupWindow.dismiss();
			Intent intent = new Intent();
			switch (v.getId()) {
				// 拍照
				case R.id.head_pupup_btn_take_photo:
//					isClick = true;
					Log.i("拍照","=============拍照============");
					try {
						if (Constants.onLine == 1) {
							String status = Environment.getExternalStorageState();
							if (status.equals(Environment.MEDIA_MOUNTED)) {
								try {
									File dir = new File(
											Environment.getExternalStorageDirectory()
													+ "/" + localTempImgDir);
									if (!dir.exists())
										dir.mkdirs();
									intent = new Intent(
											android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
									File f = new File(dir, localTempImgFileName);// localTempImgDir和localTempImageFileName是自己定义的名字
									Uri u = Uri.fromFile(f);
									intent.putExtra(
											MediaStore.Images.Media.ORIENTATION, 0);
									intent.putExtra(MediaStore.EXTRA_OUTPUT, u);
									startActivityForResult(intent, 1);
								} catch (ActivityNotFoundException e) {
									// TODO Auto-generated catch block
									Toast.makeText(MyNewActivity.this, "没有找到储存目录",
											Toast.LENGTH_LONG).show();
								}
							} else {
								Toast.makeText(MyNewActivity.this, "没有储存卡",
										Toast.LENGTH_LONG).show();
							}
						} else {
							Toast.makeText(getApplicationContext(), "请先登录用户帐号！",
									Toast.LENGTH_SHORT).show();
						}
					}catch (Exception e){

					}
					break;
				// 相册
				case R.id.head_pupup_btn_pick_photo:
//					isClick = true;

					/* 开启Pictures画面Type设定为image */
					intent.setType("image/*");
					/* 使用Intent.ACTION_GET_CONTENT这个Action */
					intent.setAction(Intent.ACTION_GET_CONTENT);
					/* 取得相片后返回本画面 */
					startActivityForResult(intent, 2);
					break;
				default:
					break;
			}
		}
	};
	// 调用系统照相机拍照
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case 1:
				Log.i("tag", data + "============-1==============" + resultCode);
				try {
					if (data != null) {
						String sdStatus = Environment.getExternalStorageState();
						if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
							Log.i("TestFile",
									"SD card is not avaiable/writeable right now.");
							return;
						}
						if (resultCode == -1) {
							String mpath = "/sdcard/headPng/" + localTempImgFileName;
							if (!localTempImgFileName.equals("")) {
								Log.i("mpath", mpath + "===============");

								upbitmap = BitmapCompressionUtils.getBitmap(mpath);
								upbitmap = ZoomBitmap.zoomImage(upbitmap,
										upbitmap.getWidth() / 2, upbitmap.getHeight() / 2);
								FileOutputStream b = null;
								File file = new File("/sdcard/headPng/");
								if (file.exists() && file.isDirectory()) {
									file.delete();
									file.mkdirs();// 创建新的文件夹
								} else {
									file.mkdirs();// 创建新的文件夹
								}
								try {
									String fileName = "/sdcard/headPng/" + localTempImgFileName;
									b = new FileOutputStream(fileName);
								} catch (FileNotFoundException e) {
									e.printStackTrace();
								}
								upbitmap.compress(Bitmap.CompressFormat.PNG, 100, b);
								mPhotoPopupWindow = new PhotoPopupWindow(MyNewActivity.this,
										upbitmap, mCanvas, mMyClick);
								mPhotoPopupWindow.showAtLocation(mHeadImage, Gravity.CENTER, 0,
										0);
							}
						}
					} else {
						Toast.makeText(this, "图片没找到", Toast.LENGTH_SHORT).show();
					}
				}catch (Exception e){

				}
				break;

			case 2:
				Log.i("tag", data + "============2==============");
				if (data != null) {
					Uri uri = data.getData();
					String path = "";

					if (!TextUtils.isEmpty(uri.getAuthority())) {
						Cursor cursor = getContentResolver().query(uri,
								new String[] { MediaStore.Images.Media.DATA },
								null, null, null);
						if (null == cursor) {
							return;
						}
						cursor.moveToFirst();
						path = cursor.getString(cursor
								.getColumnIndex(MediaStore.Images.Media.DATA));
						cursor.close();
					} else {
						path = uri.getPath();
					}
					if (path != null) {
						upbitmap = BitmapCompressionUtils.getBitmap(path);

						mPhotoPopupWindow = new PhotoPopupWindow(MyNewActivity.this,
								upbitmap, mCanvas, mMyClick);
						mPhotoPopupWindow.showAtLocation(mHeadImage,
								Gravity.CENTER, 0, 0);
						Log.i("bitmapPath", path + "======================");
					}else {
						Toast.makeText(this, "该手机暂不支持本地图片上传", Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(this, "图片没找到", Toast.LENGTH_SHORT).show();
				}
				break;
		}
	};
	private MyInterface mInterface = new MyInterface() {
		@Override
		public void onclick(Bitmap bitmap) {
			FileOutputStream b = null;
			String headPath = null;
			File file = new File("/sdcard/headPng/");
			if (file.exists() && file.isDirectory()) {
				file.delete();
				file.mkdirs();// 创建新的文件夹
			} else {
				file.mkdirs();// 创建新的文件夹
			}
			try {
				headPath = "/sdcard/headPng/" + localTempImgFileName;
				b = new FileOutputStream(headPath);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, b);
			if (headPath != null) {
				x = 3;
				mRunTask = new RunTask();
				mRunTask.execute(headPath);
			}
		}
		@Override
		public void updateOrder(
				HashMap<String, ArrayList<HashMap<String, Object>>> map,float price) {
			// TODO Auto-generated method stub

		}
	};
	class RunTask extends AsyncTask<String, Integer, String> {
		@Override
		protected String doInBackground(String... str) {
			switch (x) {
				case 3:
					try {
						uploadHead(str[0]);
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;
			}
			return null;
		}
	}
	// 上传图片
	private void uploadHead(String path) throws Exception {
		HttpClient httpclient = new DefaultHttpClient();
		String sign = Constants.sortsStr("userid=" + Constants.mId);
		Log.i("sign", sign + "=================");
		HttpPost httpPost = new HttpPost(Constants.upload + sign + "&userid="
				+ Constants.mId);
		MultipartEntity mulentity = new MultipartEntity(
				HttpMultipartMode.BROWSER_COMPATIBLE);
		// mulentity.addPart("foodname", new StringBody("ssnn"));
		// mulentity.addPart("foodstyle", new StringBody("type"));
		// mulentity.addPart("price", new StringBody("123"));

		// 添加图片表单数据
		FileBody filebody = new FileBody(new File(path));
		mulentity.addPart("foodimg", filebody);
		// mulentity.addPart("foodtab", new StringBody("123"));
		// mulentity.addPart("state", new StringBody("1"));
		httpPost.setEntity(mulentity);
		HttpResponse response = httpclient.execute(httpPost);
		HttpEntity resEntity = response.getEntity();
		String res = EntityUtils.toString(resEntity, "utf-8");
		Log.i("response", response.getEntity() + "==========上传消息==========="
				+ res);
		mHeadMap = Constants.getJsonObject(res);
		if (mHeadMap.get("success").toString().equals("true")) {
			mBitmapUtils.clearCache();
			mBitmapUtils.clearDiskCache();
			mBitmapUtils.clearMemoryCache();
			Constants.headPath = mHeadMap.get("src").toString();
		}
		handler.sendEmptyMessageDelayed(2, 0);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
			case KeyEvent.KEYCODE_BACK:
				System.gc();
				exit();
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
//				Toast.makeText(getApplicationContext(), "再按一次退出程序",
//						Toast.LENGTH_SHORT).show();
			// 利用handler延迟发送更改状态信息
			handler.sendEmptyMessageDelayed(0, 2000);
		} else {
			ExitApplication.getInstance().exit();
//				System.exit(0);
		}
	}
	// 1.咨询人工客服
	private void startChat() {
		//
		KFAPIs.startChat(this,
				"storeservices", // 1. 客服工作组ID(请务必保证大小写一致)，请在管理后台分配
				"食尚客服", // 2. 会话界面标题，可自定义
				"", // 3. 附加信息，在成功对接客服之后，会自动将此信息发送给客服;
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
	//监听：连接状态、即时通讯消息、客服在线状态
	private BroadcastReceiver mXmppreceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			//监听：连接状态
			if (action.equals(KFMainService.ACTION_XMPP_CONNECTION_CHANGED))//监听链接状态
			{
//                updateStatus(intent.getIntExtra("new_state", 0));
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
}
