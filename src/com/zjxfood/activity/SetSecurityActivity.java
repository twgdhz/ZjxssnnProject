package com.zjxfood.activity;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.project.util.DensityUtils;
import com.umeng.analytics.MobclickAgent;
import com.zjxfood.adapter.RetrieveSpinnerAdapter;
import com.zjxfood.application.ExitApplication;

public class SetSecurityActivity extends AppActivity implements OnClickListener {

	private RelativeLayout mHeadLayout;
	private ImageView mBackImage;
	private RelativeLayout mProblemLayout1, mProblemLayout2, mProblemLayout3;
	private ListView mListView;
	private RetrieveSpinnerAdapter mSpinnerAdapter;
	private String[] mArrays;
	private PopupWindow mPopupWindow;
	private TextView mProblemText1,mProblemText2,mProblemText3;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.set_security_layout);
		setImmerseLayout(findViewById(R.id.head_layout));
		ExitApplication.getInstance().addActivity(this);
		ExitApplication.getInstance().addMyAccountActivity(this);
		init();
		mArrays = getResources().getStringArray(R.array.problem);
	}

	private void init() {
		mHeadLayout = (RelativeLayout) findViewById(R.id.title_set_security_id);
		mBackImage = (ImageView) mHeadLayout
				.findViewById(R.id.set_security_back_info_image);
		mProblemLayout1 = (RelativeLayout) findViewById(R.id.set_security_layout1_problem_layout);
		mProblemLayout2 = (RelativeLayout) findViewById(R.id.set_security_layout2_problem_layout);
		mProblemLayout3 = (RelativeLayout) findViewById(R.id.set_security_layout3_problem_layout);
		mProblemText1 = (TextView) findViewById(R.id.set_security_layout1_problem_text);
		mProblemText2 = (TextView) findViewById(R.id.set_security_layout2_problem_text);
		mProblemText3 = (TextView) findViewById(R.id.set_security_layout3_problem_text);

		mBackImage.setOnClickListener(this);
		mProblemLayout1.setOnClickListener(this);
		mProblemLayout2.setOnClickListener(this);
		mProblemLayout3.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
		switch (v.getId()) {
		case R.id.set_security_back_info_image:
			finish();
			break;

		case R.id.set_security_layout1_problem_layout:
			View view = inflater.inflate(R.layout.popup_retrieve_pwd_spinner_layout, null);
			mListView = (ListView) view.findViewById(R.id.popup_ret_spinner_list);
			mSpinnerAdapter = new RetrieveSpinnerAdapter(getApplicationContext(), mArrays);
			mListView.setAdapter(mSpinnerAdapter);
			mListView.setOnItemClickListener(mItemClickListener1);
			mPopupWindow = new PopupWindow(view, DensityUtils.dp2px(
					getApplicationContext(), 250), LayoutParams.WRAP_CONTENT, false);

			mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
			mPopupWindow.setOutsideTouchable(true);
			
			mPopupWindow.setFocusable(true);
			mPopupWindow.showAsDropDown(mProblemLayout1);
			break;
		case R.id.set_security_layout2_problem_layout:
			View view2 = inflater.inflate(R.layout.popup_retrieve_pwd_spinner_layout, null);
			mListView = (ListView) view2.findViewById(R.id.popup_ret_spinner_list);
			mSpinnerAdapter = new RetrieveSpinnerAdapter(getApplicationContext(), mArrays);
			mListView.setAdapter(mSpinnerAdapter);
			mListView.setOnItemClickListener(mItemClickListener2);
			mPopupWindow = new PopupWindow(view2, DensityUtils.dp2px(
					getApplicationContext(), 250), LayoutParams.WRAP_CONTENT, false);

			mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
			mPopupWindow.setOutsideTouchable(true);
			
			mPopupWindow.setFocusable(true);
			mPopupWindow.showAsDropDown(mProblemLayout2);
			break;
		case R.id.set_security_layout3_problem_layout:
			View view3 = inflater.inflate(R.layout.popup_retrieve_pwd_spinner_layout, null);
			mListView = (ListView) view3.findViewById(R.id.popup_ret_spinner_list);
			mSpinnerAdapter = new RetrieveSpinnerAdapter(getApplicationContext(), mArrays);
			mListView.setAdapter(mSpinnerAdapter);
			mListView.setOnItemClickListener(mItemClickListener3);
			mPopupWindow = new PopupWindow(view3, DensityUtils.dp2px(
					getApplicationContext(), 250), LayoutParams.WRAP_CONTENT, false);

			mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
			mPopupWindow.setOutsideTouchable(true);
			
			mPopupWindow.setFocusable(true);
			mPopupWindow.showAsDropDown(mProblemLayout3);
			break;
		}
	}
	
	OnItemClickListener mItemClickListener1 = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			mProblemText1.setText(mArrays[position]);
			mPopupWindow.dismiss();
		}
	};
	OnItemClickListener mItemClickListener2 = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			mProblemText2.setText(mArrays[position]);
			mPopupWindow.dismiss();
		}
	};
	OnItemClickListener mItemClickListener3 = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			mProblemText3.setText(mArrays[position]);
			mPopupWindow.dismiss();
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			finish();
			break;

		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
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
