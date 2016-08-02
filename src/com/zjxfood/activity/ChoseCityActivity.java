package com.zjxfood.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.umeng.analytics.MobclickAgent;
import com.zjxfood.adapter.SortAdapter;
import com.zjxfood.application.ExitApplication;
import com.zjxfood.common.Constants;
import com.zjxfood.http.AsyUtils;
import com.zjxfood.view.CharacterParser;
import com.zjxfood.view.PinyinComparator;
import com.zjxfood.view.SortModel;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 选择城市
 * @author zjx
 *
 */
public class ChoseCityActivity extends AppActivity implements OnClickListener {

	private ListView mListView;
	private EditText mClearEditText;
	private SortAdapter adapter;
	/**
	 * 汉字转换成拼音的类
	 */
	private CharacterParser characterParser;
	private List<SortModel> SourceDateList;
	/**
	 * 根据拼音来排列ListView里面的数据类
	 */
	private PinyinComparator pinyinComparator;
	private String[] mCityList;
	private ListView mListView2;
	private Button mBeijingBtn, mShanghai, mTianjing;
	private Button mShenzhen, mChengdu, mChongqin;
	private String[] mCityCodeList;
	private ImageView mBackImage;
	private TextView mTitleText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chose_city_layout);
		setImmerseLayout(findViewById(R.id.head_layout));
		ExitApplication.getInstance().addActivity(this);
		init();
		mClearEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int arg1, int arg2,
					int arg3) {
				// 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
				filterData(s.toString());
				mListView.setVisibility(View.VISIBLE);
				mListView2.setVisibility(View.GONE);
				if (mClearEditText.getText().toString().equals("")) {
					mListView.setVisibility(View.GONE);
					mListView2.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}

			@Override
			public void afterTextChanged(Editable arg0) {
			}
		});
		mListView.setOnItemClickListener(mItemClickListener);
