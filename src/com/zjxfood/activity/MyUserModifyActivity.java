package com.zjxfood.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.umeng.analytics.MobclickAgent;
import com.zjxfood.application.ExitApplication;
import com.zjxfood.common.Constants;
import com.zjxfood.http.AsyUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
public class MyUserModifyActivity extends AppActivity implements OnClickListener {

	private RelativeLayout mHeadLayout;
	private ImageView mBackImage;
	private EditText mUserName;
	private Button mModifyBtn;
	private ArrayList<HashMap<String, Object>> mList;
	private HashMap<String, Object> mMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_user_modify_layout);
		setImmerseLayout(findViewById(R.id.head_layout));
		ExitApplication.getInstance().addActivity(this);
		ExitApplication.getInstance().addMyAccountActivity(this);
		init();
	}

	private void init() {
		mHeadLayout = (RelativeLayout) findViewById(R.id.title_my_user_modify_id);
		mBackImage = (ImageView) mHeadLayout
				.findViewById(R.id.my_user_modify_image);
		mUserName = (EditText) findViewById(R.id.my_user_modify_edit);
		mUserName.setText(Constants.mRealname);
		mModifyBtn = (Button) findViewById(R.id.my_user_modify_btn);

		mBackImage.setOnClickListener(this);
		mModifyBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.my_user_modify_image:
			finish();
			break;

		case R.id.my_user_modify_btn:
			if (!(mUserName.getText().toString().equals(""))) {
//				new Thread(runnable).start();
				moditfyUser();
			} else {
				Toast.makeText(getApplicationContext(), "名字不能为空！",
						Toast.LENGTH_SHORT).show();
			}
			break;
		}
	}

//	Runnable runnable = new Runnable() {
//		@Override
//		public void run() {
//			try {
//				String time = System.currentTimeMillis() + "";
//				String sign = Constants.sortsStr("userid=" + Constants.mId
//						+ "&username=" + mUserName.getText().toString()
//						+ "&timestamp=" + time);
//				String json = Constants.modifyUserName
//						+ sign
//						+ "&userid="
//						+ Constants.mId
//						+ "&username="
//						+ URLEncoder.encode(mUserName.getText().toString(),
//								"UTF-8") + "&timestamp=" + time;
//				String res = ReadJson.readParse(json);
//				mList = getJsonList(res);
//				if (URLDecoder.decode(mList.get(0).get("Realname").toString(),
//						"UTF-8").equals(mUserName.getText().toString())) {
//					handler.sendEmptyMessageDelayed(1, 0);
//				} else {
//					Toast.makeText(getApplicationContext(), "操作失败！",
//							Toast.LENGTH_SHORT).show();
//				}
//
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//	};

	private void moditfyUser() {
		RequestParams params = new RequestParams();
		params.put("userid", Constants.mId);
		params.put("username",mUserName.getText().toString());
		AsyUtils.get(Constants.modifyUserName2, params, new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(JSONObject response) {
				try {
					mMap = Constants.getJsonObject(response.toString());
					if (URLDecoder.decode(mMap.get("Realname").toString(),
							"UTF-8").equals(mUserName.getText().toString())) {
						handler.sendEmptyMessageDelayed(1, 0);
					} else {
						Toast.makeText(getApplicationContext(), "操作失败！",
								Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Log.i("mMap", mMap+"==============");
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
			switch (msg.what) {
			case 1:
				Constants.mRealname = mUserName.getText().toString();
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), MyNewActivity.class);
				startActivity(intent);
				ExitApplication.getInstance().finishMyAccount();
				break;

			case 2:
				Toast.makeText(getApplicationContext(), "修改失败", Toast.LENGTH_SHORT).show();
				break;
			}
		};
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

	public ArrayList<HashMap<String, Object>> getJsonList(String json)
			throws JSONException {
		JSONObject jsonObject = new JSONObject(json);
		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();

		HashMap<String, Object> map = new HashMap<String, Object>();

		map.put("Realname", jsonObject.getString("Realname"));
		list.add(map);

		return list;
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
