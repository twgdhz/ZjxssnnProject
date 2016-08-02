package com.zjxfood.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.project.util.DensityUtils;
import com.umeng.analytics.MobclickAgent;
import com.zjxfood.common.Constants;
import com.zjxfood.http.XutilsUtils;

import java.util.HashMap;

/*
 * 现金购物卷充值
 */
public class BiExchangeActivity extends AppActivity implements OnClickListener {

	private EditText mExchangeEdit;// 要兑换的食尚币数量
	private Button mExchangeBtn;
	private PopupWindow mPopupWindow;// 弹窗
	private TextView coldText;//获得的金币数量
	private LinearLayout mAlertLayout;
	private ImageView mXImage;
	private Button mBiBtn;
	private ImageView mBackImage;
	private TextView mBiText;
	private HashMap<String,Object> mMaps;
	private TextView mTitleText;
	private TextView mTitle;
	private TextView message;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.restaurant_bi_exchange_layout);
		setImmerseLayout(findViewById(R.id.head_layout));
		init();
	}

	private void init() {
		mTitleText = (TextView) findViewById(R.id.title_text);
		mExchangeBtn = (Button) findViewById(R.id.bi_exchange_btn);
		mBackImage = (ImageView) findViewById(R.id.title_back_image);
		mExchangeEdit = (EditText) findViewById(R.id.bi_exchange_edit);
		coldText = (TextView) findViewById(R.id.cold_text);
		mAlertLayout = (LinearLayout) findViewById(R.id.bi_exchange_alert_view);
		mExchangeEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
				try{
					int u = Integer.parseInt(mExchangeEdit.getText().toString());
					double avg = (double)u/10;
					String uu = String.valueOf(avg);
					coldText.setText(uu);
				}catch(Exception e){
					return;
				}
			}
			@Override
			public void afterTextChanged(Editable editable) {
				try{
					int u = Integer.parseInt(mExchangeEdit.getText().toString());
					double avg = (double)u/10;
					String uu = String.valueOf(avg);
					coldText.setText(uu);
				}catch(Exception e){
					return;
				}
			}
		});
		mTitleText.setText("金币兑换");
		mExchangeBtn.setOnClickListener(this);
		mBackImage.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bi_exchange_btn:
			if (mExchangeEdit.getText().toString().equals("")){
				Toast.makeText(this,"请输入要兑换的食尚币!",Toast.LENGTH_SHORT).show();
			}else {
				getBalance();
			}
			break;
		case R.id.title_back_image:
			finish();
			break;
		}
	}

	OnClickListener mClickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			mPopupWindow.dismiss();
			mAlertLayout.setVisibility(View.GONE);
		}
	};
	// 跳转到 我的金币 界面
	OnClickListener mBtnClick = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			mPopupWindow.dismiss();
			mAlertLayout.setVisibility(View.GONE);
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(),
					MyGoldActivity.class);
			startActivity(intent);
			finish();
		}
	};
	//充值
	private void getBalance() {
		StringBuilder sb = new StringBuilder();
		sb.append("name=" + "Shmoney2Gold" + "&userId=" + Constants.mId + "&amount=" + mExchangeEdit.getText());
		XutilsUtils.get(Constants.Exchange, sb,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
					}
					@Override
					public void onSuccess(ResponseInfo<String> res) {
						Log.i("兑换", res.result + "================");
						mMaps = Constants.getJsonObject(res.result);
						if(mMaps!=null && mMaps.size()>0){
							if(mMaps.get("Message")!=null){
								handler.sendEmptyMessageDelayed(1, 0);
//								handler.sendEmptyMessageDelayed(5, 0);
							}
						}
					}
				});
	}
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				mAlertLayout.setVisibility(View.VISIBLE);
				LayoutInflater inflater = LayoutInflater
						.from(getApplicationContext());
				View view = inflater.inflate(
						R.layout.popup_bi_chongzhi_success, null);
				mXImage = (ImageView) view
						.findViewById(R.id.popup_bi_chongzhi_x_image);
				mBiBtn = (Button) view
						.findViewById(R.id.balance_bi_chongzhi_btn);
				mBiText = (TextView) view
						.findViewById(R.id.popup_balance_pay_money_text);
				mTitle = (TextView)view.findViewById(R.id.popup_balance_pay_success_text);
				message = (TextView)view.findViewById(R.id.popup_balance_pay_success_money_text);
				ImageView img= (ImageView) view
					.findViewById(R.id.popup_binding_bank_success_image);
				mPopupWindow = new PopupWindow(view, DensityUtils.dp2px(
						getApplicationContext(), 250),
						LayoutParams.WRAP_CONTENT, false);
				mPopupWindow.showAtLocation(mExchangeBtn, Gravity.CENTER, 0, 0);
				mPopupWindow.setOutsideTouchable(true);
				mPopupWindow.setFocusable(false);
				mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
				mXImage.setOnClickListener(mClickListener);
				mBiBtn.setOnClickListener(mBtnClick);
				if (mMaps.get("Code") != null){
					if (mMaps.get("Code").toString().equals("200")){
						if (mMaps.get("Message") != null){
							mTitle.setText("金币兑换");
							message.setText("恭喜你!"+mMaps.get("Message").toString());
						}
						int u = Integer.parseInt(mExchangeEdit.getText().toString());
						double avg = (double)u/10;
						String uu = String.valueOf(avg);
						mBiText.setText("获得"+ uu + "金币");
						img.setBackgroundResource(R.drawable.success);
					}else if (mMaps.get("Code").toString().equals("201")){
						if (mMaps.get("Message") != null){
							mTitle.setText("金币兑换");
							message.setText(mMaps.get("Message").toString());
						}
						mBiText.setText("请核对您的账户余额后重试");
						img.setBackgroundResource(R.drawable.appkefu_error);
					}
				}
				break;
			}
		};
	};

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		System.gc();
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
