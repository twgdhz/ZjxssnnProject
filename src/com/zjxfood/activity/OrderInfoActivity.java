package com.zjxfood.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.zjxfood.common.Constants;
import com.zjxfood.view.RoundImageView;

public class OrderInfoActivity extends AppActivity implements OnClickListener{
	
	private RelativeLayout mHeadLayout;
	private ImageView mBackImage;
	private Button mEvaluationBtn;
	private RoundImageView mRoundImageView;
	private TextView mMerchantName;
	private TextView mPrice;
	private TextView mFlnumText;
	private TextView mAllPrice;
	private TextView mOrderDate;
	private Bundle mBundle;
	private String merchantName,price,date,imagePath,flnum,payid,flag;
	private Bitmap mBitmap;
	private ImageTask mTask;
	private TextView mOrderId;
	private ImageView mCancelX;
	private Button mGrabRedBtn;
	private PopupWindow mPopupWindow;
	private LinearLayout mAlertLayout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_info_layout);
		setImmerseLayout(findViewById(R.id.head_layout));
		init();
		mBundle = getIntent().getExtras();
		if(mBundle!=null){
			merchantName = mBundle.getString("Merchantname");
			price = mBundle.getString("Money");
			date = mBundle.getString("Createtime");
			imagePath = mBundle.getString("Logoimage");
			flnum = mBundle.getString("Flnum");
			payid = mBundle.getString("Payid");
			flag = mBundle.getString("flag");
			
		}
		mTask = new ImageTask();
		mTask.execute(imagePath);
		
		mMerchantName.setText(merchantName);
		mPrice.setText(price);
		mOrderId.setText(payid);
		mAllPrice.setText("￥"+price);
		java.text.DecimalFormat df = new java.text.DecimalFormat("#.##");
		float rebate = Float.parseFloat(price) * (Float.parseFloat(flnum) / 100);
		mFlnumText.setText("￥"+df.format(rebate));
		mOrderDate.setText(date);
		new Thread(runnable).start();
		
	}
	
	private void init(){
		mHeadLayout = (RelativeLayout) findViewById(R.id.title_order_info_id);
		mBackImage = (ImageView) mHeadLayout.findViewById(R.id.order_info_back_image);
		mEvaluationBtn = (Button) findViewById(R.id.order_info_comment_btn);
		mRoundImageView = (RoundImageView) findViewById(R.id.order_round_image_view);
		mMerchantName = (TextView) findViewById(R.id.order_info_name_text);
		mPrice = (TextView) findViewById(R.id.order_info_all_price_number);
		mFlnumText = (TextView) findViewById(R.id.order_info_detailed_rebate_number);
		mAllPrice = (TextView) findViewById(R.id.order_info_detailed_all_price_number);
		mOrderDate = (TextView) findViewById(R.id.order_info_detailed_date_number);
		mOrderId = (TextView) findViewById(R.id.order_info_code_number_text);
		mAlertLayout = (LinearLayout) findViewById(R.id.order_info_alert_view);
		
		mBackImage.setOnClickListener(this);
		mEvaluationBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.order_info_back_image:
			finish();
			break;

		case R.id.order_info_comment_btn:
			intent.setClass(getApplicationContext(), OrderEvaluationInfoActivity.class);
			startActivity(intent);
			break;
		}
	}
	
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
	class ImageTask extends AsyncTask<String, Integer, String>{

		@Override
		protected String doInBackground(String... arg0) {
//			mBitmap = Constants.getBitmap(arg0[0]);
			handler.sendEmptyMessageDelayed(1, 0);
			return null;
		}
	}
	
	Runnable runnable = new Runnable() {
		
		@Override
		public void run() {
			handler.sendEmptyMessageDelayed(2, 1000);
		}
	};
	
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				mRoundImageView.setImageBitmap(mBitmap);
				break;

			case 2:
				if(flag.equals("2")){
//					LayoutInflater inflater =
//							 LayoutInflater.from(OrderInfoActivity.this);
//							 View view = inflater.inflate(R.layout.popup_alipay_pay_success,
//							 null);
//							 mCancelX = (ImageView)
//							 view.findViewById(R.id.popup_alipay_x_image);
//							 mGrabRedBtn = (Button)
//							 view.findViewById(R.id.alipay_pay_grab_red_packets_btn);
//							
//							 mPopupWindow = new PopupWindow(view, DensityUtils.dp2px(
//							 getApplicationContext(), 250), LayoutParams.WRAP_CONTENT, false);
//							
//							 mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
//							 // mPopupWindow.setOutsideTouchable(true);
//							 mPopupWindow.showAtLocation(mBackImage, Gravity.CENTER, 0,
//							 DensityUtils.dp2px(getApplicationContext(), 68));
//							 mAlertLayout.setVisibility(View.VISIBLE);
//							
//							 mCancelX.setOnClickListener(mClickListener);
//							 mGrabRedBtn.setOnClickListener(mGrabRedClick);
				}
				break;
			case 3:
				mAlertLayout.setVisibility(View.GONE);
				break;
			}
		};
	};
	OnClickListener mClickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			mPopupWindow.dismiss();
			handler.sendEmptyMessageDelayed(3, 0);
		}
	};
	// 抢红包
		android.view.View.OnClickListener mGrabRedClick = new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), GrabRedActivity.class);
				Constants.grabNum++;
				startActivity(intent);
				mPopupWindow.dismiss();
				handler.sendEmptyMessageDelayed(3, 0);
			}
		};
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
