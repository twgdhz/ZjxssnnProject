package com.zjxfood.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.zjxfood.adapter.LogisticsListAdapter;
import com.zjxfood.application.ExitApplication;
import com.zjxfood.common.Constants;
import com.zjxfood.http.ReadJson;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

public class LogisticsActivity extends AppActivity implements OnClickListener {

	private ListView mListView;
	private LogisticsListAdapter mLogisticsListAdapter;
//	private ImageView mBackImage;
	private Bundle mBundle;
	private String fh = "";
	// private int x = 1;
	private HashMap<String, String> mHashMap;
	private ArrayList<HashMap<String, Object>> mList;
	private RunTask mRunTask;
	private TextView mNameText;
	private TextView mOrdeText;
	private TextView mHeadText;
	private TextView mFootText;
	private ImageView mBackImage;//返回按钮
	private TextView mTitleText;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.logistics_details_layout);
		setImmerseLayout(findViewById(R.id.head_layout));
		ExitApplication.getInstance().addActivity(this);
		init();
		mBundle = getIntent().getExtras();
		if (mBundle != null) {
			fh = mBundle.getString("fh");
			Log.i("fh", fh + "==============");
		}
		// fh = "中通快递%20:768391227151";
		if (!(fh.equals(""))) {
			mRunTask = new RunTask();
			mRunTask.execute("");
			// getDetail();
		}
	}

	private void init() {
		mListView = (ListView) findViewById(R.id.logistics_detail_list);
//		mBackImage = (ImageView) findViewById(R.id.title_back_image);
		mNameText = (TextView) findViewById(R.id.logistics_detail_name_text);
		mOrdeText = (TextView) findViewById(R.id.logistics_detail_order_text);
		mBackImage = (ImageView) findViewById(R.id.title_back_image);
		mTitleText = (TextView) findViewById(R.id.title_text);
		mTitleText.setText("物流详情");
		
		mBackImage.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_back_image:
			finish();
			break;
		}
	}

	class RunTask extends AsyncTask<String, Integer, String> {
		@Override
		protected String doInBackground(String... arg0) {
			String time = System.currentTimeMillis() + "";
			String str = "fh=" + fh + "&timestamp=" + time;
			String sign = Constants.sortsStr(str);
			String res = "";
			try {
				res = URLEncoder.encode(fh, "UTF-8");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			//
			try {
				String json = ReadJson.readParse(Constants.lookup + sign
						+ "&fh=" + res + "&timestamp=" + time);
				Log.i("json", json + "=======================");
				mHashMap = getJsonLists(json);
			} catch (Exception e) {
				e.printStackTrace();
			}
			handler.sendEmptyMessageDelayed(1, 0);
			return null;
		}
	}

	// private void getDetail() {
	// StringBuilder sb = new StringBuilder();
	// sb.append("fh=" + fh);
	// XutilsUtils.get(Constants.lookup2, sb,
	// new RequestCallBack<String>() {
	// @Override
	// public void onFailure(HttpException arg0, String arg1) {
	// }
	// @Override
	// public void onSuccess(ResponseInfo<String> res) {
	// try {
	// mHashMap = Constants.getJsonObject(res.result);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// handler.sendEmptyMessageDelayed(1, 0);
	// }
	// });
	// }

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				if (mHashMap != null && mHashMap.size() > 0) {
					if (mHashMap.get("status").equals("200")) {
						if (mHashMap.get("data") != null) {
							mList = Constants.getJsonArray(mHashMap.get("data")
									.toString());
						}
						mNameText.setText("信息来源：" + mHashMap.get("name"));
						mOrdeText.setText("订单编号：" + mHashMap.get("nu"));
						if (mList != null && mList.size() > 0) {
							LayoutInflater inflater = LayoutInflater
									.from(getApplicationContext());
							View headView = inflater.inflate(
									R.layout.logistics_list_head_item, null);
							mHeadText = (TextView) headView
									.findViewById(R.id.logistics_head_item_content_text);
							View footView = inflater.inflate(
									R.layout.logistics_foot_item, null);
							mFootText = (TextView) footView
									.findViewById(R.id.logistics_foot_item_content_text);
							mLogisticsListAdapter = new LogisticsListAdapter(
									getApplicationContext(), mList);
							mListView.addHeaderView(headView);
							mListView.setAdapter(mLogisticsListAdapter);
							mListView.addFooterView(footView);
							mHeadText.setText(mList.get(0).get("context")
									+ "\n" + mList.get(0).get("time"));
							mFootText.setText(mList.get(mList.size() - 1).get(
									"context")
									+ "\n"
									+ mList.get(mList.size() - 1).get("time"));
						}
					}
				}
				break;

			case 2:
				break;
			}
		};
	};

	public HashMap<String, String> getJsonLists(String json)
			throws JSONException {
		JSONObject jsonObject = new JSONObject(json);
		// ArrayList<HashMap<String, Object>> list = new
		// ArrayList<HashMap<String, Object>>();
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("name", jsonObject.getString("name"));
		map.put("com", jsonObject.getString("com"));
		map.put("nu", jsonObject.getString("nu"));
		map.put("state", jsonObject.getString("state"));
		map.put("status", jsonObject.getString("status"));
		map.put("message", jsonObject.getString("message"));

		map.put("data", jsonObject.getString("data"));
		return map;
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
