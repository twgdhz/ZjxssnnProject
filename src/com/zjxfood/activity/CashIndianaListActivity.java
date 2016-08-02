package com.zjxfood.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.zjxfood.application.ExitApplication;
import com.zjxfood.cashindiana.CashIndianaItemActivity;
import com.zjxfood.cashindiana.CashIndianaListAdapter;
import com.zjxfood.cashindiana.CashMyIndianaListActivity;
import com.zjxfood.common.Constants;
import com.zjxfood.http.XutilsUtils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 现金夺宝
 * 
 * @author zjx
 * 
 */
public class CashIndianaListActivity extends AppActivity implements OnClickListener {

	private CashIndianaListAdapter mIndianaListAdapter;
	private ListView mListView;
	private ImageView mBackImage;
	private TextView mRecordText;
	private int code = 1, page = 1;
	private ArrayList<HashMap<String, Object>> mList, mAddList;
	private HashMap<String, Object> mIndianaMap;
	private int lastVisibleIndex;
	private int x = 1;
	private RelativeLayout mLoadLayout;
	private TextView mNoText;
	private LinearLayout mCurrencyLayout,mHistoryLayout,mNoticeLayout,mRuleLayout;
	private TextView mCurrencyText,mHistoryText,mNoticeText,mRuleText;
	private View mCurrencyView,mHistoryView,mNoticeView,mRuleView;
	private WebView mWebView;
	private String isStart = "true";
	private int maxNum = 0;
	private TextView mTitleText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ExitApplication.getInstance().addActivity(this);
		setContentView(R.layout.cash_indiana_list_layout);
		setImmerseLayout(findViewById(R.id.head_layout));
		init();
		mLoadLayout.setVisibility(View.VISIBLE);
		mListView.setVisibility(View.GONE);
		//
		getIndianaList();
	}

	private void init() {
		mListView = (ListView) findViewById(R.id.indiana_list_content_list);
//		mBackImage = (ImageView) findViewById(R.id.title_back_image);
		mRecordText = (TextView) findViewById(R.id.indiana_title_record_text);
		mLoadLayout = (RelativeLayout) findViewById(R.id.indiana_list_loading_layout);
		mNoText = (TextView) findViewById(R.id.indiana_no_alert_text);
		mCurrencyLayout = (LinearLayout) findViewById(R.id.indiana_list_type_current);
		mHistoryLayout = (LinearLayout) findViewById(R.id.indiana_list_type_temporary_layout);
		mNoticeLayout = (LinearLayout) findViewById(R.id.indiana_list_type_trailer_layout);
		mRuleLayout = (LinearLayout) findViewById(R.id.indiana_list_type_rule_layout);
		mCurrencyText = (TextView) findViewById(R.id.indiana_list_type_current_text);
		mHistoryText = (TextView) findViewById(R.id.indiana_list_type_trailer_text);
		mNoticeText = (TextView) findViewById(R.id.indiana_list_type_temporary_text);
		mRuleText = (TextView) findViewById(R.id.indiana_list_type_rule_text);
		mCurrencyView = findViewById(R.id.indiana_list_type_current_view);
		mHistoryView = findViewById(R.id.indiana_list_type_trailer_view);
		mNoticeView = findViewById(R.id.indiana_list_type_temporary_view);
		mRuleView = findViewById(R.id.indiana_list_type_rule_view);
		mWebView = (WebView) findViewById(R.id.indiana_list_web_view);
		mBackImage = (ImageView) findViewById(R.id.indiana_back_info_image);
//		mTitleText = (TextView) findViewById(R.id.title_text);
//		mTitleText.setText("2元购车");
		//
		mCurrencyLayout.setOnClickListener(this);
		mHistoryLayout.setOnClickListener(this);
		mNoticeLayout.setOnClickListener(this);
		mBackImage.setOnClickListener(this);
		mRecordText.setOnClickListener(this);
		mRuleLayout.setOnClickListener(this);
	}

	OnItemClickListener mItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), CashIndianaItemActivity.class);
			Bundle bundle = new Bundle();
			if (mList != null && mList.size() > 0
					&& mList.get(position).get("Id") != null) {
				bundle.putString("id", mList.get(position).get("Id").toString());
				bundle.putString("isStart", isStart);
				intent.putExtras(bundle);
			}
			
			startActivity(intent);
		}
	};

	private void getIndianaList() {
		StringBuilder sb = new StringBuilder();
		sb.append("productName=" + "&showTypeCode=" + code + "&pageSize=5"
				+ "&currentPage=" + page);
		XutilsUtils.get(Constants.getCashList, sb,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
					}
					@Override
					public void onSuccess(ResponseInfo<String> res) {
						mIndianaMap = Constants.getJsonObjectByData(res.result);
//						Log.i("mIndianaMap", mIndianaMap+"=======================");
						if (null != mIndianaMap
								&& null != mIndianaMap.get("rows")) {
							if (x == 1) {
								mList = Constants.getJsonArray(mIndianaMap.get(
										"rows").toString());
								handler.sendEmptyMessageDelayed(1, 0);
							}else if (x == 2) {
								mAddList = Constants.getJsonArray(mIndianaMap.get(
										"rows").toString());
								handler.sendEmptyMessageDelayed(2, 0);
							}
						}
					}
				});
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				if(mList!=null && mList.size()>0){
				mLoadLayout.setVisibility(View.GONE);
				mListView.setVisibility(View.VISIBLE);
				mNoText.setVisibility(View.GONE);
				mIndianaListAdapter = new CashIndianaListAdapter(
						getApplicationContext(), mList);
				mListView.setAdapter(mIndianaListAdapter);
				mListView.setOnItemClickListener(mItemClickListener);
				mListView.setOnScrollListener(mScrollListener);
				
				}else{
					mLoadLayout.setVisibility(View.GONE);
					mListView.setVisibility(View.GONE);
					mNoText.setVisibility(View.VISIBLE);
				}
				break;

			case 2:
				maxNum = mIndianaListAdapter.getCount()-1;
				mIndianaListAdapter.notifyList(mAddList);
				
				break;
			case 3:
				mLoadLayout.setVisibility(View.GONE);
				mListView.setVisibility(View.GONE);
				mNoText.setVisibility(View.VISIBLE);
				break;
			}
		};
	};

	OnScrollListener mScrollListener = new OnScrollListener() {
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
					&& lastVisibleIndex == mIndianaListAdapter.getCount() - 1 && maxNum<mIndianaListAdapter.getCount()-1) {
				x = 2;
				page = page + 1;
				getIndianaList();
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
		case R.id.indiana_back_info_image:
			finish();
			break;
		// 中奖纪录
		case R.id.indiana_title_record_text:
			if(Constants.onLine==1){
			intent.setClass(getApplicationContext(),
					CashMyIndianaListActivity.class);
			startActivity(intent);
			}else{
				Toast.makeText(getApplicationContext(), "请先登录帐号", Toast.LENGTH_SHORT).show();
			}
			break;
			//今日夺宝
		case R.id.indiana_list_type_current:
			x = 1;
			page = 1;
			code = 1;
			isStart = "true";
			mLoadLayout.setVisibility(View.VISIBLE);
			mListView.setVisibility(View.GONE);
			mNoText.setVisibility(View.GONE);
			mWebView.setVisibility(View.GONE);
			mCurrencyText.setTextColor(getResources().getColor(R.color.back));
			mCurrencyView.setVisibility(View.VISIBLE);
			mHistoryText.setTextColor(getResources().getColor(R.color.my_user_decription2));
			mHistoryView.setVisibility(View.GONE);
			mNoticeText.setTextColor(getResources().getColor(R.color.my_user_decription2));
			mNoticeView.setVisibility(View.GONE);
			mRuleText.setTextColor(getResources().getColor(R.color.my_user_decription2));
			mRuleView.setVisibility(View.GONE);
			getIndianaList();
			break;
			//夺宝预告
		case R.id.indiana_list_type_trailer_layout:
			x = 1;
			page = 1;
			code = 2;
			isStart = "false";
			mLoadLayout.setVisibility(View.VISIBLE);
			mListView.setVisibility(View.GONE);
			mNoText.setVisibility(View.GONE);
			mWebView.setVisibility(View.GONE);
			mCurrencyText.setTextColor(getResources().getColor(R.color.my_user_decription2));
			mCurrencyView.setVisibility(View.GONE);
			mHistoryText.setTextColor(getResources().getColor(R.color.back));
			mHistoryView.setVisibility(View.VISIBLE);
			mRuleText.setTextColor(getResources().getColor(R.color.my_user_decription2));
			mRuleView.setVisibility(View.GONE);
			mNoticeText.setTextColor(getResources().getColor(R.color.my_user_decription2));
			mNoticeView.setVisibility(View.GONE);
			getIndianaList();
			break;
			//夺宝历史
		case R.id.indiana_list_type_temporary_layout:
			x = 1;
			page = 1;
			code = 0;
			isStart = "false";
			mLoadLayout.setVisibility(View.VISIBLE);
			mListView.setVisibility(View.GONE);
			mNoText.setVisibility(View.GONE);
			mWebView.setVisibility(View.GONE);
			mCurrencyText.setTextColor(getResources().getColor(R.color.my_user_decription2));
			mCurrencyView.setVisibility(View.GONE);
			mHistoryText.setTextColor(getResources().getColor(R.color.my_user_decription2));
			mHistoryView.setVisibility(View.GONE);
			mRuleText.setTextColor(getResources().getColor(R.color.my_user_decription2));
			mRuleView.setVisibility(View.GONE);
			mNoticeText.setTextColor(getResources().getColor(R.color.back));
			mNoticeView.setVisibility(View.VISIBLE);
			getIndianaList();
			break;
		case R.id.indiana_list_type_rule_layout:
			mWebView.setVisibility(View.VISIBLE);
			mLoadLayout.setVisibility(View.GONE);
			mListView.setVisibility(View.GONE);
			mNoText.setVisibility(View.GONE);
			
			mCurrencyText.setTextColor(getResources().getColor(R.color.my_user_decription2));
			mCurrencyView.setVisibility(View.GONE);
			mHistoryText.setTextColor(getResources().getColor(R.color.my_user_decription2));
			mHistoryView.setVisibility(View.GONE);
			mNoticeText.setTextColor(getResources().getColor(R.color.my_user_decription2));
			mNoticeView.setVisibility(View.GONE);
			mRuleText.setTextColor(getResources().getColor(R.color.back));
			mRuleView.setVisibility(View.VISIBLE);
			mWebView.loadUrl("http://api.zjxssnn.com/rules/car.html");
			break;
		}
	}
}
