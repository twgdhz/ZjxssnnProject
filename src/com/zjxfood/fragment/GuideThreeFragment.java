package com.zjxfood.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.umeng.analytics.MobclickAgent;
import com.zjxfood.activity.NewMainActivity;
import com.zjxfood.activity.R;

public class GuideThreeFragment extends Fragment implements OnClickListener{

	private ImageView mImageView;
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.guide_three_fragment_layout, null);
		init(view);
		return view;
	}
	
	private void init(View view){
		mImageView = (ImageView) view.findViewById(R.id.guide_three_image);
		
		mImageView.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.guide_three_image:
			Intent intent = new Intent();
			intent.setClass(getActivity(), NewMainActivity.class);
			startActivity(intent);
			getActivity().finish();
			break;

		default:
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
