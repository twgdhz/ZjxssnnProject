package com.zjxfood.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.umeng.analytics.MobclickAgent;
import com.zjxfood.adapter.NewAddressListAdapter;
import com.zjxfood.application.ExitApplication;
import com.zjxfood.common.Constants;
import com.zjxfood.http.AsyUtils;
import com.zjxfood.popupwindow.NewAddressPopupWindow;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class NewAddressActivity extends BaseActivity implements OnClickListener {

	private EditText mUserNameEdit;
	private EditText mUserPhoneEdit;
	private TextView mProvinceText;
	private TextView mCityText;
	private TextView mAreaText;
	private EditText mAddressEdit;

	private String[] mCitys, mAreas;
	private HashMap<String, String> mCityMaps, mAreaMaps;
//	private ListView mListView;
	private NewAddressListAdapter mListAdapter;
//	private PopupWindow mPopupWindow;
	private NewAddressPopupWindow mAddressPopupWindow;
	private Button mSaveBtn;
	private String provinceId = "", cityId = "", areaId = "";
	private String provinceName = "", cityName="", areaName = "";
	private boolean isSave = true;
	private ImageView mBackImage;
	private TextView mAddressManageText;
//	private LinearLayout mProvinceLayout;
	private CheckBox mCheckBox;
	private String isDefault = "true";
	private HashMap<String, Object> mAddressMap;
	public static final String action = "address.default.broadcast";
//	private ExecutorService mExecutorService = Executors.newCachedThreadPool();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_new_user_address_layout);
		setImmerseLayout(findViewById(R.id.head_layout));
		ExitApplication.getInstance().addActivity(this);
		// mProvinces = getResources().getStringArray(R.array.provinces);
		initProvinceDatas();
		init();
	}

	private void init() {
		mUserNameEdit = (EditText) findViewById(R.id.new_address_username_edit);
		mUserPhoneEdit = (EditText) findViewById(R.id.new_address_phone_edit);
		mProvinceText = (TextView) findViewById(R.id.new_address_province_edit);
		mCityText = (TextView) findViewById(R.id.new_address_city_edit);
		mAreaText = (TextView) findViewById(R.id.new_address_area_edit);
		mAddressEdit = (EditText) findViewById(R.id.new_address_address_edit);
		mSaveBtn = (Button) findViewById(R.id.new_address_save_btn);
		mBackImage = (ImageView) findViewById(R.id.add_new_address_back_info_image);
		mAddressManageText = (TextView) findViewById(R.id.add_new_address_manager_text);
		mCheckBox = (CheckBox) findViewById(R.id.address_default_check);

		mProvinceText.setOnClickListener(this);
		mCityText.setOnClickListener(this);
		mAreaText.setOnClickListener(this);
		mSaveBtn.setOnClickListener(this);
		mBackImage.setOnClickListener(this);
		mAddressManageText.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// LayoutInflater inflater =
		// LayoutInflater.from(getApplicationContext());
		// View view;
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.add_new_address_manager_text:
			intent.setClass(getApplicationContext(),
					NewAddressManageActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.add_new_address_back_info_image:
			finish();
			break;
		// 保存收货地址
		case R.id.new_address_save_btn:
			if (mCheckBox.isChecked()) {
				isDefault = "true";
			} else {
				isDefault = "false";
			}
			if (!(mUserNameEdit.getText().toString().equals(""))) {
				if (!(mUserPhoneEdit.getText().toString().equals(""))) {
					if (!(mProvinceText.getText().toString().equals(""))) {
						if (!(mAddressEdit.getText().toString().equals(""))) {
							if (isSave) {
								if (!(provinceId.equals(""))) {
									isSave = false;
									mSaveBtn.setText("保存中...");
//									new Thread(setDefaultRun).start();
//									mExecutorService.execute(setDefaultRun);
									addressHttp();
								} else {
									Toast.makeText(getApplicationContext(),
											"请选择省份！", Toast.LENGTH_SHORT)
											.show();
								}
							}
						} else {
							Toast.makeText(getApplicationContext(), "请填写详细地址！",
									Toast.LENGTH_SHORT).show();
						}
					} else {
						Toast.makeText(getApplicationContext(), "请选择省份！",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(getApplicationContext(), "请填写收货人电话！",
							Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(getApplicationContext(), "请填写收货人姓名！",
						Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.new_address_province_edit:
			View view = getWindow().peekDecorView();
			if (view != null) {
				InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
			}
			if (mProvinDatasMap != null && mProvinDatasMap.length > 0) {
				mListAdapter = new NewAddressListAdapter(
						getApplicationContext(), mProvinDatasMap);
				mAddressPopupWindow = new NewAddressPopupWindow(
						NewAddressActivity.this, mProcinItemClick, mListAdapter);
				mAddressPopupWindow.showAsDropDown(mProvinceText);

			}
			break;
		case R.id.new_address_city_edit:
			view = getWindow().peekDecorView();
			if (view != null) {
				InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
			}
			if (mCitys != null && mCitys.length > 0) {
				mListAdapter = new NewAddressListAdapter(
						getApplicationContext(), mCitys);
				mAddressPopupWindow = new NewAddressPopupWindow(
						NewAddressActivity.this, mCityItemClick, mListAdapter);
				mAddressPopupWindow.showAsDropDown(mCityText);
			}
			break;
		case R.id.new_address_area_edit:
			view = getWindow().peekDecorView();
			if (view != null) {
				InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
			}
			if (mAreas != null && mAreas.length > 0) {

				mListAdapter = new NewAddressListAdapter(
						getApplicationContext(), mAreas);
				mAddressPopupWindow = new NewAddressPopupWindow(
						NewAddressActivity.this, mAreaItemClick, mListAdapter);
				mAddressPopupWindow.showAsDropDown(mAreaText);
			}
			break;
		}
	}

	OnItemClickListener mProcinItemClick = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			mAddressPopupWindow.dismiss();

			mCitys = mCitisDatasMap.get(mProvinDatasMap[position]);
			mCityMaps = mCitysAllMap.get(mProvinDatasMap[position]);
			mProvinceText.setText(mProvinDatasMap[position]);
			provinceId = mProvinceAllMap.get(mProvinDatasMap[position]);
			provinceName = mProvinDatasMap[position];
			if (mCitys != null && mCitys.length > 0) {
				mAreas = mDistrictDatasMap.get(mCitys[0]);
				mAreaMaps = mDistrictAllMap.get(mCitys[0]);
				mCityText.setText(mCitys[0]);

				mAreaText.setText(mAreas[0]);

				areaName = mAreas[0];
				cityName = mCitys[0];
				areaId = mAreaMaps.get(mAreas[0]);
				cityId = mCityMaps.get(mCitys[0]);

			}else{
				mCityText.setText("");

				mAreaText.setText("");
			}
			// provinceId =
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
			mAddressPopupWindow.dismiss();
			mAreas = mDistrictDatasMap.get(mCitys[position]);
			mAreaMaps = mDistrictAllMap.get(mCitys[position]);
			mCityText.setText(mCitys[position]);
			if (mAreas != null && mAreas.length > 0) {
				mAreaText.setText(mAreas[0]);
				areaId = mAreaMaps.get(mAreas[0]);
			}
			
			cityId = mCityMaps.get(mCitys[position]);
			cityName = mCitys[position];
			if(mAreas!=null && mAreas.length>0){
			areaId = mAreaMaps.get(mAreas[0]);
			areaName = mAreas[0];
			}
			Log.i("city", "cityName:" + cityName + "===cityId:" + cityId);
		}
	};
	OnItemClickListener mAreaItemClick = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			mAddressPopupWindow.dismiss();
			mAreaText.setText(mAreas[position]);
			areaId = mAreaMaps.get(mAreas[position]);
			areaName = mAreas[position];
			Log.i("mAreas", "areaName:" + areaName + "=========areaId:"
					+ areaId);
		}
	};

	private void addressHttp(){
		RequestParams params = new RequestParams();
		params.put("uid", Constants.mId);
		params.put("realname", mUserNameEdit.getText().toString());
		params.put("mobile", mUserPhoneEdit.getText().toString());
		params.put("pname", provinceName);
		params.put("pid", provinceId);
		if(!cityName.equals("")){
			params.put("cname", cityName);
			params.put("cid", cityId);
		}
		if(!areaName.equals("")){
			params.put("aname", areaName);
			params.put("aid", areaId);
		}
		params.put("isdefault", isDefault+"");
		params.put("address", mAddressEdit.getText().toString());
		AsyUtils.get(Constants.setDefaultAddress2, params, new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(JSONObject response) {
				try {
					mAddressMap = Constants.getJson2Object(response.toString());
					Log.i("mAddressMap", mAddressMap+"===================");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (mAddressMap != null && mAddressMap.size() > 0) {
					if (mAddressMap.get("Code").equals("200")) {
						handler.sendEmptyMessageDelayed(1, 0);
					} else {
						handler.sendEmptyMessageDelayed(2, 0);
					}
				} else {
					handler.sendEmptyMessageDelayed(2, 0);
				}
				super.onSuccess(response);
			}
			@Override
			public void onFailure(Throwable e, JSONObject errorResponse) {
				handler.sendEmptyMessageDelayed(2, 0);
				super.onFailure(e, errorResponse);
			}
		});
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Intent intent = new Intent();
			switch (msg.what) {
			case 1:
				if (isDefault.equals("true")) {
					intent = new Intent(action);
					intent.putExtra("data", "1");
					if (mAddressMap.get("Data") != null) {
						intent.putExtra("userName", mUserNameEdit.getText()
								.toString());
						intent.putExtra("userPhone", mUserPhoneEdit.getText()
								.toString());
						intent.putExtra("userAddress", mAddressEdit.getText()
								.toString());

						intent.putExtra("addressId", mAddressMap.get("Data")
								.toString());
					}
					sendBroadcast(intent);
				}
				intent = new Intent();
				isSave = true;
				mSaveBtn.setText(getResources().getString(R.string.save));
				Toast.makeText(getApplicationContext(), "地址保存成功！",
						Toast.LENGTH_SHORT).show();
				intent.setClass(getApplicationContext(),
						NewAddressManageActivity.class);
				startActivity(intent);
				finish();
				break;

			case 2:
				isSave = true;
				Toast.makeText(getApplicationContext(), "操作失败！",
						Toast.LENGTH_SHORT).show();
				break;
			}
		};
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
	protected void setImmerseLayout(View view) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			//透明状态栏
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//			//透明导航栏
//			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

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
