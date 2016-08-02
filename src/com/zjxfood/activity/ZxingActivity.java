package com.zjxfood.activity;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.umeng.analytics.MobclickAgent;
import com.zjxfood.application.ExitApplication;
import com.zjxfood.common.Constants;
import com.zjxfood.http.XutilsUtils;
import com.zjxfood.zxing.camear.CameraManager;
import com.zjxfood.zxing.decoding.CaptureActivityHandler;
import com.zjxfood.zxing.decoding.InactivityTimer;
import com.zjxfood.zxing.view.ViewfinderView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

/**
 * Initial the camera
 * 
 * @author Ryan.Tang
 */
public class ZxingActivity extends AppActivity implements Callback {

	private CaptureActivityHandler handler;
	private ViewfinderView viewfinderView;
	private boolean hasSurface;
	private Vector<BarcodeFormat> decodeFormats;
	private String characterSet;
	private InactivityTimer inactivityTimer;
	private MediaPlayer mediaPlayer;
	private boolean playBeep;
	private static final float BEEP_VOLUME = 0.10f;
	private boolean vibrate;

	private Bundle mBundle;
	private String flag = "0";
	public static final String action = "jason.broadcast.action";
	private ArrayList<HashMap<String, Object>> mList;
	private String userId = "";
	private String json = "";
	private String price = "";
	public static String userCode = "";
	private int x = 1;

	// private RunTask mRunTask;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_capture);
		ExitApplication.getInstance().addActivity(this);
		CameraManager.init(getApplication());
		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
		mBundle = getIntent().getExtras();
		if (mBundle != null) {
			flag = mBundle.getString("flag");
		}
		Button mButtonBack = (Button) findViewById(R.id.button_back);
		mButtonBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ZxingActivity.this.finish();
			}
		});
		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		decodeFormats = null;
		characterSet = null;

		playBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}
		initBeepSound();
		vibrate = true;
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
	}

	/**
	 * 处理扫描结果
	 * 
	 * @param result
	 * @param barcode
	 */
	public void handleDecode(Result result, Bitmap barcode) {
		inactivityTimer.onActivity();
		playBeepSoundAndVibrate();
		// 扫描结果

		String resultString = result.getText();
		Log.i("resultString", resultString
				+ "===========resultString===========");
		switch (Integer.parseInt(flag)) {
		case 1:
			// userId = resultString;
			String a = ",";
			int position = resultString.indexOf(a);
			if (position > 0) {
				userId = resultString.substring(0, position);
				price = resultString.substring(position + 1,
						resultString.length());
			} else {
				userId = resultString;
			}
			Log.i("position", price + "===========position===========" + userId);
			json = "mid=" + userId;
			if (position > 0) {
				if (Constants.onLine == 1 && !(Constants.mId.equals(""))) {
					if (!(price.equals(""))) {
						x = 2;
						// mRunTask = new RunTask();
						// mRunTask.execute(json);
						Log.i("mRunTask", "===========2========");
						getMerchantInfo();
					}
				} else {
					Toast.makeText(getApplicationContext(), "请先登录帐号！",
							Toast.LENGTH_SHORT).show();
				}
			} else {
				if (!(userId.equals(""))) {
					x = 1;
					// mRunTask = new RunTask();
					// mRunTask.execute(json);
					getMerchantInfo();
				}
			}
			break;

		case 2:
			Log.i("usercode", resultString + "========data=========" + flag);

			if (!(resultString.equals(""))) {
				userCode = resultString;
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putString("result", resultString);
				// bundle.putParcelable("bitmap", barcode);
				intent.putExtras(bundle);
				this.setResult(RESULT_OK, intent);
				// startActivityForResult(intent, requestCode)
			}
			break;
		case 3:
			Intent fidintent = new Intent(action);
			fidintent.putExtra("fid", resultString);
			sendBroadcast(fidintent);
			break;
		case 4:
			String b = ",";
			int num = resultString.indexOf(b);
			if (num > 0) {
				userId = resultString.substring(0, num);
			} else {
				userId = resultString;
			}
			Log.i("position", price + "===========position===========" + userId);
			json = "mid=" + userId;
			if (!(userId.equals(""))) {
				x = 1;
				// mRunTask = new RunTask();
				// mRunTask.execute(json);
				getMerchantInfo();
			}
			break;
		}

		ZxingActivity.this.finish();
	}

