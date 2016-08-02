package com.zjxfood.activity;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import com.umeng.analytics.MobclickAgent;
import com.zjxfood.model.CityModel;
import com.zjxfood.model.DistrictModel;
import com.zjxfood.model.ProvinceModel;
import com.zjxfood.service.XmlParserHandler;


import android.app.Activity;
import android.content.res.AssetManager;

public class BaseActivity extends Activity {

	/**
	 * 所有省
	 */
	protected String[] mProvinDatasMap;
	protected HashMap<String, String> mProvinceAllMap = new HashMap<String, String>();
	// protected String[] mProvinceDatas;
	/**
	 * key - 省 value - 市
	 */
	protected Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
	protected HashMap<String, HashMap<String, String>> mCitysAllMap = new HashMap<String, HashMap<String,String>>();
	/**
	 * key - 市 values - 区
	 */
	protected Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();
	protected HashMap<String, HashMap<String, String>> mDistrictAllMap = new HashMap<String, HashMap<String,String>>();
	/**
	 * key - 区 values - 邮编
	 */
	protected Map<String, String> mZipcodeDatasMap = new HashMap<String, String>();

	/**
	 * 当前省的名称
	 */
	protected String mCurrentProviceName;
	protected String mCurrentProviceId;
	/**
	 * 当前市的名称
	 */
	protected String mCurrentCityName;
	protected String mCurrentCityId;
	/**
	 * 当前区的名称
	 */
	protected String mCurrentDistrictName = "";
	protected String mCurrentDistrictId = "";

	/**
	 * 当前区的邮政编码
	 */
	protected String mCurrentZipCode = "";

	/**
	 * 解析省市区的XML数据
	 */
	

	protected void initProvinceDatas() {
		List<ProvinceModel> provinceList = null;
		AssetManager asset = getAssets();
		try {
			InputStream input = asset.open("province.xml");
			// 创建一个解析xml的工厂对象
			SAXParserFactory spf = SAXParserFactory.newInstance();
			// 解析xml
			SAXParser parser = spf.newSAXParser();
			XmlParserHandler handler = new XmlParserHandler();
			parser.parse(input, handler);
			input.close();
			// 获取解析出来的数据
			provinceList = handler.getDataList();
			// */ 初始化默认选中的省、市、区
			if (provinceList != null && !provinceList.isEmpty()) {
				mCurrentProviceName = provinceList.get(0).getName();
				List<CityModel> cityList = provinceList.get(0).getCityList();
				if (cityList != null && !cityList.isEmpty()) {
					mCurrentCityName = cityList.get(0).getName();
					List<DistrictModel> districtList = cityList.get(0)
							.getDistrictList();
					mCurrentDistrictName = districtList.get(0).getName();
					mCurrentZipCode = districtList.get(0).getZipcode();
				}
			}
			// */
			// mProvinceDatas = new String[provinceList.size()];
			mProvinDatasMap = new String[provinceList.size()];
			for (int i = 0; i < provinceList.size(); i++) {
				// 遍历所有省的数据
				// mProvinceDatas[i] = provinceList.get(i).getName();
				mProvinDatasMap[i] = provinceList.get(i).getName();
				mProvinceAllMap.put(provinceList.get(i).getName(), provinceList.get(i).getId());
				List<CityModel> cityList = provinceList.get(i).getCityList();
				String[] cityNames = new String[cityList.size()];
				HashMap<String, String> cityMaps = new HashMap<String, String>();
				for (int j = 0; j < cityList.size(); j++) {
					// 遍历省下面的所有市的数据
					cityNames[j] = cityList.get(j).getName();
					cityMaps.put(cityList.get(j).getName(), cityList.get(j).getId());
					List<DistrictModel> districtList = cityList.get(j)
							.getDistrictList();
					String[] distrinctNameArray = new String[districtList
							.size()];
					HashMap<String, String> distrinctMap = new HashMap<String, String>();
					DistrictModel[] distrinctArray = new DistrictModel[districtList
							.size()];
					for (int k = 0; k < districtList.size(); k++) {
						// 遍历市下面所有区/县的数据
						DistrictModel districtModel = new DistrictModel(
								districtList.get(k).getName(), districtList
										.get(k).getZipcode());
						// 区/县对于的邮编，保存到mZipcodeDatasMap
						mZipcodeDatasMap.put(districtList.get(k).getName(),
								districtList.get(k).getZipcode());
						distrinctArray[k] = districtModel;
						distrinctNameArray[k] = districtModel.getName();
						distrinctMap.put(districtModel.getName(), districtModel.getZipcode());
					}
					// 市-区/县的数据，保存到mDistrictDatasMap
					mDistrictDatasMap.put(cityNames[j], distrinctNameArray);
					mDistrictAllMap.put(cityNames[j], distrinctMap);
				}
				// 省-市的数据，保存到mCitisDatasMap
//				Log.i("mCitisDatasMap",provinceList.get(i).getName()+"================="+cityNames[i]);
				mCitisDatasMap.put(provinceList.get(i).getName(), cityNames);
				mCitysAllMap.put(provinceList.get(i).getName(), cityMaps);
			}
//			Log.i("mCitisDatasMap", mCitisDatasMap.get("zipcode")+"=================");
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {

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
