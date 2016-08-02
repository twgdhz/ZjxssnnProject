package com.zjxfood.activity;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.umeng.analytics.MobclickAgent;
import com.zjxfood.adapter.NewAddressManageListAdapter;
import com.zjxfood.application.ExitApplication;
import com.zjxfood.cashindiana.CashMyIdaCreateOrderInfo;
import com.zjxfood.common.Constants;
import com.zjxfood.delete.list.ActionSheet;
import com.zjxfood.delete.list.ActionSheet.OnActionSheetSelected;
import com.zjxfood.delete.list.DelSlideListView;
import com.zjxfood.delete.list.ListViewonSingleTapUpListenner;
import com.zjxfood.delete.list.OnDeleteListioner;
import com.zjxfood.http.AsyUtils;
import com.zjxfood.indiana.MyIdaCreateOrderInfo;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;

public class NewAddressManageActivity extends AppActivity implements
		OnDeleteListioner, ListViewonSingleTapUpListenner,
		OnActionSheetSelected, OnCancelListener {

	private DelSlideListView mListView;
	private ArrayList<HashMap<String, Object>> mList;
	private NewAddressManageListAdapter mListAdapter;
	int delID = 0;
	private Button mAddNewBtn;
	private ImageView mBackImage;
	private Bundle mBundle;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_address_management_layout);
		setImmerseLayout(findViewById(R.id.head_layout));
		ExitApplication.getInstance().addActivity(this);
		init();
		mBundle = getIntent().getExtras();
		if (mBundle != null) {
//			flag = mBundle.getString("flag");
		}
		getUserAddress();
	}

	private void init() {
		mListView = (DelSlideListView) findViewById(R.id.new_address_manage_list);
		mAddNewBtn = (Button) findViewById(R.id.address_management_add_btn);
		mBackImage = (ImageView) findViewById(R.id.address_management_back_info_image);
		mBackImage.setOnClickListener(mBackClick);

		mAddNewBtn.setOnClickListener(mAddNewClick);

	}

	private void getUserAddress() {
		Log.i("getUserAddress", "============getUserAddress==============");
		RequestParams params = new RequestParams();
		params.put("userid", Constants.mId);
		AsyUtils.get(Constants.getAddress2, params,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONArray response) {
						mList = Constants.getJsonArray(response.toString());
						handler.sendEmptyMessageDelayed(1, 0);
						super.onSuccess(response);
					}
				});
	}

	android.view.View.OnClickListener mBackClick = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			finish();
		}
	};

	OnClickListener mAddNewClick = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), NewAddressActivity.class);
			startActivity(intent);
			finish();
		}
	};

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				mListAdapter = new NewAddressManageListAdapter(
						getApplicationContext(), mList);
				mListView.setAdapter(mListAdapter);
				mListView.setDeleteListioner(NewAddressManageActivity.this);
				mListView
						.setSingleTapUpListenner(NewAddressManageActivity.this);
				mListAdapter
						.setOnDeleteListioner(NewAddressManageActivity.this);
				mListView.setOnItemClickListener(mItemClickListener);
				break;

			default:
				break;
			}
		};
	};

	OnItemClickListener mItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			Intent intent = new Intent();
			if (MallDetailActivity.mFlag.equals("1")) {
//				intent.setClass(getApplicationContext(),
//						MallChoseAddressActivity.class);
				intent.setClass(getApplicationContext(),
						NewMallCreateOrderActivity.class);
				intent.putExtra("flag", "2");
				intent.putExtra("userName", mList.get(position).get("Realname").toString());
				intent.putExtra("userPhone", mList.get(position).get("Mobile").toString());
				intent.putExtra("userAddress", mList.get(position).get("Address").toString());
				intent.putExtra("addressId", mList.get(position).get("Id").toString());
			} else if (MallDetailActivity.mFlag.equals("2")) {
				intent.setClass(getApplicationContext(),
						MyAuctionOrderItemActivity.class);
			}else if (MallDetailActivity.mFlag.equals("3")) {
				intent.setClass(getApplicationContext(),
						MyIdaCreateOrderInfo.class);
			}else if (MallDetailActivity.mFlag.equals("4")) {
				intent.setClass(getApplicationContext(),
						CashMyIdaCreateOrderInfo.class);
			}
			Bundle bundle = new Bundle();
			bundle.putString("flag", "2");
			bundle.putString("userName", mList.get(position).get("Realname")
					.toString());
			bundle.putString("userPhone", mList.get(position).get("Mobile")
					.toString());
			bundle.putString("userAddress", mList.get(position).get("Address")
					.toString());
			bundle.putString("addressId", mList.get(position).get("Id")
					.toString());
			intent.putExtras(bundle);
			startActivity(intent);
			finish();
		}
	};

	@Override
	public void onCancel(DialogInterface arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSingleTapUp() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isCandelete(int position) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void onDelete(int ID) {
		delID = ID;
		Log.i("deleteid", mList.get(ID).get("Address") + "========"
				+ mList.get(ID).get("Id"));
		ActionSheet.showSheet(this, this, this, mList.get(ID).get("Id")
				.toString());
	}

	@Override
	public void onBack() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(int whichButton) {
		switch (whichButton) {
		case 0:
//			if
			try{
			mList.remove(delID);
			mListView.deleteItem();
			mListAdapter.notifyDataSetChanged();
			}catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case 1:

			break;
		default:
			break;
		}
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
