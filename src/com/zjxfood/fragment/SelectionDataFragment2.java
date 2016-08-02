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
import com.zjxfood.adapter.SelectionDataGridAdapter2;
import com.zjxfood.reserve.SelectionDataActivity;
import com.zjxfood.view.MyGridView;

@SuppressLint("NewApi")
public class SelectionDataFragment2 extends Fragment implements OnClickListener {

	private MyGridView mGridView;
	private SelectionDataGridAdapter2 mGridAdapter;
	private List<String> mList;
	private String[] str = {"11:00","11:30","12:00","17:00","17:30","18:00","18:30","19:00"};
	private int mSizePosition = 0;
	private int[] week6 = {0,5,10,15,20,25,30,35};
	private int[] week7 = {1,6,11,16,21,26,31,36};
	private String mSelectWeek = "";
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.selection_data_fragment_layout2, null);
		initView(view);
		mList = new ArrayList<String>();
		for(int i=0;i<8;i++){
			for(int j=0;j<5;j++){
				mList.add(str[i]);
			}
		}
		mGridAdapter = new SelectionDataGridAdapter2(getActivity(), mList);
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
		mGridView = (MyGridView) view.findViewById(R.id.selection_data_grid_view2);
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		intent.setClass(getActivity(), MallActivity.class);
		switch (v.getId()) {
		
		}
	}
	
	public void nofityAdapter(){
		mGridAdapter.cancelSelection(mSizePosition);
		mSizePosition = 0;
	}
	OnItemClickListener mItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			if(Arrays.binarySearch(week6, position)>0){
				mSelectWeek = "周六";
			}else if(Arrays.binarySearch(week7, position)>0){
				mSelectWeek = "周末";
			}
			mSizePosition = position;
			mGridAdapter.setSeclection(position);
			mGridAdapter.notifyDataSetChanged();
			if(position==0 || position==1 || position==5 || position==6 ||position==10 || position==11 ||position==15 || position==16 ||position==20 || position==21 ||position==25 || position==26||position==30 || position==31||position==35 || position==36){
				Toast.makeText(getActivity(), "当前选择项："+mSelectWeek+"："+mList.get(position), Toast.LENGTH_SHORT).show();
				Log.i("value", mList.get(position));
				SelectionDataActivity.selectDate = mSelectWeek+"："+mList.get(position);
			}
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
