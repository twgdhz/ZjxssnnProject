package com.zjxfood.red;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.project.util.BitmapUtilSingle;
import com.umeng.analytics.MobclickAgent;
import com.zjxfood.activity.AppActivity;
import com.zjxfood.activity.R;
import com.zjxfood.common.Constants;
import com.zjxfood.http.XutilsUtils;

import java.util.ArrayList;
import java.util.HashMap;

public class RedWinInfoActivity extends AppActivity implements OnClickListener {

	private ListView mListView;
	private RedWinInfoAdapter mWinInfoAdapter;
	private ImageView mBackImage;
	private TextView mInfoText;
	private Bundle mBundle;
	private String mHbId, mUserName, mPrice, mMessage;
	private int page = 1;
	private HashMap<String, Object> mGrabMap, mInfoMap;
	private ArrayList<HashMap<String, Object>> mList, mAddList, mInfoList;
	private TextView mNameText, mMsgText, mPriceText;
	private ImageView mHeadImage;
	private BitmapUtils mBitmapUtils;
	private TextView mRedInfoText;
	public static final String action = "red.default.broadcast";
	private int lastVisibleIndex;
	private int x = 1;
	private int maxNum = 0;
	private TextView mYuanText, mPriceShowText;
	private String mState,mTotal;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.red_win_info_layout);
		setImmerseLayout(findViewById(R.id.head_layout));
		init();
		mBitmapUtils = BitmapUtilSingle.getBitmapInstance(getApplicationContext());
		mBitmapUtils.configDefaultLoadFailedImage(getResources().getDrawable(R.drawable.hongbao_faile));
		mBundle = getIntent().getExtras();
		if (mBundle != null) {
			mHbId = mBundle.getString("hbId");
			mUserName = mBundle.getString("userName");
			mPrice = mBundle.getString("price");
			mMessage = mBundle.getString("message");
			mState = mBundle.getString("state");
			mTotal = mBundle.getString("total");
			if (mHbId != null) {
				getGrabList();
				getRedInfo();
			}
			mNameText.setText("来自" + mUserName + "的红包");
			mMsgText.setText(mMessage);

			if (mState != null) {
				if (mState.equals("1")) {
					mPriceText.setText(mPrice);
					mYuanText.setVisibility(View.VISIBLE);
					mPriceShowText.setVisibility(View.VISIBLE);
				} else if (mState.equals("2")) {
					mMsgText.setText("发给"+mTotal+"位朋友的红包，共");
					mPriceText.setText(mPrice);
					mPriceShowText.setVisibility(View.GONE);
				}else if(mState.equals("3")){//领取完
					mPriceText.setText(mPrice);
					mYuanText.setVisibility(View.VISIBLE);
					mPriceShowText.setVisibility(View.GONE);
					mMsgText.setText("红包已经被领取完");
					mPriceText.setVisibility(View.GONE);
					mYuanText.setVisibility(View.GONE);
				}else if(mState.equals("4")){//已领取
					mPriceText.setText(mPrice);
					mYuanText.setVisibility(View.VISIBLE);
					mPriceShowText.setVisibility(View.VISIBLE);
					mMsgText.setText("已经领取过该红包了");
					mPriceText.setVisibility(View.GONE);
					mYuanText.setVisibility(View.GONE);
				}else if(mState.equals("5")){//已过期
					mPriceText.setText(mPrice);
					mYuanText.setVisibility(View.VISIBLE);
					mPriceShowText.setVisibility(View.VISIBLE);
					mMsgText.setText("红包已经过期");
					mPriceText.setVisibility(View.GONE);
					mYuanText.setVisibility(View.GONE);
					mPriceShowText.setVisibility(View.GONE);
				}
			}
		}
	}

	private void init() {
		mListView = (ListView) findViewById(R.id.red_win_content_list);
		mBackImage = (ImageView) findViewById(R.id.red_win_back_info_image);
		mInfoText = (TextView) findViewById(R.id.red_win_info_text);
		mNameText = (TextView) findViewById(R.id.red_win_content_user_text_name);
		mPriceText = (TextView) findViewById(R.id.red_win_content_price);
		mMsgText = (TextView) findViewById(R.id.red_win_content_gongxi);
		mHeadImage = (ImageView) findViewById(R.id.red_issued_content_user_image);
		mRedInfoText = (TextView) findViewById(R.id.red_win_content_numbers);
		mYuanText = (TextView) findViewById(R.id.red_win_content_yuan);
		mPriceShowText = (TextView) findViewById(R.id.red_win_content_price_msg);

		mBackImage.setOnClickListener(this);
//		mInfoText.setOnClickListener(this);
	}

	// 红包的领取列表
	private void getGrabList() {
		StringBuilder sb = new StringBuilder();
		sb.append("hbId=" + mHbId + "&pageSize=10" + "&currentPage=" + page);
		XutilsUtils.get(Constants.getGrabList, sb,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
					}
					@Override
					public void onSuccess(ResponseInfo<String> res) {
						mGrabMap = Constants.getJsonObjectByData(res.result);
						if (mGrabMap != null && mGrabMap.size() > 0
								&& null != mGrabMap.get("rows")) {
							if (x == 1) {
								mList = Constants.getJsonArray(mGrabMap.get(
										"rows").toString());
								handler.sendEmptyMessageDelayed(1, 0);
							} else if (x == 2) {
								mAddList = Constants.getJsonArray(mGrabMap.get(
										"rows").toString());
								handler.sendEmptyMessageDelayed(2, 0);
							}
						}
						Log.i("红包列表", mGrabMap+"=================");
					}
				});
	}

	// 红包的详情
	private void getRedInfo() {
		StringBuilder sb = new StringBuilder();
		sb.append("hbId=" + mHbId);
		XutilsUtils.get(Constants.getRedInfo, sb,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
					}
					@Override
					public void onSuccess(ResponseInfo<String> res) {
						mInfoMap = Constants.getJsonObjectByData(res.result);
						if (mInfoMap != null && mInfoMap.size() > 0) {
							handler.sendEmptyMessageDelayed(3, 0);
						}
						Log.i("mInfoMap", mInfoMap + "===============");
					}
				});
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				mWinInfoAdapter = new RedWinInfoAdapter(
						getApplicationContext(), mList);
				mListView.setAdapter(mWinInfoAdapter);
				mListView.setOnScrollListener(mScrollListener);
				break;
			case 2:
				maxNum = mWinInfoAdapter.getCount() - 1;
				mWinInfoAdapter.notifyList(mAddList);
				break;
			case 3:
				if(mInfoMap.get("SendUserPhoto")!=null){
				mBitmapUtils.display(mHeadImage, mInfoMap.get("SendUserPhoto")
						.toString());
				}else{
					mHeadImage.setImageDrawable(getResources().getDrawable(R.drawable.hongbao_faile));
				}
				mRedInfoText.setText("共" + mInfoMap.get("TotalNum")
						+ "个红包,总金额：" + mInfoMap.get("TotalMoney") + "元");
				// mInfoText.setText("个红包,总金额：");
				break;
			}
		};
	};

	OnScrollListener mScrollListener = new OnScrollListener() {
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
					&& lastVisibleIndex == mWinInfoAdapter.getCount() - 1
					&& maxNum < mWinInfoAdapter.getCount() - 1) {
				x = 2;
				page = page + 1;
				getGrabList();
			}
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			// 计算最后可见条目的索引
			lastVisibleIndex = firstVisibleItem + visibleItemCount - 1;

		}
	};

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.red_win_back_info_image:
			intent = new Intent(action);
			intent.putExtra("data", "1");
			sendBroadcast(intent);
			finish();
			break;

		case R.id.red_win_info_text:
			intent.setClass(getApplicationContext(), RedIssuedActivity.class);
			startActivity(intent);
			finish();
			break;
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Intent intent;
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			intent = new Intent(action);
			intent.putExtra("data", "1");
			sendBroadcast(intent);
			finish();
			break;
		default:
			break;
		}
		return false;
	}
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
}
