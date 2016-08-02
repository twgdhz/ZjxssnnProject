package com.zjxfood.activity;

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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.zjxfood.adapter.MyAuctionAdapter;
import com.zjxfood.adapter.MyAuctionOrderAdapter;
import com.zjxfood.common.Constants;
import com.zjxfood.http.XutilsUtils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 我的竞拍记录
 * 
 * @author zjx
 * 
 */
public class MyAuctionActivity extends AppActivity implements OnClickListener {

	private ListView mListView;
	private MyAuctionAdapter mAuctionRecordAdapter;
	private int page = 1;
	private HashMap<String, Object> mAuictionMap;
	private ArrayList<HashMap<String, Object>> mAuctionList;
	private LinearLayout mRecordLayout, mNoPayLayout, mTruePayLayout;
	private String payState = "1";// 支付状态
	private MyAuctionOrderAdapter mAuctionOrderAdapter;
	private View mRecordView, mNoPayView, mTruePayView;
	private RelativeLayout mAlertLayout;
	private int x = 1;//未支付、已付款、竞拍记录状态
	private ImageView mBackImage;
	private String st = "1";//标识记录/未付款
	private int lastVisibleIndex;
	private String type = "1";//标识选择列表下拉
	private TextView mTitleText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_auction_order_layout);
		setImmerseLayout(findViewById(R.id.head_layout));
		init();
		getRecordList();
	}

	private void init() {
		mListView = (ListView) findViewById(R.id.my_auction_order_list);
		mRecordLayout = (LinearLayout) findViewById(R.id.auction_order_record_layout);
		mNoPayLayout = (LinearLayout) findViewById(R.id.auction_order_no_layout);
		mTruePayLayout = (LinearLayout) findViewById(R.id.auction_order_true_layout);
		mRecordView = findViewById(R.id.auction_order_record_text_line);
		mNoPayView = findViewById(R.id.auction_order_no_text_line);
		mTruePayView = findViewById(R.id.auction_order_true_line);
		mAlertLayout = (RelativeLayout) findViewById(R.id.auction_order_no_show_layout);
//		mBackImage = (ImageView) findViewById(R.id.title_back_image);
		mBackImage = (ImageView) findViewById(R.id.title_back_image);
		mTitleText = (TextView) findViewById(R.id.title_text);
		mTitleText.setText("竞拍订单");

		mRecordLayout.setOnClickListener(this);
		mNoPayLayout.setOnClickListener(this);
		mTruePayLayout.setOnClickListener(this);
		mBackImage.setOnClickListener(this);
	}

	// 获取竞拍记录列表
