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
import com.zjxfood.adapter.AllEvaluationListAdapter;
import com.zjxfood.common.Constants;
import com.zjxfood.http.XutilsUtils;
import com.zjxfood.photoview.activity.ViewPagerActivity;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 所有评论
 * 
 * @author zjx
 * 
 */
public class AllEvaluationActiviy extends AppActivity implements OnClickListener {

	private ListView mListView;
	private AllEvaluationListAdapter mAllEvaluationListAdapter;
	private RelativeLayout mHeadLayout;
//	private ImageView mBackImage;
	private ArrayList<HashMap<String, Object>> mList;
//	private RunTask mRunTask;
	private Bundle mBundle;
	private String mId;
	private int x = 1;
	// 最后可见条目的索引
	private int lastVisibleIndex;
	private TextView mEvalShow;
	private int page = 1;
	private ArrayList<HashMap<String, Object>> mAddList;
	private ArrayList<String> mPathList;
	private ImageView mBackImage;//返回按钮
	private TextView mTitleText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.all_evaluation_layout);
		setImmerseLayout(findViewById(R.id.head_layout));
		init();
		mBundle = getIntent().getExtras();
		if (mBundle != null) {
			mId = mBundle.getString("Id");
		}
		httpRun();
	}

	private void init() {
		mListView = (ListView) findViewById(R.id.all_evaluation_list);
		mHeadLayout = (RelativeLayout) findViewById(R.id.title_all_evaluation_id);
//		mBackImage = (ImageView) mHeadLayout
//				.findViewById(R.id.title_back_image);
		mEvalShow = (TextView) findViewById(R.id.all_evaluation_not_show);
		mBackImage = (ImageView) findViewById(R.id.title_back_image);
		mTitleText = (TextView) findViewById(R.id.title_text);
		mTitleText.setText("全部评价");

		mBackImage.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_back_image:
			System.gc();
			finish();
			break;

		default:
			break;
		}
	}
	private void httpRun() {
		StringBuilder sb = new StringBuilder();
			sb.append("mid=" + mId+"&page="+page+"&pagesize=5");
		XutilsUtils.get(Constants.getEvaluationById2, sb,
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
							handler.sendEmptyMessageDelayed(2, 0);
						}
					}
				});
	}

	OnScrollListener mScrollListener = new OnScrollListener() {

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
					&& lastVisibleIndex == mAllEvaluationListAdapter.getCount()-1) {
				x = 2;
				page = page+1;
				httpRun();
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
				if (mList.size() > 0) {
					mEvalShow.setVisibility(View.GONE);
					mAllEvaluationListAdapter = new AllEvaluationListAdapter(
							getApplicationContext(), mList);
					mListView.setAdapter(mAllEvaluationListAdapter);
					mListView.setOnScrollListener(mScrollListener);
					mListView.setOnItemClickListener(mItemClickListener);
				} else {
					mEvalShow.setVisibility(View.VISIBLE);
				}
				break;
			case 2:
				mList.addAll(mAddList);
				Log.i("notify", "=========notify==========");
				mAllEvaluationListAdapter.notify(mList);
				break;
			}
		};
	};

	OnItemClickListener mItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			mPathList = new ArrayList<String>();
			if(mList.get(position).get("images1")!=null && !(mList.get(position).get("images1").toString().equals(""))){
				mPathList.add(mList.get(position).get("images1").toString());
			}
			if(mList.get(position).get("images2")!=null && !(mList.get(position).get("images2").toString().equals(""))){
				mPathList.add(mList.get(position).get("images2").toString());
			}
			if(mList.get(position).get("images3")!=null && !(mList.get(position).get("images3").toString().equals(""))){
				mPathList.add(mList.get(position).get("images3").toString());
			}
			if(mPathList!=null && mPathList.size()>0){
				Intent intent = new Intent();
				intent.setClass(AllEvaluationActiviy.this,
						ViewPagerActivity.class);
				Bundle bundle = new Bundle();
				bundle.putStringArrayList("list", mPathList);
				bundle.putInt("position", position);
				intent.putExtras(bundle);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
		}
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
		// TODO Auto-generated method stub
		super.onDestroy();
		System.gc();
	}
	@Override
	protected void onResume() {
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
