package com.zjxfood.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.project.util.BitmapUtilSingle;
import com.project.util.ScreenUtils;
import com.project.util.Utils;
import com.umeng.analytics.MobclickAgent;
import com.zjxfood.application.ExitApplication;
import com.zjxfood.common.Constants;
import com.zjxfood.http.XutilsUtils;
import com.zjxfood.popupwindow.AuctionSuccessPopupWindow;
import com.zjxfood.view.TimeDownView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 竞拍详情页面
 * @author zjx
 *
 */
public class DisplayAuctionItemActivity extends AppActivity implements
		OnClickListener {
	private RelativeLayout mCanshuLayout;
	private RelativeLayout mBtnLayout;
	private String mId = "";
	private Bundle mBundle;
	private HashMap<String, Object> mMap1, mMap2;
	private TextView mMallState, mMallName, mMallPrice;
	private ImageView mAuctionImage;
	private BitmapUtils mBitmapUtils;
	private TimeDownView mTimedownview;
	private ArrayList<HashMap<String, Object>> mRecordList;
	private HashMap<String, Object> mRecordMap;
	private LinearLayout mRecordListLayout;
	private TextView mPhoneText, mPriceText;
	private boolean isStart = false;
	private TextView mPayText;
	private PopupWindow mPopupWindow;
	private TextView mOkText, mCancelText;
	private EditText mEditText;
	private HashMap<String, Object> mOutPriceMap;
	private RelativeLayout mRecordLayout;
	private HashMap<String, Object> mOrderMap,mShMaps;
	private AuctionSuccessPopupWindow mSuccessPopupWindow;
	 private TextView mAuctionPeople;
	private TextView mShmoneyText;
	private Timer mTimer;
	private int[] time;
	private int n=1;
	private RelativeLayout mDetailLayout;
	private HashMap<String, Object> mTotalMap;
	private TimerTask task;
	private TextView mAuctionShow;
	private ImageView mBackImage;//返回按钮
	private TextView mTitleText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_display_auction_item_layout);
		setImmerseLayout(findViewById(R.id.head_layout));
		ExitApplication.getInstance().addActivity(this);
		init();
		mBitmapUtils = BitmapUtilSingle
				.getBitmapInstance(getApplicationContext());
		mBitmapUtils
				.configDefaultLoadFailedImage(R.drawable.merchant_occupying);
		mBundle = getIntent().getExtras();
		mTimer = new Timer();
		if (mBundle != null) {
			mId = mBundle.getString("id");
			getDetail();
			getRecords();
			if(mId!=null){
				getAuctionAllNumbers();
			}
		}
		Log.i("我的食尚币", Constants.mShMoney + "==============");
		SharedPreferences sp = getApplicationContext()
				.getSharedPreferences("限时拍卖", MODE_PRIVATE);
		if(sp!=null && sp.getString("Content", "")!=null && !sp.getString("Content", "").toString().equals("")){
			mAuctionShow.setText(sp.getString("Content", ""));
		}
		getAllSource();
		// getDefalt();
	}

	private void init() {
		mCanshuLayout = (RelativeLayout) findViewById(R.id.main_display_child_canshu_layout);
//		mBackImage = (ImageView) findViewById(R.id.title_back_image);
		mBackImage = (ImageView) findViewById(R.id.title_back_image);
		mTitleText = (TextView) findViewById(R.id.title_text);
		mTitleText.setText("限时拍卖");
		mBtnLayout = (RelativeLayout) findViewById(R.id.main_display_item_btn);
		mMallState = (TextView) findViewById(R.id.display_child_head_show);
		mMallName = (TextView) findViewById(R.id.main_display_child_name_text);
		mMallPrice = (TextView) findViewById(R.id.main_display_child_price_value);
		mAuctionImage = (ImageView) findViewById(R.id.main_display_child_image);
		android.view.ViewGroup.LayoutParams params = mAuctionImage
				.getLayoutParams();
//		params.height = DensityUtils.dp2px(getApplicationContext(), 220);
		params.height = (int) (ScreenUtils.getScreenWidth(getApplicationContext())*0.75);
		mAuctionImage.setLayoutParams(params);
		mTimedownview = (TimeDownView) findViewById(R.id.main_display_child_timedownview);
		mRecordListLayout = (LinearLayout) findViewById(R.id.main_display_child_bid_list);
		mPayText = (TextView) findViewById(R.id.main_display_item_btn_text);
		mRecordLayout = (RelativeLayout) findViewById(R.id.main_display_child_bid_layout);
		mDetailLayout = (RelativeLayout) findViewById(R.id.main_display_child_canshu_layout);
		 mAuctionPeople = (TextView)
		 findViewById(R.id.main_display_child_people_text);
		 mAuctionShow = (TextView) findViewById(R.id.main_display_item_paimaishuoming_text);

		mRecordLayout.setOnClickListener(this);
		mCanshuLayout.setOnClickListener(this);
		mBackImage.setOnClickListener(this);
		mBtnLayout.setOnClickListener(this);
		mDetailLayout.setOnClickListener(this);
	}

	private void addRecord() {
		mRecordListLayout.removeAllViews();
		LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
		View view = inflater.inflate(R.layout.auction_item_record_layout, null);
		mPhoneText = (TextView) view
				.findViewById(R.id.auction_item_record_phone);
		mPriceText = (TextView) view
				.findViewById(R.id.auction_item_record_price);
		if (!Constants.isNull(mRecordList.get(0).get("UserName"))) {
			if(mRecordList.get(0).get("UserName").toString().equals(Constants.mUserName)){
				mPhoneText.setText(mRecordList.get(0).get("UserName").toString());
			}else{
				mPhoneText.setText(Utils.splitePhone(mRecordList.get(0).get("UserName").toString()));
			}
		}
		if (!Constants.isNull(mRecordList.get(0).get("Price"))) {
			mPriceText.setText("出价："
					+ mRecordList.get(0).get("Price").toString());
		}

		mRecordListLayout.addView(view);
	}


	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		// 拍卖
		case R.id.main_display_item_btn:
			
			if (isStart) {
				if (Constants.onLine == 1) {
					try{
					LayoutInflater inflater = LayoutInflater
							.from(getApplicationContext());
					View view = inflater.inflate(
							R.layout.display_auction_popup, null);
					mOkText = (TextView) view
							.findViewById(R.id.display_auction_ok);
					mCancelText = (TextView) view
							.findViewById(R.id.display_auction_cancel);
					mEditText = (EditText) view
							.findViewById(R.id.display_popup_chujia_value);
					mShmoneyText = (TextView) view
							.findViewById(R.id.merchant_settled_popup_content_tishi);
					mPopupWindow = new PopupWindow(view,
							LayoutParams.MATCH_PARENT,
							LayoutParams.MATCH_PARENT, false);
					mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
					mPopupWindow.setOutsideTouchable(true);
					mPopupWindow.setFocusable(true);
					ColorDrawable dw = new ColorDrawable(0xb0000000);
					// 设置SelectPicPopupWindow弹出窗体的背景
					mPopupWindow.setBackgroundDrawable(dw);
					// 设置SelectPicPopupWindow弹出窗体动画效果
					mPopupWindow.setAnimationStyle(R.style.AnimTop_miss);
					mPopupWindow.showAtLocation(mCanshuLayout, Gravity.CENTER,
							0, 0);
					mOkText.setOnClickListener(clickListener);
					mCancelText.setOnClickListener(clickListener);
					if(Constants.mShMoney!=null){
					mShmoneyText.setText("提示：你还有" + Constants.mShMoney
							+ "食尚币可用");
					}
					}catch(Exception e){
						e.printStackTrace();
					}
				} else {
					Toast.makeText(getApplicationContext(), "请先登录帐号",
							Toast.LENGTH_SHORT).show();
				}
			}
			break;
		// 查看参数
		case R.id.main_display_child_canshu_layout:
			if(mMap2!=null && mMap2.size()>0){
			if(mMap2.get("Detail")!=null){
			intent.setClass(getApplicationContext(),
					AuctionDetailActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("detail", mMap2.get("Detail").toString());
			intent.putExtras(bundle);
			startActivity(intent);
			}else{
				Toast.makeText(getApplicationContext(), "该商品暂时没有参数介绍",
						Toast.LENGTH_SHORT).show();
			}
			}
			break;

		case R.id.title_back_image:
			finish();
			break;
		// 查看竞拍记录
		case R.id.main_display_child_bid_layout:
			intent.setClass(getApplicationContext(),
					AuctionRecordActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("id", mId);
			intent.putExtras(bundle);
			startActivity(intent);
			break;
		}
	}

	OnClickListener clickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.display_auction_ok:
				if (Constants.onLine == 1 && !Constants.mId.equals("")) {
					try{
					Log.i("mShMoney", Constants.mShMoney + "==============");
					if (Constants.mShMoney != null
							&& !mEditText.getText().toString().equals("")
							&& !Constants.mShMoney.equals("")) {
						if (Integer.parseInt(Constants.mShMoney) > Float.parseFloat(mEditText.getText().toString())){
							outPrice();
						} else {
							Toast.makeText(getApplicationContext(), "食尚币不足",
									Toast.LENGTH_SHORT).show();

						}
					}
					}catch(Exception e){
						e.printStackTrace();
					}
				}
				if (mPopupWindow != null && mPopupWindow.isShowing()) {
					mPopupWindow.dismiss();
				}
				break;

			case R.id.display_auction_cancel:
				if (mPopupWindow != null && mPopupWindow.isShowing()) {
					mPopupWindow.dismiss();
				}
				break;
			}
		}
	};

	// 商品竞拍出价
		private void outPrice() {
			StringBuilder sb = new StringBuilder();
				sb.append("userId=" + Constants.mId+"&userName="+Constants.mUserName+"&auctionId="+mId+"&price="+mEditText.getText().toString());
			
			XutilsUtils.get(Constants.outPrice, sb,
					new RequestCallBack<String>() {
						@Override
						public void onFailure(HttpException arg0, String arg1) {
						}
						@Override
						public void onSuccess(ResponseInfo<String> res) {
							Log.i("出价", res.result+"===============");
							mOutPriceMap = Constants.getJsonObject(res.result);
							if (mOutPriceMap != null) {
								handler.sendEmptyMessageDelayed(3, 0);
							}
						}
					});
		}

	// 获得商品详情
	private void getDetail() {
		StringBuilder sb = new StringBuilder();
		sb.append("id="+mId);
		XutilsUtils.get(Constants.getAuctionDetail, sb,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
					}
					@Override
					public void onSuccess(ResponseInfo<String> res) {
						mMap1 = Constants.getJsonObject(res.result);
						if (mMap1 != null && mMap1.get("Code")!=null
								&& mMap1.get("Code").toString().equals("200")) {
							mMap2 = Constants.getJsonObject(mMap1.get("Data")
									.toString());
							if (mMap2 != null) {
								handler.sendEmptyMessageDelayed(1, 0);
							}
						}
					}
				});
	}

	// 获得竞拍记录
	private void getRecords() {
		StringBuilder sb = new StringBuilder();
		sb.append("auctionId=" + mId).append("&currentPage=1&pageSize=1");
		XutilsUtils.get(Constants.getAuctionRecord, sb,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
					}

					@Override
					public void onSuccess(ResponseInfo<String> res) {
						try {
							mRecordMap = Constants
									.getJsonObjectByData(res.result);
							mRecordList = Constants.getJsonArray(mRecordMap
									.get("rows").toString());
						} catch (Exception e) {
							e.printStackTrace();
						}
						if (mRecordList != null && mRecordList.size() > 0) {
							handler.sendEmptyMessageDelayed(2, 0);
						}
					}
				});
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				if(n==1){
					mMallName.setText("品牌：" + mMap2.get("ProductName").toString());
					mMallPrice.setText(mMap2.get("BasePrice").toString() + "食尚币");
					if (!Constants.isNull(mMap2.get("ImgUrl"))) {
						mBitmapUtils.display(mAuctionImage, mMap2.get("ImgUrl")
								.toString());
					}
					}
				if (!Constants.isNull(mMap2.get("StartTime"))
						&& !Constants.isNull(mMap2.get("EndTime"))) {
					mMallState.setText(Utils.subTime(mMap2
							.get("StartTime").toString())
							+ "开始");
					if (Utils.isStart(mMap2.get("StartTime").toString())) {
						mBtnLayout
								.setBackgroundResource(R.drawable.bg_display_auction_bottom_style);
						mPayText.setText("还未开始");
						isStart = false;
						time = Utils.formatStartTime(mMap2.get("StartTime")
								.toString());
						if (time != null) {
							mTimedownview.setTimes(time);
							mTimedownview.setType("未开始");
							mTimedownview.setIsStart("nostart");
							if (!mTimedownview.isRun()) {
								mTimedownview.run();
							}
						}
					} else if (Utils.isEnd(mMap2.get("EndTime").toString())) {
						mBtnLayout
								.setBackgroundResource(R.drawable.bg_display_auction_true_style);
						mPayText.setText("我要出价");
						isStart = true;
						flushAuction();
						
						time = Utils.formatStartTime(mMap2.get("EndTime")
								.toString());
						Log.i("EndTime", time[0]+"==="+time[1]+"==="+time[2]+"==="+time[3]+"===");
						if (time != null) {
							mTimedownview.setType("竞拍中");
							mTimedownview.setIsStart("isstart");
							mTimedownview.setTimes(time);
							if (!mTimedownview.isRun()) {
								mTimedownview.run();
							}
						}
					} else {
//						mMallState.setText("已结束");
						mTimedownview.setType("已结束");
						mBtnLayout
								.setBackgroundResource(R.drawable.bg_display_auction_bottom_style);
						mPayText.setText("已经结束");
						isStart = false;
					}
				}
				
				break;
			case 2:
				addRecord();
				
				break;
			case 3:
				if (mPopupWindow != null && mPopupWindow.isShowing()) {
					mPopupWindow.dismiss();
				}
				if (mOutPriceMap.get("Code") != null
						&& mOutPriceMap.get("Code").toString().equals("200")) {
					try{
					mSuccessPopupWindow = new AuctionSuccessPopupWindow(
							DisplayAuctionItemActivity.this, mOutPriceMap.get(
									"Message").toString());
					mSuccessPopupWindow.showAtLocation(mBackImage,
							Gravity.CENTER, 0, 0);
					}catch(Exception e){
						e.printStackTrace();
					}
				} else {
					if (mOutPriceMap.get("Message") != null) {
						Toast.makeText(getApplicationContext(),
								mOutPriceMap.get("Message").toString(),
								Toast.LENGTH_SHORT).show();
					}
				}
				break;
			case 4:
				if (mOrderMap.get("Code") != null
						&& mOrderMap.get("Code").toString().equals("200")) {
					outPrice();
				} else {
					if (mOrderMap.get("Message") != null) {
						Toast.makeText(getApplicationContext(),
								mOrderMap.get("Message").toString(), Toast.LENGTH_SHORT).show();
					}
				}
				break;
			case 5:
				getDetail();
				getRecords();
				if(mId!=null){
					getAuctionAllNumbers();
				}
				break;
			}
		};
	};
	
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		if (mTimer != null) {
			mTimer.cancel();
			if (task != null) {
				task.cancel();
			}
		}
	};
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
	}

	private void getAuctionAllNumbers() {
		StringBuilder sb = new StringBuilder();
		sb.append("auctionId="+mId+"&currentPage=1&pageSize=1");
		XutilsUtils.get(Constants.getAuctionRecord, sb,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
					}
					@Override
					public void onSuccess(ResponseInfo<String> res) {
						Log.i("参与人数", res+"===============");
						mTotalMap = Constants.getJsonObjectByData(res.result);
						mAuctionPeople.setText("出价次数：" + mTotalMap.get("total"));
					}
				});
	}

	private void flushAuction(){
		mTimer = new Timer();
		if (mTimer != null) {
			if (task != null) {
				task.cancel(); // 将原任务从队列中移除
			}
			task = new TimerTask() {
				@Override
				public void run() {
					handler.sendEmptyMessageDelayed(5, 0);
				}
			};
			mTimer.schedule(task, 5000);
		}
		n++;
	}

	//获取可用食尚币
	private void getAllSource() {
		StringBuilder sb = new StringBuilder();
		sb.append("UserId="+Constants.mId+"&currencyType=4");
		XutilsUtils.get(Constants.getGoldShmony, sb,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
					}
					@Override
					public void onSuccess(ResponseInfo<String> res) {
						Log.i("msg2",res.result+"==============================");
						mShMaps = Constants.getJsonObjectByData(res.result);
						if(mShMaps!=null && mShMaps.size()>0){
							if(mShMaps.get("AvailableBalance")!=null) {
								Constants.mShMoney = mShMaps.get("AvailableBalance").toString();
							}
						}
					}
				});
	}
}
