package com.zjxfood.activity;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class MainDetail1Activity extends AppActivity implements OnClickListener{

	private TextView mTextView;
	private String str = "<font color='#323232'>如您在使用食尚男女个人版时，遇到使用不畅，无法更新，请关注</font><font color='red'>zjxssnn</font><font color='#323232'>公众号，根据公众号中提示，卸载老版本，重新下载最新版食尚男女。</color>";
	private ImageView mBackImage;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_help_layout);
		setImmerseLayout(findViewById(R.id.head_layout));
		init();
		mTextView.setText(Html.fromHtml(str));
	}
	
	private void init(){
		mTextView = (TextView) findViewById(R.id.main_help_content_text);
		mBackImage = (ImageView) findViewById(R.id.main_help_info_image);
		
		mBackImage.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.main_help_info_image:
			finish();
			break;

		default:
			break;
		}
	}
}
