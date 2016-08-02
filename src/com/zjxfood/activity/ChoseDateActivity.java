package com.zjxfood.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

public class ChoseDateActivity extends AppActivity {
//	private GridView mGridView;
//	private ArrayList<HashMap<String, String>> srcTable;
//	private SimpleAdapter saTable;// 适配器
private ImageView mBackImage;//返回按钮
	private TextView mTitleText;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reserve_order_chose_date_layout);
		setImmerseLayout(findViewById(R.id.head_layout));
		init();
	}

	private void init() {
//		mGridView = (GridView) findViewById(R.id.chose_date_gridview_layout);
		mBackImage = (ImageView) findViewById(R.id.title_back_image);
		mTitleText = (TextView) findViewById(R.id.title_text);
		mTitleText.setText("选择时间");
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
