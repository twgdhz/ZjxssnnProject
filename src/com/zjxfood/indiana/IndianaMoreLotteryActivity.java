package com.zjxfood.indiana;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.zjxfood.activity.AppActivity;
import com.zjxfood.activity.R;
import com.zjxfood.common.Constants;
import com.zjxfood.http.XutilsUtils;

import java.util.ArrayList;
import java.util.HashMap;

public class IndianaMoreLotteryActivity extends AppActivity implements
		OnClickListener {

	private ImageView mBackImage;
	private ListView mListView;
	private Bundle mBundle;
	private String mId;
	private int n = 1;
	private HashMap<String, Object> mLotterMap;
	private ArrayList<HashMap<String, Object>> mLotterList, mAddList;
	private int page = 1;
	private int lastVisibleIndex;
	private MyIndianaInfoAdapter mIndianaInfoAdapter;
	private String mLuckyNum;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.indiana_info_more_lottery_layout);
		setImmerseLayout(findViewById(R.id.head_layout));
		init();
		mBundle = getIntent().getExtras();
		if (mBundle != null) {
			mId = mBundle.getString("id");
			mLuckyNum = mBundle.getString("luckyNum");
			getAllLotteryNumber();
		}
	}

	private void init() {
		mBackImage = (ImageView) findViewById(R.id.indiana_more_lottery_back_info_image);
		mListView = (ListView) findViewById(R.id.indiana_info_more_lottery_list);
		
		mBackImage.setOnClickListener(this);
	}

	// 获取所有抽奖号
	private void getAllLotteryNumber() {
		StringBuilder sb = new StringBuilder();
		sb.append("gameId=" + mId + "&userId=0" + "&pageSize=10"
				+ "&currentPage=" + page);
		XutilsUtils.get(Constants.getJoinList, sb,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
					}
					@Override
					public void onSuccess(ResponseInfo<String> res) {
						mLotterMap = Constants.getJsonObjectByData(res.result);
						Log.i("抽奖号码", res.result + "==============");
						if (mLotterMap != null && mLotterMap.size() > 0
								&& mLotterMap.get("rows") != null) {
							if (n == 1) {
								mLotterList = Constants.getJsonArray(mLotterMap
										.get("rows").toString());
								handler.sendEmptyMessageDelayed(1, 0);
							} else if (n == 2) {
								mAddList = Constants.getJsonArray(mLotterMap
										.get("rows").toString());
								handler.sendEmptyMessageDelayed(2, 0);
							}
						}
					}
				});
	}

	OnScrollListener mScrollListener = new OnScrollListener() {
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
					&& lastVisibleIndex == mIndianaInfoAdapter.getCount() - 1) {
				n = 2;
				page = page + 1;
				getAllLotteryNumber();
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
				mIndianaInfoAdapter = new MyIndianaInfoAdapter(getApplicationContext(), mLotterList,mLuckyNum);
				mListView.setAdapter(mIndianaInfoAdapter);
				mListView.setOnScrollListener(mScrollListener);
				break;

			case 2:
				mIndianaInfoAdapter.notifyList(mAddList);
				break;
			}
		};
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.indiana_more_lottery_back_info_image:
			finish();
			break;

		default:
			break;
		}
	}

}
