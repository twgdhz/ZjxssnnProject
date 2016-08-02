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
public class FirstFragment extends Fragment implements OnClickListener {

	private LinearLayout mZhongcanLayout,mXicanLayout,mRhllLayout,mZzLayout;
	private LinearLayout mHuoguoLayout,mJbkfLayout,mLytdLayout,mXcLayout;
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.first_fragment_layout, null);
		initView(view);

		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	private void initView(View view) {
		mZhongcanLayout = (LinearLayout) view.findViewById(R.id.zhongcan_layout);
		mXicanLayout = (LinearLayout) view.findViewById(R.id.xican_layout);
		mRhllLayout = (LinearLayout) view.findViewById(R.id.rhll_layout);
		mZzLayout = (LinearLayout) view.findViewById(R.id.zizhu_layout);
		mHuoguoLayout = (LinearLayout) view.findViewById(R.id.huoguo_layout);
		mJbkfLayout = (LinearLayout) view.findViewById(R.id.jbkf_layout);
		mLytdLayout = (LinearLayout) view.findViewById(R.id.lytd_layout);
		mXcLayout = (LinearLayout) view.findViewById(R.id.xc_layout);
		
		mZhongcanLayout.setOnClickListener(this);
		mXicanLayout.setOnClickListener(this);
		mRhllLayout.setOnClickListener(this);
		mZzLayout.setOnClickListener(this);
		mHuoguoLayout.setOnClickListener(this);
		mJbkfLayout.setOnClickListener(this);
		mLytdLayout.setOnClickListener(this);
		mXcLayout.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		Bundle bundle;
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.zhongcan_layout:
//			if(MainActivity.isClick){
//			intent.setClass(getActivity(), CommodityListActivity.class);
//			bundle = new Bundle();
//			bundle.putString("type", "中餐");
//			bundle.putString("id", "1");
//			Log.i("MainActivity.mCityCode", MainActivity.mCityCode+"===============");
//			bundle.putString("cityId", MainActivity.mCityCode);
//			intent.putExtras(bundle);
//			startActivity(intent);
//			}
			break;
		case R.id.xican_layout:
//			if(MainActivity.isClick){
//			intent.setClass(getActivity(), CommodityListActivity.class);
//			bundle = new Bundle();
//			bundle.putString("type", "西餐");
//			bundle.putString("id", "2");
//			bundle.putString("cityId", MainActivity.mCityCode);
//			intent.putExtras(bundle);
//			startActivity(intent);
//			}
			break;
		case R.id.rhll_layout:
//			if(MainActivity.isClick){
//			intent.setClass(getActivity(), CommodityListActivity.class);
//			bundle = new Bundle();
//			bundle.putString("type", "日韩料理");
//			bundle.putString("id", "3");
//			bundle.putString("cityId", MainActivity.mCityCode);
//			intent.putExtras(bundle);
//			startActivity(intent);
//			}
			break;
		case R.id.zizhu_layout:
//			if(MainActivity.isClick){
//			intent.setClass(getActivity(), CommodityListActivity.class);
//			bundle = new Bundle();
//			bundle.putString("type", "自助");
//			bundle.putString("id", "4");
//			bundle.putString("cityId", MainActivity.mCityCode);
//			intent.putExtras(bundle);
//			startActivity(intent);
//			}
			break;
		case R.id.huoguo_layout:
//			if(MainActivity.isClick){
//			intent.setClass(getActivity(), CommodityListActivity.class);
//			bundle = new Bundle();
//			bundle.putString("type", "火锅");
//			bundle.putString("id", "5");
//			bundle.putString("cityId", MainActivity.mCityCode);
//			intent.putExtras(bundle);
//			startActivity(intent);
//			}
			break;
		case R.id.jbkf_layout:
//			if(MainActivity.isClick){
//			intent.setClass(getActivity(), CommodityListActivity.class);
//			bundle = new Bundle();
//			bundle.putString("type", "酒吧咖啡");
//			bundle.putString("id", "6");
//			bundle.putString("cityId", MainActivity.mCityCode);
//			intent.putExtras(bundle);
//			startActivity(intent);
//			}
			break;
		case R.id.lytd_layout:
//			if(MainActivity.isClick){
//			intent.setClass(getActivity(), CommodityListActivity.class);
//			bundle = new Bundle();
//			bundle.putString("type", "冷饮甜点");
//			bundle.putString("id", "7");
//			bundle.putString("cityId", MainActivity.mCityCode);
//			intent.putExtras(bundle);
//			startActivity(intent);
//			}
			break;
		case R.id.xc_layout:
//			if(MainActivity.isClick){
//			intent.setClass(getActivity(), CommodityListActivity.class);
//			bundle = new Bundle();
//			bundle.putString("type", "小吃");
//			bundle.putString("id", "8");
//			bundle.putString("cityId", MainActivity.mCityCode);
//			intent.putExtras(bundle);
//			startActivity(intent);
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
