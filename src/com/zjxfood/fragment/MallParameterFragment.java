package com.zjxfood.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.umeng.analytics.MobclickAgent;
import com.zjxfood.activity.R;
import com.zjxfood.adapter.MallDetailGridAdapter;
import com.zjxfood.adapter.MallParameterAdapter;
import com.zjxfood.common.Constants;
import com.zjxfood.http.AsyUtils;
import com.zjxfood.view.MyScrollListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

@SuppressLint("NewApi")
public class MallParameterFragment extends Fragment implements OnClickListener {
	private MyScrollListView mListView;
	private MallParameterAdapter mAdapter;
	private String mGid = "";
	private ArrayList<HashMap<String, Object>> mList;
	private TextView mNotText;
	private TextView mHeadText;
	private GridView mGridView;
	private ArrayList<HashMap<String, Object>> mSizeList;
	private MallDetailGridAdapter mGridAdapter;
	private int mSizePosition;
	private String mSizeId = "";
	public static final String action = "mall.parameter.broadcast";
//	public MallParameterFragment(String gid) {
//		this.mGid = gid;
//	}

	public MallParameterFragment() {
		super();
	}
	public static MallIntroductionFragment newInstance(Context context,String gid) {
		MallIntroductionFragment f = new MallIntroductionFragment();

		Bundle bundle = new Bundle();
		bundle.putString("gid", gid);
		f.setArguments(bundle);
		return f;
	}
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.mall_detail_parameter_layout,
				null);
		initView(view);
		if(mGid!=null){
		getMallValue();
		getSizeHttp();
		}
		SharedPreferences sp = getActivity().getApplicationContext()
				.getSharedPreferences("商品参数", getActivity().MODE_PRIVATE);
		if(sp!=null && sp.getString("Content", "")!=null && !sp.getString("Content", "").toString().equals("")){
			mHeadText.setText(sp.getString("Content", ""));
		}
				
		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle args = getArguments();
		if (args != null) {
			mGid = args.getString("gid");
		}
	}

	private void initView(View view) {
		mListView = (MyScrollListView) view
				.findViewById(R.id.mall_detail_parameter_list);
		mNotText = (TextView) view.findViewById(R.id.mall_detail_parameter_not_text);
		mHeadText = (TextView) view.findViewById(R.id.mall_detail_head_text);
		mGridView = (GridView) view.findViewById(R.id.mall_detail_parameter_grid);
	}


	@Override
	public void onClick(View arg0) {
	}
	private void getMallValue() {
		Log.i("getMallValue", "==============");
		RequestParams params = new RequestParams();
		params.put("gid", mGid);
		AsyUtils.get(Constants.getMallValue, params,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONObject response) {
						Log.i("JSONArray", response + "=============");
						try {
							mList = Constants.getJsonArrayByData(response.toString());
						} catch (JSONException e) {
							e.printStackTrace();
						}
							handler.sendEmptyMessageDelayed(1, 0);
						
						super.onSuccess(response);
					}
				});
	}
	
	private void getSizeHttp(){
		RequestParams params = new RequestParams();
		params.put("gid", mGid);
		AsyUtils.get(Constants.getMallSize2, params, new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(JSONArray response) {
				mSizeList = Constants.getJsonArray(response.toString());
				Log.i("onSuccess", mSizeList+"===============");
				handler.sendEmptyMessageDelayed(2, 0);
				super.onSuccess(response);
			}
		});
	}
	
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				if(mList!=null && mList.size()>0){
					mNotText.setVisibility(View.GONE);
					mHeadText.setVisibility(View.VISIBLE);
					mAdapter = new MallParameterAdapter(getActivity(), mList);
					mListView.setAdapter(mAdapter);
					}else{
						mNotText.setVisibility(View.VISIBLE);
						mHeadText.setVisibility(View.GONE);
					}
				break;

			case 2:
				mGridAdapter = new MallDetailGridAdapter(getActivity(), mSizeList);
				mGridView.setAdapter(mGridAdapter);
				mGridView.setOnItemClickListener(mOnItemClickListener);
				break;
			}
		};
	};

	// Grid监听事件
	OnItemClickListener mOnItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			mSizePosition = position;
			mGridAdapter.setSeclection(position);
			mGridAdapter.notifyDataSetChanged();
			mSizeId = mSizeList.get(position).get("Id").toString();
			Intent intent = new Intent(action);
			intent.putExtra("sizeId", mSizeId);
			intent.putExtra("mSizePosition", mSizePosition);
			intent.putExtra("list",  (Serializable)mSizeList);
			intent.putExtra("data", "2");
			getActivity().sendBroadcast(intent);
			Log.i("sizeId", mSizeId + "=======选择尺码=======");
		}
	};
	
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("MainScreen"); // 统计页面
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("MainScreen");
	}
}