//	private void getMerchantInfo() {
//		RequestParams params = new RequestParams();
//		params.put("mid", userId);
//		AsyUtils.get(Constants.getIdMerchantInfo2, params,
//				new AsyncHttpResponseHandler() {
//					@Override
//					@Deprecated
//					public void onSuccess(int statusCode, String content) {
//						Log.i("getIdMerchantInfo2", content
//								+ "========onSuccess===1======");
//						mList = Constants.getJsonArray(content);
//						if (x == 1) {
//							mHandler.sendEmptyMessageDelayed(1, 0);
//						} else if (x == 2) {
//							Log.i("getIdMerchantInfo2", "===========2==========");
//							mHandler.sendEmptyMessageDelayed(2, 0);
//						}
//						super.onSuccess(statusCode, content);
//					}
//				});
//	}
	private void getMerchantInfo() {
		StringBuilder sb = new StringBuilder();
			sb.append("mid=" + userId);
		XutilsUtils.get(Constants.getIdMerchantInfo2, sb,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
					}
					@Override
					public void onSuccess(ResponseInfo<String> res) {
						Log.i("getIdMerchantInfo2", res.result
								+ "========onSuccess===1======");
						mList = Constants.getJsonArray(res.result);
						if (x == 1) {
							mHandler.sendEmptyMessageDelayed(1, 0);
						} else if (x == 2) {
							Log.i("getIdMerchantInfo2", "===========2==========");
							mHandler.sendEmptyMessageDelayed(2, 0);
						}
					}
				});
	}

	// private void getMerchantInfo(){
	// StringBuilder sb = new StringBuilder();
	// sb.append("mid="+userId);
	// XutilsUtils.get(Constants.getIdMerchantInfo2, sb, new
	// RequestCallBack<String>() {
	// @Override
	// public void onFailure(HttpException arg0, String arg1) {}
	// @Override
	// public void onSuccess(ResponseInfo<String> res) {
	// Log.i("getIdMerchantInfo2", res.result+"========onSuccess=========");
	// mList = Constants.getJsonArray(res.result);
	// if(x==1){
	// mHandler.sendEmptyMessageDelayed(1, 0);
	// }else if(x==2){
	// mHandler.sendEmptyMessageDelayed(2, 0);
	// }
	// }
	// });
	// }

	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				if (mList != null && mList.size() > 0) {
					Intent intent = new Intent();
					intent.setClass(getApplicationContext(),
							MerchantInfoActivity.class);
					Bundle bundle = new Bundle();
					if (!Constants.isNull(mList.get(0).get("Merchantname"))) {
						bundle.putString("merchantName",
								mList.get(0).get("Merchantname").toString());
					}
					if (!Constants.isNull(mList.get(0).get("Logoimage"))) {
						bundle.putString("Logoimage",
								mList.get(0).get("Logoimage").toString());
					}
					if (!Constants.isNull(mList.get(0).get("Address"))) {
						bundle.putString("merchantAddress",
								mList.get(0).get("Address").toString());
					}
					if (!Constants.isNull(mList.get(0).get("Phone"))) {
						bundle.putString("Phone", mList.get(0).get("Phone")
								.toString());
					}
					if (!Constants.isNull(mList.get(0).get("Introduction"))) {
						bundle.putString("Introduction",
								mList.get(0).get("Introduction").toString());
					}
					if (!Constants.isNull(mList.get(0).get("Userid"))) {
						bundle.putString("Userid", mList.get(0).get("Userid")
								.toString());
					}
					if (!Constants.isNull(mList.get(0).get("Id"))) {
						bundle.putString("Id", mList.get(0).get("Id")
								.toString());
					}
					if (!Constants.isNull(mList.get(0).get("Flnum"))) {
						bundle.putString("Flnum", mList.get(0).get("Flnum")
								.toString());
					}
					if (mList.get(0).get("plstar") != null) {
						bundle.putString("plstar", mList.get(0).get("plstar")
								.toString());
					} else {
						bundle.putString("plstar", "0");
					}
					if (!Constants.isNull(mList.get(0).get("verifyState"))) {
						bundle.putString("verifyState",
								mList.get(0).get("verifyState").toString());
					}
					if (!Constants.isNull(mList.get(0).get("Images1"))) {
						bundle.putString("Images1", mList.get(0).get("Images1")
								.toString());
					}
					if (!Constants.isNull(mList.get(0).get("Images2"))) {
						bundle.putString("Images2", mList.get(0).get("Images2")
								.toString());
					}
					if (!Constants.isNull(mList.get(0).get("Images3"))) {
						bundle.putString("Images3", mList.get(0).get("Images3")
								.toString());
					}
					if (!Constants.isNull(mList.get(0).get("Images4"))) {
						bundle.putString("Images4", mList.get(0).get("Images4")
								.toString());
					}
					if (!Constants.isNull(mList.get(0).get("Images5"))) {
						bundle.putString("Images5", mList.get(0).get("Images5")
								.toString());
					}
					// bundle.putSerializable("bitmaps", mBitmaps);
					intent.putExtras(bundle);
					startActivity(intent);
				}
				break;

			case 2:
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putString("price", Float.parseFloat(price) + "");
//				bundle.putString("flag", "2");//扫描
				if (mList != null && mList.size() > 0) {
					if (!Constants.isNull(mList.get(0).get("Merchantname"))) {
						bundle.putString("merchantName",
								mList.get(0).get("Merchantname").toString());
					}
					if (!Constants.isNull(mList.get(0).get("Userid"))) {
						bundle.putString("mId", mList.get(0).get("Userid")
								.toString());
					}
					if (!Constants.isNull(mList.get(0).get("Logoimage"))) {
						bundle.putString("Logoimage",
								mList.get(0).get("Logoimage").toString());
					}
					if (!Constants.isNull(mList.get(0).get("Flnum"))) {
						bundle.putString("flnum", mList.get(0).get("Flnum")
								.toString());
					}
					if (mList.get(0).get("plstar") != null) {
						bundle.putString("plstar",
								mList.get(0).get("plstar").toString());
					} else {
						bundle.putString("plstar", "0");
					}
					if (!Constants.isNull(mList.get(0).get("ordercount"))) {
						bundle.putString("ordercount", mList.get(0).get("ordercount")
								.toString());
					}
					bundle.putString("type", "commodity");
					bundle.putString("MallOrMerchant", "merchant");
					intent.putExtras(bundle);
					intent.setClass(getApplicationContext(),
							CheckOutActivity.class);
					startActivity(intent);
				}
				break;
			}
		};
	};

	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);
		} catch (IOException ioe) {
			return;
		} catch (RuntimeException e) {
			return;
		}
		if (handler == null) {
			handler = new CaptureActivityHandler(this, decodeFormats,
					characterSet);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;

	}

	public ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		return handler;
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();

	}

	private void initBeepSound() {
		if (playBeep && mediaPlayer == null) {
			// The volume on STREAM_SYSTEM is not adjustable, and users found it
			// too loud,
			// so we now play on the music stream.
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);

			AssetFileDescriptor file = getResources().openRawResourceFd(
					R.raw.beep);
			try {
				mediaPlayer.setDataSource(file.getFileDescriptor(),
						file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepare();
			} catch (IOException e) {
				mediaPlayer = null;
			}
		}
	}

	private static final long VIBRATE_DURATION = 200L;

	private void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		if (vibrate) {
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	/**
	 * When the beep has finished playing, rewind to queue up another one.
	 */
	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};
}