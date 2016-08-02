package com.zjxfood.reserve;

import com.zjxfood.activity.R;

import android.app.Activity;
import android.os.Bundle;

public class ReserveListChildActivity extends Activity{
	
	private Bundle mBundle;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reserve_order_list_child);
	}
}
