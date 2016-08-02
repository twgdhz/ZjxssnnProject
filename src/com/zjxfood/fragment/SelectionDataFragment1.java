package com.zjxfood.fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import com.umeng.analytics.MobclickAgent;
import com.zjxfood.activity.MallActivity;
import com.zjxfood.activity.R;
import com.zjxfood.adapter.SelectionDataGridAdapter;
import com.zjxfood.reserve.SelectionDataActivity;
import com.zjxfood.view.MyGridView;

@SuppressLint("NewApi")
public class SelectionDataFragment1 extends Fragment implements OnClickListener {

	private MyGridView mGridView;
	private SelectionDataGridAdapter mGridAdapter;
	private List<String> mList;
	private int mSizePosition = 0;
	private int[] week1 = {0,5,10,15,20,25,30,35};
	private int[] week2 = {1,6,11,16,21,26,31,36};
	private int[] week3 = {2,7,12,17,22,27,32,37};
	private int[] week4 = {3,8,13,18,23,28,33,38};
	private int[] week5 = {4,9,14,19,24,29,34,39};
	
	private String mSelectWeek = "";
	private String[] str = {"11:00","11:30","12:00","17:00","17:30","18:00","18:30","19:00"};
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.selection_data_fragment_layout, null);
		initView(view);
		mList = new ArrayList<String>();
		for(int i=0;i<8;i++){
			for(int j=0;j<5;j++){
				mList.add(str[i]);
			}
		}
		mGridAdapter = new SelectionDataGridAdapter(getActivity(), mList);
		
		mGridView.setAdapter(mGridAdapter);
		mGridView.setOnItemClickListener(mItemClickListener);
		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	private void initView(View view) {
		mGridView = (MyGridView) view.findViewById(R.id.selection_data_grid_view);
	}
	public void nofityAdapter(){
		mGridAdapter.cancelSelection(mSizePosition);
		mSizePosition = 0;
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		intent.setClass(getActivity(), MallActivity.class);
		switch (v.getId()) {
		
		}
	}
	OnItemClickListener mItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			Log.i("position", position+"==============");
			mSizePosition = position;
			mGridAdapter.setSeclection(position);
			mGridAdapter.notifyDataSetChanged();
			if(Arrays.binarySearch(week1, position)>0){
				mSelectWeek = "周一";
			}else if(Arrays.binarySearch(week2, position)>0){
				mSelectWeek = "周二";
			}else if(Arrays.binarySearch(week3, position)>0){
				mSelectWeek = "周三";
			}else if(Arrays.binarySearch(week4, position)>0){
				mSelectWeek = "周四";
			}else if(Arrays.binarySearch(week5, position)>0){
				mSelectWeek = "周五";
			}
			SelectionDataActivity.selectDate = mSelectWeek+"："+mList.get(position);
			Toast.makeText(getActivity(), "当前选择项："+mSelectWeek+"："+mList.get(position), Toast.LENGTH_SHORT).show();
			Log.i("value", mList.get(position)+"===week1"+week1.toString());
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
