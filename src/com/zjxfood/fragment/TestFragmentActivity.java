package com.zjxfood.fragment;

import com.zjxfood.activity.R;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class TestFragmentActivity extends FragmentActivity implements OnClickListener{
	private Fragment fragment;
	private Button button1,button2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_fragment_layout);
		
		init();
	}
	
	private void init(){
		button1 = (Button) findViewById(R.id.btn1);
		button2 = (Button) findViewById(R.id.btn2);
		
		button1.setOnClickListener(this);
		button2.setOnClickListener(this);
	}

	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		Fragment fragment;
		FragmentTransaction transaction;
		switch (v.getId()) {
		case R.id.btn1:
//			TestFragment2 newFragment = new TestFragment2();
//			transaction =getFragmentManager().beginTransaction();
//			transaction.replace(R.id.left_fragment,newFragment);
//			transaction.addToBackStack(null);
//			transaction.commit();
			fragment = new TestFragment1();
			replaceFragment(fragment);
			break;

		case R.id.btn2:
			fragment = new TestFragment2();
			replaceFragment(fragment);
			break;
		}
	}
	
	private void replaceFragment(Fragment newFragment) {

		 FragmentTransaction trasection =
		 getFragmentManager().beginTransaction();
		 if(!newFragment.isAdded()){
		         try{
		                 //FragmentTransaction trasection =
		         getFragmentManager().beginTransaction();
		         trasection.replace(R.id.linearLayout2, newFragment);
		         trasection.addToBackStack(null);
		         trasection.commit();

		         }catch (Exception e) {
		                         // TODO: handle exception
		                  //AppConstants.printLog(e.getMessage());

		                 }
		 }else
		         trasection.show(newFragment);

		    }
}