//	private void getRecordList() {
//		RequestParams params = new RequestParams();
//		params.put("userId", Constants.mId);
//		params.put("isEnd", "true");
//		params.put("pageSize", "10");
//		params.put("currentPage", page + "");
//		AsyUtils.get(Constants.getAuctionOrderList, params,
//				new AsyncHttpResponseHandler() {
//					@Override
//					@Deprecated
//					public void onSuccess(int statusCode, String content) {
//						mAuictionMap = Constants.getJsonObjectByData(content);
//						Log.i("mAuictionMap", mAuictionMap + "================");
//						if (mAuictionMap != null
//								&& mAuictionMap.get("rows") != null) {
//							mAuctionList = Constants.getJsonArray(mAuictionMap
//									.get("rows").toString());
//							if(x==1){
//							handler.sendEmptyMessageDelayed(1, 0);
//							}else if(x==2){
//								handler.sendEmptyMessageDelayed(4, 0);
//							}
//						} else {
//							handler.sendEmptyMessageDelayed(3, 0);
//						}
//						super.onSuccess(statusCode, content);
//					}
//				});
//	}
	private void getRecordList() {
		StringBuilder sb = new StringBuilder();
			sb.append("userId=" + Constants.mId+"&isEnd=true"+"&pageSize=10"+"&currentPage="+page);
		XutilsUtils.get(Constants.getAuctionOrderList, sb,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
					}
					@Override
					public void onSuccess(ResponseInfo<String> res) {
						mAuictionMap = Constants.getJsonObjectByData(res.result);
						Log.i("mAuictionMap", mAuictionMap + "================");
						if (mAuictionMap != null
								&& mAuictionMap.get("rows") != null) {
							mAuctionList = Constants.getJsonArray(mAuictionMap
									.get("rows").toString());
							if(x==1){
							handler.sendEmptyMessageDelayed(1, 0);
							}else if(x==2){
								handler.sendEmptyMessageDelayed(4, 0);
							}
						} else {
							handler.sendEmptyMessageDelayed(3, 0);
						}
					}
				});
	}

		private void getPayOrderList() {
			StringBuilder sb = new StringBuilder();
				sb.append("pageSize=5").append("&currentPage="+page+"&userId="+Constants.mId+"&statusCode="+payState)
				.append("&orderNum=");
			
			XutilsUtils.get(Constants.getAuctionOrderLists, sb,
					new RequestCallBack<String>() {
						@Override
						public void onFailure(HttpException arg0, String arg1) {
							Log.i("onFailure", arg1 + "================");
							handler.sendEmptyMessageDelayed(3, 0);
						}
						@Override
						public void onSuccess(ResponseInfo<String> res) {
							mAuictionMap = Constants.getJsonObjectByData(res.result);
							Log.i("mAuictionMap", mAuictionMap + "================");
							if (mAuictionMap != null
									&& mAuictionMap.get("rows") != null) {
								mAuctionList = Constants.getJsonArray(mAuictionMap
										.get("rows").toString());
								if(x==1){
								handler.sendEmptyMessageDelayed(2, 0);
								}else if(x==2){
									handler.sendEmptyMessageDelayed(5, 0);
								}
							} else {
								handler.sendEmptyMessageDelayed(3, 0);
							}
						}
					});
		}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				if (mAuctionList != null && mAuctionList.size() > 0) {
					mAlertLayout.setVisibility(View.GONE);
					mListView.setVisibility(View.VISIBLE);
					mAuctionRecordAdapter = new MyAuctionAdapter(
							getApplicationContext(), mAuctionList);
					mListView.setAdapter(mAuctionRecordAdapter);
					mListView.setOnItemClickListener(mItemClickListener);
					mListView.setOnScrollListener(mScrollListener);
				}else{
					mListView.setVisibility(View.GONE);
					mAlertLayout.setVisibility(View.VISIBLE);
				}
				break;

			case 2:
				if (mAuctionList != null && mAuctionList.size() > 0) {
					mAlertLayout.setVisibility(View.GONE);
					mListView.setVisibility(View.VISIBLE);
					mAuctionOrderAdapter = new MyAuctionOrderAdapter(
							getApplicationContext(), mAuctionList);
					mListView.setAdapter(mAuctionOrderAdapter);
					if(payState.equals("1")){
						mListView.setOnItemClickListener(mItemClickListener2);
					}else if(payState.equals("2")){
					mListView.setOnItemClickListener(mItemClickListener3);
					}
					mListView.setOnScrollListener(mScrollListener);
				}else{
					mListView.setVisibility(View.GONE);
					mAlertLayout.setVisibility(View.VISIBLE);
				}
				break;
			case 3:
				mListView.setVisibility(View.GONE);
				mAlertLayout.setVisibility(View.VISIBLE);
				break;
			case 4:
				if(mAuctionList!=null && mAuctionList.size()>0){
				mAuctionRecordAdapter.notifyList(mAuctionList);
				}
				break;
			case 5:
				
				break;
			}
		};
	};
	//创建订单
	OnItemClickListener mItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(),
					MyAuctionOrderItemActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("flag", "1");
			bundle.putString("st", st);
			if (mAuctionList != null && mAuctionList.size() > 0) {
				if (!Constants.isNull(mAuctionList.get(position).get(
						"ProductName"))) {
					bundle.putString("ProductName", mAuctionList.get(position)
							.get("ProductName").toString());
				}
				if (!Constants.isNull(mAuctionList.get(position).get(
						"Price"))) {
					bundle.putString("BasePrice", mAuctionList.get(position)
							.get("Price").toString());
				}
				if (!Constants.isNull(mAuctionList.get(position).get(
						"ExtField1"))) {
					bundle.putString("ExtField1", mAuctionList.get(position)
							.get("ExtField1").toString());
				}
				if (!Constants.isNull(mAuctionList.get(position).get(
						"ExtField2"))) {
					bundle.putString("ExtField2", mAuctionList.get(position)
							.get("ExtField2").toString());
				}
				if (!Constants.isNull(mAuctionList.get(position).get(
						"ExtField3"))) {
					bundle.putString("ExtField3", mAuctionList.get(position)
							.get("ExtField3").toString());
				}
				if (!Constants.isNull(mAuctionList.get(position).get(
						"ExtField4"))) {
					bundle.putString("ExtField4", mAuctionList.get(position)
							.get("ExtField4").toString());
				}
				if (!Constants.isNull(mAuctionList.get(position).get(
						"ExtField5"))) {
					bundle.putString("ExtField5", mAuctionList.get(position)
							.get("ExtField5").toString());
				}
				if (!Constants.isNull(mAuctionList.get(position).get("Detail"))) {
					bundle.putString("Detail",
							mAuctionList.get(position).get("Detail").toString());
				}
				if (!Constants.isNull(mAuctionList.get(position).get(
						"ShipmentFee"))) {
					bundle.putString("ShipmentFee", mAuctionList.get(position)
							.get("ShipmentFee").toString());
				}
				if (!Constants.isNull(mAuctionList.get(position).get("ImgUrl"))) {
					bundle.putString("ImgUrl",
							mAuctionList.get(position).get("ImgUrl").toString());
				}
				if (!Constants.isNull(mAuctionList.get(position).get("Id"))) {
					bundle.putString("Id",
							mAuctionList.get(position).get("Id").toString());
				}
				intent.putExtras(bundle);
			}
			
			startActivity(intent);
		}
	};
	//未付款
	OnItemClickListener mItemClickListener2 = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(),
					MyAuctionOrderNoPayItemActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("flag", "1");
			
			if (mAuctionList != null && mAuctionList.size() > 0) {
				Log.i("mAuctionList", mAuctionList+"===============");
				if (!Constants.isNull(mAuctionList.get(position).get(
						"OrderId"))) {
					bundle.putString("OrderId", mAuctionList.get(position)
							.get("OrderId").toString());
				}
				if (!Constants.isNull(mAuctionList.get(position).get(
						"AutionId"))) {
					bundle.putString("AutionId", mAuctionList.get(position)
							.get("AutionId").toString());
				}
				if (!Constants.isNull(mAuctionList.get(position).get(
						"AddressId"))) {
					bundle.putString("AddressId", mAuctionList.get(position)
							.get("AddressId").toString());
				}
				if (!Constants.isNull(mAuctionList.get(position).get(
						"ProductName"))) {
					bundle.putString("ProductName", mAuctionList.get(position)
							.get("ProductName").toString());
				}
				if (!Constants.isNull(mAuctionList.get(position).get(
						"EndPrice"))) {
					bundle.putString("BasePrice", mAuctionList.get(position)
							.get("EndPrice").toString());
				}
				if (!Constants.isNull(mAuctionList.get(position).get(
						"CreateTime"))) {
					bundle.putString("ExtField1", mAuctionList.get(position)
							.get("CreateTime").toString());
				}
				if (!Constants.isNull(mAuctionList.get(position).get(
						"ShipmentNum"))) {
					bundle.putString("ExtField2", mAuctionList.get(position)
							.get("ShipmentNum").toString());
				}
				if (!Constants.isNull(mAuctionList.get(position).get(
						"ShipmentCompany"))) {
					bundle.putString("ExtField3", mAuctionList.get(position)
							.get("ShipmentCompany").toString());
				}
				if (!Constants.isNull(mAuctionList.get(position).get(
						"ShipmentTime"))) {
					bundle.putString("ExtField4", mAuctionList.get(position)
							.get("ShipmentTime").toString());
				}
				if (!Constants.isNull(mAuctionList.get(position).get(
						"ShipmentNickName"))) {
					bundle.putString("ExtField5", mAuctionList.get(position)
							.get("ShipmentNickName").toString());
				}
				if (!Constants.isNull(mAuctionList.get(position).get("Detail"))) {
					bundle.putString("Detail",
							mAuctionList.get(position).get("Detail").toString());
				}
				if (!Constants.isNull(mAuctionList.get(position).get(
						"ShipmentFee"))) {
					bundle.putString("ShipmentFee", mAuctionList.get(position)
							.get("ShipmentFee").toString());
				}
				if (!Constants.isNull(mAuctionList.get(position).get("ImgUrl"))) {
					bundle.putString("ImgUrl",
							mAuctionList.get(position).get("ImgUrl").toString());
				}
				if (!Constants.isNull(mAuctionList.get(position).get("OrderId"))) {
					bundle.putString("Id",
							mAuctionList.get(position).get("OrderId").toString());
				}
				intent.putExtras(bundle);
			}
			
			startActivity(intent);
		}
	};
	//已付款
	OnItemClickListener mItemClickListener3 = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(),
					MyAuctionOrderPayInfoActivity.class);
			Bundle bundle = new Bundle();
			if (mAuctionList != null && mAuctionList.size() > 0) {
				if(mAuctionList.get(position).get("OrderId")!=null){
					bundle.putString("id", mAuctionList.get(position).get("OrderId").toString());
					intent.putExtras(bundle);
				}
			}
			
			startActivity(intent);
		}
	};
	
	OnScrollListener mScrollListener = new OnScrollListener() {
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			Log.i("page", page + "===========1=========");
			try{
			if(type.equals("1")){
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
						&& lastVisibleIndex == mAuctionRecordAdapter.getCount() - 1) {
					x = 2;
					page = page + 1;
					getRecordList();
				}
			}else if(type.equals("2") || type.equals("3")){
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
						&& lastVisibleIndex == mAuctionOrderAdapter.getCount() - 1) {
					x = 2;
					page = page + 1;
					getPayOrderList();
				}
			}
			}catch(Exception e){
				e.printStackTrace();
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
		switch (v.getId()) {
		// 竞拍商品
		case R.id.auction_order_record_layout:
			x = 1;
			page = 1;
			type = "1";
			getRecordList();
			mRecordView.setVisibility(View.VISIBLE);
			mNoPayView.setVisibility(View.GONE);
			mTruePayView.setVisibility(View.GONE);
			break;
		// 未付款
		case R.id.auction_order_no_layout:
			x = 1;
			payState = "1";
			type = "2";
			page = 1;
			getPayOrderList();
			mRecordView.setVisibility(View.GONE);
			mNoPayView.setVisibility(View.VISIBLE);
			mTruePayView.setVisibility(View.GONE);
			break;
		// 已付款
		case R.id.auction_order_true_layout:
			x = 1;
			payState = "2";
			type = "3";
			page = 1;
			getPayOrderList();
			mRecordView.setVisibility(View.GONE);
			mNoPayView.setVisibility(View.GONE);
			mTruePayView.setVisibility(View.VISIBLE);
			break;
		case R.id.title_back_image:
			finish();
			break;
		}
	}
}
