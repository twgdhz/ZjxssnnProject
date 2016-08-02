package com.zjxfood.indiana;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.zjxfood.common.Constants;
import com.zjxfood.http.XutilsUtils;
import com.zjxfood.popupwindow.CjSuccessPopupWindow;
import com.zjxfood.view.MyScrollListView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 奖品详情
 * @author zjx
 *
 */
public class IndianaItemActivity extends AppActivity implements OnClickListener {

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
	private float num=0;
	private ProgressBar mBar;
	private int x=1;
	private LinearLayout mDetailLayout;
	private LinearLayout mRuleLayout;
	private MyScrollListView mScrollListView;
	private HashMap<String, Object> mLotterMap;
	private ArrayList<HashMap<String, Object>> mLotterList,mAddList;
	private int page = 1;
	private MyIndianaInfoAdapter mIndianaInfoAdapter;
	private TextView mMoreText;
	private TextView mStateText;
	private HashMap<String, Object> mStateMap;
	private TextView mNumLimitText;
	private LinearLayout mStateLayout;

	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.indiana_info_layout);
		Log.i("奖品详情","====================");
		setImmerseLayout(findViewById(R.id.head_layout));
		mBitmapUtils = BitmapUtilSingle
				.getBitmapInstance(getApplicationContext());
		init();
		mBundle = getIntent().getExtras();
		if (mBundle != null) {
			mId = mBundle.getString("id");
		}
		if (mId != null) {
			getIndianaDetail();
		}
	};

	private void init() {
		mBackImage = (ImageView) findViewById(R.id.indana_info_back_info_image);
		mMallImage = (ImageView) findViewById(R.id.indiana_info_head_image);
		LayoutParams params = mMallImage.getLayoutParams();
		params.height = (int) (ScreenUtils.getScreenWidth(getApplicationContext())*(0.75));
		mMallImage.setLayoutParams(params);
		mMyIndianaText = (TextView) findViewById(R.id.title_indiana_info_my);
		mNameText = (TextView) findViewById(R.id.indiana_info_name_text);
		mPriceText = (TextView) findViewById(R.id.indiana_info_price_text);
		mNumLeastText = (TextView) findViewById(R.id.indiana_people_num_text);
		mTotalPersonText = (TextView) findViewById(R.id.indiana_people_num_shengyu_text);
		mCjButton = (Button) findViewById(R.id.indiana_info_cj_btn);
		mBar = (ProgressBar) findViewById(R.id.indiana_info_progress_bar);
		mDetailLayout = (LinearLayout) findViewById(R.id.indiana_detail_layout);
		mRuleLayout = (LinearLayout) findViewById(R.id.indiana_detail_rule_layout);
		mScrollListView = (MyScrollListView) findViewById(R.id.indiana_info_all_list);
		mMoreText = (TextView) findViewById(R.id.indiana_rule_more_text);
		mStateText = (TextView) findViewById(R.id.indiana_info_state_text_value);
		mNumLimitText = (TextView) findViewById(R.id.indiana_info_numlimit_text);
		mStateLayout = (LinearLayout) findViewById(R.id.indiana_info_state_layout);
//		mWebView = (WebView) findViewById(R.id.indiana_info_web);

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
		XutilsUtils.get(Constants.getIndianaDetail, sb,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
					}
					@Override
					public void onSuccess(ResponseInfo<String> res) {
						mDetailMap = Constants.getJsonObjectByData(res.result);
						if (mDetailMap != null && mDetailMap.size() > 0) {
							handler.sendEmptyMessageDelayed(1, 0);
						}
					}
				});
	}
	
	// 获取所有抽奖号
		private void getAllLotteryNumber() {
			StringBuilder sb = new StringBuilder();
			sb.append("gameId=" + mId+"&userId=0"+"&pageSize=50"+"&currentPage="+page);
			XutilsUtils.get(Constants.getJoinList, sb,
					new RequestCallBack<String>() {
						@Override
						public void onFailure(HttpException arg0, String arg1) {
						}
						@Override
						public void onSuccess(ResponseInfo<String> res) {
							mLotterMap = Constants.getJsonObjectByData(res.result);
//							Log.i("抽奖号码", res.result+"==============");
							if (mLotterMap != null && mLotterMap.size() > 0 && mLotterMap.get("rows")!=null) {
								mLotterList = Constants.getJsonArray(mLotterMap.get("rows").toString());
								handler.sendEmptyMessageDelayed(3, 0);
							}
						}
					});
		}

	// 抽奖
	private void getCj() {
		StringBuilder sb = new StringBuilder();
		sb.append("userId=" + Constants.mId + "&gameId=" + mId);
		XutilsUtils.get(Constants.getCj, sb, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				isClick = true;
			}

			@Override
			public void onSuccess(ResponseInfo<String> res) {
				mCjMap = Constants.getJsonObject(res.result);
				if (mCjMap != null && mCjMap.size() > 0) {
					handler.sendEmptyMessageDelayed(2, 0);
				}
				isClick = true;
			}
		});
	}
	
	// 获取50个时间之和
		private void getLastSum() {
			StringBuilder sb = new StringBuilder();
			sb.append("gameId=" + mId);
			XutilsUtils.get(Constants.getLastSum, sb,
					new RequestCallBack<String>() {
						@Override
						public void onFailure(HttpException arg0, String arg1) {
						}
						@Override
						public void onSuccess(ResponseInfo<String> res) {
							mStateMap = Constants.getJsonObject(res.result);
							if(mStateMap!=null && mStateMap.size()>0){
								if(mStateMap.get("Code").toString().equals("200")){
									handler.sendEmptyMessageDelayed(4, 0);
								}
							}
//							if (mDetailMap != null && mDetailMap.size() > 0) {
//								handler.sendEmptyMessageDelayed(1, 0);
//							}
						}
					});
		}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				if(x==1){
				if (mDetailMap.get("ImgUrl") != null) {
					mBitmapUtils.display(mMallImage, mDetailMap.get("ImgUrl")
							.toString());
				}
				if (mDetailMap.get("ProductName") != null) {
					mNameText.setText("【第"+mId+"期】"+mDetailMap.get("ProductName").toString());
				}
				java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
				if (mDetailMap.get("SHB") != null) {
//					Double price = Double.parseDouble(mDetailMap.get("Price")
//							.toString());
//					mPriceText.setText(df.format(price) + "食尚币/次");
					mPriceText.setText(mDetailMap.get("SHB") + "食尚币/次");
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
				if(null!=mDetailMap.get("NumLeast") && null!=mDetailMap.get("TotalPerson")){
					num = (Float.parseFloat(mDetailMap.get("TotalPerson").toString()))/(Float.parseFloat(mDetailMap.get("NumLeast").toString()));
					mBar.setProgress((int) (num*100));
//					Log.i("num", num+"==============="+mDetailMap.get("TotalPerson").toString()+"==="+mDetailMap.get("NumLeast"));
				}
				if(mDetailMap.get("NumLimit")!=null){
					mNumLimitText.setText("每人限抽"+mDetailMap.get("NumLimit")+"次");
				}
				if(Constants.onLine==1){
				getAllLotteryNumber();
				}
				if(mDetailMap.get("HasCaculateNum")!=null){
					Log.i("HasCaculateNum", mDetailMap.get("HasCaculateNum")+"===========");
					if(mDetailMap.get("HasCaculateNum").toString().equals("true")){
						if(mDetailMap.get("LuckyNum")!=null){
							if(mDetailMap.get("LuckyNum").toString().equals("0")){
								mStateText.setText("未达到开奖条件，所有参与用户花费的食尚币已退回。");
							}else{
								getLastSum();
							}
						}
					}
				}
				break;

			case 2:
				
				if (mCjMap.get("Code")!=null && mCjMap.get("Code").toString().equals("200") && mCjMap.get("Data") != null) {
					try {
						mSuccessPopupWindow = new CjSuccessPopupWindow(
								IndianaItemActivity.this, mCjMap.get("Data")
										.toString(),clickListener);
						mSuccessPopupWindow.showAtLocation(mBackImage,
								Gravity.CENTER, 0, 0);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}else{
					if(mCjMap.get("Code")!=null && mCjMap.get("Message")!=null){
						Toast.makeText(getApplicationContext(), mCjMap.get("Message").toString(), Toast.LENGTH_SHORT).show();
					}
				}
				break;
			case 3:
				
				mIndianaInfoAdapter = new MyIndianaInfoAdapter(getApplicationContext(), mLotterList,mDetailMap.get("LuckyNum").toString());
				mScrollListView.setAdapter(mIndianaInfoAdapter);
				break;
			case 4:
				mStateLayout.setVisibility(View.VISIBLE);
				String sum = mStateMap.get("Data").toString();
				String caculateRule = mDetailMap.get("CaculateRule").toString();
				String totalPerson = mDetailMap.get("TotalPerson").toString();
				String luckyNum = mDetailMap.get("LuckyNum").toString();
				mStateText.setText("("+(sum+"+"+caculateRule)+")%"+totalPerson+"+"+"10000001"+"="+luckyNum);
				break;
			}
		};
	};

	OnClickListener clickListener = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			if(mSuccessPopupWindow!=null && mSuccessPopupWindow.isShowing()){
				mSuccessPopupWindow.dismiss();
				x=2;
				getIndianaDetail();
			}
		}
	};
	
	
	
	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		Bundle bundle;
		switch (v.getId()) {
		//更多
		case R.id.indiana_rule_more_text:
			if(Constants.onLine==1){
			intent.setClass(getApplicationContext(),
					IndianaMoreLotteryActivity.class);
			bundle = new Bundle();
			if(mDetailMap!=null && mDetailMap.size()>0 && mDetailMap.get("LuckyNum")!=null){
				bundle.putString("luckyNum", mDetailMap.get("LuckyNum").toString());
			}
			bundle.putString("id", mId);
			
			intent.putExtras(bundle);
			startActivity(intent);
			}else{
				Toast.makeText(getApplicationContext(), "请先登录帐号", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.indiana_detail_rule_layout:
			intent.setClass(getApplicationContext(),
					IndianRuleActivity.class);
			startActivity(intent);
			break;
		//奖品详情
		case R.id.indiana_detail_layout:
			if(mDetailMap!=null && mDetailMap.size()>0){
				if(mDetailMap.get("Detail")!=null){
				intent.setClass(getApplicationContext(),
						AuctionDetailActivity.class);
				bundle = new Bundle();
				bundle.putString("detail", mDetailMap.get("Detail").toString());
				intent.putExtras(bundle);
				startActivity(intent);
				}else{
					Toast.makeText(getApplicationContext(), "该商品暂时没有详情介绍",
							Toast.LENGTH_SHORT).show();
				}
				}
			break;
		case R.id.indana_info_back_info_image:
			finish();
			break;
			//我的夺宝
		case R.id.title_indiana_info_my:
			if(Constants.onLine==1){
			intent.setClass(getApplicationContext(),
					MyIndianaListActivity.class);
			startActivity(intent);
			}else{
				Toast.makeText(getApplicationContext(), "请先登录帐号", Toast.LENGTH_SHORT).show();
			}
			break;
		// 抽奖
		case R.id.indiana_info_cj_btn:
			if (Constants.onLine == 1) {
				if (mId != null && Constants.mId != null) {
					if(isClick){
						isClick = false;
					getCj();
					}
				}
			} else {
				Toast.makeText(getApplicationContext(), "请先登录帐号",
						Toast.LENGTH_SHORT).show();
			}
			break;
		}
	}

}
