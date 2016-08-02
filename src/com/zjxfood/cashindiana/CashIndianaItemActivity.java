package com.zjxfood.cashindiana;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.project.util.BitmapUtilSingle;
import com.project.util.ScreenUtils;
import com.zjxfood.activity.AppActivity;
import com.zjxfood.activity.AuctionDetailActivity;
import com.zjxfood.activity.R;
import com.zjxfood.application.ExitApplication;
import com.zjxfood.common.Constants;
import com.zjxfood.http.XutilsUtils;
import com.zjxfood.popupwindow.CjSuccessPopupWindow;
import com.zjxfood.view.MyScrollListView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 奖品详情
 * 
 * @author zjx
 * 
 */
public class CashIndianaItemActivity extends AppActivity implements
		OnClickListener {

	private ImageView mBackImage;
	private ImageView mMallImage;
	private TextView mMyIndianaText;// 我的夺宝
	private Bundle mBundle;
	private String mId;
	private HashMap<String, Object> mDetailMap, mCjMap;
	private TextView mNameText, mPriceText, mNumLeastText, mTotalPersonText;
	private BitmapUtils mBitmapUtils;
	private Button mCjButton;
	private CjSuccessPopupWindow mSuccessPopupWindow;
	private boolean isClick = true;
	private float num = 0;
	private ProgressBar mBar;
	// private MyProgressBar myProgressBar;
	private int x = 1;
	// private int n = 1;
	private LinearLayout mDetailLayout;
	private LinearLayout mRuleLayout;
	private MyScrollListView mScrollListView;
	private HashMap<String, Object> mLotterMap;
	private ArrayList<HashMap<String, Object>> mLotterList, mAddList;
	private int page = 1;
	private CashMyIndianaInfoAdapter mIndianaInfoAdapter;
	private TextView mMoreText;
	private TextView mStateText;
	private HashMap<String, Object> mStateMap;
	private TextView mNumLimitText;
	private LinearLayout mStateLayout;
	private PopupWindow mPayWindow;
	private TextView mOkText, mCancelText;
	private EditText mCashPriceEdit;
	private TextView mCashCishuText;
	private String mIsStart="";

	// private WebView mWebView;

	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ExitApplication.getInstance().addActivity(this);
		setContentView(R.layout.cash_indiana_info_layout);
		setImmerseLayout(findViewById(R.id.head_layout));
		mBitmapUtils = BitmapUtilSingle
				.getBitmapInstance(getApplicationContext());
		init();
		mBundle = getIntent().getExtras();
		if (mBundle != null) {
			mId = mBundle.getString("id");
			mIsStart = mBundle.getString("isStart");
		}
		if (mId != null) {
			getIndianaDetail();
		}
		// mWebView.loadUrl("http://api.zjxssnn.com/rules/lucky.html");
	};

	private void init() {
		mBackImage = (ImageView) findViewById(R.id.indana_info_back_info_image);
		mMallImage = (ImageView) findViewById(R.id.indiana_info_head_image);
		LayoutParams params = mMallImage.getLayoutParams();
		params.height = (int) (ScreenUtils
				.getScreenWidth(getApplicationContext()) * (0.75));
		mMallImage.setLayoutParams(params);
		mMyIndianaText = (TextView) findViewById(R.id.title_indiana_info_my);
		mNameText = (TextView) findViewById(R.id.indiana_info_name_text);
		mPriceText = (TextView) findViewById(R.id.indiana_info_price_text);
		mNumLeastText = (TextView) findViewById(R.id.indiana_people_num_text);
		mTotalPersonText = (TextView) findViewById(R.id.indiana_people_num_shengyu_text);
		mCjButton = (Button) findViewById(R.id.cash_indiana_info_cj_btn);
		mBar = (ProgressBar) findViewById(R.id.indiana_info_progress_bar);
		mDetailLayout = (LinearLayout) findViewById(R.id.indiana_detail_layout);
		mRuleLayout = (LinearLayout) findViewById(R.id.indiana_detail_rule_layout);
		mScrollListView = (MyScrollListView) findViewById(R.id.indiana_info_all_list);
		mMoreText = (TextView) findViewById(R.id.indiana_rule_more_text);
		mStateText = (TextView) findViewById(R.id.indiana_info_state_text_value);
		mNumLimitText = (TextView) findViewById(R.id.indiana_info_numlimit_text);
		mStateLayout = (LinearLayout) findViewById(R.id.indiana_info_state_layout);
		// mWebView = (WebView) findViewById(R.id.indiana_info_web);
//
		mCjButton.setOnClickListener(this);
		mBackImage.setOnClickListener(this);
		mMyIndianaText.setOnClickListener(this);
		mDetailLayout.setOnClickListener(this);
		mRuleLayout.setOnClickListener(this);
		mMoreText.setOnClickListener(this);
	}

	// 获取奖品详情
	private void getIndianaDetail() {
		StringBuilder sb = new StringBuilder();
		sb.append("Id=" + mId);
		XutilsUtils.get(Constants.getCashDetail, sb,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
					}
					@Override
					public void onSuccess(ResponseInfo<String> res) {
						mDetailMap = Constants.getJsonObjectByData(res.result);
						if (mDetailMap != null && mDetailMap.size() > 0) {
							Log.i("现金抽奖列表详情", mDetailMap+"===============");
							handler.sendEmptyMessageDelayed(1, 0);
						}
					}
				});
	}

	// 获取所有抽奖号
	private void getAllLotteryNumber() {
		StringBuilder sb = new StringBuilder();
		sb.append("gameId=" + mId + "&userId=0" + "&pageSize=50"
				+ "&currentPage=" + page);
		XutilsUtils.get(Constants.getCashJoinList, sb,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
					}

					@Override
					public void onSuccess(ResponseInfo<String> res) {
						mLotterMap = Constants.getJsonObjectByData(res.result);
						// Log.i("抽奖号码", res.result+"==============");
						if (mLotterMap != null && mLotterMap.size() > 0
								&& mLotterMap.get("rows") != null) {
							mLotterList = Constants.getJsonArray(mLotterMap
									.get("rows").toString());
							handler.sendEmptyMessageDelayed(3, 0);
						}
					}
				});
	}