//		new Thread(runnable).start();
		getCity();
	}

	private void init() {
		mListView = (ListView) findViewById(R.id.chose_city_list);
		mClearEditText = (EditText) findViewById(R.id.chose_city_search);
		// 实例化汉字转拼音类
		characterParser = CharacterParser.getInstance();

		pinyinComparator = new PinyinComparator();
		mListView2 = (ListView) findViewById(R.id.chose_city_list2);

		mBeijingBtn = (Button) findViewById(R.id.hot_city_beijing);
		mShanghai = (Button) findViewById(R.id.hot_city_shanghai);
		mTianjing = (Button) findViewById(R.id.hot_city_tianjing);
		mShenzhen = (Button) findViewById(R.id.hot_city_shenzhen);
		mChengdu = (Button) findViewById(R.id.hot_city_chengdu);
		mChongqin = (Button) findViewById(R.id.hot_city_chongqing);
//		mBackImage = (ImageView) findViewById(R.id.title_back_image);
		mBackImage = (ImageView) findViewById(R.id.title_back_image);
		mTitleText = (TextView) findViewById(R.id.title_text);
		mTitleText.setText("选择区域");

		mBeijingBtn.setOnClickListener(this);
		mShanghai.setOnClickListener(this);
		mTianjing.setOnClickListener(this);
		mShenzhen.setOnClickListener(this);
		mChengdu.setOnClickListener(this);
		mChongqin.setOnClickListener(this);
		mBackImage.setOnClickListener(this);
	}

	/**
	 * 为ListView填充数据
	 * 
	 * @param date
	 * @return
	 */
	private List<SortModel> filledData(String[] date) {
		List<SortModel> mSortList = new ArrayList<SortModel>();

		for (int i = 0; i < date.length; i++) {
			SortModel sortModel = new SortModel();
			sortModel.setName(date[i]);
			// 汉字转换成拼音
			String pinyin = characterParser.getSelling(date[i]);
			String sortString = pinyin.substring(0, 1).toUpperCase();

			// 正则表达式，判断首字母是否是英文字母
			if (sortString.matches("[A-Z]")) {
				sortModel.setSortLetters(sortString.toUpperCase());
			} else {
				sortModel.setSortLetters("#");
			}
			mSortList.add(sortModel);
		}
		return mSortList;
	}

	/**
	 * 根据输入框中的值来过滤数据并更新ListView
	 * 
	 * @param filterStr
	 */
	private void filterData(String filterStr) {
		try{
		List<SortModel> filterDateList = new ArrayList<SortModel>();

		if (TextUtils.isEmpty(filterStr)) {
			filterDateList = SourceDateList;
		} else {
			filterDateList.clear();
			if(SourceDateList!=null && SourceDateList.size()>0){
			for (SortModel sortModel : SourceDateList) {
				String name = sortModel.getName();
				if(!Constants.isNull(name) && !Constants.isNull(filterStr.toString())){
				if (name.indexOf(filterStr.toString()) != -1
						|| characterParser.getSelling(name).startsWith(
								filterStr.toString())) {
					filterDateList.add(sortModel);
				}
				}
			}
			}
		}
		// 根据a-z进行排序
		if(filterDateList!=null && filterDateList.size()>0){
		Collections.sort(filterDateList, pinyinComparator);
		adapter.updateListView(filterDateList);
		}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	private void getCity(){
		RequestParams params = new RequestParams();
		params.put("city", "1");
		AsyUtils.get(Constants.getAllCity2, params, new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(JSONArray response) {
				Constants.mAllCityList = Constants.getJsonArray(response.toString());
				if(Constants.mAllCityList!=null && Constants.mAllCityList.size()>0){
					for (int i = 0; i < Constants.mAllCityList.size(); i++) {
						if (Constants.mAllCityList.get(i).get("Id").toString().equals("1")) {
							Constants.mAllCityList.remove(i);
						}
						if (Constants.mAllCityList.get(i).get("Id").toString().equals("2")) {
							Constants.mAllCityList.remove(i);
						}
						if (Constants.mAllCityList.get(i).get("Id").toString().equals("3")) {
							Constants.mAllCityList.remove(i);
						}
						if (Constants.mAllCityList.get(i).get("Id").toString().equals("4")) {
							Constants.mAllCityList.remove(i);
						}
					}
				}
				handler.sendEmptyMessageDelayed(1, 0);
				super.onSuccess(response);
			}
		});
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				mCityList = new String[Constants.mAllCityList.size()];
				mCityCodeList = new String[Constants.mAllCityList.size()];
				for (int i = 0; i < Constants.mAllCityList.size(); i++) {
					mCityList[i] = Constants.mAllCityList.get(i).get("Cityname")
							.toString();
					mCityCodeList[i] = Constants.mAllCityList.get(i).get("Cityid")
							.toString();
				}
				
				SourceDateList = filledData(mCityList);
				// 根据a-z进行排序源数据
				Collections.sort(SourceDateList, pinyinComparator);
				adapter = new SortAdapter(ChoseCityActivity.this,
						SourceDateList);
				mListView.setAdapter(adapter);
				mListView2.setAdapter(adapter);
				mListView.setOnItemClickListener(mItemClickListener);
				mListView2.setOnItemClickListener(mItemClickListener);
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
			intent.setClass(getApplicationContext(), NewMainActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("city",
					((SortModel) adapter.getItem(position)).getName());
			
			for(int i=0;i<Constants.mAllCityList.size();i++){
				if((((SortModel) adapter.getItem(position)).getName()).equals(Constants.mAllCityList.get(i).get("Cityname").toString())){
					NewMainActivity.mCityCode = Constants.mAllCityList.get(i).get("Cityid").toString();
					bundle.putString("cityId",Constants.mAllCityList.get(i).get("Cityid").toString());
					Log.i("cityId", Constants.mAllCityList.get(i).get("Cityid").toString()+"=======cityId==========");
				}
			}
			
			intent.putExtras(bundle);
			startActivity(intent);
			mClearEditText.setText(((SortModel) adapter.getItem(position))
					.getName());
			finish();
		}
	};

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		switch (v.getId()) {
		case R.id.hot_city_beijing:
			intent.setClass(getApplicationContext(), NewMainActivity.class);
			bundle.putString("city", "北京市");
			NewMainActivity.mCityCode = "110000";
			bundle.putString("cityId", "110000");
			intent.putExtras(bundle);
			startActivity(intent);

			break;

		case R.id.hot_city_shanghai:
			intent.setClass(getApplicationContext(), NewMainActivity.class);
			bundle.putString("city", "上海市");
			NewMainActivity.mCityCode = "310000";
			bundle.putString("cityId", "310000");
			intent.putExtras(bundle);
			startActivity(intent);
			break;
		case R.id.hot_city_tianjing:
			intent.setClass(getApplicationContext(), NewMainActivity.class);
			bundle.putString("city", "天津市");
			bundle.putString("cityId", "120000");
			NewMainActivity.mCityCode = "120000";
			intent.putExtras(bundle);
			startActivity(intent);

			break;
		case R.id.hot_city_shenzhen:
			intent.setClass(getApplicationContext(), NewMainActivity.class);
			bundle.putString("city", "深圳市");
			bundle.putString("cityId", "440300");
			NewMainActivity.mCityCode = "440300";
			intent.putExtras(bundle);
			startActivity(intent);
			break;
		case R.id.hot_city_chengdu:
			intent.setClass(getApplicationContext(), NewMainActivity.class);

			bundle.putString("city", "成都市");
			bundle.putString("cityId", "510100");
			NewMainActivity.mCityCode = 510100+"";
			intent.putExtras(bundle);
			startActivity(intent);
			break;
		case R.id.hot_city_chongqing:
			intent.setClass(getApplicationContext(), NewMainActivity.class);
			bundle.putString("city", "重庆市");
			bundle.putString("cityId", "500000");
			NewMainActivity.mCityCode = "500000";
			intent.putExtras(bundle);
			startActivity(intent);
			break;
		case R.id.title_back_image:
			finish();
			break;
		}
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		System.gc();
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
