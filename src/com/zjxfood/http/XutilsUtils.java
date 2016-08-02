package com.zjxfood.http;

import android.util.Log;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.zjxfood.common.Constants;

public class XutilsUtils {
	private static HttpUtils http = new HttpUtils();
	
	@SuppressWarnings("unchecked")
	public static void get(String url,StringBuilder sb,RequestCallBack callBack){
		String time = System.currentTimeMillis() + "";
		String sign = "";
		String sort = "";
		if (sb.toString().equals("")) {
			sign = Constants.sortsStr("timestamp=" + time);
//			sort = Constants.sortsStr(sign+"&partner=app_android_new");
			sb.append("partner=app_android_new").append("&sign="+sign).append("&timestamp="+time);
		} else {
			sign = Constants.sortsStr(sb.toString() + "&timestamp=" + time);
//			sort = Constants.sortsStr(sign+"&partner=app_android_new");
			sb.append("&partner=app_android_new").append("&sign="+sign).append("&timestamp="+time);
		}
		url = url+sb;
		Log.i("params=====", "url:"+url);
		http.send(HttpMethod.GET, url,callBack);
	}
	public static void post(String url,StringBuilder sb, RequestParams params, RequestCallBack callBack){
		String time = System.currentTimeMillis() + "";
		String sign = "";
		StringBuilder sb2 = new StringBuilder();
		sign = Constants.sortsStr(sb.toString() + "&timestamp=" + time);
		sb2.append("partner=app_android_new").append("&sign="+sign).append("&timestamp="+time);
//		if (params.toString().equals("")) {
//			sign = Constants.sortsStr("timestamp=" + time);
//			sort = Constants.sortsStr(sign+"&partner=app_android_new");
//			sb.append("partner=app_android_new").append("&sign="+sign).append("&timestamp="+time);
//			params.addBodyParameter("partner","app_android_new");
//			params.addBodyParameter("sign",sign);
//			params.addBodyParameter("timestamp",time);
//		} else {
//
//			sort = Constants.sortsStr(sign+"&partner=app_android_new");

//			params.addBodyParameter("partner","app_android_new");
//			params.addBodyParameter("sign",sign);
//			params.addBodyParameter("timestamp",time);
//		}
		url = url+sb2;
		Log.i("params=====", "url:"+url);
		http.send(HttpMethod.POST, url,params,callBack);
//		RequestParams params = new RequestParams();
//
//		http.send(HttpMethod.GET, url,params,callBack);
	}
//	
//	public static void getByParams(String url,StringBuilder sb,RequestCallBack callBack){
//		String time = System.currentTimeMillis() + "";
//		String sign = "";
//		if (sb.toString().equals("")) {
//			sign = Constants.sortsStr("timestamp=" + time);
//		} else {
//			sign = Constants.sortsStr(sb.toString() + "&timestamp=" + time);
//		}
//		sb.append(url).append("partner=app_android_new").append("&sign="+sign).append("&timestamp="+time);
//		Log.i("params", "url:"+sb);
//		http.send(HttpMethod.GET, sb.toString(),callBack);
//	}
}
