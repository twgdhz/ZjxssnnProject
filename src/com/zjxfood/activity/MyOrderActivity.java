package com.zjxfood.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.umeng.analytics.MobclickAgent;
import com.zjxfood.adapter.MyOrderListAdapter;
import com.zjxfood.application.ExitApplication;
import com.zjxfood.common.Constants;
import com.zjxfood.http.XutilsUtils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 我的消费订单
 * 
 * @author zjx
 * 
 */
public class MyOrderActivity extends AppActivity implements OnClickListener {

	private ListView mListView;
	private MyOrderListAdapter mOrderListAdapter;
	private RelativeLayout mHeadLayout;
	private ImageView mBackImage;
	private ArrayList<HashMap<String, Object>> mList;
	// private OrderTask mOrderTask;
	private int x = 3;
	private TextView mAllOrder, mNotEvaluatedp;
	private View mAllOrderView, mNotView;
	// 最后可见条目的索引
	private int lastVisibleIndex;
//	private int size = 10;
//	private String userId = Constants.mId;
	private int m = 1;// 当前列表显示的是全部评价或已评价
	private int page = 1;
	private ArrayList<HashMap<String, Object>> mAddList;
	private RelativeLayout mAlertLayout;
	private RelativeLayout mLoadLayout;
	private TextView mTitleText;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_order_layout);
		setImmerseLayout(findViewById(R.id.head_layout));
		ExitApplication.getInstance().addActivity(this);
		ExitApplication.getInstance().addMyOrderActivity(this);
		initView();
		getNoEvalList();
	}

	private void initView() {
		mListView = (ListView) findViewById(R.id.my_order_list);
		mHeadLayout = (RelativeLayout) findViewById(R.id.my_order_title_id);
//		mBackImage = (ImageView) mHeadLayout.findViewById(R.id.title_back_image);
		mBackImage = (ImageView) findViewById(R.id.title_back_image);
		mTitleText = (TextView) findViewById(R.id.title_text);
		mTitleText.setText("消费订单");
		mAllOrder = (TextView) findViewById(R.id.my_order_all_text);
		mAllOrderView = findViewById(R.id.my_order_all_text_line);
		mNotEvaluatedp = (TextView) findViewById(R.id.my_order_not_comment_text);
		mNotView = findViewById(R.id.my_order_not_comment_line);
		mAlertLayout = (RelativeLayout) findViewById(R.id.my_order_alert_layout);
		mLoadLayout = (RelativeLayout) findViewById(R.id.my_order_loading_layout);

		mBackImage.setOnClickListener(this);
		mAllOrder.setOnClickListener(this);
		mNotEvaluatedp.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_back_image:
			finish();
			break;

		case R.id.my_order_all_text:
			mListView.setVisibility(View.GONE);
			mAlertLayout.setVisibility(View.GONE);
			mLoadLayout.setVisibility(View.VISIBLE);
			page = 1;
			m = 1;
			mAllOrder.setTextColor(getResources().getColor(
					R.color.my_order_text_color));
			mAllOrderView.setVisibility(View.VISIBLE);

			mNotEvaluatedp.setTextColor(getResources().getColor(
					R.color.my_user_decription2));
			mNotView.setVisibility(View.GONE);
			x = 3;
			getNoEvalList();
			break;

		case R.id.my_order_not_comment_text:
			mListView.setVisibility(View.GONE);
			mLoadLayout.setVisibility(View.VISIBLE);
			mAlertLayout.setVisibility(View.GONE);
			page = 1;
			m = 2;
			mAllOrder.setTextColor(getResources().getColor(
					R.color.my_user_decription2));
			mAllOrderView.setVisibility(View.GONE);

			mNotEvaluatedp.setTextColor(getResources().getColor(
					R.color.my_order_text_color));
			mNotView.setVisibility(View.VISIBLE);
			x = 1;
			getAllEvalList();
			break;
		}
	}

	OnItemClickListener mItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), MyOrderInfoActivity.class);
