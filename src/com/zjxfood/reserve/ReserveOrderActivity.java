package com.zjxfood.reserve;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.project.util.DensityUtils;
import com.umeng.analytics.MobclickAgent;
import com.zjxfood.activity.R;
import com.zjxfood.adapter.ReserveOrderListAdapter;
import com.zjxfood.adapter.ReserveOrderListAdapter2;
import com.zjxfood.application.ExitApplication;
import com.zjxfood.common.Constants;
import com.zjxfood.http.AsyUtils;
import com.zjxfood.interfaces.MyInterfaceImpl;
import com.zjxfood.interfaces.MyInterface;
import com.zjxfood.popupwindow.ReserveOrderPopupWindow;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ReserveOrderActivity extends Activity implements OnClickListener {

	private ReserveOrderListAdapter mOrderListAdapter;
	private ListView mListView;
	private Button mConfirmBtn;
	private ImageView mOrderImage;
	private MyInterfaceImpl mMyClick;
	private HashMap<String, ArrayList<HashMap<String, Object>>> mHashMap;
	private HashMap<String, ArrayList<HashMap<String, Object>>> mHashMap2;
	private Bitmap mBitmap;
	private TextView mOrderPrice;
	private ImageView mBackImage;
	private ReserveOrderPopupWindow mOrderPopupWindow;
	private String page = "1";
	private ArrayList<HashMap<String, Object>> mList;
	private ReserveOrderListAdapter2 mListAdapter2;
	private String mPrice = "0";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reserve_order_list_layout);
		ExitApplication.getInstance().addActivity(this);
		ExitApplication.getInstance().addMyCommodityActivity(this);
		init();
		mMyClick = new MyInterfaceImpl();
		mMyClick.setListener(mInterface);
		mOrderImage.setDrawingCacheEnabled(true);
		Resources res = getResources();
		mBitmap = BitmapFactory.decodeResource(res, R.drawable.after_booking);
		getReserveList();
	}

	private void init() {
		mListView = (ListView) findViewById(R.id.reserve_order_list);
		mConfirmBtn = (Button) findViewById(R.id.reserve_order_bottom_chose_btn);
		mOrderImage = (ImageView) findViewById(R.id.reserve_order_bottom_image);
		mOrderPrice = (TextView) findViewById(R.id.reserve_order_bottom_text);
		mBackImage = (ImageView) findViewById(R.id.resever_order_info_image);

		mConfirmBtn.setOnClickListener(this);
		mBackImage.setOnClickListener(this);
		mOrderImage.setOnClickListener(this);
	}

	@SuppressWarnings({ "unchecked", "unchecked" })
	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		//
		// 订单查看
		case R.id.reserve_order_bottom_image:
			if (mHashMap != null && mHashMap.size() > 0) {
				Iterator entries = mHashMap.entrySet().iterator();
				mHashMap2 = new HashMap<String, ArrayList<HashMap<String, Object>>>();
				int i = 0;
				while (entries.hasNext()) {
					Map.Entry entry = (Map.Entry) entries.next();
					mHashMap2.put(i + "",
							(ArrayList<HashMap<String, Object>>) entry
									.getValue());
					i++;
				}
//				Log.i("mHashMap2", mHashMap2.get("1").get(0).get("name")+"==============");
				if (mHashMap2 != null && mHashMap2.size() > 0) {
					mListAdapter2 = new ReserveOrderListAdapter2(
							getApplicationContext(), mHashMap2,mMyClick,Float.parseFloat(mPrice));
					mOrderPopupWindow = new ReserveOrderPopupWindow(
							ReserveOrderActivity.this, mListAdapter2);
					mOrderPopupWindow.showAtLocation(mConfirmBtn,
							Gravity.BOTTOM, 0, 0);
				}
			}
			break;
		// 返回
		case R.id.resever_order_info_image:
			finish();
			break;
		case R.id.reserve_order_bottom_chose_btn:
			if (mConfirmBtn.getText().toString().equals("确定")) {
				intent.setClass(getApplicationContext(),
						ReserveUserActivity.class);
				startActivity(intent);
			} else {

				Toast.makeText(getApplicationContext(), "还未选择菜品",
						Toast.LENGTH_SHORT).show();
			}

			break;

		default:
			break;
		}
	}

	private void getReserveList() {
		RequestParams params = new RequestParams();
		params.put("mobileAppUserType", "1");
		params.put("Mid", "32");
		params.put("pageSize", "12");
		params.put("currentPage", page);
		params.put("orderBy", "0");
		AsyUtils.get(Constants.getReserveList, params,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONObject response) {
						try {
							mList = Constants.getJsonArrayByData(response
									.toString());
						} catch (JSONException e) {
							e.printStackTrace();
						}
						handler.sendEmptyMessageDelayed(2, 0);
						super.onSuccess(response);
					}
				});
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				if (mHashMap.size() > 0) {
					mOrderImage.setImageBitmap(createCornerBitmap(mBitmap,
							mHashMap.size()));
//					mHashMap.get(key)
					mOrderPrice.setText("共：￥"+mPrice);
					mConfirmBtn.setText("确定");
					mConfirmBtn
							.setBackgroundResource(R.drawable.bg_reserve_order_chose_btn_style);
				} else {
					mOrderImage.setImageResource(R.drawable.empty_list_not);
					mOrderPrice.setText("你还没有预定~");
					mConfirmBtn.setText("还没选择");
					mConfirmBtn
							.setBackgroundResource(R.drawable.bg_reserve_order_btn_style);
				}
				break;

			case 2:
				if (mList != null && mList.size() > 0) {
					mOrderListAdapter = new ReserveOrderListAdapter(
							getApplicationContext(), mMyClick, mList);
					mListView.setAdapter(mOrderListAdapter);
					mListView.setOnItemClickListener(mItemClickListener);
				}
				break;
			}
		};
	};

	OnItemClickListener mItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(),
					ReserveListChildActivity.class);
			startActivity(intent);
		}
	};

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}

	private MyInterface mInterface = new MyInterface() {
		@Override
		public void updateOrder(
				HashMap<String, ArrayList<HashMap<String, Object>>> map,float price) {
			mHashMap = map;
			mPrice = price+"";
			Log.i("mHashMap", mHashMap + "==============");
			handler.sendEmptyMessageDelayed(1, 0);
		}

		@Override
		public void onclick(Bitmap bitmap) {
		}
	};

	private Bitmap createCornerBitmap(Bitmap oldBitmap, int num) {
		// 新建画布
		int width = oldBitmap.getWidth();
		int height = oldBitmap.getHeight();
		Bitmap newBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(newBitmap);
		// --->先画原来的图片
		Paint bitmapPaint = new Paint();
		// 防止抖动
		bitmapPaint.setDither(true);
		// 对Bitmap进行滤波处理
		bitmapPaint.setFilterBitmap(true);
		Rect src = new Rect(0, 0, oldBitmap.getWidth(), oldBitmap.getHeight());
		Rect dst = new Rect(0, 0, newBitmap.getWidth(), newBitmap.getHeight());
		canvas.drawBitmap(oldBitmap, src, dst, bitmapPaint);
		// 画圆 设置成数字的背景
		Paint paintCircle = new Paint(); // 设置一个笔刷大小是3的红色的画笔
		paintCircle.setColor(Color.RED);
		paintCircle.setStrokeJoin(Paint.Join.ROUND);
		paintCircle.setStrokeCap(Paint.Cap.ROUND);
		paintCircle.setStrokeWidth(3);
		canvas.drawCircle(
				width / 2 + DensityUtils.dp2px(getApplicationContext(), 35),
				height / 3 - DensityUtils.dp2px(getApplicationContext(), 15),
				DensityUtils.dp2px(getApplicationContext(), 15), paintCircle);
		// --->再画新加的数字
		Paint countPaint = new Paint(Paint.ANTI_ALIAS_FLAG
				| Paint.DEV_KERN_TEXT_FLAG);
		countPaint.setColor(Color.WHITE);
		countPaint.setTextSize(DensityUtils.dp2px(getApplicationContext(), 20));
		countPaint.setTypeface(Typeface.DEFAULT_BOLD);
		canvas.drawText(num + "",
				width / 2 + DensityUtils.dp2px(getApplicationContext(), 30),
				height / 3 - DensityUtils.dp2px(getApplicationContext(), 8),
				countPaint);
		return newBitmap;
	}
}
