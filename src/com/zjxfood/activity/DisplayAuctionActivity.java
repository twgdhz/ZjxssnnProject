package com.zjxfood.activity;

import android.annotation.SuppressLint;
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
import com.zjxfood.adapter.DisplayAuctionAdapter;
import com.zjxfood.adapter.DisplayAuctionTemporaryAdapter;
import com.zjxfood.adapter.DisplayAuctionTrailerAdapter;
import com.zjxfood.common.Constants;
import com.zjxfood.http.XutilsUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * 限时拍卖
 * 
 * @author zjx
 * 
 */
public class DisplayAuctionActivity extends AppActivity implements OnClickListener {

	private ListView mListView;
	private DisplayAuctionAdapter mAuctionAdapter;
	private DisplayAuctionTrailerAdapter mTrailerAdapter;
	private DisplayAuctionTemporaryAdapter mTemporaryAdapter;
	private ImageView mBackImage;
	private int page = 1;
	private ArrayList<HashMap<String, Object>> mAuctionList,mAddList;
	private HashMap<String, Object> mAuctionMap;
	private int lastVisibleIndex;
	private int x = 1;
	private TextView mAuctionDate,mAuctionWeek;
	private Calendar c = Calendar.getInstance();
	private static String mWay;
	private RelativeLayout mAlertLayout;
	public static final String action = "auction.default.broadcast";
	private int maxNum= 0;
	private LinearLayout mCurrentLayout,mTemporaryLayout,mTrailerLayout;
	private TextView mCurrentText,mTrailerText,mTemporaryText;
	private View mCurrentView,mTrailerView,mTemporaryView;
	private int mTypeCode=1;
	private RelativeLayout mLoadLayout;
	private int n=1;
	private TextView mTitleText;

