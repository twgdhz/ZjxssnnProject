package com.zjxfood.activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Xml;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.project.util.DensityUtils;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;
import com.zjxfood.activity.wxapi.WXPayEntryActivity;
import com.zjxfood.application.ExitApplication;
import com.zjxfood.common.Constants;
import com.zjxfood.http.ReadJson;
import com.zjxfood.view.RoundImageView;
import com.zjxfood.view.TiltTextView;
import com.zjxfood.weixinpay.MD5;
import com.zjxfood.weixinpay.Util;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 微信支付
 * @author zjx
 *
 */
public class JHWeixinPayActivity extends AppActivity implements OnClickListener {
	
	//支付
	private Button mPayBtn;
	private RelativeLayout mHeadLayout;
	//返回
	private ImageView mBackImage;
	private PopupWindow mPopupWindow;
	private LinearLayout mAlertLayout;
	//取消
	private ImageView mCancelX;
	//抢红包
	private Button mGrabRedBtn;
	private static final String TAG = "MicroMsg.SDKSample.PayActivity";
	private PayReq req;
	private final IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);
	private Map<String, String> resultunifiedorder;
	private StringBuffer sb;
	private Bundle mBundle;
	//价格、名称、id
	private String price,merchantName,mId;
	private TextView mPriceText;
	//图片路径
	private String mLoginImagePath;
	private RoundImageView mRoundImageView;
	private Bitmap mBitmap;
	//返利
	private String mFlum;
	private TiltTextView mFlumText;
	private TextView mMerchantName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ExitApplication.getInstance().addActivity(this);
		setContentView(R.layout.weixin_pay_layout);
		setImmerseLayout(findViewById(R.id.head_layout));
		mBundle = getIntent().getExtras();
		if(mBundle!=null){
			price = mBundle.getString("price");
			merchantName = mBundle.getString("merchantName");
			mId = mBundle.getString("mId");
			mLoginImagePath = mBundle.getString("LoginImage");
			mFlum = mBundle.getString("flum");
		}
		init();
		mPriceText.setText("￥"+price);
		mFlumText.setText("返利"+mFlum+"%");
		mMerchantName.setText(merchantName);
		req = new PayReq();
		sb = new StringBuffer();

		msgApi.registerApp(Constants.APP_ID);
		generpreId();
		
		IntentFilter filter = new IntentFilter(WXPayEntryActivity.action);
		registerReceiver(mBroadcastReceiver, filter);
		new Thread(imageRun).start();
	}

	private void init() {
		mPayBtn = (Button) findViewById(R.id.weixin_liji_pay_btn);
		mHeadLayout = (RelativeLayout) findViewById(R.id.title_weixin_pay_id);
		mBackImage = (ImageView) mHeadLayout
				.findViewById(R.id.pay_weixin_back_image);
		mAlertLayout = (LinearLayout) findViewById(R.id.weixin_pay_alert_view);
		mPriceText = (TextView) findViewById(R.id.weixin_pay_money_text);
		mRoundImageView = (RoundImageView) findViewById(R.id.round_image_view);
		mFlumText = (TiltTextView) findViewById(R.id.weixin_pay_rebate_text);
		mMerchantName = (TextView) findViewById(R.id.weixin_pay_name_text);

		mPayBtn.setOnClickListener(this);
		mBackImage.setOnClickListener(this);
	}

	// 生成prepay_id
	private void generpreId() {
		GetPrepayIdTask getPrepayId = new GetPrepayIdTask();
		getPrepayId.execute();
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.weixin_liji_pay_btn:
			
			genPayReq();

			break;
		case R.id.pay_weixin_back_image:
			finish();
			break;
		}
	}

	Runnable payRun = new Runnable() {

		@Override
		public void run() {
			String str = "userid=" + Constants.mId
					+ "&mid=" + mId + "&money="+price
					+ "&payid=" + CheckOutActivity.orderId + "&issuccess=" + 1
					+ "&zffs=weixin";
			try {
				String res = ReadJson.getJson(Constants.getPay, str);
				Log.i("weixinpay", res + "=========res==============");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};

	BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent intent) {
			Log.i("weixinpay", intent.getExtras().getString("data")
					+ "=============z结果查询============");
			new Thread(payRun).start();
			LayoutInflater inflater = LayoutInflater
					.from(getApplicationContext());
			View view = inflater.inflate(R.layout.popup_weixin_pay_success,
					null);
			mCancelX = (ImageView) view
					.findViewById(R.id.popup_weixin_pay_x_image);
			mGrabRedBtn = (Button) view
					.findViewById(R.id.weixin_pay_grab_red_packets_btn);

			mPopupWindow = new PopupWindow(view, DensityUtils.dp2px(
					getApplicationContext(), 250), LayoutParams.WRAP_CONTENT,
					false);
			mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
			// mPopupWindow.setOutsideTouchable(true);
			mPopupWindow.showAtLocation(mPayBtn, Gravity.CENTER, 0,
					DensityUtils.dp2px(getApplicationContext(), 68));
			mAlertLayout.setVisibility(View.VISIBLE);

			mCancelX.setOnClickListener(mClickListener);
			mGrabRedBtn.setOnClickListener(mGrabRedClick);
		}
	};

	// 抢红包
	android.view.View.OnClickListener mGrabRedClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), GrabRedActivity.class);
			startActivity(intent);
			mPopupWindow.dismiss();
			handler.sendEmptyMessageDelayed(1, 0);
		}
	};

	OnClickListener mClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			mPopupWindow.dismiss();
			handler.sendEmptyMessageDelayed(1, 0);
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

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				mAlertLayout.setVisibility(View.GONE);
				break;

			case 2:
				// 支付
				sendPayReq();
				break;
			case 3:
				mRoundImageView.setImageBitmap(mBitmap);
				break;
			}
		};
	};

	/**
	 * 生成签名
	 */

	private String genPackageSign(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < params.size(); i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append("key=");
		sb.append(Constants.API_KEY);

		String packageSign = MD5.getMessageDigest(sb.toString().getBytes())
				.toUpperCase();
		Log.e("orion", packageSign);
		return packageSign;
	}

	private String genAppSign(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < params.size(); i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append("key=");
		sb.append(Constants.API_KEY);

		this.sb.append("sign str\n" + sb.toString() + "\n\n");
		String appSign = MD5.getMessageDigest(sb.toString().getBytes())
				.toUpperCase();
		Log.e("orion", appSign);
		return appSign;
	}

	private String toXml(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();
		sb.append("<xml>");
		for (int i = 0; i < params.size(); i++) {
			sb.append("<" + params.get(i).getName() + ">");

			sb.append(params.get(i).getValue());
			sb.append("</" + params.get(i).getName() + ">");
		}
		sb.append("</xml>");

		Log.e("orion", sb.toString());
		return sb.toString();
	}

	private class GetPrepayIdTask extends
			AsyncTask<Void, Void, Map<String, String>> {
		private ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(JHWeixinPayActivity.this,
					getString(R.string.app_tip),
					getString(R.string.getting_prepayid));
		}

		@Override
		protected void onPostExecute(Map<String, String> result) {
			if (dialog != null) {
				dialog.dismiss();
			}
			sb.append("prepay_id\n" + result.get("prepay_id") + "\n\n");
			// show.setText(sb.toString());

			resultunifiedorder = result;

		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected Map<String, String> doInBackground(Void... params) {

			String url = String
					.format("https://api.mch.weixin.qq.com/pay/unifiedorder");
			String entity = genProductArgs();

			Log.e("orion", entity);

			byte[] buf = Util.httpPost(url, entity);

			String content = new String(buf);
			Log.e("orion", content);
			Map<String, String> xml = decodeXml(content);

			return xml;
		}
	}

	public Map<String, String> decodeXml(String content) {

		try {
			Map<String, String> xml = new HashMap<String, String>();
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(new StringReader(content));
			int event = parser.getEventType();
			while (event != XmlPullParser.END_DOCUMENT) {

				String nodeName = parser.getName();
				switch (event) {
				case XmlPullParser.START_DOCUMENT:

					break;
				case XmlPullParser.START_TAG:

					if ("xml".equals(nodeName) == false) {
						// 实例化student对象
						xml.put(nodeName, parser.nextText());
					}
					break;
				case XmlPullParser.END_TAG:
					break;
				}
				event = parser.next();
			}

			return xml;
		} catch (Exception e) {
			Log.e("orion", e.toString());
		}
		return null;

	}

	private String genNonceStr() {
		Random random = new Random();
		return MD5.getMessageDigest(String.valueOf(random.nextInt(10000))
				.getBytes());
	}

	private long genTimeStamp() {
		return System.currentTimeMillis() / 1000;
	}

	private String genOutTradNo() {
		Random random = new Random();
		return MD5.getMessageDigest(String.valueOf(random.nextInt(10000))
				.getBytes());
	}

	private String genProductArgs() {
		StringBuffer xml = new StringBuffer();
		try {
			String nonceStr = genNonceStr();

			xml.append("</xml>");
			List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
			packageParams
					.add(new BasicNameValuePair("appid", Constants.APP_ID));
			packageParams.add(new BasicNameValuePair("body","[食尚男女]"+merchantName));
			packageParams
					.add(new BasicNameValuePair("mch_id", Constants.MCH_ID));
			packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));
			packageParams.add(new BasicNameValuePair("notify_url",
					"http://121.40.35.3/test"));
			packageParams.add(new BasicNameValuePair("out_trade_no",
					genOutTradNo()));
			packageParams.add(new BasicNameValuePair("spbill_create_ip",
					"127.0.0.1"));
			
			String s = (Float.parseFloat(price))*100+"";
			s.indexOf(".");
			Log.i("price", s.indexOf(".")+"======s.indexOf()=========");
			s = s.substring(0, s.indexOf("."));
			Log.i("price", s+"======s=========");
			packageParams.add(new BasicNameValuePair("total_fee", s));
			packageParams.add(new BasicNameValuePair("trade_type", "APP"));

			String sign = genPackageSign(packageParams);
			packageParams.add(new BasicNameValuePair("sign", sign));

			String xmlstring = toXml(packageParams);

			return new String(xmlstring.toString().getBytes(),"ISO8859-1");

		} catch (Exception e) {
			Log.e(TAG, "==========================" + e.getMessage());
			return null;
		}
	}

	private void genPayReq() {

		req.appId = Constants.APP_ID;
		req.partnerId = Constants.MCH_ID;
		req.prepayId = resultunifiedorder.get("prepay_id");
		req.packageValue = "Sign=WXPay";
		req.nonceStr = genNonceStr();
		req.timeStamp = String.valueOf(genTimeStamp());
		List<NameValuePair> signParams = new LinkedList<NameValuePair>();
		signParams.add(new BasicNameValuePair("appid", req.appId));
		signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
		signParams.add(new BasicNameValuePair("package", req.packageValue));
		signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
		signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
		signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));

		req.sign = genAppSign(signParams);

		sb.append("sign\n" + req.sign + "\n\n");

		// show.setText(sb.toString());

		Log.e("orion", signParams.toString());
		handler.sendEmptyMessageDelayed(2, 0);
	}

	private void sendPayReq() {
		msgApi.registerApp(Constants.APP_ID);
		msgApi.sendReq(req);
	}

	Runnable imageRun = new Runnable() {
		@Override
		public void run() {
//			mBitmap = Constants.getHttpBitmap2(mLoginImagePath);
			mBitmap = ImageLoader.getInstance().loadImageSync(mLoginImagePath);
//			mBitmap = BitmapCompressionUtils.getBitmap(mLoginImagePath);
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
