package com.zjxfood.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.umeng.analytics.MobclickAgent;
import com.zjxfood.activity.MallActivity;
import com.zjxfood.activity.R;
import com.zjxfood.adapter.IconAdapter;

import java.util.ArrayList;
import java.util.HashMap;

@SuppressLint("NewApi")
public class MallFragment1 extends Fragment{

	private GridView mGridView;
	private IconAdapter mAdapter;
	private ArrayList<HashMap<String,Object>> mList;
	private String mType,mShowCode,mProportion,mTypeName;


	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.mall_fragment_layout1, null);
		initView(view);
		mAdapter = new IconAdapter(getActivity(),mList);
		mGridView.setAdapter(mAdapter);
		mGridView.setOnItemClickListener(onItemClickListener2);
		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	@SuppressLint("ValidFragment")
	public MallFragment1(ArrayList<HashMap<String,Object>> list,String type,String showCode,String proportion,String typeName){
		this.mList = list;
		this.mType = type;
		this.mShowCode = showCode;
		this.mProportion = proportion;
		this.mTypeName = typeName;
	}
	@SuppressLint("ValidFragment")
	public MallFragment1(){
	}
	private void initView(View view) {
		mGridView = (GridView) view.findViewById(R.id.mall_ssb_grid1);

	}

	AdapterView.OnItemClickListener onItemClickListener2 = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
			Bundle bundle;
			Intent intent = new Intent();
			intent.setClass(getActivity(), MallActivity.class);
			bundle = new Bundle();
			if(mList.get(i).get("code")!=null) {

				bundle.putString("code", mList.get(i).get("code").toString());
			}
			bundle.putString("group", "1");
			if(mList.get(i).get("name")!=null) {
				bundle.putString("titleName", mList.get(i).get("name").toString());
			}
			bundle.putString("type",mType);
			bundle.putString("shouCode",mShowCode);
			bundle.putString("proportion",mProportion);
			bundle.putString("typeName",mTypeName);
			if (mList.get(i).get("Id") != null) {
				bundle.putString("pid", mList.get(i).get("Id").toString());
			}
			intent.putExtras(bundle);
			startActivity(intent);
		}
	};


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