	@SuppressLint("SimpleDateFormat")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_mall_display_auction);
		setImmerseLayout(findViewById(R.id.head_layout));
		init();
		getAuctionList();
		SimpleDateFormat formatter = new SimpleDateFormat("MM月dd日");       
		Date curDate = new Date(System.currentTimeMillis());//获取当前时间       
		String date = formatter.format(curDate);   
		mAuctionDate.setText(date);mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));  
        if("1".equals(mWay)){
            mWay ="星期天";  
        }else if("2".equals(mWay)){
            mWay ="星期一";  
        }else if("3".equals(mWay)){
            mWay ="星期二";  
        }else if("4".equals(mWay)){
            mWay ="星期三";  
        }else if("5".equals(mWay)){
            mWay ="星期四";  
        }else if("6".equals(mWay)){
            mWay ="星期五";  
        }else if("7".equals(mWay)){
            mWay ="星期六";  
        }
        mAuctionWeek.setText(mWay);
	}

	private void init() {
		mListView = (ListView) findViewById(R.id.display_auction_list);
		mAuctionDate = (TextView) findViewById(R.id.display_auction_head_text);
		mAuctionWeek = (TextView) findViewById(R.id.display_auction_head_week);
		mAlertLayout = (RelativeLayout) findViewById(R.id.display_auction_alert_layout);
		mCurrentLayout = (LinearLayout) findViewById(R.id.auction_type_current);
		mTemporaryLayout = (LinearLayout) findViewById(R.id.auction_type_temporary_layout);
		mTrailerLayout = (LinearLayout) findViewById(R.id.auction_type_trailer_layout);
		mCurrentText = (TextView) findViewById(R.id.auction_type_current_text);
		mTrailerText = (TextView) findViewById(R.id.auction_type_trailer_text);
		mTemporaryText = (TextView) findViewById(R.id.auction_type_temporary_text);
		mCurrentView = findViewById(R.id.auction_type_current_view);
		mTrailerView = findViewById(R.id.auction_type_trailer_view);
		mTemporaryView = findViewById(R.id.auction_type_temporary_view);
		mLoadLayout = (RelativeLayout) findViewById(R.id.auction_list_loading_layout);
		mBackImage = (ImageView) findViewById(R.id.title_back_image);
		mTitleText = (TextView) findViewById(R.id.title_text);
		mTitleText.setText("限时拍卖");
		
		mBackImage.setOnClickListener(this);
		mCurrentLayout.setOnClickListener(this);
		mTemporaryLayout.setOnClickListener(this);
		mTrailerLayout.setOnClickListener(this);
	}

	OnItemClickListener mItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			if (mAuctionList != null
					&& mAuctionList.size()>0) {
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(),
						DisplayAuctionItemActivity.class);
				Bundle bundle = new Bundle();
				if(!Constants.isNull(mAuctionList.get(position).get("Id"))){
				bundle.putString("id", mAuctionList.get(position).get("Id")
						.toString());
				}
				intent.putExtras(bundle);
				startActivity(intent);
			}
		}
	};

	private void getAuctionList() {
		StringBuilder sb = new StringBuilder();
		sb.append("showTypeCode="+mTypeCode+"&pageSize=5&currentPage="+page);
		XutilsUtils.get(Constants.getAuctionList, sb,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
					}
					@Override
					public void onSuccess(ResponseInfo<String> res) {
						Log.i("mAuctionMap", res.result+"============");
						try {
							mAuctionMap = Constants.getJsonObjectByData(res.result);
						} catch (Exception e) {
							e.printStackTrace();
						}
						if(mAuctionMap!=null && mAuctionMap.size()>0 && mAuctionMap.get("rows")!=null){
						if(n==1){
							if(x==1){
								mAuctionList = Constants.getJsonArray(mAuctionMap.get("rows").toString());
								handler.sendEmptyMessageDelayed(1, 0);
							}else if(x==2){
								mAddList = Constants.getJsonArray(mAuctionMap.get("rows").toString());
								handler.sendEmptyMessageDelayed(2, 0);
							}
						}else if(n ==2){
							if(x==1){
							mAuctionList = Constants.getJsonArray(mAuctionMap.get("rows").toString());
							handler.sendEmptyMessageDelayed(4, 0);
							}else if(x==2){
								mAddList = Constants.getJsonArray(mAuctionMap.get("rows").toString());
								handler.sendEmptyMessageDelayed(6, 0);
							}
						}else if(n==3){
							if(x==1){
							mAuctionList = Constants.getJsonArray(mAuctionMap.get("rows").toString());
							handler.sendEmptyMessageDelayed(5, 0);
							}else if(x==2){
								mAddList = Constants.getJsonArray(mAuctionMap.get("rows").toString());
								handler.sendEmptyMessageDelayed(7, 0);
							}
						}
					}
					}
				});
	}

	OnScrollListener mScrollListener = new OnScrollListener() {
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
//			Log.i("page", maxNum + "======="+mAuctionAdapter.getCount());
			if(n==1){
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
						&& lastVisibleIndex == mAuctionAdapter.getCount() - 1) {
					x = 2;
					page = page + 1;
					getAuctionList();
				}
			}else if(n==2){
				if(mTrailerAdapter!=null){
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
						&& lastVisibleIndex == mTrailerAdapter.getCount() - 1) {
					x = 2;
					page = page + 1;
					getAuctionList();
				}
				}
			}else if(n==3){
				if(mTemporaryAdapter!=null){
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
						&& lastVisibleIndex == mTemporaryAdapter.getCount() - 1) {
					x = 2;
					page = page + 1;
					getAuctionList();
				}
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
				if (mAuctionList != null && mAuctionList.size() > 0) {
					mLoadLayout.setVisibility(View.GONE);
					mListView.setVisibility(View.VISIBLE);
					mAlertLayout.setVisibility(View.GONE);
					mListView.setVisibility(View.VISIBLE);
					mAuctionAdapter = new DisplayAuctionAdapter(
							getApplicationContext(), mAuctionList);
					mListView.setAdapter(mAuctionAdapter);
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
					mAuctionAdapter.notifyList(mAddList);
					maxNum = mAuctionAdapter.getCount();
					Log.i("刷新", mAuctionAdapter.getCount()+"====");
				}
				break;
			case 3:
				mAlertLayout.setVisibility(View.VISIBLE);
				mListView.setVisibility(View.GONE);
				break;
			case 4:
				if (mAuctionList != null && mAuctionList.size() > 0) {
					mLoadLayout.setVisibility(View.GONE);
					mListView.setVisibility(View.VISIBLE);
					mAlertLayout.setVisibility(View.GONE);
					mListView.setVisibility(View.VISIBLE);
					mTrailerAdapter = new DisplayAuctionTrailerAdapter(getApplicationContext(), mAuctionList);
					mListView.setAdapter(mTrailerAdapter);
					mListView.setOnItemClickListener(mItemClickListener);
					mListView.setOnScrollListener(mScrollListener);
				}else{
					mAlertLayout.setVisibility(View.VISIBLE);
					mListView.setVisibility(View.GONE);
					mLoadLayout.setVisibility(View.GONE);
				}
				break;
			case 5:
				if (mAuctionList != null && mAuctionList.size() > 0) {
					mLoadLayout.setVisibility(View.GONE);
					mListView.setVisibility(View.VISIBLE);
					mAlertLayout.setVisibility(View.GONE);
					mListView.setVisibility(View.VISIBLE);
					mTemporaryAdapter = new DisplayAuctionTemporaryAdapter(getApplicationContext(), mAuctionList);
//					mTrailerAdapter = new DisplayAuctionTrailerAdapter(getApplicationContext(), mAuctionList);
					mListView.setAdapter(mTemporaryAdapter);
					mListView.setOnItemClickListener(mItemClickListener);
					mListView.setOnScrollListener(mScrollListener);
				}else{
					mAlertLayout.setVisibility(View.VISIBLE);
					mListView.setVisibility(View.GONE);
					mLoadLayout.setVisibility(View.GONE);
				}
				break;
			case 6:
				if (mAddList != null && mAddList.size() > 0) {
					if(mTrailerAdapter!=null){
					mTrailerAdapter.notifyList(mAddList);
					}
				}
				break;
			case 7:
				if (mAddList != null && mAddList.size() > 0) {
					if(mTemporaryAdapter!=null){
					mTemporaryAdapter.notifyList(mAddList);
					}
				}
				break;
			}
		};
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_back_image:
			finish();
			break;
			//当天竞拍
		case R.id.auction_type_current:
			mTypeCode = 1;
			page = 1;
			getAuctionList();
			n = 1;
			x =1;
			mLoadLayout.setVisibility(View.VISIBLE);
			mAlertLayout.setVisibility(View.GONE);
			mListView.setVisibility(View.GONE);
			mCurrentText.setTextColor(getResources().getColor(R.color.back));
			mCurrentView.setVisibility(View.VISIBLE);
			
			mTrailerText.setTextColor(getResources().getColor(R.color.my_user_decription2));
			mTrailerView.setVisibility(View.GONE);
			mTemporaryText.setTextColor(getResources().getColor(R.color.my_user_decription2));
			mTemporaryView.setVisibility(View.GONE);
			break;
			//竞拍预告
		case R.id.auction_type_trailer_layout:
			mTypeCode = 2;
			page = 1;
			getAuctionList();
			n = 2;
			x =1;
			mLoadLayout.setVisibility(View.VISIBLE);
			mListView.setVisibility(View.GONE);
			mAlertLayout.setVisibility(View.GONE);
			mCurrentText.setTextColor(getResources().getColor(R.color.my_user_decription2));
			mCurrentView.setVisibility(View.GONE);
			
			mTrailerText.setTextColor(getResources().getColor(R.color.back));
			mTrailerView.setVisibility(View.VISIBLE);
			mTemporaryText.setTextColor(getResources().getColor(R.color.my_user_decription2));
			mTemporaryView.setVisibility(View.GONE);
			break;
			//临时竞拍
		case R.id.auction_type_temporary_layout:
			mTypeCode = 0;
			page = 1;
			getAuctionList();
			n = 3;
			x =1;
			mLoadLayout.setVisibility(View.VISIBLE);
			mListView.setVisibility(View.GONE);
			mAlertLayout.setVisibility(View.GONE);
			mCurrentText.setTextColor(getResources().getColor(R.color.my_user_decription2));
			mCurrentView.setVisibility(View.GONE);
			
			mTrailerText.setTextColor(getResources().getColor(R.color.my_user_decription2));
			mTrailerView.setVisibility(View.GONE);
			mTemporaryText.setTextColor(getResources().getColor(R.color.back));
			mTemporaryView.setVisibility(View.VISIBLE);
			break;
		}
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Intent intent = new Intent(action);
		intent.putExtra("data", "1");
		sendBroadcast(intent);
//		Log.i("返回首页", "返回首页 刷新数据");
	}
}
