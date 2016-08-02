package com.zjxfood.indiana;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
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

/**
 * 中奖纪录
 * @author zjx
 *
 */
public class IndianaWinningRecordActivity extends AppActivity implements OnClickListener{

	private ImageView mBackImage;
	private ListView mListView;
	private IndianaWinRecordAdapter mRecordAdapter;
	private HashMap<String, Object> mIndianaMap;
	private int page = 1;
	private ArrayList<HashMap<String, Object>> mIndianaList;
	private int x = 1;
	private int lastVisibleIndex;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.indiana_win_record_layout);
		setImmerseLayout(findViewById(R.id.head_layout));
		init();
		getIndianaList();
	}
	
	private void init(){
		mBackImage = (ImageView) findViewById(R.id.win_record_back_info_image);
		mListView = (ListView) findViewById(R.id.indiana_win_record_list);
		
		mBackImage.setOnClickListener(this);
	}

	private void getIndianaList() {
		StringBuilder sb = new StringBuilder();
			sb.append("productName=" +"&showTypeCode=0"+"&pageSize=5"+"&currentPage="+page);
		XutilsUtils.get(Constants.getIndianaList, sb,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
					}
					@Override
					public void onSuccess(ResponseInfo<String> res) {
						mIndianaMap = Constants.getJsonObjectByData(res.result);
						if(null!=mIndianaMap && null!=mIndianaMap.get("rows")){
							mIndianaList = Constants.getJsonArray(mIndianaMap.get("rows").toString());
						}
						if(null!=mIndianaList && mIndianaList.size()>0){
							if(x==1){
							handler.sendEmptyMessageDelayed(1, 0);
							}else if(x==2){
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
						&& lastVisibleIndex == mRecordAdapter.getCount() - 1) {
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
	
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				mRecordAdapter = new IndianaWinRecordAdapter(getApplicationContext(),mIndianaList);
				mListView.setAdapter(mRecordAdapter);
				mListView.setOnScrollListener(mScrollListener);
				break;

			case 2:
				mRecordAdapter.notifyList(mIndianaList);
				break;
			}
		};
	};
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.win_record_back_info_image:
			finish();
			break;

		default:
			break;
		}
	}
}
