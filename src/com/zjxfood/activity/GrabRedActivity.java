package com.zjxfood.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
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
import com.zjxfood.model.MathRandom;

/**
 * 抢红包
 * @author zjx
 *
 */
public class GrabRedActivity extends AppActivity implements OnClickListener {

	private RelativeLayout mHeadLayout;
	private ImageView mBackImage;
	private ImageView mStartImage;
	private int num;// 中奖号
	private PopupWindow mPopupBi;
	private LinearLayout mAlertLayout;
	private TextView mWinningInfo;
	private TextView mWinningDetail;
	private String source2 = "<font color='white'>当前拥有</font><font color='#FFA800'>1000</font><font color='white'>币，你可以兑换</color>";
	private String source80 = "<font color='white'>恭喜你已获得</font><font color='#FFA800'>80</font><font color='white'>币</color>";
	private String source10 = "<font color='white'>恭喜你已获得</font><font color='#FFA800'>10</font><font color='white'>币</color>";

	private String source20 = "<font color='white'>恭喜你已获得</font><font color='#FFA800'>20</font><font color='white'>币</color>";
	private String source50 = "<font color='white'>恭喜你已获得</font><font color='#FFA800'>50</font><font color='white'>币</color>";

	private ImageView mBiXImage;
	private PopupWindow mPopupNoWinning;
	private Button mConfirmBtn;
	private ImageView mNoWinXImage;
	private PopupWindow mPopupWindow;
	private Button mSeeRuleBtn1, mSeeRuleBtn2;
	private TextView mSurPlusShmoney;
	private String shmoney;// 现金购物卷
	private int n = 1;
	private Bundle mBundle;
	private String Id;
	private String allShmoney = "";
	private TextView mTitleText;
//	private ExecutorService mExecutorService = Executors.newCachedThreadPool();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.grab_red_packets_layout);
		setImmerseLayout(findViewById(R.id.head_layout));
		ExitApplication.getInstance().addActivity(this);
		ExitApplication.getInstance().addMyOrderActivity(this);
		mBundle = getIntent().getExtras();
		if(mBundle!=null){
			Id = mBundle.getString("Id");
		}
		init();
	}

	@SuppressLint("NewApi")
	private void init() {
		mHeadLayout = (RelativeLayout) findViewById(R.id.title_grab_red_id);
//		mBackImage = (ImageView) mHeadLayout
//				.findViewById(R.id.title_back_image);
		mBackImage = (ImageView) findViewById(R.id.title_back_image);
		mTitleText = (TextView) findViewById(R.id.title_text);
		mTitleText.setText("抽奖");
		mStartImage = (ImageView) findViewById(R.id.grab_red_zhuanpan_start_image);
		mAlertLayout = (LinearLayout) findViewById(R.id.grab_red_alert_view);

		mBackImage.setOnClickListener(this);
		mStartImage.setOnClickListener(this);
		mAlertLayout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		MathRandom a = new MathRandom();  
		switch (v.getId()) {
		case R.id.title_back_image:
			finish();
			System.gc();
			break;

		case R.id.grab_red_zhuanpan_start_image:
			if (n>0) {
				n--;
				LayoutInflater inflater = LayoutInflater
						.from(getApplicationContext());
//				Random random = new Random();
//				num = Constants.gifts[random.nextInt(54)];
				num = a.PercentageRandom();
				Log.i("tag", num + "=========================");
				switch (num) {
				case 0:

					View view1 = inflater.inflate(
							R.layout.popup_no_winning_layout, null);
					mConfirmBtn = (Button) view1
							.findViewById(R.id.grab_red_no_winning_confirm_btn);
					mNoWinXImage = (ImageView) view1
							.findViewById(R.id.pop_grab_red_no_x_image);
					mSurPlusShmoney = (TextView) view1
							.findViewById(R.id.grab_red_no_winning_shishangbi);

					mPopupNoWinning = new PopupWindow(view1,
							DensityUtils.dp2px(getApplicationContext(), 250),
							LayoutParams.WRAP_CONTENT, false);

					mPopupNoWinning.setBackgroundDrawable(new BitmapDrawable());
					// mPopupWindow.setOutsideTouchable(true);
					mPopupNoWinning.showAtLocation(mStartImage, Gravity.CENTER,
							0, DensityUtils.dp2px(getApplicationContext(), 68));
					mAlertLayout.setVisibility(View.VISIBLE);

					mConfirmBtn.setOnClickListener(mPopupNoWinningClick);
					mNoWinXImage.setOnClickListener(mPopupNoWinningClick);

					mSurPlusShmoney
							.setText("当前现金购物卷：" + Constants.mShMoney + "币");
					shmoney = 0+"";
//					new Thread(hbRun).start();
//					mExecutorService.execute(hbRun);
					addBi();
					break;

				case 1:
					View view2 = inflater.inflate(
							R.layout.popup_grab_red_layout, null);
					mWinningInfo = (TextView) view2
							.findViewById(R.id.grab_red_winning_detail);
					mWinningDetail = (TextView) view2
							.findViewById(R.id.grab_red_detail_show);
					mBiXImage = (ImageView) view2
							.findViewById(R.id.pop_grab_red_x_image);
					mSeeRuleBtn1 = (Button) view2
							.findViewById(R.id.grab_red_check_for_details_btn);

					mPopupBi = new PopupWindow(view2, DensityUtils.dp2px(
							getApplicationContext(), 250),
							LayoutParams.WRAP_CONTENT, false);

					mPopupBi.setBackgroundDrawable(new BitmapDrawable());
					// mPopupWindow.setOutsideTouchable(true);
					mPopupBi.showAtLocation(mStartImage, Gravity.CENTER, 0,
							DensityUtils.dp2px(getApplicationContext(), 68));
					mAlertLayout.setVisibility(View.VISIBLE);
					shmoney = 10 + "";
					if(!Constants.mShMoney.equals("")){
					int money10 = Integer.parseInt(shmoney)
							+ Integer.parseInt(Constants.mShMoney);
					
					Constants.mShMoney = money10+"";
					
					source2 = "<font color='white'>当前拥有</font>"
							+ "<font color='#FFA800'>" + money10 + "</font>"
							+ "<font color='white'>币，你可以兑换</color>";

					mWinningInfo.setText(Html.fromHtml(source10));
					mWinningDetail.setText(Html.fromHtml(source2));
					mBiXImage.setOnClickListener(mPopupBiClick);
					mSeeRuleBtn1.setOnClickListener(mGrabRedClick);
//					new Thread(hbRun).start();
//					mExecutorService.execute(hbRun);
					addBi();
					}
					break;
				case 2:
					View view3 = inflater.inflate(
							R.layout.popup_grab_red_layout, null);
					mWinningInfo = (TextView) view3
							.findViewById(R.id.grab_red_winning_detail);
					mWinningDetail = (TextView) view3
							.findViewById(R.id.grab_red_detail_show);
					mBiXImage = (ImageView) view3
							.findViewById(R.id.pop_grab_red_x_image);
					mSeeRuleBtn2 = (Button) view3
							.findViewById(R.id.grab_red_check_for_details_btn);
					mPopupBi = new PopupWindow(view3, DensityUtils.dp2px(
							getApplicationContext(), 250),
							LayoutParams.WRAP_CONTENT, false);
					shmoney = 20 + "";
					Log.i("shmoney", shmoney+"=======1=======");
					mPopupBi.setBackgroundDrawable(new BitmapDrawable());
					// mPopupWindow.setOutsideTouchable(true);
					mPopupBi.showAtLocation(mStartImage, Gravity.CENTER, 0,
							DensityUtils.dp2px(getApplicationContext(), 68));
					mAlertLayout.setVisibility(View.VISIBLE);
					int money20 = Integer.parseInt(shmoney)
							+ Integer.parseInt(Constants.mShMoney);
					Constants.mShMoney = money20+"";
					Log.i("shmoney", money20+"=======2======="+Integer.parseInt(shmoney)+"==="+Integer.parseInt(Constants.mShMoney));
					
					mWinningInfo.setText(Html.fromHtml(source20));
					source2 = "<font color='white'>当前拥有</font>"
							+ "<font color='#FFA800'>" + money20 + "</font>"
							+ "<font color='white'>币，你可以兑换</color>";
					mWinningDetail.setText(Html.fromHtml(source2));
					mBiXImage.setOnClickListener(mPopupBiClick);
					mSeeRuleBtn2.setOnClickListener(mGrabRedClick);
//					new Thread(hbRun).start();
//					mExecutorService.execute(hbRun);
					addBi();
					break;
				case 5:
					View view5 = inflater.inflate(
							R.layout.popup_grab_red_layout, null);
					mWinningInfo = (TextView) view5
							.findViewById(R.id.grab_red_winning_detail);
					mWinningDetail = (TextView) view5
							.findViewById(R.id.grab_red_detail_show);
					mBiXImage = (ImageView) view5
							.findViewById(R.id.pop_grab_red_x_image);
					mSeeRuleBtn2 = (Button) view5
							.findViewById(R.id.grab_red_check_for_details_btn);
					mPopupBi = new PopupWindow(view5, DensityUtils.dp2px(
							getApplicationContext(), 250),
							LayoutParams.WRAP_CONTENT, false);
					shmoney = 50 + "";
					Log.i("shmoney", shmoney+"=======1=======");
					mPopupBi.setBackgroundDrawable(new BitmapDrawable());
					// mPopupWindow.setOutsideTouchable(true);
					mPopupBi.showAtLocation(mStartImage, Gravity.CENTER, 0,
							DensityUtils.dp2px(getApplicationContext(), 68));
					mAlertLayout.setVisibility(View.VISIBLE);
					int money50 = Integer.parseInt(shmoney)
							+ Integer.parseInt(Constants.mShMoney);
					Constants.mShMoney = money50+"";
					Log.i("shmoney", money50+"=======2======="+Integer.parseInt(shmoney)+"==="+Integer.parseInt(Constants.mShMoney));
					
					mWinningInfo.setText(Html.fromHtml(source50));
					source2 = "<font color='white'>当前拥有</font>"
							+ "<font color='#FFA800'>" + money50 + "</font>"
							+ "<font color='white'>币，你可以兑换</color>";
					mWinningDetail.setText(Html.fromHtml(source2));
					mBiXImage.setOnClickListener(mPopupBiClick);
					mSeeRuleBtn2.setOnClickListener(mGrabRedClick);
//					new Thread(hbRun).start();
//					mExecutorService.execute(hbRun);
					addBi();
					break;
				case 8:
					View view8 = inflater.inflate(
							R.layout.popup_grab_red_layout, null);
					mWinningInfo = (TextView) view8
							.findViewById(R.id.grab_red_winning_detail);
					mWinningDetail = (TextView) view8
							.findViewById(R.id.grab_red_detail_show);
					mBiXImage = (ImageView) view8
							.findViewById(R.id.pop_grab_red_x_image);
					mSeeRuleBtn2 = (Button) view8
							.findViewById(R.id.grab_red_check_for_details_btn);
					mPopupBi = new PopupWindow(view8, DensityUtils.dp2px(
							getApplicationContext(), 250),
							LayoutParams.WRAP_CONTENT, false);
					shmoney = 80 + "";
					Log.i("shmoney", shmoney+"=======1=======");
					mPopupBi.setBackgroundDrawable(new BitmapDrawable());
					// mPopupWindow.setOutsideTouchable(true);
					mPopupBi.showAtLocation(mStartImage, Gravity.CENTER, 0,
							DensityUtils.dp2px(getApplicationContext(), 68));
					mAlertLayout.setVisibility(View.VISIBLE);
					int money80 = Integer.parseInt(shmoney)
							+ Integer.parseInt(Constants.mShMoney);
					Constants.mShMoney = money80+"";
					Log.i("shmoney", money80+"=======2======="+Integer.parseInt(shmoney)+"==="+Integer.parseInt(Constants.mShMoney));
					
					mWinningInfo.setText(Html.fromHtml(source80));
					source2 = "<font color='white'>当前拥有</font>"
							+ "<font color='#FFA800'>" + money80 + "</font>"
							+ "<font color='white'>币，你可以兑换</color>";
					mWinningDetail.setText(Html.fromHtml(source2));
					mBiXImage.setOnClickListener(mPopupBiClick);
					mSeeRuleBtn2.setOnClickListener(mGrabRedClick);
//					new Thread(hbRun).start();
//					mExecutorService.execute(hbRun);
					addBi();
					break;
				}
			}else{
				Toast.makeText(getApplicationContext(), "当前没有可用的抢红包次数！", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.grab_red_alert_view:
			
			break;
		}
	}

	OnClickListener mPopupBiClick = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			mPopupBi.dismiss();
			handler.sendEmptyMessageDelayed(1, 0);
		}
	};
	OnClickListener mPopupNoWinningClick = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			mPopupNoWinning.dismiss();
			handler.sendEmptyMessageDelayed(1, 0);
		}
	};
	OnClickListener mCashClick = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			mPopupWindow.dismiss();
			handler.sendEmptyMessageDelayed(1, 0);
		}
	};

	//抢红包
	OnClickListener mGrabRedClick = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), MallActivity.class);
			startActivity(intent);
			ExitApplication.getInstance().finishMyOrder();
			mPopupBi.dismiss();
			mAlertLayout.setVisibility(View.GONE);
			handler.sendEmptyMessageDelayed(1, 0);
		}
	};

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				mAlertLayout.setVisibility(View.GONE);
				break;

			case 2:
				Constants.mShMoney = allShmoney;
				break;
			}
		};
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			System.gc();
			finish();
			
			break;

		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	//抢红包成功增加现金购物卷
//	private void addBi(){
//		RequestParams params = new RequestParams();
//		params.put("userid", Constants.mId);
//		params.put("orderid", Id);
//		params.put("money", shmoney);
//		AsyUtils.get(Constants.getShmoneyByHb2, params, new AsyncHttpResponseHandler(){
//			@Override
//			@Deprecated
//			public void onSuccess(int statusCode, String content) {
//				Log.i("content", content+"=============="+statusCode);
//				super.onSuccess(statusCode, content);
//			}
//		});
//	}
	private void addBi() {
		StringBuilder sb = new StringBuilder();
			sb.append("userid=" + Constants.mId+"&orderid="+Id+"&money="+shmoney);
		XutilsUtils.get(Constants.getShmoneyByHb2, sb,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
					}
					@Override
					public void onSuccess(ResponseInfo<String> res) {
						Log.i("content", res.result+"==============");
					}
				});
	}

	@Override
	protected void onDestroy() {
		System.gc();
		super.onDestroy();
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
