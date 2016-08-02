package com.zjxfood.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.util.Log;

import com.zjxfood.common.Constants;

/**
 * 访问网络数据
 * @author lenovo
 *
 */
public class ReadJson {
	
	public static String readParse(String urlPath) throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		byte[] data = new byte[1024];
		int len = 0;
		URL url = new URL(urlPath);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		String code = new Integer(conn.getResponseCode()).toString();//响应编码 200为成功 500为服务器错误
		Log.i("code", code+"=========code===========");
		InputStream inputStream = conn.getInputStream();
		while ((len = inputStream.read(data)) != -1) {
			outputStream.write(data, 0, len);
		}
		inputStream.close();
		return new String(outputStream.toByteArray());
	}
	
	public static String getJson(String paramHead,String json) throws IOException {
		String time = System.currentTimeMillis()+"";
		String sign = "";
		String res = "";
		if(json.equals("")){
			sign = Constants.sortsStr(json+"timestamp="+time);
			res = paramHead+sign+"&"+"timestamp="+time;
		}else{
			sign = Constants.sortsStr(json+"&timestamp="+time);
			res = paramHead+sign+"&"+json+"&timestamp="+time;
		}
		Log.i("res", res+"=========res===========");
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		byte[] data = new byte[1024];
		int len = 0;
		URL url = new URL(res);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		String code = new Integer(conn.getResponseCode()).toString();
		Log.i("code", code+"=========code===========");
		InputStream inputStream = conn.getInputStream();
		while ((len = inputStream.read(data)) != -1) {
			outputStream.write(data, 0, len);
		}
		inputStream.close();
		return new String(outputStream.toByteArray());
	}
}
