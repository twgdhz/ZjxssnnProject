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
import com.zjxfood.activity.MallActivity;
import com.zjxfood.activity.R;

@SuppressLint("NewApi")
public class ProMallFragment extends Fragment implements OnClickListener {

	private LinearLayout mFuzhuangLayout, mXiemaoLayout, mShumaLayout,
			mJiadianLayout;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.pro_mall_fragment_layout, null);
		initView(view);
		//
		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	private void initView(View view) {
		mFuzhuangLayout = (LinearLayout) view
				.findViewById(R.id.mall_content_fuzhuang_image);
		mXiemaoLayout = (LinearLayout) view
				.findViewById(R.id.mall_content_computer_accessories_image);
		mShumaLayout = (LinearLayout) view
				.findViewById(R.id.mall_content_electronics_image_layout);
		mJiadianLayout = (LinearLayout) view
				.findViewById(R.id.mall_content_large_home_image);


		mFuzhuangLayout.setOnClickListener(this);
		mXiemaoLayout.setOnClickListener(this);
		mShumaLayout.setOnClickListener(this);
		mJiadianLayout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Bundle bundle;
		Intent intent = new Intent();
		intent.setClass(getActivity(), MallActivity.class);
		switch (v.getId()) {
		// 服装
		case R.id.mall_content_fuzhuang_image:
			bundle = new Bundle();
			bundle.putString("group", "A");
			bundle.putString("titleName", "服装分类");
			intent.putExtras(bundle);
			startActivity(intent);
			break;
		// 鞋帽
		case R.id.mall_content_computer_accessories_image:
			bundle = new Bundle();
			bundle.putString("group", "B");
			bundle.putString("titleName", "鞋帽分类");
			intent.putExtras(bundle);
			startActivity(intent);
			break;
		// 个护化妆
		case R.id.mall_content_electronics_image_layout:
			bundle = new Bundle();
			bundle.putString("group", "I");
			bundle.putString("titleName", "个护化妆分类");
			intent.putExtras(bundle);
			startActivity(intent);
			break;
		// 家电
		case R.id.mall_content_large_home_image:
			bundle = new Bundle();
			bundle.putString("group", "D");
			bundle.putString("titleName", "家电分类");
			intent.putExtras(bundle);
			startActivity(intent);
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
