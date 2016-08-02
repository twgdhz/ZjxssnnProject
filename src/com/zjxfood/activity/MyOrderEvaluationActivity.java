package com.zjxfood.activity;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.project.util.DensityUtils;
import com.project.util.ZoomBitmap;
import com.umeng.analytics.MobclickAgent;
import com.zjxfood.application.ExitApplication;
import com.zjxfood.common.Constants;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MyOrderEvaluationActivity extends AppActivity implements
		OnClickListener {

	private ImageView mBackImage;
	private ImageView mMerchantImage;
	private TextView mMerchantName;
	private TextView mMerchantPrice;
	private Button mSubBtn;
	private Bundle mBundle;
	private String imagePath, merchantName, priceStr;
	private ImageView mScore1, mScore2, mScore3, mScore4, mScore5;
	private int flag = 1;
	private EditText mEvaluationEdit;
	private String mId;
	private int x = 1;
	private String res = "";
	private RunTask mRunTask;
	private ImageView mSlideImage1, mSlideImage2, mSlideImage3;
	private TextView mLocationHead, mCamearHead;// 本地相册 拍照上传
	private PopupWindow mPopupWindow;
	private LinearLayout mALertLayout;
	private ImageView[] mImageViews;
	private int position = 0;
	private Button popupBtn;
	private String[] mPaths = new String[3];
	private String mPath = "";
	private String orderId;
	private PopupWindow mHbPopWindow;
	private Button mToHbBtn, mCancelHbBtn;
	private boolean isClick = true;// 让评论按钮只能单次被点一次
	private boolean isAlertPop = true;
	private Bitmap mBitmap;
	private BitmapUtils mBitmapUtils;
	private TextView mTitleText;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_order_info_evaluation_layout);
		setImmerseLayout(findViewById(R.id.head_layout));
		ExitApplication.getInstance().addActivity(this);
		ExitApplication.getInstance().addMyOrderActivity(this);
		init();
		mBitmapUtils = new BitmapUtils(getApplicationContext());
		mBundle = getIntent().getExtras();
		if (mBundle != null) {
			imagePath = mBundle.getString("ImagePath");
			merchantName = mBundle.getString("MerchantName");
			priceStr = mBundle.getString("MerchantPrice");
			mId = mBundle.getString("MMid");
			orderId = mBundle.getString("orderId");
		}
		Log.i("mid", mId + "============mId===============orderId："+orderId);
		setResource();
		displayBriefMemory();
	}
	 private void displayBriefMemory() {
	        final ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);    
	        ActivityManager.MemoryInfo info = new ActivityManager.MemoryInfo();   
	        activityManager.getMemoryInfo(info);    
	        Log.i("tag","系统剩余内存:"+(info.availMem >> 10)+"k");   
	        Log.i("tag","系统是否处于低内存运行："+info.lowMemory);
	        Log.i("tag","当系统剩余内存低于"+info.threshold+"时就看成低内存运行");
	    } 
	private void init() {
//		mBackImage = (ImageView) findViewById(R.id.title_back_image);
		mBackImage = (ImageView) findViewById(R.id.title_back_image);
		mTitleText = (TextView) findViewById(R.id.title_text);
		mTitleText.setText("订单评价");
		mSubBtn = (Button) findViewById(R.id.order_evaluation_submit_btn);
		mMerchantImage = (ImageView) findViewById(R.id.order_info_evaluation_image);
		mMerchantName = (TextView) findViewById(R.id.order_info_evaluation_name);
		mMerchantPrice = (TextView) findViewById(R.id.order_info_evaluation_price);
		mScore1 = (ImageView) findViewById(R.id.order_info_evaluation_xx_1);
		mScore2 = (ImageView) findViewById(R.id.order_info_evaluation_xx_2);
		mScore3 = (ImageView) findViewById(R.id.order_info_evaluation_xx_3);
		mScore4 = (ImageView) findViewById(R.id.order_info_evaluation_xx_4);
		mScore5 = (ImageView) findViewById(R.id.order_info_evaluation_xx_5);
		mEvaluationEdit = (EditText) findViewById(R.id.order_info_evaluation_edit);
		mSlideImage1 = (ImageView) findViewById(R.id.order_info_evaluation_upload_image);
		mSlideImage2 = (ImageView) findViewById(R.id.order_info_evaluation_upload_image2);
		mSlideImage3 = (ImageView) findViewById(R.id.order_info_evaluation_upload_image3);
		mALertLayout = (LinearLayout) findViewById(R.id.my_evaluation_alert_view);
		mImageViews = new ImageView[3];
		mImageViews[0] = mSlideImage1;
		mImageViews[1] = mSlideImage2;
		mImageViews[2] = mSlideImage3;

		mPaths[0] = "";
		mPaths[1] = "";
		mPaths[2] = "";

		mBackImage.setOnClickListener(this);
		mScore1.setOnClickListener(this);
		mScore2.setOnClickListener(this);
		mScore3.setOnClickListener(this);
		mScore4.setOnClickListener(this);
		mScore5.setOnClickListener(this);
		mSubBtn.setOnClickListener(this);
		mSlideImage1.setOnClickListener(this);
		mSlideImage2.setOnClickListener(this);
		mSlideImage3.setOnClickListener(this);
		mALertLayout.setOnClickListener(this);
	}

	private void setResource() {
		mBitmapUtils.display(mMerchantImage, imagePath);
//		ImageLoader.getInstance().displayImage(imagePath, mMerchantImage);
		mMerchantName.setText(merchantName);
		mMerchantPrice.setText("￥" + priceStr);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_back_image:
			if (mRunTask != null
					&& mRunTask.getStatus() == AsyncTask.Status.RUNNING) {
				mRunTask.cancel(true);
			}
			isAlertPop = false;
			recyBitmap();
			if(mImageViews!=null && mImageViews.length>0){
				for(int i=0;i>mImageViews.length;i++){
					releaseImageViewResouce(mImageViews[i]);
				}
			}
			System.gc();
			finish();
			
			break;

		case R.id.order_info_evaluation_xx_1:
			mScore1.setImageResource(R.drawable.evaluation_xx_true);
			mScore2.setImageResource(R.drawable.evaluation_xx_not);
			mScore3.setImageResource(R.drawable.evaluation_xx_not);
			mScore4.setImageResource(R.drawable.evaluation_xx_not);
			mScore5.setImageResource(R.drawable.evaluation_xx_not);
			flag = 1;
			break;
		case R.id.order_info_evaluation_xx_2:
			mScore1.setImageResource(R.drawable.evaluation_xx_true);
			mScore2.setImageResource(R.drawable.evaluation_xx_true);
			mScore3.setImageResource(R.drawable.evaluation_xx_not);
			mScore4.setImageResource(R.drawable.evaluation_xx_not);
			mScore5.setImageResource(R.drawable.evaluation_xx_not);
			flag = 2;
			break;
		case R.id.order_info_evaluation_xx_3:
			mScore1.setImageResource(R.drawable.evaluation_xx_true);
			mScore2.setImageResource(R.drawable.evaluation_xx_true);
			mScore3.setImageResource(R.drawable.evaluation_xx_true);
			mScore4.setImageResource(R.drawable.evaluation_xx_not);
			mScore5.setImageResource(R.drawable.evaluation_xx_not);
			flag = 3;
			break;
		case R.id.order_info_evaluation_xx_4:
			mScore1.setImageResource(R.drawable.evaluation_xx_true);
			mScore2.setImageResource(R.drawable.evaluation_xx_true);
			mScore3.setImageResource(R.drawable.evaluation_xx_true);
			mScore4.setImageResource(R.drawable.evaluation_xx_true);
			mScore5.setImageResource(R.drawable.evaluation_xx_not);
			flag = 4;
			break;
		case R.id.order_info_evaluation_xx_5:
			mScore1.setImageResource(R.drawable.evaluation_xx_true);
			mScore2.setImageResource(R.drawable.evaluation_xx_true);
			mScore3.setImageResource(R.drawable.evaluation_xx_true);
			mScore4.setImageResource(R.drawable.evaluation_xx_true);
			mScore5.setImageResource(R.drawable.evaluation_xx_true);
			flag = 5;
			break;

		case R.id.order_evaluation_submit_btn:
			if (isClick) {
				mSubBtn.setText("正在提交...");
				if (!(mEvaluationEdit.getText().toString().equals(""))) {
					mRunTask = new RunTask();
					mRunTask.execute();
				} else {
					Toast.makeText(getApplicationContext(), "评论内容不能为空！",
							Toast.LENGTH_SHORT).show();
				}
			}
			
//			Intent intent = new Intent();
//			intent.setClass(getApplicationContext(), GrabRedActivity.class);
//			Bundle bundle = new Bundle();
//			bundle.putString("Id", orderId);
//			intent.putExtras(bundle);
//			startActivity(intent);
//			finish();
			break;
		// 图片一上传
		case R.id.order_info_evaluation_upload_image:
			position = 0;
			alertPopup();
			break;
		case R.id.order_info_evaluation_upload_image2:
			position = 1;
			alertPopup();
			break;
		case R.id.order_info_evaluation_upload_image3:
			position = 2;
			alertPopup();
			break;
		case R.id.my_evaluation_alert_view:

			break;
		}
	}

	private void alertPopup() {
		LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
		View view = inflater
				.inflate(R.layout.popup_evaluation_png_layout, null);
		mLocationHead = (TextView) view
				.findViewById(R.id.upload_evaluation_location_text);
		mCamearHead = (TextView) view
				.findViewById(R.id.upload_evaluation_camear_text);
		popupBtn = (Button) view
				.findViewById(R.id.popup_upload_evaluation__btn);

		mPopupWindow = new PopupWindow(view, DensityUtils.dp2px(
				getApplicationContext(), 220), DensityUtils.dp2px(
				getApplicationContext(), 230), false);
		mPopupWindow.showAtLocation(mSubBtn, Gravity.CENTER, 0, 0);
		mALertLayout.setVisibility(View.VISIBLE);
		mLocationHead.setOnClickListener(mBrowseClick);
		mCamearHead.setOnClickListener(mCamearClick);
		popupBtn.setOnClickListener(mDismissClick);
	}

	OnClickListener mDismissClick = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			mPopupWindow.dismiss();
			mALertLayout.setVisibility(View.GONE);
		}
	};
	// 本地上传
	OnClickListener mBrowseClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			mPopupWindow.dismiss();
			mALertLayout.setVisibility(View.GONE);
			Intent intent = new Intent();
			/* 开启Pictures画面Type设定为image */
			intent.setType("image/*");
			/* 使用Intent.ACTION_GET_CONTENT这个Action */
			intent.setAction(Intent.ACTION_GET_CONTENT);
			/* 取得相片后返回本画面 */
			startActivityForResult(intent, 2);
		}
	};

	// 拍照上传
	OnClickListener mCamearClick = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			mPopupWindow.dismiss();
			mALertLayout.setVisibility(View.GONE);
			Intent intent = new Intent();
			if (Constants.onLine == 1) {
				intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(intent, 1);
			} else {
				Toast.makeText(getApplicationContext(), "请先登录用户帐号！",
						Toast.LENGTH_SHORT).show();
			}
		}
	};

	//
	@SuppressLint("NewApi")
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		// 拍照
		case 1:
			if (data != null) {
				String sdStatus = Environment.getExternalStorageState();
				if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
					Log.i("TestFile",
							"SD card is not avaiable/writeable right now.");
					return;
				}
				String name = Constants.mId + "ssnn.png";
				Bundle bundle = data.getExtras();
				if (bundle != null) {
					mBitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式

					FileOutputStream b = null;
					File file = new File(Constants.fileUrl);
					if (file.exists() && file.isDirectory()) {
						file.delete();
						file.mkdirs();// 创建新的文件夹
					} else {
						file.mkdirs();// 创建新的文件夹
					}
					String fileName = Constants.fileUrl + name;
					Log.i("bitmapPath", fileName
							+ "==========fileName============");
					try {
						b = new FileOutputStream(fileName);

						mBitmap.compress(Bitmap.CompressFormat.PNG, 100, b);// 把数据写入文件
						if (ZoomBitmap.getBitmapSize(mBitmap) > 4000000) {
							while (true) {
								mBitmap = ZoomBitmap.zoomImage(mBitmap,
										mBitmap.getWidth() / 3,
										mBitmap.getHeight() / 3);
								if (ZoomBitmap.getBitmapSize(mBitmap) < 4000000) {
									break;
								}
							}
						}
						mImageViews[position].setImageBitmap(mBitmap);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} finally {
						try {
							b.flush();
							b.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					mPaths[position] = fileName;
				}
			}
			break;
		// 本地上传
		case 2:
			if (data != null) {
				
				Uri uri = data.getData();
				String path = "";
				if (!TextUtils.isEmpty(uri.getAuthority())) {
					Cursor cursor = getContentResolver().query(uri,
							new String[] { MediaStore.Images.Media.DATA },
							null, null, null);
					if (null == cursor) {
						// Toast.makeText(this, "图片没找到", Toast.LENGTH_SHORT)
						// .show();
						return;
					}
					cursor.moveToFirst();
					path = cursor.getString(cursor
							.getColumnIndex(MediaStore.Images.Media.DATA));
					cursor.close();
				} else {
					path = uri.getPath();
				}
				if (path != null) {
					BitmapFactory.Options opts = new BitmapFactory.Options();
					opts.inJustDecodeBounds = true;
//					opts.inSampleSize = 2;
					BitmapFactory.decodeFile(path, opts);
					opts.inSampleSize = ZoomBitmap.computeSampleSize(opts, -1,
							100 * 100);
					opts.inJustDecodeBounds = false;

					mBitmap = BitmapFactory.decodeFile(path, opts);
					Log.i("bitmapsize", ZoomBitmap.getBitmapSize(mBitmap)
							+ "===================");
					mImageViews[position].setImageBitmap(mBitmap);
					
					mPaths[position] = path;
				} else {
					Toast.makeText(this, "该手机暂不支持本地上传！", Toast.LENGTH_SHORT)
							.show();
				}
			}
			break;
		}
	};

	// 评论上传
	private void uploadEvaluation(String[] path) throws Exception {
		String time = System.currentTimeMillis()+"";
		String str = "userid=" + Constants.mId + "&mid="
				+ mId + "&pltext=" + (mEvaluationEdit.getText().toString()).replaceAll("\r|\n", "").replaceAll(" ", "")
				+ "&plstar1=" + flag + "&plstar2=" + flag + "&plstar3=" + flag
				+ "&orderid=" + orderId+"&timestamp="+time;
		String sign = Constants.sortsStr(str);
		try {
			res = Constants.addPl
					+ sign
					+ "&userid="
					+ Constants.mId
					+ "&mid="
					+ mId
					+ "&pltext="
					+ (mEvaluationEdit.getText().toString()).replaceAll("\r|\n", "").replaceAll(" ", "") + "&plstar1=" + flag + "&plstar2=" + flag
					+ "&plstar3=" + flag + "&orderid=" + orderId+"&timestamp="+time;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HttpClient httpclient = new DefaultHttpClient();
		Log.i("res", res + "=========res========");
		HttpPost httpPost = new HttpPost(res);
		MultipartEntity mulentity = new MultipartEntity(
				HttpMultipartMode.BROWSER_COMPATIBLE);
		// mulentity.addPart("foodname", new StringBody("ssnn"));
		// mulentity.addPart("foodstyle", new StringBody("type"));
		// mulentity.addPart("price", new StringBody("123"));

		// 添加图片表单数据
		if (!(path[0].equals(""))) {
			FileBody filebody = new FileBody(new File(path[0]));
			mulentity.addPart("foodimg1", filebody);
		}
		if (!(path[1].equals(""))) {
			FileBody filebody2 = new FileBody(new File(path[1]));
			mulentity.addPart("foodimg2", filebody2);
		}
		if (!(path[2].equals(""))) {
			FileBody filebody3 = new FileBody(new File(path[2]));
			mulentity.addPart("foodimg3", filebody3);
		}
		httpPost.setEntity(mulentity);
		HttpResponse response = httpclient.execute(httpPost);
		
		HttpEntity resEntity = response.getEntity();
		String json = EntityUtils.toString(resEntity, "utf-8");
		Log.i("response", response.getEntity() + "==========上传消息==========="
				+ json);
		if (isAlertPop) {
			if (json.equals("1")) {
				handler.sendEmptyMessageDelayed(1, 0);
			} else {
				handler.sendEmptyMessageDelayed(2, 0);
			}
		}
	}

	class RunTask extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... arg0) {
			if (isCancelled())
				return null;
			switch (x) {
			case 1:
				try {
					uploadEvaluation(mPaths);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case 2:
				try {
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			}
			return null;
		}
	}

	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			if (mRunTask != null
					&& mRunTask.getStatus() == AsyncTask.Status.RUNNING) {
				mRunTask.cancel(true);
			}
			isAlertPop = false;
			recyBitmap();
			if(mImageViews!=null && mImageViews.length>0){
				for(int i=0;i>mImageViews.length;i++){
					releaseImageViewResouce(mImageViews[i]);
				}
			}
			System.gc();
			finish();
			break;

		default:
			break;
		}
		return false;
	};

	public static void releaseImageViewResouce(ImageView imageView) {
        if (imageView == null) return;
        Drawable drawable = imageView.getDrawable();
        if (drawable != null && drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            Bitmap bitmap = bitmapDrawable.getBitmap();
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
            }
        }
    }
	public void recyBitmap(){
		if (mBitmap != null && !mBitmap.isRecycled()) {
			Log.i("mBitmap", "========recycled============");
			// 回收并且置为null
			mBitmap.recycle();
			mBitmap = null;
		}
	}
	
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				mSubBtn.setText("评价");
//				Toast.makeText(getApplicationContext(), "评论成功！",
//						Toast.LENGTH_SHORT).show();
				LayoutInflater inflater = LayoutInflater
						.from(getApplicationContext());
				View view = inflater.inflate(R.layout.popup_alert_hb_layout,
						null);
				mToHbBtn = (Button) view
						.findViewById(R.id.popup_hb_to_confirm_btn);
				mCancelHbBtn = (Button) view
						.findViewById(R.id.popup_hb_cancel_cancel_btn);
				mHbPopWindow = new PopupWindow(view, DensityUtils.dp2px(
						getApplicationContext(), 250),
						LayoutParams.WRAP_CONTENT, false);

				mHbPopWindow.setBackgroundDrawable(new BitmapDrawable());
				// mPopupWindow.setOutsideTouchable(true);
				mHbPopWindow.showAtLocation(mBackImage, Gravity.CENTER, 0, 0);
				mALertLayout.setVisibility(View.VISIBLE);
				mToHbBtn.setOnClickListener(mGrabHbClick);
				mCancelHbBtn.setOnClickListener(mPopDismissClick);
				isClick = true;
				recyBitmap();
				if(mImageViews!=null && mImageViews.length>0){
					for(int i=0;i>mImageViews.length;i++){
						releaseImageViewResouce(mImageViews[i]);
					}
				}
				break;

			case 2:
				recyBitmap();
				if(mImageViews!=null && mImageViews.length>0){
					for(int i=0;i>mImageViews.length;i++){
						releaseImageViewResouce(mImageViews[i]);
					}
				}
				isClick = true;
				mSubBtn.setText("评价");
				Log.i("res", res + "==========上传消息==2=========");
				Toast.makeText(getApplicationContext(), "评论失败,内容格式不正确或已经评论过了！",
						Toast.LENGTH_SHORT).show();
				break;
			}
		};
	};

	// 抢红包
	OnClickListener mGrabHbClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (mBitmap != null && !mBitmap.isRecycled()) {
				Log.i("mBitmap", "========recycled============");
				// 回收并且置为null
				mBitmap.recycle();
				mBitmap = null;
			}
			if(mImageViews!=null && mImageViews.length>0){
				for(int i=0;i>mImageViews.length;i++){
					releaseImageViewResouce(mImageViews[i]);
				}
			}
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), GrabRedActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("Id", orderId);
			intent.putExtras(bundle);
			startActivity(intent);
			finish();
			mALertLayout.setVisibility(View.GONE);
			mHbPopWindow.dismiss();
		}
	};

	OnClickListener mPopDismissClick = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			mALertLayout.setVisibility(View.GONE);
			mHbPopWindow.dismiss();

		}
	};
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
