package com.zjxfood.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.zjxfood.common.Constants;
import com.zjxfood.http.XutilsUtils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 竞拍记录
 * @author zjx
 *
 */
public class AuctionRecordActivity extends AppActivity implements OnClickListener {

	private ListView mListView;
	private AuctionRecordAdapter mRecordAdapter;
	private Bundle mBundle;
	private int page = 1;
	private String mId = "";
	private ArrayList<HashMap<String, Object>> mRecoedList,mAddList;
	private HashMap<String, Object> mRecoedMap;
	private int lastVisibleIndex;
	private int x = 1;
	private ImageView mBackImage;//返回按钮
	private TextView mTitleText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.auction_record_layout);
		setImmerseLayout(findViewById(R.id.head_layout));
		init();
		mBundle = getIntent().getExtras();
		if(mBundle!=null){
			mId = mBundle.getString("id");
			getRecords();
		}
	}

	private void init() {
		mListView = (ListView) findViewById(R.id.auction_record_list);
//		mBackImage = (ImageView) findViewById(R.id.auction_record_back_info_image);
		mBackImage = (ImageView) findViewById(R.id.title_back_image);
		mTitleText = (TextView) findViewById(R.id.title_text);
		mTitleText.setText("出价记录");
		
		mBackImage.setOnClickListener(this);
	}

	private void getRecords() {
		StringBuilder sb = new StringBuilder();
		sb.append("auctionId="+mId).append("&currentPage="+page).append("&pageSize=10");
		XutilsUtils.get(Constants.getAuctionRecord, sb,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
					}
					@Override
					public void onSuccess(ResponseInfo<String> res) {
//						Log.i("出价记录", res.result+"=============");
						try {
							mRecoedMap = Constants.getJsonObjectByData(res.result);
							
							if(mRecoedMap!=null && mRecoedMap.size()>0 && mRecoedMap.get("rows")!=null){
								if(x==1){
								mRecoedList = Constants.getJsonArray(mRecoedMap.get("rows").toString());
								if (mRecoedList != null && mRecoedList.size() > 0) {
									handler.sendEmptyMessageDelayed(1, 0);
								}
								}else if(x==2){
									mAddList = Constants.getJsonArray(mRecoedMap.get("rows").toString());
									if (mAddList != null && mAddList.size() > 0) {
										handler.sendEmptyMessageDelayed(2, 0);
									}
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						
					}
				});
	}
	
	OnScrollListener mScrollListener = new OnScrollListener() {
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
						&& lastVisibleIndex == mRecordAdapter.getCount() - 1) {
					x = 2;
					page = page + 1;
					getRecords();
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
		case R.id.title_back_image:
			finish();
			break;

		default:
			break;
		}
	}
	
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				mRecordAdapter = new AuctionRecordAdapter(getApplicationContext(), mRecoedList);
				mListView.setAdapter(mRecordAdapter);
				mListView.setOnScrollListener(mScrollListener);
				break;

			case 2:
//				mRecoedList.addAll(mAddList);
				mRecordAdapter.notifyList(mAddList);
				break;
			}
		};
	};
}
