package com.zjxfood.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.umeng.analytics.MobclickAgent;
import com.zjxfood.adapter.MemberListAdapter;
import com.zjxfood.common.Constants;
import com.zjxfood.http.XutilsUtils;

import java.util.ArrayList;
import java.util.HashMap;

public class MyMemberActivity extends AppActivity implements OnClickListener {

	private ListView mListView;
	private MemberListAdapter mListAdapter;
	private ImageView mBackImage;
	private TextView mMyUser;// 我的会员
	private TextView mTourist;// 我的游客
	private ArrayList<HashMap<String, Object>> mList;
	private int x = 1;
	// private RunTask mRunTask;
	private TextView mNowUser, mNowUserNumbers, mDescription,mShText;
//	private Bundle mBundle;
	private String users;
	// 最后可见条目的索引
	private int lastVisibleIndex;
	// private int size = 10;
	private int flag = 1;
	private int page = 1;
	private ArrayList<HashMap<String, Object>> mAddList;
	private String isJh = "true";
	private RelativeLayout mYoukeLayout;
	private ImageView mSearchImage;
	private String mTitle = "true";
	private HashMap<String,Object> mUsersMap1,mUsersMap2,mTouristRewMap;
	private TextView mAlertText;
	private TextView mTouristRewText;
	private String mIsMerchant = "false";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_users_layout);
		setImmerseLayout(findViewById(R.id.head_layout));
		init();

		getLists();
	}

	private void init() {
		mShText = (TextView) findViewById(R.id.my_sh_text);
		mListView = (ListView) findViewById(R.id.my_users_list);
		mBackImage = (ImageView) findViewById(R.id.my_users_back_info_image);
		mMyUser = (TextView) findViewById(R.id.my_user_text);
		mTourist = (TextView) findViewById(R.id.my_tourist_text);
		mNowUser = (TextView) findViewById(R.id.my_users_text);
		mNowUserNumbers = (TextView) findViewById(R.id.my_users_number_text);
		mDescription = (TextView) findViewById(R.id.my_users_description_text);
		mYoukeLayout = (RelativeLayout) findViewById(R.id.jiangli_show_layout);
		mSearchImage = (ImageView) findViewById(R.id.my_users_search_id);
		mAlertText = (TextView) findViewById(R.id.users_alert_text);
		mTouristRewText = (TextView) findViewById(R.id.tourist_jl);

		mShText.setOnClickListener(this);
		mBackImage.setOnClickListener(this);
		mMyUser.setOnClickListener(this);
		mTourist.setOnClickListener(this);
		mSearchImage.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
			//商户
			case R.id.my_sh_text:
				mSearchImage.setVisibility(View.GONE);
				mIsMerchant = "true";
				mDescription.setVisibility(View.GONE);
				mYoukeLayout.setVisibility(View.GONE);
				mShText.setTextColor(getResources().getColor(
						R.color.main_color16));
				mTourist.setTextColor(getResources().getColor(
						R.color.main_color3));
				mMyUser.setTextColor(getResources().getColor(
						R.color.main_color3));
				x = 4;
				getLists();
				page = 1;
				break;
			//查询
			case R.id.my_users_search_id:
			intent.setClass(getApplicationContext(),SearchUserActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("title",mTitle);
				intent.putExtras(bundle);
				startActivity(intent);
				break;
		case R.id.my_users_back_info_image:
			finish();
			break;
		// 我的会员
		case R.id.my_user_text:
			mTitle = "true";
			mIsMerchant = "false";
			mSearchImage.setVisibility(View.VISIBLE);
			mDescription.setVisibility(View.VISIBLE);
			mYoukeLayout.setVisibility(View.GONE);
			SharedPreferences sp = getApplicationContext()
			.getSharedPreferences("我的会员", MODE_PRIVATE);
			
			page = 1;
			flag = 1;
			isJh = "true";

			if(sp!=null && sp.getString("Content", "")!=null && !sp.getString("Content", "").toString().equals("")){
				mDescription.setText(sp.getString("Content", ""));
			}else{
				mDescription.setText(getResources().getString(
				R.string.my_users_description));
			}
			
			mMyUser.setTextColor(getResources().getColor(
					R.color.main_color16));
			mTourist.setTextColor(getResources().getColor(
					R.color.main_color3));
			mShText.setTextColor(getResources().getColor(
					R.color.main_color3));
			x = 1;
			getLists();
			break;
		// 我的游客
		case R.id.my_tourist_text:
			mTitle = "false";
			mIsMerchant = "false";
			mSearchImage.setVisibility(View.VISIBLE);
			mDescription.setVisibility(View.VISIBLE);
			mYoukeLayout.setVisibility(View.VISIBLE);
			sp = getApplicationContext()
			.getSharedPreferences("我的游客", MODE_PRIVATE);
			page = 1;
			flag = 2;
			isJh = "false";
			if(sp!=null && sp.getString("Content", "")!=null && !sp.getString("Content", "").toString().equals("")){
				mDescription.setText(sp.getString("Content", ""));
			}else{
				mDescription.setText(getResources().getString(
				R.string.my_users_description2));
			}
			mMyUser.setTextColor(getResources().getColor(
					R.color.main_color3));
			mTourist.setTextColor(getResources().getColor(
					R.color.main_color16));
			mShText.setTextColor(getResources().getColor(
					R.color.main_color3));
			x = 2;
			getLists();
			getTouristRew();
			break;
		}
	}
	private void getLists() {
		StringBuilder sb = new StringBuilder();
		sb.append("userId="+ Constants.mId + "&isJh="+isJh+"&page="+page+"&pagesize=10"+"&returnDeep="+0+"&isMerchant="+mIsMerchant);
		XutilsUtils.get(Constants.getMyUsersubordinate, sb,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
					}
					@Override
					public void onSuccess(ResponseInfo<String> res) {
						Log.i("会员列表",res.result+"========================");
						mUsersMap1 = Constants.getJsonObject(res.result);
						if(mUsersMap1!=null && mUsersMap1.get("Code")!=null){
							if(mUsersMap1.get("Code").toString().equals("200") && mUsersMap1.get("Data")!=null){
								mUsersMap2 = Constants.getJsonObject(mUsersMap1.get("Data").toString());
								if(mUsersMap2!=null && mUsersMap2.get("rows")!=null) {
									if (x == 1) {
										mList = Constants.getJsonArray(mUsersMap2.get("rows").toString());
										Log.i("mList",mList+"========================");
										handler.sendEmptyMessageDelayed(1, 0);
									} else if (x == 2) {
										mList = Constants.getJsonArray(mUsersMap2.get("rows").toString());
										handler.sendEmptyMessageDelayed(2, 0);
									} else if (x == 3) {
										mAddList = Constants.getJsonArray(mUsersMap2.get("rows").toString());
										handler.sendEmptyMessageDelayed(3, 0);
									}else if (x == 4) {
										mList = Constants.getJsonArray(mUsersMap2.get("rows").toString());
										handler.sendEmptyMessageDelayed(5, 0);
									}
								}
							}else{
								if(mUsersMap1.get("Message")!=null){
									Toast.makeText(getApplicationContext(),mUsersMap1.get("Message").toString(),Toast.LENGTH_SHORT).show();
								}
							}
						}

					}
				});
	}
	private void getTouristRew() {
		StringBuilder sb = new StringBuilder();
		sb.append("userId="+ Constants.mId);
		XutilsUtils.get(Constants.getMyTouristReward, sb,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
					}
					@Override
					public void onSuccess(ResponseInfo<String> res) {
						Log.i("getTouristRew",res.result+"===========游客旗下奖励=============");
						mTouristRewMap = Constants.getJsonObject(res.result);
						if(mTouristRewMap!=null && mTouristRewMap.get("Code")!=null){
							if(mTouristRewMap.get("Code").toString().equals("200")){
								handler.sendEmptyMessageDelayed(4,0);
							}else{
								if(mTouristRewMap.get("Message")!=null){
									Toast.makeText(getApplicationContext(),mTouristRewMap.get("Message").toString(),Toast.LENGTH_SHORT).show();
								}
							}
						}
					}
				});
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				mNowUser.setText("当前会员：");
				if(mUsersMap2.get("total")!=null){
				mNowUserNumbers.setText(mUsersMap2.get("total") + "个");
				}
				if (mList != null && mList.size() > 0) {
					mListAdapter = new MemberListAdapter(
							getApplicationContext(), mList, x);
					mListView.setAdapter(mListAdapter);
					mListView.setOnScrollListener(mScrollListener);
					mAlertText.setVisibility(View.GONE);
					mListView.setVisibility(View.VISIBLE);
				}else{
					mAlertText.setVisibility(View.VISIBLE);
					mListView.setVisibility(View.GONE);
				}
				break;
			case 2:
				mNowUser.setText("当前游客：");
				if(mUsersMap2.get("total")!=null){
				mNowUserNumbers.setText(mUsersMap2.get("total") + "个");
				}
				if (mList != null && mList.size() > 0) {
					mListAdapter = new MemberListAdapter(
							getApplicationContext(), mList, x);
					mListView.setAdapter(mListAdapter);
					mListView.setOnScrollListener(mScrollListener);
					mAlertText.setVisibility(View.GONE);
					mListView.setVisibility(View.VISIBLE);
				}else{
					mAlertText.setVisibility(View.VISIBLE);
					mListView.setVisibility(View.GONE);
				}
				break;
			case 3:
				if (mAddList != null && mAddList.size() > 0) {
					mListAdapter.notify(mAddList);
				}
				break;
				case 4:
					if(mTouristRewMap.get("Data")!=null) {
						mTouristRewText.setText(mTouristRewMap.get("Data").toString());
					}
					break;
				case 5:
					mNowUser.setText("当前商户：");
					if(mUsersMap2.get("total")!=null){
						mNowUserNumbers.setText(mUsersMap2.get("total") + "个");
					}
					if (mList != null && mList.size() > 0) {
						mListAdapter = new MemberListAdapter(
								getApplicationContext(), mList, x);
						mListView.setAdapter(mListAdapter);
						mListView.setOnScrollListener(mScrollListener);
						mAlertText.setVisibility(View.GONE);
						mListView.setVisibility(View.VISIBLE);
					}else{
						mAlertText.setVisibility(View.VISIBLE);
						mListView.setVisibility(View.GONE);
					}
					break;
			}
		};
	};

	OnScrollListener mScrollListener = new OnScrollListener() {
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
					&& lastVisibleIndex == mListAdapter.getCount() - 1) {
				x = 3;
				page = page + 1;
				switch (flag) {
				case 1:
					isJh = "true";
//					getNumbers();
					getLists();
					break;
				case 2:
					isJh = "false";
//					getNumbers();
					getLists();
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

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
}
