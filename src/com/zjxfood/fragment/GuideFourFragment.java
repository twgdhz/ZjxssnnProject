package com.zjxfood.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import com.project.util.DensityUtils;
import com.project.util.ScreenUtils;
import com.umeng.analytics.MobclickAgent;
import com.zjxfood.activity.NewMainActivity;
import com.zjxfood.activity.R;

public class GuideFourFragment extends Fragment implements OnClickListener{

	private ImageView mImageView;
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.guide_foure_fragment_layout, null);
		mImageView = (ImageView) view.findViewById(R.id.guide_three_btn);
		LayoutParams params = mImageView.getLayoutParams();
		params.width = ScreenUtils.getScreenWidth(getActivity())/2;
		params.height = DensityUtils.dp2px(getActivity(), 40);
		mImageView.setLayoutParams(params);
		mImageView.setOnClickListener(this);
		
		return view;
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
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.guide_three_btn:
			Intent intent = new Intent();
			intent.setClass(getActivity(), NewMainActivity.class);
			startActivity(intent);
			getActivity().finish();
			break;

		default:
			break;
		}
	}
}
