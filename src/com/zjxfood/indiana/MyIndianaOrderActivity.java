package com.zjxfood.indiana;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import com.zjxfood.activity.AppActivity;
import com.zjxfood.activity.R;
import com.zjxfood.common.Constants;
import com.zjxfood.http.XutilsUtils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 抽奖订单
 * 
 * @author zjx
 * 
 */
public class MyIndianaOrderActivity extends AppActivity implements OnClickListener {

	private ImageView mBackImage;
	private ListView mListView;
	private MyIndianaOrderAdapter mIndianaOrderAdapter;
	private int page = 1, code = 1;
	private HashMap<String, Object> mWinMap,mPayMap;
	private ArrayList<HashMap<String, Object>> mWinList, mAddList,mPayList;
	private int x = 1;
	private int lastVisibleIndex;
	private MyIndianaNoPayAdapter mNoPayAdapter;//未付款adapter
	private LinearLayout mAllLayout,mNoPayLayout,mPayLayout;
	private TextView mAllText,mNoPayText,mPayText;
	private View mAllView,mNoPayView,mPayView;
	private RelativeLayout mLoadLayout;
	private TextView mNoDataText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_indiana_order_layout);
		setImmerseLayout(findViewById(R.id.head_layout));
		init();
		if (Constants.mId != null) {
			getIndianaSuc();
		}
	}

	private void init() {
		mBackImage = (ImageView) findViewById(R.id.my_indiana_order_back_info_image);
		mListView = (ListView) findViewById(R.id.my_indiana_order_list);
		mAllLayout = (LinearLayout) findViewById(R.id.my_indiana_order_all_layout);
		mNoPayLayout = (LinearLayout) findViewById(R.id.my_indiana_order_nopay_layout);
		mPayLayout = (LinearLayout) findViewById(R.id.my_indiana_order_payed_layout);
		mAllText = (TextView) findViewById(R.id.my_indiana_order_all_text);
		mNoPayText = (TextView) findViewById(R.id.my_indiana_order_nopay_text);
		mPayText = (TextView) findViewById(R.id.my_indiana_order_payed_text);
		mAllView = findViewById(R.id.my_indiana_order_all_text_line);
		mNoPayView = findViewById(R.id.my_indiana_order_nopay_line);
		mPayView = findViewById(R.id.my_indiana_order_payed_line);
		mLoadLayout = (RelativeLayout) findViewById(R.id.my_order_loading_layout);
		mNoDataText = (TextView) findViewById(R.id.my_indiana_order_alert_text);

		mBackImage.setOnClickListener(this);
		mAllLayout.setOnClickListener(this);
		mNoPayLayout.setOnClickListener(this);
		mPayLayout.setOnClickListener(this);
	}

	

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				if(mWinList!=null && mWinList.size()>0){
				mListView.setVisibility(View.VISIBLE);
				mLoadLayout.setVisibility(View.GONE);
				mNoDataText.setVisibility(View.GONE);
				mIndianaOrderAdapter = new MyIndianaOrderAdapter(
						getApplicationContext(), mWinList);
				mListView.setAdapter(mIndianaOrderAdapter);
				mListView.setOnScrollListener(mScrollListener);
				mListView.setOnItemClickListener(mClickListener1);
				}else{
					mNoDataText.setVisibility(View.VISIBLE);
					mListView.setVisibility(View.GONE);
					mLoadLayout.setVisibility(View.GONE);
				}
				break;

			case 2:
				mIndianaOrderAdapter.notifyList(mAddList);
				break;
				//未付款列表
			case 3:
				mListView.setVisibility(View.VISIBLE);
				mLoadLayout.setVisibility(View.GONE);
				mNoDataText.setVisibility(View.GONE);
				mNoPayAdapter = new MyIndianaNoPayAdapter(getApplicationContext(), mPayList);
				mListView.setAdapter(mNoPayAdapter);
				mListView.setOnItemClickListener(mClickListener2);
				break;
			case 5:
				mListView.setVisibility(View.GONE);
				mLoadLayout.setVisibility(View.GONE);
				mNoDataText.setVisibility(View.VISIBLE);
				break;
			}
		};
	};

	OnScrollListener mScrollListener = new OnScrollListener() {
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
					&& lastVisibleIndex == mIndianaOrderAdapter.getCount() - 1) {
				x = 2;
				page = page + 1;
				getIndianaSuc();
			}
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			// 计算最后可见条目的索引
			lastVisibleIndex = firstVisibleItem + visibleItemCount - 1;

		}
	};

	OnItemClickListener mClickListener1 = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), MyIdaCreateOrderInfo.class);
			Bundle bundle = new Bundle();
			if (mWinList != null && mWinList.size() > 0
					&& mWinList.get(position).get("Id") != null) {
				bundle.putString("id", mWinList.get(position).get("Id").toString());
			}
			java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
			
			if(mWinList.get(position).get("ShipmentFee")!=null){
				Double price = Double.parseDouble(mWinList.get(position).get("ShipmentFee")
						.toString());
				bundle.putString("price", df.format(price));
			}
			bundle.putString("flag", "1");
			intent.putExtras(bundle);
			startActivity(intent);
		}
	};
	OnItemClickListener mClickListener2 = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			Intent intent = new Intent();
			if(code==1){
				intent.setClass(getApplicationContext(), MyIndianaNoPayActivity.class);
			}else if(code==2){
				intent.setClass(getApplicationContext(), MyIndianaPayedActivity.class);
			}
			Bundle bundle = new Bundle();
			if (mPayList != null && mPayList.size() > 0) {
				java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
				if(mPayList.get(position).get("Id")!=null){
					bundle.putString("id", mPayList.get(position).get("Id").toString());
				}
				if(mPayList.get(position).get("ShipmentFee")!=null){
//					Double price = Double.parseDouble(mNoPayList.get(position).get("ShipmentFee")
//							.toString());
					bundle.putString("price", mPayList.get(position).get("ShipmentFee").toString());
				}
				if(mPayList.get(position).get("OrderId")!=null){
					bundle.putString("OrderId", mPayList.get(position).get("OrderId").toString());
				}
				if(mPayList.get(position).get("ProductName")!=null){
					bundle.putString("ProductName", mPayList.get(position).get("ProductName").toString());
				}
				if(mPayList.get(position).get("ImgUrl")!=null){
					bundle.putString("ImgUrl", mPayList.get(position).get("ImgUrl").toString());
				}
				if(mPayList.get(position).get("UserName")!=null){
					bundle.putString("UserName", mPayList.get(position).get("UserName").toString());
				}
				if(mPayList.get(position).get("AddressId")!=null){
					bundle.putString("AddressId", mPayList.get(position).get("AddressId").toString());
				}
				if(mPayList.get(position).get("Address")!=null){
					bundle.putString("Address", mPayList.get(position).get("Address").toString());
				}
				if(mPayList.get(position).get("ShipmentName")!=null){
					bundle.putString("ShipmentName", mPayList.get(position).get("ShipmentName").toString());
				}
				if(mPayList.get(position).get("Mobile")!=null){
					bundle.putString("Mobile", mPayList.get(position).get("Mobile").toString());
				}
				if(mPayList.get(position).get("ShipmentNum")!=null){
					bundle.putString("ShipmentNum", mPayList.get(position).get("ShipmentNum").toString());
				}
				if(mPayList.get(position).get("ShipmentTime")!=null){
					bundle.putString("ShipmentTime", mPayList.get(position).get("ShipmentTime").toString());
				}
				if(mPayList.get(position).get("ShipmentNickName")!=null){
					bundle.putString("ShipmentNickName", mPayList.get(position).get("ShipmentNickName").toString());
				}
				if(mPayList.get(position).get("ShipmentCompany")!=null){
					bundle.putString("ShipmentCompany", mPayList.get(position).get("ShipmentCompany").toString());
				}
			}
			bundle.putString("flag", "1");
			intent.putExtras(bundle);
			startActivity(intent);
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.my_indiana_order_back_info_image:
			finish();
			break;
			//全部商品列表
		case R.id.my_indiana_order_all_layout:
			mLoadLayout.setVisibility(View.VISIBLE);
			mListView.setVisibility(View.GONE);
			mNoDataText.setVisibility(View.GONE);
			mAllText.setTextColor(getResources().getColor(R.color.my_order_fanli_color));
			mAllView.setVisibility(View.VISIBLE);
			mNoPayText.setTextColor(getResources().getColor(R.color.my_indiana_text_color));
			mNoPayView.setVisibility(View.GONE);
			mPayText.setTextColor(getResources().getColor(R.color.my_indiana_text_color));
			mPayView.setVisibility(View.GONE);
			page = 1;
			x = 1;
			code = 1;
			getIndianaSuc();
			
			break;
		case R.id.my_indiana_order_nopay_layout:
			mLoadLayout.setVisibility(View.VISIBLE);
			mListView.setVisibility(View.GONE);
			mNoDataText.setVisibility(View.GONE);
			mAllText.setTextColor(getResources().getColor(R.color.my_indiana_text_color));
			mAllView.setVisibility(View.GONE);
			mNoPayText.setTextColor(getResources().getColor(R.color.my_order_fanli_color));
			mNoPayView.setVisibility(View.VISIBLE);
			mPayText.setTextColor(getResources().getColor(R.color.my_indiana_text_color));
			mPayView.setVisibility(View.GONE);
			page = 1;
			x = 1;
			code = 1;
			getNoPayOrder();
			break;
		case R.id.my_indiana_order_payed_layout:
			mLoadLayout.setVisibility(View.VISIBLE);
			mListView.setVisibility(View.GONE);
			mNoDataText.setVisibility(View.GONE);
			mAllText.setTextColor(getResources().getColor(R.color.my_indiana_text_color));
			mAllView.setVisibility(View.GONE);
			mNoPayText.setTextColor(getResources().getColor(R.color.my_indiana_text_color));
			mNoPayView.setVisibility(View.GONE);
			mPayText.setTextColor(getResources().getColor(R.color.my_order_fanli_color));
			mPayView.setVisibility(View.VISIBLE);
			page = 1;
			x = 1;
			code = 2;
			getNoPayOrder();
			break;
		}
	}
	
	// 获取已中奖列表
		private void getIndianaSuc() {
			StringBuilder sb = new StringBuilder();
			sb.append("userId=" + Constants.mId + "&showTypeCode=" + code
					+ "&pageSize=5" + "&currentPage=" + page);
			XutilsUtils.get(Constants.getMyIndiana, sb,
					new RequestCallBack<String>() {
						@Override
						public void onFailure(HttpException arg0, String arg1) {
						}

						@Override
						public void onSuccess(ResponseInfo<String> res) {
							mWinMap = Constants.getJsonObjectByData(res.result);
							if (mWinMap != null && mWinMap.size() > 0
									&& mWinMap.get("rows") != null) {

								if (x == 1) {
									mWinList = Constants.getJsonArray(mWinMap.get(
											"rows").toString());
									if (mWinList != null && mWinList.size() > 0) {
										handler.sendEmptyMessageDelayed(1, 0);
									}else{
										handler.sendEmptyMessageDelayed(5, 0);
									}
								} else if (x == 2) {
									mAddList = Constants.getJsonArray(mWinMap.get(
											"rows").toString());
									if (mAddList != null && mWinList.size() > 0) {
										handler.sendEmptyMessageDelayed(2, 0);
									}
								}
							}
						}
					});
		}
		// 获取未/已 付款订单
		private void getNoPayOrder() {
			StringBuilder sb = new StringBuilder();
			sb.append("pageSize=5" + "&currentPage=" + page
					+ "&statusCode="+code + "&userId=" + Constants.mId+"&orderNum="+"&startTime="+"&endTime="+"&isAdmin=false");
			XutilsUtils.get(Constants.getIndianaOrder, sb,
					new RequestCallBack<String>() {
						@Override
						public void onFailure(HttpException arg0, String arg1) {
						}
						@Override
						public void onSuccess(ResponseInfo<String> res) {
							mPayMap = Constants.getJsonObjectByData(res.result);
							if (mPayMap != null && mPayMap.size() > 0
									&& mPayMap.get("rows") != null) {
								if (x == 1) {
									mPayList = Constants.getJsonArray(mPayMap.get(
											"rows").toString());
									if (mPayList != null && mPayList.size() > 0) {
										handler.sendEmptyMessageDelayed(3, 0);
									}else{
										handler.sendEmptyMessageDelayed(5, 0);
									}
								} else if (x == 2) {
									mAddList = Constants.getJsonArray(mPayMap.get(
											"rows").toString());
									if (mPayList != null && mPayList.size() > 0) {
										handler.sendEmptyMessageDelayed(4, 0);
									}
								}
							}
						}
					});
		}
}
