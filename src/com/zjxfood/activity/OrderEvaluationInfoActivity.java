package com.zjxfood.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.umeng.analytics.MobclickAgent;
import com.zjxfood.application.ExitApplication;

public class OrderEvaluationInfoActivity extends AppActivity implements
		OnClickListener {

	private RelativeLayout mHeadLayout;
	private ImageView mBackImage;
	private ImageView mTextureImage1, mTextureImage2, mTextureImage3,
			mTextureImage4, mTextureImage5;
	private ImageView mServiceImage1, mServiceImage2, mServiceImage3,
			mServiceImage4, mServiceImage5;
	private ImageView mEnvironment1, mEnvironment2, mEnvironment3,
			mEnvironment4, mEnvironment5;
	private int textureFlag=4,serviceFlag=4,environmentFlag=4;
	private ImageView[] mTextureImages;
	private ImageView[] mServiceImages;
	private ImageView[] mEnvironmentImages;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_commodity_evaluation_layout);
		setImmerseLayout(findViewById(R.id.head_layout));
		ExitApplication.getInstance().addActivity(this);

		init();
		mTextureImages = new ImageView[5];
		mServiceImages = new ImageView[5];
		mEnvironmentImages = new ImageView[5];
		//
		setImages();
	}

	
	private void init() {
		mHeadLayout = (RelativeLayout) findViewById(R.id.order_commodity_evaluation_title_id);
		mBackImage = (ImageView) mHeadLayout
				.findViewById(R.id.order_evalution_back_image);
		mTextureImage1 = (ImageView) findViewById(R.id.order_evaluation_commodity_grade_image1);
		mTextureImage2 = (ImageView) findViewById(R.id.order_evaluation_commodity_grade_image2);
		mTextureImage3 = (ImageView) findViewById(R.id.order_evaluation_commodity_grade_image3);
		mTextureImage4 = (ImageView) findViewById(R.id.order_evaluation_commodity_grade_image4);
		mTextureImage5 = (ImageView) findViewById(R.id.order_evaluation_commodity_grade_image5);
		mServiceImage1 = (ImageView) findViewById(R.id.order_evaluation_service_grade_image1);
		mServiceImage2 = (ImageView) findViewById(R.id.order_evaluation_service_grade_image2);
		mServiceImage3 = (ImageView) findViewById(R.id.order_evaluation_service_grade_image3);
		mServiceImage4 = (ImageView) findViewById(R.id.order_evaluation_service_grade_image4);
		mServiceImage5 = (ImageView) findViewById(R.id.order_evaluation_service_grade_image5);
		mEnvironment1 = (ImageView) findViewById(R.id.order_evaluation_eat_grade_image1);
		mEnvironment2 = (ImageView) findViewById(R.id.order_evaluation_eat_grade_image2);
		mEnvironment3 = (ImageView) findViewById(R.id.order_evaluation_eat_grade_image3);
		mEnvironment4 = (ImageView) findViewById(R.id.order_evaluation_eat_grade_image4);
		mEnvironment5 = (ImageView) findViewById(R.id.order_evaluation_eat_grade_image5);

		mBackImage.setOnClickListener(this);
		mTextureImage1.setOnClickListener(this);
		mTextureImage2.setOnClickListener(this);
		mTextureImage3.setOnClickListener(this);
		mTextureImage4.setOnClickListener(this);
		mTextureImage5.setOnClickListener(this);
		mServiceImage1.setOnClickListener(this);
		mServiceImage2.setOnClickListener(this);
		mServiceImage3.setOnClickListener(this);
		mServiceImage4.setOnClickListener(this);
		mServiceImage5.setOnClickListener(this);
		mEnvironment1.setOnClickListener(this);
		mEnvironment2.setOnClickListener(this);
		mEnvironment3.setOnClickListener(this);
		mEnvironment4.setOnClickListener(this);
		mEnvironment5.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.order_evalution_back_image:
			finish();
			break;

		case R.id.order_evaluation_commodity_grade_image1:
			textureFlag = 1;
			setTexture(textureFlag);
			break;
		case R.id.order_evaluation_commodity_grade_image2:
			textureFlag = 2;
			setTexture(textureFlag);
			break;
		case R.id.order_evaluation_commodity_grade_image3:
			textureFlag = 3;
			setTexture(textureFlag);
			break;
		case R.id.order_evaluation_commodity_grade_image4:
			textureFlag = 4;
			setTexture(textureFlag);
			break;
		case R.id.order_evaluation_commodity_grade_image5:
			textureFlag = 5;
			setTexture(textureFlag);
			break;
		case R.id.order_evaluation_service_grade_image1:
			serviceFlag = 1;
			setService(serviceFlag);
			break;
		case R.id.order_evaluation_service_grade_image2:
			serviceFlag = 2;
			setService(serviceFlag);
			break;
		case R.id.order_evaluation_service_grade_image3:
			serviceFlag = 3;
			setService(serviceFlag);
			break;
		case R.id.order_evaluation_service_grade_image4:
			serviceFlag = 4;
			setService(serviceFlag);
			break;
		case R.id.order_evaluation_service_grade_image5:
			serviceFlag = 5;
			setService(serviceFlag);
			break;
		case R.id.order_evaluation_eat_grade_image1:
			environmentFlag = 1;
			setEnvironment(environmentFlag);
			break;
		case R.id.order_evaluation_eat_grade_image2:
			environmentFlag = 2;
			setEnvironment(environmentFlag);
			break;
		case R.id.order_evaluation_eat_grade_image3:
			environmentFlag = 3;
			setEnvironment(environmentFlag);
			break;
		case R.id.order_evaluation_eat_grade_image4:
			environmentFlag = 4;
			setEnvironment(environmentFlag);
			break;
		case R.id.order_evaluation_eat_grade_image5:
			environmentFlag = 5;
			setEnvironment(environmentFlag);
			break;
		}
	}
	
	
	private void setImages(){
		mTextureImages[0] = mTextureImage1;
		mTextureImages[1] = mTextureImage2;
		mTextureImages[2] = mTextureImage3;
		mTextureImages[3] = mTextureImage4;
		mTextureImages[4] = mTextureImage5;
		mServiceImages[0] = mServiceImage1;
		mServiceImages[1] = mServiceImage2;
		mServiceImages[2] = mServiceImage3;
		mServiceImages[3] = mServiceImage4;
		mServiceImages[4] = mServiceImage5;
		
		mEnvironmentImages[0] = mEnvironment1;
		mEnvironmentImages[1] = mEnvironment2;
		mEnvironmentImages[2] = mEnvironment3;
		mEnvironmentImages[3] = mEnvironment4;
		mEnvironmentImages[4] = mEnvironment5;
	}
	
	private void setTexture(int n){
		for(int i=0;i<n;i++){
			mTextureImages[i].setImageDrawable(getResources().getDrawable(R.drawable.xingxing1));
		}
		for(int i=n;i<5;i++){
			mTextureImages[i].setImageDrawable(getResources().getDrawable(R.drawable.xingxing2));
		}
	}
	private void setService(int n){
		for(int i=0;i<n;i++){
			mServiceImages[i].setImageDrawable(getResources().getDrawable(R.drawable.xingxing1));
		}
		for(int i=n;i<5;i++){
			mServiceImages[i].setImageDrawable(getResources().getDrawable(R.drawable.xingxing2));
		}
	}
	private void setEnvironment(int n){
		for(int i=0;i<n;i++){
			mEnvironmentImages[i].setImageDrawable(getResources().getDrawable(R.drawable.xingxing1));
		}
		for(int i=n;i<5;i++){
			mEnvironmentImages[i].setImageDrawable(getResources().getDrawable(R.drawable.xingxing2));
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			finish();
			break;

		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
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
