package com.zjxfood.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.project.util.BitmapCompressionUtils;
import com.project.util.ZoomBitmap;
import com.tencent.tauth.Tencent;
import com.umeng.analytics.MobclickAgent;
import com.zjxfood.adapter.MerchantSettledListAdapter;
import com.zjxfood.adapter.MerchantSettledTypeListAdapter;
import com.zjxfood.application.ExitApplication;
import com.zjxfood.common.Constants;
import com.zjxfood.http.XutilsUtils;
import com.zjxfood.popupwindow.MerchantSettledPopupWindow;
import com.zjxfood.popupwindow.MerchantSettledTypePopupWindow;
import com.zjxfood.popupwindow.SettledPhotoPopupWindow;
import com.zjxfood.popupwindow.SettledSuccessPopupWindow;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MerchantSettledActivity extends BaseActivity implements
		OnClickListener {
	private EditText mMerchantName;// 商户名称
	private TextView mProvinceText, mCityText, mAreaText;
	private EditText mMerchantAddress;// 地址
	private TextView mMerchantType;// 类别
	private EditText mMerchantPhone;// 电话
	private EditText mMerchantLegalPerson;// 法人姓名
	private EditText mMerchantIdcard;// 法人身份证
	private EditText mMerchantSRecommendation;// 上级推荐码
	// private ImageView mTypeImage;
	private ImageView mAddImage1, mAddImage2;
	private Button mSubmitBtn;
	private MerchantSettledPopupWindow mSettledPopupWindow;
	private MerchantSettledListAdapter mSettledListAdapter;
	private MerchantSettledTypeListAdapter mTypeListAdapter;
	private MerchantSettledTypePopupWindow mTypePopupWindow;
	private String[] mCitys, mAreas;
	private HashMap<String, String> mCityMaps, mAreaMaps;
	private String provinceId = "", cityId = "", areaId = "";
	private String provinceName = "", cityName, areaName = "";
	private ImageView mBackImage;
	private RelativeLayout mProvinceLayout, mCityLayout, mAreaLayout,
			mTypeLayout;
//	private HashMap<String, Object> mGroupMap;
	private ArrayList<HashMap<String, Object>> mGroupList;
	private String mTypeId = "";
	private SettledPhotoPopupWindow mPhotoPopupWindow;
	private int flag = 0;
	private String localTempImgDir = "appImage/merchant";
	private String localTempImgFileName = "";
	private Bitmap mBitmap;
	private String mPath;
	private AsyncHttpClient httpClient;
	private String[] mFilePath = new String[2];
	private HashMap<String, Object> mDataMap;
	private SettledSuccessPopupWindow mSuccessPopupWindow;
	private boolean isSave = true;
	private RelativeLayout mQQLayout1, mQQLayout2;
	private TextView mTitleText;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.merchant_settled_layout);
		setImmerseLayout(findViewById(R.id.head_layout));
		ExitApplication.getInstance().addActivity(this);
		initProvinceDatas();
		init();
		checkTencentInstance();
		httpClient = new AsyncHttpClient();
		getGroup();
	}

	private void init() {
		mMerchantName = (EditText) findViewById(R.id.merchant_settled_name_edit);
		mProvinceText = (TextView) findViewById(R.id.merchant_settled_province_edit);
		mCityText = (TextView) findViewById(R.id.merchant_settled_city_edit);
		mAreaText = (TextView) findViewById(R.id.merchant_settled_area_edit);
		mMerchantAddress = (EditText) findViewById(R.id.merchant_settled_address_edit);
		mMerchantType = (TextView) findViewById(R.id.merchant_settled_type_edit);
		mMerchantPhone = (EditText) findViewById(R.id.merchant_settled_phone_edit);
		mMerchantLegalPerson = (EditText) findViewById(R.id.merchant_settled_legalperson_name_edit);
		mMerchantIdcard = (EditText) findViewById(R.id.merchant_settled_legalperson_card_edit);
		mMerchantSRecommendation = (EditText) findViewById(R.id.merchant_settled_superior_recommendation_edit);
//		mBackImage = (ImageView) findViewById(R.id.title_back_image);
		mBackImage = (ImageView) findViewById(R.id.title_back_image);
		mTitleText = (TextView) findViewById(R.id.title_text);
		mTitleText.setText("商户申请入驻");
		mProvinceLayout = (RelativeLayout) findViewById(R.id.merchant_settled_province_layout);
		mCityLayout = (RelativeLayout) findViewById(R.id.merchant_settled_city_layout);
		mAreaLayout = (RelativeLayout) findViewById(R.id.merchant_settled_area_layout);
		mTypeLayout = (RelativeLayout) findViewById(R.id.merchant_settled_type_layout);

		mAddImage1 = (ImageView) findViewById(R.id.merchant_settled_add_image1);
		mAddImage2 = (ImageView) findViewById(R.id.merchant_settled_add_image2);
		mSubmitBtn = (Button) findViewById(R.id.merchant_settled_btn_submit);
		mQQLayout1 = (RelativeLayout) findViewById(R.id.merchant_settled_qq_content_layout1);
		mQQLayout2 = (RelativeLayout) findViewById(R.id.merchant_settled_qq_content_layout2);

		mQQLayout1.setOnClickListener(this);
		mQQLayout2.setOnClickListener(this);
		mProvinceLayout.setOnClickListener(this);
		mCityLayout.setOnClickListener(this);
		mAreaLayout.setOnClickListener(this);
		// mTypeImage.setOnClickListener(this);
		mBackImage.setOnClickListener(this);
		mTypeLayout.setOnClickListener(this);
		mAddImage1.setOnClickListener(this);
		mAddImage2.setOnClickListener(this);
		mSubmitBtn.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// QQ1
		case R.id.merchant_settled_qq_content_layout1:
			String uin = "2037927294";
			if (!"".equals(uin)) {
				int ret = MallDetailActivity.mTencent.startWPAConversation(
						MerchantSettledActivity.this, uin, "");
				if (ret != 0) {
					Toast.makeText(getApplicationContext(),
							"start WPA conversation failed. error:" + ret,
							Toast.LENGTH_LONG).show();
				}
			}
			break;
		// QQ2
		case R.id.merchant_settled_qq_content_layout2:
			uin = "814646211";
			if (!"".equals(uin)) {
				int ret = MallDetailActivity.mTencent.startWPAConversation(
						MerchantSettledActivity.this, uin, "");
				if (ret != 0) {
					Toast.makeText(getApplicationContext(),
							"start WPA conversation failed. error:" + ret,
							Toast.LENGTH_LONG).show();
				}
			}
			break;
		case R.id.merchant_settled_add_image1:
			flag = 0;
			mPhotoPopupWindow = new SettledPhotoPopupWindow(
					MerchantSettledActivity.this, mClickListener);
			mPhotoPopupWindow.showAtLocation(mMerchantName, Gravity.BOTTOM
					| Gravity.CENTER_HORIZONTAL, 0, 0);
			break;
		case R.id.merchant_settled_add_image2:
			flag = 1;
			mPhotoPopupWindow = new SettledPhotoPopupWindow(
					MerchantSettledActivity.this, mClickListener);
			mPhotoPopupWindow.showAtLocation(mMerchantName, Gravity.BOTTOM
					| Gravity.CENTER_HORIZONTAL, 0, 0);
			break;
		case R.id.title_back_image:
			finish();
			break;
		// 省份
		case R.id.merchant_settled_province_layout:
			hideWindow();
			if (mProvinDatasMap != null && mProvinDatasMap.length > 0) {
				mSettledListAdapter = new MerchantSettledListAdapter(
						getApplicationContext(), mProvinDatasMap);
				mSettledPopupWindow = new MerchantSettledPopupWindow(
						MerchantSettledActivity.this, mProvinceItemClick,
						mSettledListAdapter);
				mSettledPopupWindow.showAsDropDown(mProvinceText);
			}
			break;
		// 城市
		case R.id.merchant_settled_city_layout:
			hideWindow();
			if (mCitys != null && mCitys.length > 0) {
				// mCityImage.setImageResource(R.drawable.iconfont_xiala_up);
				mSettledListAdapter = new MerchantSettledListAdapter(
						getApplicationContext(), mCitys);
				mSettledPopupWindow = new MerchantSettledPopupWindow(
						MerchantSettledActivity.this, mCityItemClick,
						mSettledListAdapter);
				mSettledPopupWindow.showAsDropDown(mCityText);
			} else {
				Toast.makeText(getApplicationContext(), "请选择省份！",
						Toast.LENGTH_SHORT).show();
			}
			break;
		// 区域
		case R.id.merchant_settled_area_layout:
			hideWindow();
			if (mAreas != null && mAreas.length > 0) {
				// mAreaImage.setImageResource(R.drawable.iconfont_xiala_up);
				mSettledListAdapter = new MerchantSettledListAdapter(
						getApplicationContext(), mAreas);
				mSettledPopupWindow = new MerchantSettledPopupWindow(
						MerchantSettledActivity.this, mAreaItemClick,
						mSettledListAdapter);
				mSettledPopupWindow.showAsDropDown(mAreaText);
			} else {
				Toast.makeText(getApplicationContext(), "请选择省份！",
						Toast.LENGTH_SHORT).show();
			}
			break;
		// 类别
		case R.id.merchant_settled_type_layout:
			Log.i("type", "==========1=============");
			hideWindow();
			if (mGroupList != null && mGroupList.size() > 0) {
				mTypeListAdapter = new MerchantSettledTypeListAdapter(
						getApplicationContext(), mGroupList);
				mTypePopupWindow = new MerchantSettledTypePopupWindow(
						MerchantSettledActivity.this, mTypeItemClick,
						mTypeListAdapter);
				mTypePopupWindow.showAsDropDown(mMerchantType);
			}
			break;
		// 提交
		case R.id.merchant_settled_btn_submit:
			// handler.sendEmptyMessageDelayed(1, 0);
			if (!mMerchantName.getText().toString().equals("")) {
				if (!provinceId.equals("")) {
					if (!cityId.equals("")) {
						if (!areaId.equals("")) {
							if (!mMerchantAddress.getText().toString()
									.equals("")) {
								if (!mTypeId.equals("")) {
									if (!mMerchantPhone.getText().toString()
											.equals("")) {
										if (!mMerchantLegalPerson.getText()
												.toString().equals("")) {
//											if (!mMerchantIdcard.getText()
//													.toString().equals("")
//													&& IsIDcard(mMerchantIdcard
//															.getText()
//															.toString())) {
//												if (!mMerchantSRecommendation
//														.getText().toString()
//														.equals("")) {
//
//													Log.i("path", mFilePath[0]
//															+ "===="
//															+ mFilePath[1]);
//													if (mFilePath[0] != null
//															&& mFilePath[1] != null) {
														if (isSave) {
															mSubmitBtn
																	.setText("提交中...");
															isSave = false;
															new Thread(
																	submitRun)
																	.start();
														}
//													} else {
//														Toast.makeText(
//																getApplicationContext(),
//																"请上传所需图片！",
//																Toast.LENGTH_SHORT)
//																.show();
//													}
//												} else {
//													Toast.makeText(
//															getApplicationContext(),
//															"请填写上级推荐码！",
//															Toast.LENGTH_SHORT)
//															.show();
//												}
//											} else {
//												Toast.makeText(
//														getApplicationContext(),
//														"身份证格式不正确！",
//														Toast.LENGTH_SHORT)
//														.show();
//											}
										} else {
											Toast.makeText(
													getApplicationContext(),
													"请填写联系人姓名！",
													Toast.LENGTH_SHORT).show();
										}
									} else {
										Toast.makeText(getApplicationContext(),
												"请填写联系电话！", Toast.LENGTH_SHORT)
												.show();
									}
								} else {
									Toast.makeText(getApplicationContext(),
											"请选择餐饮类别！", Toast.LENGTH_SHORT)
											.show();
								}
							} else {
								Toast.makeText(getApplicationContext(),
										"请填写详细地址！", Toast.LENGTH_SHORT).show();
							}
						}
					}
				} else {
					Toast.makeText(getApplicationContext(), "请选择所在城市！",
							Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(getApplicationContext(), "商户名称不能为空！",
						Toast.LENGTH_SHORT).show();
			}
			break;
		}
	}

	private void submitInfo() {
		httpClient = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		try {
			// params.put("merchantname", URLEncoder.encode(mMerchantName
			// .getText().toString(), "UTF-8"));
			params.put("merchantname", mMerchantName.getText().toString());
			params.put("mtype", "3");
			params.put("provinceid", provinceId);
			params.put("cityid", cityId);
			params.put("areaid", areaId);
			params.put("address", mMerchantAddress.getText().toString());
			params.put("groupid", mTypeId);
			params.put("phone", mMerchantPhone.getText().toString());
			params.put("legalpersonname", mMerchantLegalPerson.getText()
					.toString());
			params.put("legalpersoncardno", mMerchantIdcard.getText()
					.toString());
			params.put("blnum", "");
			params.put("usercode", mMerchantSRecommendation.getText()
					.toString());
			if (mFilePath[0] != null) {
				File file = new File(mFilePath[0]);
				params.put("file1", file);
			}
			if (mFilePath[1] != null) {
				File file = new File(mFilePath[1]);
				params.put("file2", file);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		httpClient.post(Constants.merchantSettled, params,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONArray response) {
						Log.i("JSONArray", response + "=================");
						super.onSuccess(response);
					}

					@Override
					public void onSuccess(JSONObject response) {
						Log.i("JSONObject", response + "=================");
						super.onSuccess(response);
					}
				});
	}

	OnClickListener mClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			mPhotoPopupWindow.dismiss();
			Intent intent = new Intent();
			switch (v.getId()) {
			// 拍照
			case R.id.head_pupup_btn_take_photo:
				localTempImgFileName = Constants.mId + "_" + flag
						+ "merchant.png";
				Log.i("localTempImgFileName", localTempImgFileName+"=======1=========");
				// intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				// startActivityForResult(intent, 1);
				String status = Environment.getExternalStorageState();
				if (status.equals(Environment.MEDIA_MOUNTED)) {
					try {
						File dir = new File(
								Environment.getExternalStorageDirectory() + "/"
										+ localTempImgDir);
						if (!dir.exists())
							dir.mkdirs();
						intent = new Intent(
								android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
						File f = new File(dir, localTempImgFileName);
						// localTempImgDir和localTempImageFileName是自己定义的名字
						Uri u = Uri.fromFile(f);
						intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
						intent.putExtra(MediaStore.EXTRA_OUTPUT, u);
						startActivityForResult(intent, 1);
					} catch (ActivityNotFoundException e) {
						// TODO Auto-generated catch block
						Toast.makeText(MerchantSettledActivity.this,
								"没有找到储存目录", Toast.LENGTH_LONG).show();
					}
				} else {
					Toast.makeText(MerchantSettledActivity.this, "没有储存卡",
							Toast.LENGTH_LONG).show();
				}

				break;
			// 相册
			case R.id.head_pupup_btn_pick_photo:
				/* 开启Pictures画面Type设定为image */
				intent.setType("image/*");
				/* 使用Intent.ACTION_GET_CONTENT这个Action */
				intent.setAction(Intent.ACTION_GET_CONTENT);
				/* 取得相片后返回本画面 */
				startActivityForResult(intent, 2);
				break;
			}
		}
	};

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		// 拍照
		case 1:
			String sdStatus = Environment.getExternalStorageState();
			if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
				Log.i("TestFile",
						"SD card is not avaiable/writeable right now.");
				return;
			}
			if (resultCode == -1) {
				String mpath = "/sdcard/appImage/merchant/"
						+ localTempImgFileName;
				if(!localTempImgFileName.equals("")){
				mPath = mpath;
				Log.i("mpath", mpath + "===============");

				mBitmap = BitmapCompressionUtils.getBitmap(mpath);
//				mBitmap = ImageLoader.getInstance().loadImageSync(mpath);
				mBitmap = ZoomBitmap.zoomImage(mBitmap, mBitmap.getWidth() / 2,
						mBitmap.getHeight() / 2);
				FileOutputStream b = null;
				File file = new File("/sdcard/appImage/merchant/");
				if (file.exists() && file.isDirectory()) {
					file.delete();
					file.mkdirs();// 创建新的文件夹
				} else {
					file.mkdirs();// 创建新的文件夹
				}
				try {
					String fileName = "/sdcard/appImage/merchant/"
							+ localTempImgFileName;
					b = new FileOutputStream(fileName);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				mBitmap.compress(Bitmap.CompressFormat.PNG, 100, b);
				if (flag == 0) {
					mAddImage1.setImageBitmap(mBitmap);
				} else if (flag == 1) {
					mAddImage2.setImageBitmap(mBitmap);
				}
			}
			mFilePath[flag] = mPath;
			}
			break;
		// 相册
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
					mPath = path;
					mBitmap = BitmapCompressionUtils.getBitmap(path);
					if (flag == 0) {
						mAddImage1.setImageBitmap(mBitmap);
					} else if (flag == 1) {
						mAddImage2.setImageBitmap(mBitmap);
					}
				} else {
					Toast.makeText(this, "该手机暂不支持本地图片上传", Toast.LENGTH_SHORT)
							.show();
				}
			} else {
				Toast.makeText(this, "图片没找到", Toast.LENGTH_SHORT).show();
			}
			mFilePath[flag] = mPath;
			break;
		}
	};

	OnItemClickListener mProvinceItemClick = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			mSettledPopupWindow.dismiss();

			mCitys = mCitisDatasMap.get(mProvinDatasMap[position]);
			mCityMaps = mCitysAllMap.get(mProvinDatasMap[position]);
			mProvinceText.setText(mProvinDatasMap[position]);
			provinceName = mProvinDatasMap[position];
			provinceId = mProvinceAllMap.get(mProvinDatasMap[position]);
			if (mCitys != null && mCitys.length > 0) {
				mAreas = mDistrictDatasMap.get(mCitys[0]);
				mAreaMaps = mDistrictAllMap.get(mCitys[0]);
				mCityText.setText(mCitys[0]);
				mAreaText.setText(mAreas[0]);

				areaName = mAreas[0];
				cityName = mCitys[0];
				areaId = mAreaMaps.get(mAreas[0]);
				cityId = mCityMaps.get(mCitys[0]);
			}
			Log.i("mProvinceText", "provinceName:" + provinceName
					+ "===provinceId:" + provinceId);
			Log.i("mProvinceText默认", "默认cityName:" + cityName + "===cityId:"
					+ cityId);
			Log.i("mProvinceText默认", "默认areaName:" + areaName + "===areaId:"
					+ areaId);
		}
	};
	OnItemClickListener mCityItemClick = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			try {
				if (mSettledPopupWindow != null && mSettledPopupWindow.isShowing()) {
					mSettledPopupWindow.dismiss();
				}
				mAreas = mDistrictDatasMap.get(mCitys[position]);
				mAreaMaps = mDistrictAllMap.get(mCitys[position]);
				mCityText.setText(mCitys[position]);

				mAreaText.setText(mAreas[0]);
				areaId = mAreaMaps.get(mAreas[0]);

				cityId = mCityMaps.get(mCitys[position]);
				cityName = mCitys[position];
			}catch (Exception e){
				e.printStackTrace();
			}
			Log.i("city", "cityName:" + cityName + "===cityId:" + cityId);
		}
	};
	OnItemClickListener mAreaItemClick = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			if(mSettledPopupWindow!=null && mSettledPopupWindow.isShowing()){
			mSettledPopupWindow.dismiss();
			}
			mAreaText.setText(mAreas[position]);
			areaId = mAreaMaps.get(mAreas[position]);
			areaName = mAreas[position];
			Log.i("mAreas", "areaName:" + areaName + "=========areaId:"
					+ areaId);
		}
	};
	OnItemClickListener mTypeItemClick = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			mTypePopupWindow.dismiss();
			mMerchantType.setText(mGroupList.get(position).get("name")
					.toString());
			mTypeId = mGroupList.get(position).get("id").toString();

		}
	};

	private void getGroup(){
	StringBuilder sb = new StringBuilder();
		sb.append("");
		XutilsUtils.get(Constants.getGroup2, sb, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {}
			@Override
			public void onSuccess(ResponseInfo<String> res) {
				try {
					mGroupList = Constants.getJsonArrayByData(res.result);
//					Log.i("mGroupList", mGroupList+"===================");
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

	OnClickListener mPopupClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.merchant_settled_popup_content_confirm:
				mSuccessPopupWindow.dismiss();
				finish();
				break;
			}
		}
	};

	// 隐藏软键盘
	private void hideWindow() {
		View view = getWindow().peekDecorView();
		if (view != null) {
			InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
	}

	Runnable submitRun = new Runnable() {
		@Override
		public void run() {
			uploadSubmit(mFilePath);
		}
	};
	
	private void uploadHttp(){
		RequestParams params = new RequestParams();
		params.put("merchantname", mMerchantName.getText());
		params.put("mtype", "3");
		params.put("provinceid", provinceId);
		params.put("cityid", cityId);
		params.put("areaid", areaId);
		params.put("address", mMerchantAddress.getText());
		params.put("groupid", mTypeId);
		params.put("phone", mMerchantPhone.getText());
		
		params.put("legalpersonname", mMerchantLegalPerson.getText());
		params.put("legalpersoncardno", mMerchantIdcard.getText());
		params.put("usercode", mMerchantSRecommendation.getText());
	}

	// 评论上传
	private void uploadSubmit(String[] path) {
		String time = System.currentTimeMillis() + "";
		String str = "";
		if(mMerchantSRecommendation.getText().toString().equals("")){
			str = "merchantname="
					+ mMerchantName.getText() + "&mtype=3" + "&provinceid="
					+ provinceId + "&cityid=" + cityId + "&areaid=" + areaId
					+ "&address=" + mMerchantAddress.getText() + "&groupid="
					+ mTypeId + "&phone=" + mMerchantPhone.getText()
					+ "&legalpersonname=" + mMerchantLegalPerson.getText()
					+ "&timestamp=" + time;
		}else{
			str = "merchantname="
					+ mMerchantName.getText() + "&mtype=3" + "&provinceid="
					+ provinceId + "&cityid=" + cityId + "&areaid=" + areaId
					+ "&address=" + mMerchantAddress.getText() + "&groupid="
					+ mTypeId + "&phone=" + mMerchantPhone.getText()
					+ "&legalpersonname=" + mMerchantLegalPerson.getText()
					+ "&usercode=" + mMerchantSRecommendation.getText()
					+ "&timestamp=" + time;
		}
		
		String sign = Constants.sortsStr(str);
		String res = "";
		try {
			res = Constants.merchantSettled
					+ sign
					+ "&"+str;
		} catch (Exception e) {
			e.printStackTrace();
		}
		HttpClient httpclient = new DefaultHttpClient();
		Log.i("res", res + "=========res========");
		try {
		HttpPost httpPost = new HttpPost(res);
		MultipartEntity mulentity = new MultipartEntity(
				HttpMultipartMode.BROWSER_COMPATIBLE);
		// 添加图片表单数据
//		if (path[0] != null) {
//			FileBody filebody = new FileBody(new File(path[0]));
//			mulentity.addPart("foodimg1", filebody);
//		}
//		if (path[1] != null) {
//			FileBody filebody2 = new FileBody(new File(path[1]));
//			mulentity.addPart("foodimg2", filebody2);
//		}
		httpPost.setEntity(mulentity);
		
			HttpResponse response = httpclient.execute(httpPost);

			HttpEntity resEntity = response.getEntity();
			String json = EntityUtils.toString(resEntity, "utf-8");
			mDataMap = Constants.getJson2Object(json);
			Log.i("mDataMap", mDataMap + "==========上传消息===========");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (mDataMap != null && mDataMap.size() > 0) {
			if (mDataMap.get("Code").toString().equals("200")) {
				handler.sendEmptyMessageDelayed(1, 0);
			} else {
				handler.sendEmptyMessageDelayed(2, 0);
			}
		} else {
			handler.sendEmptyMessageDelayed(2, 0);
		}

	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				isSave = true;
				mSubmitBtn.setText("提交");
				if (mDataMap.get("Message") != null) {
					mSuccessPopupWindow = new SettledSuccessPopupWindow(
							MerchantSettledActivity.this, mDataMap.get(
									"Message").toString(), mPopupClick);
					mSuccessPopupWindow.showAtLocation(mAddImage1,
							Gravity.CENTER, 0, 0);
				}
				break;
			case 2:
				isSave = true;
				mSubmitBtn.setText("提交");
				if(mDataMap!=null && mDataMap.size()>0){
					Toast.makeText(getApplicationContext(), mDataMap.get("Message").toString(),
							Toast.LENGTH_SHORT).show();
				}else{
				Toast.makeText(getApplicationContext(), "系统错误，请联系技术客服！",
						Toast.LENGTH_SHORT).show();
				}
				break;
			}
		};
	};

	/**
	 * 验证输入身份证号
	 * 
	 * @param 待验证的字符串
	 * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean IsIDcard(String str) {
		String regex = "(^(\\d{17})(\\d|[xX])$)";
		return match(regex, str);
	}

	/**
	 * @param regex
	 *            正则表达式字符串
	 * @param str
	 *            要匹配的字符串
	 * @return 如果str 符合 regex的正则表达式格式,返回true, 否则返回 false;
	 */
	private static boolean match(String regex, String str) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}

	protected void checkTencentInstance() {
		MallDetailActivity.mAppid = "222222";
		MallDetailActivity.mTencent = Tencent.createInstance(
				MallDetailActivity.mAppid, MerchantSettledActivity.this);
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
	protected void setImmerseLayout(View view) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			//透明状态栏
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//			//透明导航栏
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

			int statusBarHeight = getStatusBarHeight(this.getBaseContext());
			view.setPadding(0, statusBarHeight, 0, 0);
		}
	}
	/**
	 * 用于获取状态栏的高度。 使用Resource对象获取（推荐这种方式）
	 *
	 * @return 返回状态栏高度的像素值。
	 */
	public static int getStatusBarHeight(Context context) {
		int result = 0;
		int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen",
				"android");
		if (resourceId > 0) {
			result = context.getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}
}
