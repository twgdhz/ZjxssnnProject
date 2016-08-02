package com.zjxfood.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.umeng.analytics.MobclickAgent;
import com.zjxfood.activity.R;

@SuppressLint("NewApi")
public class SecondFragment extends Fragment implements OnClickListener {

	private LinearLayout mYxLayout,mHpLayout,mGgLayout,mZtglayout;
	private LinearLayout mSklayout,mMslayout,mHxlayout,mQtlayout;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.second_fragment_layout, null);
		init(view);
		return view;
	}

	//
	private void init(View view) {
		mYxLayout = (LinearLayout) view.findViewById(R.id.yanxi_layout);
		mHpLayout = (LinearLayout) view.findViewById(R.id.hp_layout);
		mGgLayout = (LinearLayout) view.findViewById(R.id.ganguo_layout);
		mZtglayout = (LinearLayout) view.findViewById(R.id.ztg_layout);
		mSklayout = (LinearLayout) view.findViewById(R.id.shaokao_layout);
		mMslayout = (LinearLayout) view.findViewById(R.id.mianshi_layout);
		mHxlayout = (LinearLayout) view.findViewById(R.id.hx_layout);
		mQtlayout = (LinearLayout) view.findViewById(R.id.qt_layout);
		
		mYxLayout.setOnClickListener(this);
		mHpLayout.setOnClickListener(this);
		mGgLayout.setOnClickListener(this);
		mZtglayout.setOnClickListener(this);
		mSklayout.setOnClickListener(this);
		mMslayout.setOnClickListener(this);
		mHxlayout.setOnClickListener(this);
		mQtlayout.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		Bundle bundle;
		switch (v.getId()) {
		case R.id.ztg_layout:
//			if (MainActivity.isClick) {
//				intent.setClass(getActivity(), CommodityListActivity.class);
//				bundle = new Bundle();
//				bundle.putString("type", "酒店");
//				bundle.putString("id", "12");
//				bundle.putString("cityId", MainActivity.mCityCode);
//				intent.putExtras(bundle);
//				startActivity(intent);
//			}
			break;

		case R.id.hp_layout:
//			if (MainActivity.isClick) {
//				intent.setClass(getActivity(), CommodityListActivity.class);
//				bundle = new Bundle();
//				bundle.putString("type", "丽人");
//				bundle.putString("id", "10");
//				bundle.putString("cityId", MainActivity.mCityCode);
//				intent.putExtras(bundle);
//				startActivity(intent);
//			}
			break;
		case R.id.mianshi_layout:
//			if (MainActivity.isClick) {
//				intent.setClass(getActivity(), CommodityListActivity.class);
//				bundle = new Bundle();
//				bundle.putString("type", "浴足");
//				bundle.putString("id", "14");
//				bundle.putString("cityId", MainActivity.mCityCode);
//				intent.putExtras(bundle);
//				startActivity(intent);
//			}
			break;
		case R.id.shaokao_layout:
//			if (MainActivity.isClick) {
//				intent.setClass(getActivity(), CommodityListActivity.class);
//				bundle = new Bundle();
//				bundle.putString("type", "健身");
//				bundle.putString("id", "13");
//				bundle.putString("cityId", MainActivity.mCityCode);
//				intent.putExtras(bundle);
//				startActivity(intent);
//			}
			break;
		case R.id.hx_layout:
//			if (MainActivity.isClick) {
//				intent.setClass(getActivity(), CommodityListActivity.class);
//				bundle = new Bundle();
//				bundle.putString("type", "海鲜");
//				bundle.putString("id", "15");
//				bundle.putString("cityId", MainActivity.mCityCode);
//				intent.putExtras(bundle);
//				startActivity(intent);
//			}
			break;
		case R.id.yanxi_layout:
//			if (MainActivity.isClick) {
//				intent.setClass(getActivity(), CommodityListActivity.class);
//				bundle = new Bundle();
//				bundle.putString("type", "Ktv");
//				bundle.putString("id", "9");
//				bundle.putString("cityId", MainActivity.mCityCode);
//				intent.putExtras(bundle);
//				startActivity(intent);
//			}
			break;
		case R.id.ganguo_layout:
//			if (MainActivity.isClick) {
//				intent.setClass(getActivity(), CommodityListActivity.class);
//				bundle = new Bundle();
//				bundle.putString("type", "干锅烧烤");
//				bundle.putString("id", "11");
//				bundle.putString("cityId", MainActivity.mCityCode);
//				intent.putExtras(bundle);
//				startActivity(intent);
//			}
			break;
		case R.id.qt_layout:
//			if (MainActivity.isClick) {
//				intent.setClass(getActivity(), CommodityListActivity.class);
//				bundle = new Bundle();
//				bundle.putString("type", "其他");
//				bundle.putString("id", "16");
//				bundle.putString("cityId", MainActivity.mCityCode);
//				intent.putExtras(bundle);
//				startActivity(intent);
//			}
			break;
		}
	}
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("MainScreen"); //统计页面
	}
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("MainScreen");
	}
}
