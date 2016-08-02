package com.zjxfood.red;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.project.util.BitmapUtilSingle;
import com.umeng.analytics.MobclickAgent;
import com.zjxfood.activity.AppActivity;
import com.zjxfood.activity.R;
import com.zjxfood.application.ExitApplication;
import com.zjxfood.common.Constants;
import com.zjxfood.http.XutilsUtils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 已发出的红包
 * 
 * @author zjx
 * 
 */
public class RedIssuedActivity extends AppActivity implements OnClickListener {

	private ListView mListView;
	private RedIssuedListAdapter mRedIssuedListAdapter;
	private RedSendListAdapter mSendListAdapter;
	private int page = 1;
	private HashMap<String, Object> mRecordMap, mTotalMap;
	private ArrayList<HashMap<String, Object>> mList, mAddList;
	private TextView mIssueTotal, mPriceTotal;// 抢到的红包个数和总金额
	private RelativeLayout mReceivedLayout, mIssueLayout;
	private BitmapUtils mBitmapUtils;
	private ImageView mHeadImage;
	private TextView mStateText;
	private int lastVisibleIndex;
	private int x = 1;
	private int maxNum = 0;
	private int state = 1;
	private TextView mReceivedText, mSendText;
	private View mReceivedView, mSendView;
	private ImageView mBackImage;
	// private LinearLayout mLoadLayout;
	private ProgressBar mBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ExitApplication.getInstance().addActivity(this);
		setContentView(R.layout.red_issued_list_layout);
		setImmerseLayout(findViewById(R.id.head_layout));
		init();
		mBitmapUtils = BitmapUtilSingle
				.getBitmapInstance(getApplicationContext());
		getRedRecord();
		getTotalRed();
		mBitmapUtils.display(mHeadImage, Constants.headPath);
	}

	private void init() {
		mListView = (ListView) findViewById(R.id.red_issued_list);
		mIssueTotal = (TextView) findViewById(R.id.red_issued_content_10_text);
		mPriceTotal = (TextView) findViewById(R.id.red_issued_content_price_text);
		mReceivedLayout = (RelativeLayout) findViewById(R.id.title_shoudao_layout);
		mIssueLayout = (RelativeLayout) findViewById(R.id.title_issued_layout);
		mHeadImage = (ImageView) findViewById(R.id.red_issued_content_user_image);
		mStateText = (TextView) findViewById(R.id.red_issued_content_text);
		mReceivedText = (TextView) findViewById(R.id.title_shoudao_text);
		mSendText = (TextView) findViewById(R.id.title_issued_text);
		mReceivedView = findViewById(R.id.issue_view);
		mSendView = findViewById(R.id.send_view);
		mBackImage = (ImageView) findViewById(R.id.issued_title_back_info_image);
		mBar = (ProgressBar) findViewById(R.id.red_issued_list_progress);

		mBackImage.setOnClickListener(this);
		mReceivedLayout.setOnClickListener(this);
		mIssueLayout.setOnClickListener(this);
	}

	// 抢到的红包列表
	private void getRedRecord() {
		StringBuilder sb = new StringBuilder();
		sb.append("userId=" + Constants.mId + "&pageSize=5" + "&currentPage="
				+ page);
		XutilsUtils.get(Constants.getMyRedList, sb,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
					}

					@Override
					public void onSuccess(ResponseInfo<String> res) {
						mRecordMap = Constants.getJsonObjectByData(res.result);
						if (mRecordMap != null && mRecordMap.size() > 0
								&& null != mRecordMap.get("rows")) {

							if (x == 1) {
								mList = Constants.getJsonArray(mRecordMap.get(
										"rows").toString());
								handler.sendEmptyMessageDelayed(1, 0);
							} else if (x == 2) {
								mAddList = Constants.getJsonArray(mRecordMap
										.get("rows").toString());
								handler.sendEmptyMessageDelayed(3, 0);
							}
						}
						// Log.i("抢红包", mRecordMap + "==================");
					}
				});
	}

	// 抢到的红包总数和金额
	private void getTotalRed() {
		StringBuilder sb = new StringBuilder();
		sb.append("userId=" + Constants.mId);
		XutilsUtils.get(Constants.getTotalRed, sb,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
					}

					@Override
					public void onSuccess(ResponseInfo<String> res) {
						mTotalMap = Constants.getJsonObjectByData(res.result);
						if (mTotalMap != null && mTotalMap.size() > 0) {
							handler.sendEmptyMessageDelayed(2, 0);
						}
						Log.i("红包金额和总数", mTotalMap + "==================");
					}
				});
	}

	// 我发出去的红包列表
	private void getSendRecord() {
		StringBuilder sb = new StringBuilder();
		sb.append("userId=" + Constants.mId + "&pageSize=5" + "&currentPage="
				+ page);
		XutilsUtils.get(Constants.getSendRed, sb,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
					}
					@Override
					public void onSuccess(ResponseInfo<String> res) {
						mRecordMap = Constants.getJsonObjectByData(res.result);
						if (mRecordMap != null && mRecordMap.size() > 0
								&& null != mRecordMap.get("rows")) {
							if (x == 1) {
								mList = Constants.getJsonArray(mRecordMap.get(
										"rows").toString());
								handler.sendEmptyMessageDelayed(4, 0);
							} else if (x == 2) {
								mAddList = Constants.getJsonArray(mRecordMap
										.get("rows").toString());
								handler.sendEmptyMessageDelayed(3, 0);
							}
						}
//						 Log.i("抢到的红包列表", mRecordMap + "==================");
					}
				});
	}

	// 抢到的红包总数和金额
	private void getSendTotal() {
		StringBuilder sb = new StringBuilder();
		sb.append("userId=" + Constants.mId);
		XutilsUtils.get(Constants.getSendTotal, sb,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
					}
					@Override
					public void onSuccess(ResponseInfo<String> res) {
						mTotalMap = Constants.getJsonObjectByData(res.result);
						if (mTotalMap != null && mTotalMap.size() > 0) {
							handler.sendEmptyMessageDelayed(2, 0);
						}
						Log.i("红包金额和总数", mTotalMap + "==================");
					}
				});
	}

	OnScrollListener mScrollListener = new OnScrollListener() {
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			if (state == 1) {
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
						&& lastVisibleIndex == mRedIssuedListAdapter.getCount() - 1
						&& maxNum < mRedIssuedListAdapter.getCount() - 1) {
					x = 2;
					page = page + 1;
					getRedRecord();
				}
			} else if (state == 2) {
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
						&& lastVisibleIndex == mSendListAdapter.getCount() - 1
						&& maxNum < mSendListAdapter.getCount() - 1) {
					x = 2;
					page = page + 1;
					getSendRecord();
				}
			}
		}
		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			// 计算最后可见条目的索引
			lastVisibleIndex = firstVisibleItem + visibleItemCount - 1;

		}
	};

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				if(mList!=null && mList.size()>0){
				mStateText.setText("收到");
				mRedIssuedListAdapter = new RedIssuedListAdapter(
						getApplicationContext(), mList);
				mListView.setAdapter(mRedIssuedListAdapter);
				mListView.setOnItemClickListener(itemClickListener);
				mListView.setOnScrollListener(mScrollListener);
				mBar.setVisibility(View.GONE);
				mListView.setVisibility(View.VISIBLE);
				}
				break;

			case 2:
				if (state == 1) {
					mStateText.setText("收到");
				} else if (state == 2) {
					mStateText.setText("发出");
				}
				if (mTotalMap.get("TotalMoney") != null) {
					mPriceTotal.setText(mTotalMap.get("TotalMoney").toString());
				}
				if (mTotalMap.get("TotalNum") != null) {
					mIssueTotal.setText(mTotalMap.get("TotalNum").toString());
				}
				break;
			case 3:
				if (state == 1) {
					maxNum = mRedIssuedListAdapter.getCount() - 1;
					mRedIssuedListAdapter.notifyList(mAddList);
				} else if (state == 2) {
					maxNum = mSendListAdapter.getCount() - 1;
					mSendListAdapter.notifyList(mAddList);
				}

				break;
			case 4:
				mStateText.setText("发出");
				// Log.i("发出的列表", mList + "==========");
				mSendListAdapter = new RedSendListAdapter(
						getApplicationContext(), mList);
				mListView.setAdapter(mSendListAdapter);
				mListView.setOnItemClickListener(itemClickListener);
				mListView.setOnScrollListener(mScrollListener);
				mBar.setVisibility(View.GONE);
				mListView.setVisibility(View.VISIBLE);
				break;
			}
		};
	};

	OnItemClickListener itemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			intent.setClass(getApplicationContext(), RedWinInfoActivity.class);
			if (state == 1) {
				if (mList.get(position).get("Id") != null) {
					bundle.putString("hbId", mList.get(position).get("Id")
							.toString());
				}
				if (mList.get(position).get("SendUserName") != null) {
					bundle.putString("userName",
							mList.get(position).get("SendUserName").toString());
				}
				if (mList.get(position).get("HbMoney") != null) {
					bundle.putString("price", mList.get(position)
							.get("HbMoney").toString());
				}

				if (mList.get(position).get("Memo") != null) {
					bundle.putString("message", mList.get(position).get("Memo")
							.toString());
				}
				bundle.putString("state","1");
			} else if (state == 2) {
				if (mList.get(position).get("Id") != null) {
					bundle.putString("hbId", mList.get(position).get("Id")
							.toString());
				}
				if (mList.get(position).get("SendUserName") != null) {
					bundle.putString("userName",
							mList.get(position).get("SendUserName").toString());
				}
				if (mList.get(position).get("TotalMoney") != null) {
					bundle.putString("price",
							mList.get(position).get("TotalMoney").toString());
				}
				if (mList.get(position).get("Memo") != null) {
					bundle.putString("message", mList.get(position).get("Memo")
							.toString());
				}
				if (mList.get(position).get("TotalNum") != null) {
					bundle.putString("total", mList.get(position).get("TotalNum")
							.toString());
				}
				bundle.putString("state","2");
			}

			intent.putExtras(bundle);
			startActivity(intent);
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.issued_title_back_info_image:
			finish();
			break;
		// 我收到的红包
		case R.id.title_shoudao_layout:
			page = 1;
			x = 1;
			state = 1;
			maxNum = 0;
			getTotalRed();
			getRedRecord();
			mBar.setVisibility(View.VISIBLE);
			mListView.setVisibility(View.GONE);
			mReceivedText.setTextColor(getResources().getColor(
					R.color.red_title_text_color));
			mReceivedView.setVisibility(View.VISIBLE);
			mSendText.setTextColor(getResources().getColor(R.color.white));
			mSendView.setVisibility(View.GONE);
			break;
		// 我发出的红包
		case R.id.title_issued_layout:
			page = 1;
			state = 2;
			maxNum = 0;
			x = 1;
			getSendRecord();
			getSendTotal();
			mBar.setVisibility(View.VISIBLE);
			mListView.setVisibility(View.GONE);
			mReceivedText.setTextColor(getResources().getColor(R.color.white));
			mReceivedView.setVisibility(View.GONE);
			mSendText.setTextColor(getResources().getColor(
					R.color.red_title_text_color));
			mSendView.setVisibility(View.VISIBLE);
			break;
		}
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