//			intent.setClass(getApplicationContext(), GrabRedActivity.class);
			Bundle bundle = new Bundle();
			if (!Constants.isNull(mList.get(position).get("Payid"))) {
				bundle.putString("payId", mList.get(position).get("Payid")
						.toString());
			}
			intent.putExtras(bundle);
			startActivity(intent);
		}
	};

	OnScrollListener mScrollListener = new OnScrollListener() {

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
					&& lastVisibleIndex == mOrderListAdapter.getCount() - 1) {
				page = page + 1;
				switch (m) {
				case 1:
					x = 4;
					getAllEvalList();
					break;

				case 2:
					x = 2;
					getNoEvalList();
					break;
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

	// String str = "userid=" + Constants.mId + "&pagesize=10" + "&page="
	// + page;
	// 获取已经评价订单
	private void getAllEvalList() {
		StringBuilder sb = new StringBuilder();
		sb.append("uid=" + Constants.mId).append("&pagesize=10")
				.append("&page=" + page).append("&hadcomment=true");
		XutilsUtils.get(Constants.getOrderList3, sb,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
					}
					@Override
					public void onSuccess(ResponseInfo<String> res) {
						try{
						if (x == 1) {
							mList = Constants.getJsonArrayByData(res.result);
							handler.sendEmptyMessageDelayed(1, 0);
						} else if (x == 4) {
							mAddList = Constants.getJsonArrayByData(res.result);
							handler.sendEmptyMessageDelayed(2, 0);
						}
						}catch(Exception e){
							e.printStackTrace();
						}
						Log.i("已评价", mList+"=====================");
					}
				});
	}

	// 获取未评价订单
	private void getNoEvalList() {
		StringBuilder sb = new StringBuilder();
		sb.append("uid=" + Constants.mId).append("&pagesize=10")
				.append("&page=" + page).append("&hadcomment=false");
		XutilsUtils.get(Constants.getOrderList3, sb,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						handler.sendEmptyMessageDelayed(4, 0);
					}
					@Override
					public void onSuccess(ResponseInfo<String> res) {
						try{
						if (x == 3) {
							mList = Constants.getJsonArrayByData(res.result);
							
							handler.sendEmptyMessageDelayed(3, 0);
						} else if (x == 2) {
							mAddList = Constants.getJsonArrayByData(res.result);
							handler.sendEmptyMessageDelayed(2, 0);
						}
						}catch(Exception exception){
							exception.printStackTrace();
						}
						Log.i("未评价", mList+"=====================");
					}
				});
	}

	

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				if (mList != null && mList.size() > 0) {
					mLoadLayout.setVisibility(View.GONE);
					mAlertLayout.setVisibility(View.GONE);
					mListView.setVisibility(View.VISIBLE);
					mOrderListAdapter = new MyOrderListAdapter(
							getApplicationContext(), mList);
					mListView.setAdapter(mOrderListAdapter);
					mListView.setOnItemClickListener(mItemClickListener);
					mListView.setOnScrollListener(mScrollListener);
				}else{
					
					mAlertLayout.setVisibility(View.VISIBLE);
					mListView.setVisibility(View.GONE);
					mLoadLayout.setVisibility(View.GONE);
				}
				
				break;

			case 2:
				if (mAddList != null && mAddList.size() > 0) {
					mList.addAll(mAddList);
					mOrderListAdapter.notify(mList);
				}
				break;
			case 3:
//				Log.i("mList", mList+"==============");
				if (mList != null && mList.size() > 0) {
					mLoadLayout.setVisibility(View.GONE);
					mAlertLayout.setVisibility(View.GONE);
					mListView.setVisibility(View.VISIBLE);
					mOrderListAdapter = new MyOrderListAdapter(
							getApplicationContext(), mList);
					mListView.setAdapter(mOrderListAdapter);
					mListView.setOnItemClickListener(mItemClickListener);
					mListView.setOnScrollListener(mScrollListener);
				}else{
					Log.i("handler", "===================");
					mLoadLayout.setVisibility(View.GONE);
					mAlertLayout.setVisibility(View.VISIBLE);
					mListView.setVisibility(View.GONE);
				}
				break;
			case 4:
				mLoadLayout.setVisibility(View.GONE);
				mAlertLayout.setVisibility(View.VISIBLE);
				mListView.setVisibility(View.GONE);
				break;
			}
		};
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

	@Override
	protected void onDestroy() {
		System.gc();
		super.onDestroy();
	}

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
