package com.zjxfood.model;



import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;

import com.zjxfood.activity.R;

public class GetKey extends Activity{
	private static EditText et_pkgname;
	   private PackageManager manager;
	   private  PackageInfo packageInfo;
	   private  android.content.pm.Signature[] signatures;
	   private  StringBuilder builder;
	   private  String signature;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_bottom_menu_layout);
		manager = getPackageManager();
	       builder = new StringBuilder();
		getSignature();
	}
	
	public void getSignature() {
//	       String pkgname = et_pkgname.getText().toString();
	       boolean isEmpty = TextUtils.isEmpty("com.zjx.food");
	       if (isEmpty) {
//	           Toast.makeText(this, "应用程序的包名不能为空！", Toast.LENGTH_SHORT);
	       } else {
	           try {
	               /** 通过包管理器获得指定包名包含签名的包信息 **/
	               packageInfo = manager.getPackageInfo("com.zjx.food", PackageManager.GET_SIGNATURES);
	               /******* 通过返回的包信息获得签名数组 *******/
	               signatures = packageInfo.signatures;
	               /******* 循环遍历签名数组拼接应用签名 *******/
	               for (Signature signature : signatures) {
	                   builder.append(signature.toCharsString());
	               }
	               /************** 得到应用签名 **************/
	               signature = builder.toString();
	               Log.i("签名", signature+"===================");
	           } catch (NameNotFoundException e) {
	               e.printStackTrace();
	           }
	       }
	   }
}
