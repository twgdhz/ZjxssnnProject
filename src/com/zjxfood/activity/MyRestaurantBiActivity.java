package com.zjxfood.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.umeng.analytics.MobclickAgent;
import com.zjxfood.adapter.ShmoneyListAdapter;
import com.zjxfood.common.Constants;
import com.zjxfood.http.XutilsUtils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 我的食尚币
 * 
 * @author zjx
 * 
 */
public class MyRestaurantBiActivity extends AppActivity implements OnClickListener {

//	private LinearLayout mThisMonthLayout;
//	private LinearLayout mNextMonthLayout;
	private RelativeLayout mHeadLayout;
	private ImageView mBackImage;
	private TextView mShMoney;
	private Button mExchangeGiftBtn;
	private TextView mBiChongzhi;
	private ListView mListView;
	private ArrayList<HashMap<String, Object>> mList;
	private ShmoneyListAdapter mShmoneyListAdapter;
	private String mShmoney = "";
	// 最后可见条目的索引
	private int lastVisibleIndex;
//	private int size = 15;
	private int x = 1;
//	private RunTask mRunTask;
	private int page = 1;
	private ArrayList<HashMap<String, Object>> mAddList;
//	private ExecutorService mExecutorService = Executors.newCachedThreadPool();
	private TextView mTitleText;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_restaurant_bi_layout);
		setImmerseLayout(findViewById(R.id.head_layout));
		init();
		getBiList();
		getBiHttp();
	}

	private void init() {
//		mThisMonthLayout = (LinearLayout) findViewById(R.id.restaurantthis_month_list_layout);
//		mNextMonthLayout = (LinearLayout) findViewById(R.id.restaurant_bi_next_list_layout);
		mHeadLayout = (RelativeLayout) findViewById(R.id.title_restaurant_bi_layout);
//		mBackImage = (ImageView) mHeadLayout	.findViewById(R.id.title_back_image);
		mBackImage = (ImageView) findViewById(R.id.title_back_image);
		mTitleText = (TextView) findViewById(R.id.title_text);
		mTitleText.setText("我的食尚币");
		mShMoney = (TextView) findViewById(R.id.restaurant_bi_now_surplus_number);
		mExchangeGiftBtn = (Button) findViewById(R.id.restaurant_bi_bottom_btn);
		mBiChongzhi = (TextView) findViewById(R.id.restaurant_chongzhi_text);
		mListView = (ListView) findViewById(R.id.monsy_list);

		mBackImage.setOnClickListener(this);
		mExchangeGiftBtn.setOnClickListener(this);
		mBiChongzhi.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.title_back_image:
			finish();
			break;

		case R.id.restaurant_bi_bottom_btn:
			intent.setClass(getApplicationContext(), MallIndexActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.restaurant_chongzhi_text:
			intent.setClass(getApplicationContext(), BiChongzhiActivity.class);
			startActivity(intent);
			finish();
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			finish();
			break;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
	//获取食尚币列表
//	private void getBiList(){
//		RequestParams params = new RequestParams();
//		params.put("userid", Constants.mId);
//		params.put("pagesize", "10");
//		params.put("page",page+"");
//		AsyUtils.get(Constants.getMonsyList2, params, new AsyncHttpResponseHandler(){
//			@Override
//			@Deprecated
//			public void onSuccess(int statusCode, String content) {
//				if(x==1){
//				mList = Constants.getJsonArray(content);
//				handler.sendEmptyMessageDelayed(1, 0);
//				}else if(x==2){
//					mAddList = Constants.getJsonArray(content);
//					handler.sendEmptyMessageDelayed(3, 0);
//				}
//				super.onSuccess(statusCode, content);
//			}
//		});
//	}
	
	private void getBiList() {
		StringBuilder sb = new StringBuilder();
		sb.append("userid="+Constants.mId+"&pagesize=10"+"&page="+page);
		XutilsUtils.get(Constants.getMonsyList2, sb,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
					}
					@Override
					public void onSuccess(ResponseInfo<String> res) {
						if(x==1){
							mList = Constants.getJsonArray(res.result);
							handler.sendEmptyMessageDelayed(1, 0);
							}else if(x==2){
								mAddList = Constants.getJsonArray(res.result);
								handler.sendEmptyMessageDelayed(3, 0);
							}
						}
				});
	}
	
	//获取食尚币
//	private void getBiHttp(){
//		RequestParams params = new RequestParams();
//		params.put("username", Constants.mUserName);
//		AsyUtils.get(Constants.getUserShmoney2, params, new AsyncHttpResponseHandler(){
//			@Override
//			@Deprecated
//			public void onSuccess(int statusCode, String content) {
//				mShmoney = content;
//				handler.sendEmptyMessageDelayed(2, 0);
//				super.onSuccess(statusCode, content);
//			}
//		});
//	}
	private void getBiHttp() {
		StringBuilder sb = new StringBuilder();
		sb.append("username="+Constants.mUserName);
		XutilsUtils.get(Constants.getUserShmoney2, sb,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
					}
					@Override
					public void onSuccess(ResponseInfo<String> res) {
						mShmoney = res.result;
						handler.sendEmptyMessageDelayed(2, 0);
					}
				});
	}

	OnScrollListener mScrollListener = new OnScrollListener() {
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
					&& lastVisibleIndex == mShmoneyListAdapter.getCount()-1) {
				x = 2;
				page = page+1;
				getBiList();
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
				if(mList!=null){
				mShmoneyListAdapter = new ShmoneyListAdapter(
						getApplicationContext(), mList);
				mListView.setAdapter(mShmoneyListAdapter);
				mListView.setOnScrollListener(mScrollListener);
				}
				break;

			case 2:
				mShMoney.setText(mShmoney);
				Constants.mShMoney = mShmoney;
				break;
			case 3:
				if(mAddList!=null && mAddList.size()>0){
				mShmoneyListAdapter.notify(mAddList);
				}
				break;
			}
		};
	};
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}
}
