package com.zjxfood.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.project.util.DensityUtils;
import com.umeng.analytics.MobclickAgent;
import com.zjxfood.application.ExitApplication;
import com.zjxfood.common.Constants;
import com.zjxfood.http.XutilsUtils;

import java.util.ArrayList;
import java.util.HashMap;

public class ServiceProviderActivity extends AppActivity implements OnClickListener{

	private ImageView mBackImage;
	private Bundle mBundle;
	private TextView mMerchantName;
	private TextView mMerchantContent;
	private TextView mMerchantPhone;
	private TextView mMerchantAddress;
//	private int x = 1;
	private ArrayList<HashMap<String, Object>> mArrayList;
//	private RunTask mRunTask;
	private LinearLayout mPhoneImage;
	private String phone = "";
	private LinearLayout mDaohangLayout;
	private String address = "";
	private ImageView mTitleImage;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.service_provider_layout);
		setImmerseLayout(findViewById(R.id.head_layout));
		ExitApplication.getInstance().addActivity(this);
		init();
		mBundle = getIntent().getExtras();
		if(mBundle!=null){
			address = mBundle.getString("userMerchantAddress");
			phone = mBundle.getString("userMerchantPhone");
			mMerchantName.setText(mBundle.getString("userMerchantName"));
			mMerchantContent.setText(mBundle.getString("userMerchantContent"));
			mMerchantPhone.setText("电话："+mBundle.getString("userMerchantPhone"));
			mMerchantAddress.setText("地址："+mBundle.getString("userMerchantAddress"));
		}else{
//			mRunTask = new RunTask();
//			mRunTask.execute("");
			getListHttp();
		}
	}
	
	private void init(){
		mBackImage = (ImageView) findViewById(R.id.service_provider_back_image);
		mMerchantName = (TextView) findViewById(R.id.service_provider_info_text);
		mMerchantContent = (TextView) findViewById(R.id.service_provider_content_text);
		mMerchantPhone = (TextView) findViewById(R.id.service_provider_phone_text);
		mMerchantAddress = (TextView) findViewById(R.id.service_provider_address_text);
		mPhoneImage = (LinearLayout) findViewById(R.id.service_provider_phone_layout);
		mDaohangLayout = (LinearLayout) findViewById(R.id.service_provider_address_layout);
		mTitleImage = (ImageView) findViewById(R.id.service_provider_title_image);
		
		LayoutParams params = mTitleImage.getLayoutParams();
		params.height = DensityUtils.dp2px(getApplicationContext(), 140);
		mTitleImage.setLayoutParams(params);
		mBackImage.setOnClickListener(this);
		mPhoneImage.setOnClickListener(this);
		mDaohangLayout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.service_provider_back_image:
			finish();
			break;

		case R.id.service_provider_phone_layout:
			try{
			if(!(phone.equals(""))){
			intent.setAction("android.intent.action.CALL");
			intent.setData(Uri.parse("tel:" + phone));
			startActivity(intent);
			}else{
				Toast.makeText(getApplicationContext(), "暂时没有联系电话！", Toast.LENGTH_SHORT).show();
			}
			}catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case R.id.service_provider_address_layout:
//			if(!(address.equals(""))){
//			intent.setClass(getApplicationContext(), RouteActivity.class);
//			Bundle bundle = new Bundle();
//			bundle.putString("address", address);
//			intent.putExtras(bundle);
//			startActivity(intent);
//			}else{
//				Toast.makeText(getApplicationContext(), "暂时没有地址！", Toast.LENGTH_SHORT).show();
//			}
			break;
		}
	}
	
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				mMerchantName.setText(mArrayList.get(0).get("merchantname").toString());
				if(mArrayList.get(0).get("introduction")!=null){
				mMerchantContent.setText(mArrayList.get(0).get("introduction").toString());
				}
				if(mArrayList.get(0).get("phone")!=null){
					phone = mArrayList.get(0).get("phone").toString();
				mMerchantPhone.setText("电话："+mArrayList.get(0).get("phone").toString());
				}
				if(mArrayList.get(0).get("address")!=null){
					address = mArrayList.get(0).get("address").toString();
				mMerchantAddress.setText("地址："+mArrayList.get(0).get("address").toString());
				}
				break;

			case 2:
				finish();
				Toast.makeText(getApplicationContext(), "暂时没有服务商！", Toast.LENGTH_SHORT).show();
				break;
			}
		};
	};
	
//	class RunTask extends AsyncTask<String, Integer, String>{
//		@Override
//		protected String doInBackground(String... str) {
//			switch (x) {
//			case 1:
//				try {
//					String str2 = "uid="+Constants.mId;
////					String json = ReadJson.readParse(Constants.getUserMerchant+"uid="+Constants.mId);
//					String json = ReadJson.getJson(Constants.getUserMerchant, str2);
//					mArrayList = Constants.getJsonArray(json);
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				if(mArrayList!=null && mArrayList.size()>0){
//					handler.sendEmptyMessageDelayed(1, 0);
//				}else{
//					handler.sendEmptyMessageDelayed(2, 0);
//				}
//				break;
//			default:
//				break;
//			}
//			return null;
//		}
//	}
	
//	private void getListHttp(){
//		RequestParams params = new RequestParams();
//		params.put("uid", Constants.mId);
//		AsyUtils.get(Constants.getUserMerchant2, params, new AsyncHttpResponseHandler(){
//			@Override
//			@Deprecated
//			public void onSuccess(int statusCode, String content) {
//				mArrayList = Constants.getJsonArray(content);
//				if(mArrayList!=null && mArrayList.size()>0){
//					handler.sendEmptyMessageDelayed(1, 0);
//				}else{
//					handler.sendEmptyMessageDelayed(2, 0);
//				}
//				super.onSuccess(statusCode, content);
//			}
//			@Override
//			@Deprecated
//			public void onFailure(Throwable error, String content) {
//				handler.sendEmptyMessageDelayed(2, 0);
//				super.onFailure(error, content);
//			}
//		});
//	}
	private void getListHttp() {
		StringBuilder sb = new StringBuilder();
			sb.append("uid=" + Constants.mId);
		XutilsUtils.get(Constants.getUserMerchant2, sb,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						handler.sendEmptyMessageDelayed(2, 0);
					}
					@Override
					public void onSuccess(ResponseInfo<String> res) {
						mArrayList = Constants.getJsonArray(res.result);
						if(mArrayList!=null && mArrayList.size()>0){
							handler.sendEmptyMessageDelayed(1, 0);
						}else{
							handler.sendEmptyMessageDelayed(2, 0);
						}
					}
				});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
}
