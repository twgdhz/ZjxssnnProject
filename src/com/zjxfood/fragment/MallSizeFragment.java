package com.zjxfood.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.umeng.analytics.MobclickAgent;
import com.zjxfood.activity.R;
import com.zjxfood.adapter.MallDetailGridAdapter;
import com.zjxfood.common.Constants;
import com.zjxfood.http.AsyUtils;

import org.json.JSONArray;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

@SuppressLint("NewApi")
public class MallSizeFragment extends Fragment implements OnClickListener {
	private String mGid = "";
	private GridView mGridView;
	public static final String action = "mall.parameter.broadcast";
	private ArrayList<HashMap<String, Object>> mSizeList;
	private MallDetailGridAdapter mGridAdapter;
	private String mSizeId = "";
	private int mSizePosition = 0;
	
//	public MallSizeFragment(String gid) {
//		mGid = gid;
//	}
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
		View view = inflater.inflate(R.layout.mall_detail_size_layout,
				null);
		initView(view);
		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle args = getArguments();
		if (args != null) {
			mGid = args.getString("gid");
		}
		getMallSize();
	}

	private void initView(View view) {
		mGridView = (GridView) view.findViewById(R.id.mall_detail_size_grid);
	}
	
	// 获取尺码
	private void getMallSize() {
		RequestParams params = new RequestParams();
		params.put("gid", mGid);
		if (!(mGid.equals(""))) {
			AsyUtils.get(Constants.getMallSize2, params,
					new JsonHttpResponseHandler() {
						@Override
						public void onSuccess(JSONArray response) {
							Log.i("getMallSize2", response + "==========");
							mSizeList = Constants.getJsonArray(response
									.toString());
							handler.sendEmptyMessageDelayed(1, 0);
							super.onSuccess(response);
						}

					});
		}
	}

	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				mGridAdapter = new MallDetailGridAdapter(getActivity(), mSizeList);
				mGridView.setAdapter(mGridAdapter);
				mGridView.setOnItemClickListener(mOnItemClickListener);
				break;

			default:
				break;
			}
		};
	};
	@Override
	public void onClick(View arg0) {
	}
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