//	// 抽奖
//	private void getCj() {
//		StringBuilder sb = new StringBuilder();
//		sb.append("userId=" + Constants.mId + "&gameId=" + mId);
//		XutilsUtils.get(Constants.getCj, sb, new RequestCallBack<String>() {
//			@Override
//			public void onFailure(HttpException arg0, String arg1) {
//				isClick = true;
//			}
//			@Override
//			public void onSuccess(ResponseInfo<String> res) {
//				mCjMap = Constants.getJsonObject(res.result);
//				if (mCjMap != null && mCjMap.size() > 0) {
//					handler.sendEmptyMessageDelayed(2, 0);
//				}
//				isClick = true;
//			}
//		});
//	}

	// 获取50个时间之和
	private void getLastSum() {
		StringBuilder sb = new StringBuilder();
		sb.append("gameId=" + mId);
		XutilsUtils.get(Constants.getCashLastSum, sb,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
					}

					@Override
					public void onSuccess(ResponseInfo<String> res) {
						mStateMap = Constants.getJsonObject(res.result);
						if (mStateMap != null && mStateMap.size() > 0) {
							if (mStateMap.get("Code").toString().equals("200")) {
								handler.sendEmptyMessageDelayed(4, 0);
							}
						}
						// if (mDetailMap != null && mDetailMap.size() > 0) {
						// handler.sendEmptyMessageDelayed(1, 0);
						// }
					}
				});
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				
				if(mIsStart.equals("true")){
					mCjButton.setVisibility(View.VISIBLE);
				}else if(mIsStart.equals("false")){
					if(mDetailMap!=null && mDetailMap.get("LuckyNum")!=null && !mDetailMap.get("LuckyNum").toString().equals("0")){
						mCjButton.setVisibility(View.GONE);
					}else{
						mCjButton.setText("未开始");
					}
				}
				if (x == 1) {
					if (mDetailMap.get("ImgUrl") != null) {
						mBitmapUtils.display(mMallImage,
								mDetailMap.get("ImgUrl").toString());
					}
					if (mDetailMap.get("ProductName") != null) {
						mNameText.setText("【第" + mId + "期】"
								+ mDetailMap.get("ProductName").toString());
					}
					java.text.DecimalFormat df = new java.text.DecimalFormat(
							"#.00");
					if (mDetailMap.get("RMB") != null) {
						// Double price =
						// Double.parseDouble(mDetailMap.get("Price")
						// .toString());
						// mPriceText.setText(df.format(price) + "食尚币/次");
						mPriceText.setText(mDetailMap.get("RMB") + "元/次");
					}
					if (mDetailMap.get("NumLeast") != null) {
						mNumLeastText.setText("总需"
								+ mDetailMap.get("NumLeast").toString() + "人次");
					}
				}
				if (mDetailMap.get("TotalPerson") != null) {
					mTotalPersonText.setText("已达"
							+ mDetailMap.get("TotalPerson").toString() + "人次");
				}
				if (null != mDetailMap.get("NumLeast")
						&& null != mDetailMap.get("TotalPerson")) {
					num = (Float.parseFloat(mDetailMap.get("TotalPerson")
							.toString()))
							/ (Float.parseFloat(mDetailMap.get("NumLeast")
									.toString()));
					mBar.setProgress((int) (num * 100));
					// Log.i("num",
					// num+"==============="+mDetailMap.get("TotalPerson").toString()+"==="+mDetailMap.get("NumLeast"));
				}
				if (mDetailMap.get("NumLimit") != null) {
//					mNumLimitText.setText("每人限抽" + mDetailMap.get("NumLimit")
//							+ "次");
				}
				if (Constants.onLine == 1) {
					getAllLotteryNumber();
				}
				if (mDetailMap.get("HasCaculateNum") != null) {
					Log.i("HasCaculateNum", mDetailMap.get("HasCaculateNum")
							+ "===========");
					if (mDetailMap.get("HasCaculateNum").toString()
							.equals("true")) {
						if (mDetailMap.get("LuckyNum") != null) {
							if (mDetailMap.get("LuckyNum").toString()
									.equals("0")) {
								mStateText.setText("未达到开奖条件，所有参与用户花费的食尚币已退回。");
							} else {
								getLastSum();
							}
						}
					}
				}
				break;

			case 2:

				if (mCjMap.get("Code") != null
						&& mCjMap.get("Code").toString().equals("200")
						&& mCjMap.get("Data") != null) {
					try {
						mSuccessPopupWindow = new CjSuccessPopupWindow(
								CashIndianaItemActivity.this, mCjMap
										.get("Data").toString(), clickListener);
						mSuccessPopupWindow.showAtLocation(mBackImage,
								Gravity.CENTER, 0, 0);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					if (mCjMap.get("Code") != null
							&& mCjMap.get("Message") != null) {
						Toast.makeText(getApplicationContext(),
								mCjMap.get("Message").toString(),
								Toast.LENGTH_SHORT).show();
					}
				}
				break;
			case 3:
				if(mLotterList!=null && mLotterList.size()>0){
				mIndianaInfoAdapter = new CashMyIndianaInfoAdapter(
						getApplicationContext(), mLotterList, mDetailMap.get(
								"LuckyNum").toString());
				mScrollListView.setAdapter(mIndianaInfoAdapter);
				}
				break;
			case 4:
				mStateLayout.setVisibility(View.VISIBLE);
				String sum = mStateMap.get("Data").toString();
				String caculateRule = mDetailMap.get("CaculateRule").toString();
				String totalPerson = mDetailMap.get("TotalPerson").toString();
				String luckyNum = mDetailMap.get("LuckyNum").toString();
				mStateText.setText("(" + (sum + "+" + caculateRule) + ")%"
						+ totalPerson + "+" + "10000001" + "=" + luckyNum);
				break;
			}
		};
	};

	OnClickListener clickListener = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			if (mSuccessPopupWindow != null && mSuccessPopupWindow.isShowing()) {
				mSuccessPopupWindow.dismiss();
				x = 2;
				getIndianaDetail();
			}
		}
	};

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		Bundle bundle;
		switch (v.getId()) {
		// 更多
		case R.id.indiana_rule_more_text:
			if (Constants.onLine == 1) {
				intent.setClass(getApplicationContext(),
						CashIndianaMoreLotteryActivity.class);
				bundle = new Bundle();
				if (mDetailMap != null && mDetailMap.size() > 0
						&& mDetailMap.get("LuckyNum") != null) {
					bundle.putString("luckyNum", mDetailMap.get("LuckyNum")
							.toString());
				}
				bundle.putString("id", mId);

				intent.putExtras(bundle);
				startActivity(intent);
			} else {
				Toast.makeText(getApplicationContext(), "请先登录帐号",
						Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.indiana_detail_rule_layout:
			intent.setClass(getApplicationContext(),
					CashIndianRuleActivity.class);
			startActivity(intent);
			break;
		// 奖品详情
		case R.id.indiana_detail_layout:
			if (mDetailMap != null && mDetailMap.size() > 0) {
				if (mDetailMap.get("Detail") != null) {
					intent.setClass(getApplicationContext(),
							AuctionDetailActivity.class);
					bundle = new Bundle();
					bundle.putString("detail", mDetailMap.get("Detail")
							.toString());
					intent.putExtras(bundle);
					startActivity(intent);
				} else {
					Toast.makeText(getApplicationContext(), "该商品暂时没有详情介绍",
							Toast.LENGTH_SHORT).show();
				}
			}
			break;
		case R.id.indana_info_back_info_image:
			finish();
			break;
		// 我的夺宝
		case R.id.title_indiana_info_my:
			if (Constants.onLine == 1) {
				intent.setClass(getApplicationContext(),
						CashMyIndianaListActivity.class);
				bundle = new Bundle();
				bundle.putString("gameId", mId);
				intent.putExtras(bundle);
				startActivity(intent);
			} else {
				Toast.makeText(getApplicationContext(), "请先登录帐号",
						Toast.LENGTH_SHORT).show();
			}
			break;
		// 抽奖
		case R.id.cash_indiana_info_cj_btn:

			if (Constants.onLine == 1) {
				if(mCjButton.getText().toString().equals("立即抽奖")){
				LayoutInflater inflater = LayoutInflater
						.from(getApplicationContext());
				View view = inflater.inflate(R.layout.cash_price_popup,
						null);
				mOkText = (TextView) view.findViewById(R.id.merchant_ok_text);
				mCancelText = (TextView) view
						.findViewById(R.id.merchant_ok_cancel);
				mCashCishuText = (TextView) view
						.findViewById(R.id.display_popup_chujia_text);
//				mCashCishuText.setVisibility(View.GONE);
				mCashPriceEdit = (EditText) view
						.findViewById(R.id.merchant_price_edit);
//				mCashPriceEdit.setHint("请输入抽奖次数");
				mPayWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT,
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
				mOkText.setOnClickListener(payClick);
				mCancelText.setOnClickListener(payClick);
				}else if(mCjButton.getText().toString().equals("未开始")){
					Toast.makeText(getApplicationContext(), "抽奖未开始",
							Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(getApplicationContext(), "请先登录帐号",
						Toast.LENGTH_SHORT).show();
			}
			break;
		}
	}

	OnClickListener payClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			switch (v.getId()) {
			case R.id.merchant_ok_text:
				if (mCashPriceEdit.getText().toString() != null
						&& !mCashPriceEdit.getText().toString().equals("")) {
					if (isCashTrue(mCashPriceEdit.getText().toString())) {
						try{
						intent.setClass(getApplicationContext(),
								CashPayWayActivity.class);
						Bundle bundle = new Bundle();
						
						float sum = Integer.parseInt(mCashPriceEdit.getText().toString())*Float.parseFloat(mDetailMap.get("RMB").toString());
						
//						bundle.putString("price", mCashPriceEdit.getText().toString());
						bundle.putString("price", sum+"");
//						bundle.putString("price", "0.01");
						bundle.putString("merchantName", "2元购车");
						bundle.putString("totalNum", mCashPriceEdit.getText().toString());
						bundle.putString("id", mId);
						intent.putExtras(bundle);
						startActivity(intent);
						}catch (Exception e) {
							e.printStackTrace();
						}
					} else {
						Toast.makeText(getApplicationContext(),
								"请输入有效的整数", Toast.LENGTH_SHORT).show();
					}
				}
				if (mPayWindow != null && mPayWindow.isShowing()) {
					mPayWindow.dismiss();
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

	private boolean isCashTrue(String price) {
		try {
			int temp = Integer.valueOf(price);// 把字符串强制转换为数字
			if (temp >=1) {
				return true;
			} else {
				return false;
			}
//			return true;// 如果是数字，返回True
		} catch (Exception e) {
			return false;// 如果抛出异常，返回False
		}
//		int temp = (int) Float.parseFloat(price);
//		

	}
}
