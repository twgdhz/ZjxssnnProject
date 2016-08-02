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
 * 我的夺宝
 * @author zjx
 *
 */
public class MyIndianaListActivity extends AppActivity implements OnClickListener{

	private ImageView mBackImage;
	private ListView mListView;
	private MyIndianaListAdapter mIndianaListAdapter;
	private ArrayList<HashMap<String, Object>> mList,mAddList;
	private int page=1;
	private int code=-1;
	private HashMap<String, Object> mMap;
	private int lastVisibleIndex;
	private int x = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_indiana_list_layout);
		setImmerseLayout(findViewById(R.id.head_layout));
		init();
		if(Constants.mId!=null){
		getList();
		}
	}
	
	private void init(){
		mBackImage = (ImageView) findViewById(R.id.my_indiana_list_back_info_image);
		mListView = (ListView) findViewById(R.id.my_indiana_list);
		
		mBackImage.setOnClickListener(this);
	}

	private void getList() {
		StringBuilder sb = new StringBuilder();
		sb.append("userId=" + Constants.mId + "&showTypeCode=" + code+"&pageSize=5"+"&currentPage="+page);
		XutilsUtils.get(Constants.getMyIndiana, sb, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				
			}
			@Override
			public void onSuccess(ResponseInfo<String> res) {
				mMap = Constants.getJsonObjectByData(res.result);
				if (mMap != null && mMap.size() > 0 && mMap.get("rows")!=null) {
					
					if(x==1){
						mList = Constants.getJsonArray(mMap.get("rows").toString());
						if(mList!=null && mList.size()>0){
							handler.sendEmptyMessageDelayed(1, 0);
						}
						
					}else if(x==2){
						mAddList = Constants.getJsonArray(mMap.get("rows").toString());
						if(mAddList!=null && mAddList.size()>0){
							handler.sendEmptyMessageDelayed(2, 0);
						}
					}
					
				}
			}
		});
	}
	
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				mIndianaListAdapter = new MyIndianaListAdapter(getApplicationContext(),mList);
				mListView.setAdapter(mIndianaListAdapter);
				mListView.setOnItemClickListener(mItemClickListener);
				mListView.setOnScrollListener(mScrollListener);
				break;

			case 2:
				mIndianaListAdapter.notifyList(mAddList);
				break;
			}
		};
	};
	
	OnScrollListener mScrollListener = new OnScrollListener() {
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
					&& lastVisibleIndex == mIndianaListAdapter.getCount() - 1) {
				x = 2;
				page = page + 1;
				getList();
			}
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			// 计算最后可见条目的索引
			lastVisibleIndex = firstVisibleItem + visibleItemCount - 1;

		}
	};
	
	OnItemClickListener mItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), MyIndianaInfoActivity.class);
			if(mList!=null && mList.size()>0){
			Bundle bundle = new Bundle();
			if(mList.get(position).get("Id")!=null){
			bundle.putString("id", mList.get(position).get("Id").toString());
			}
			if(mList.get(position).get("ProductName")!=null){
				bundle.putString("ProductName", mList.get(position).get("ProductName").toString());
				}
			if(mList.get(position).get("Price")!=null){
				bundle.putString("Price", mList.get(position).get("Price").toString());
				}
			if(mList.get(position).get("ImgUrl")!=null){
				bundle.putString("ImgUrl", mList.get(position).get("ImgUrl").toString());
				}
			if(mList.get(position).get("StartTime")!=null){
				bundle.putString("StartTime", mList.get(position).get("StartTime").toString());
				}
			if(mList.get(position).get("EndTime")!=null){
				bundle.putString("EndTime", mList.get(position).get("EndTime").toString());
				}
			if(mList.get(position).get("LuckyNum")!=null){
				bundle.putString("LuckyNum", mList.get(position).get("LuckyNum").toString());
				}
			intent.putExtras(bundle);
			}
			startActivity(intent);
		}
	};
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.my_indiana_list_back_info_image:
			finish();
			break;

		default:
			break;
		}
	}
}
