package com.zjxfood.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.project.util.ScreenUtils;
import com.umeng.analytics.MobclickAgent;
import com.zjxfood.adapter.PopMallThreeTypeAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class MallListTypeActivity extends AppActivity implements OnClickListener{
	private RelativeLayout mHeadLayout;
	private ImageView mBackImage;
	private GridView mTypeGrid;
	private PopMallThreeTypeAdapter mThreeTypeAdapter;
	private PopupWindow mOnePopup;
	private ArrayList<HashMap<String, Object>> mThreeList;
	private Bundle mBundle;
	private ListView mListView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mall_list_popup_chose_layout);
		setImmerseLayout(findViewById(R.id.head_layout));
		init();
		mBundle = getIntent().getExtras();
		if(mBundle!=null){
			mThreeList = (ArrayList<HashMap<String, Object>>) mBundle.get("list");
		}
		mThreeTypeAdapter = new PopMallThreeTypeAdapter(
				getApplicationContext(), mThreeList);
		mListView.setAdapter(mThreeTypeAdapter);
		LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
		View headView = inflater.inflate(
				R.layout.mall_list_one_head_layout, null);
		mListView.addHeaderView(headView);
		mListView.setOnItemClickListener(mClickListener);
		handler.sendEmptyMessageDelayed(1, 0);
	}
	
	private void init(){
		mHeadLayout = (RelativeLayout) findViewById(R.id.title_mall_list_chose);
		mBackImage = (ImageView) findViewById(R.id.mall_list_popup_back_info_image);
		mListView = (ListView) findViewById(R.id.mall_list_popup_chose_list);
		
		android.view.ViewGroup.LayoutParams params = mListView.getLayoutParams();
		params.width = ScreenUtils.getScreenWidth(getApplicationContext())/3;
		mListView.setLayoutParams(params);
		mBackImage.setOnClickListener(this);
		
	}
	
	private void initPopup(){
		LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
		View view = inflater.inflate(
				R.layout.mall_list_chose_value_layout, null);
		View headView = inflater.inflate(
				R.layout.mall_list_one_head_layout, null);
		mTypeGrid = (GridView) view
				.findViewById(R.id.mall_chose_value_grid);

		mThreeTypeAdapter = new PopMallThreeTypeAdapter(
				getApplicationContext(), mThreeList);
		mTypeGrid.setAdapter(mThreeTypeAdapter);
		mOnePopup = new PopupWindow(
				view,
				(ScreenUtils.getScreenWidth(getApplicationContext()) / 3)*2,
				LayoutParams.MATCH_PARENT, false);
		mOnePopup.setAnimationStyle(R.style.AnimAlphaInToOut);
//		mOnePopup.showAsDropDown(mHeadLayout);
		mOnePopup.showAsDropDown(mHeadLayout, ScreenUtils.getScreenWidth(getApplicationContext()) / 3, 0);
		// mTypePopup.showAsDropDown(mTypeLayout, 0,
		// -(mTypeLayout.getHeight()));
//		mTypeOneListView.setOnItemClickListener(mTypeItemClick);
//		mTypeOneListView.addHeaderView(headView);
	
	}

	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				initPopup();
				break;

			default:
				break;
			}
		};
	};
	
	OnItemClickListener mClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			initPopup();
		}
	};
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.mall_list_popup_back_info_image:
			finish();
			break;

		default:
			break;
		}
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
