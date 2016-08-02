package com.zjxfood.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.project.util.BitmapUtilSingle;
import com.umeng.analytics.MobclickAgent;
import com.zjxfood.common.Constants;

import java.io.File;
import java.io.FileOutputStream;

import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;

public class MyQrCodeActivity extends AppActivity implements OnClickListener{

	private ImageView mBackImage;
	private Button mShareBtn;
//	private String content = "食尚男女正式上线啦，美食包罗万象，商品任您选购，快来点燃您疯狂的味蕾吧！即日下载，可获得100元现金大奖哦，快来扫扫我吧！";
	private String content1 = "推荐用户注册时可让用户使用微信扫描该二维码，用户在微信绑定手机号后会自动将你设为对方的推荐人。";
	private String content2 = "二维码分享到朋友圈或保存到手机时，30天(自下载或分享时计算)后会过期。遇到二维码过期的情况，可立即从本页面进行下载或直接扫描上面的二维码。";

	private String path = "";
	private ImageView mImage;
	private BitmapUtils mBitmapUtils;
	private TextView mTitleText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.qr_code_layout);
		setImmerseLayout(findViewById(R.id.head_layout));
		mBitmapUtils = BitmapUtilSingle
				.getBitmapInstance(getApplicationContext());
//		ShareSDK.initSDK(this);
		init();
		mBitmapUtils.display(mImage,Constants.getMyCode+"userid="+Constants.mId);
//		getCode();
	}
	
	private void init(){
//		mBackImage = (ImageView) findViewById(R.id.title_back_image);
		mShareBtn = (Button) findViewById(R.id.my_code_share_btn);
		mImage = (ImageView) findViewById(R.id.qr_code_show);
		mBackImage = (ImageView) findViewById(R.id.title_back_image);
		mTitleText = (TextView) findViewById(R.id.title_text);
		mTitleText.setText("我的推广二维码");
		
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.qr_code_down);
		File dir = new File("/mnt/sdcard/ZjxProject/qrImage/");
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File bitmapFile = new File("/mnt/sdcard/ZjxProject/qrImage/my_qr_code.png");
		if (!bitmapFile.exists()) {
			try {
				bitmapFile.createNewFile();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(bitmapFile);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
			path = "/mnt/sdcard/ZjxProject/qrImage/my_qr_code.png";
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		mBackImage.setOnClickListener(this);
		mShareBtn.setOnClickListener(this);
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_back_image:
			finish();
			break;

		case R.id.my_code_share_btn:
			if(!(path.equals(""))){
				showShare(getApplicationContext(),"分享",true);
			}
			
			break;
		}
	}

	/**
	 * 演示调用ShareSDK执行分享
	 *
	 * @param context
	 * @param platformToShare  指定直接分享平台名称（一旦设置了平台名称，则九宫格将不会显示）
	 * @param showContentEdit  是否显示编辑页
	 */
	public static void showShare(Context context, String platformToShare, boolean showContentEdit) {
		OnekeyShare oks = new OnekeyShare();
		oks.setSilent(!showContentEdit);
		if (platformToShare != null) {
			oks.setPlatform(platformToShare);
		}
		//ShareSDK快捷分享提供两个界面第一个是九宫格 CLASSIC  第二个是SKYBLUE
		oks.setTheme(OnekeyShareTheme.CLASSIC);
		// 令编辑页面显示为Dialog模式
		oks.setDialogMode();
		// 在自动授权时可以禁用SSO方式
		oks.disableSSOWhenAuthorize();
		//oks.setAddress("12345678901"); //分享短信的号码和邮件的地址
		oks.setTitle("ShareSDK--Title");
		oks.setTitleUrl("http://mob.com");
		oks.setText("ShareSDK--文本");
		//oks.setImagePath("/sdcard/test-pic.jpg");  //分享sdcard目录下的图片
//		oks.setImageUrl(randomPic()[0]);
		oks.setUrl("http://www.mob.com"); //微信不绕过审核分享链接
		//oks.setFilePath("/sdcard/test-pic.jpg");  //filePath是待分享应用程序的本地路劲，仅在微信（易信）好友和Dropbox中使用，否则可以不提供
		oks.setComment("分享"); //我对这条分享的评论，仅在人人网和QQ空间使用，否则可以不提供
		oks.setSite("ShareSDK");  //QZone分享完之后返回应用时提示框上显示的名称
		oks.setSiteUrl("http://mob.com");//QZone分享参数
		oks.setVenueName("ShareSDK");
		oks.setVenueDescription("This is a beautiful place!");
		// 将快捷分享的操作结果将通过OneKeyShareCallback回调
		//oks.setCallback(new OneKeyShareCallback());
		// 去自定义不同平台的字段内容
		//oks.setShareContentCustomizeCallback(new ShareContentCustomizeDemo());
		// 在九宫格设置自定义的图标
		Bitmap logo = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
		String label = "ShareSDK";
		OnClickListener listener = new OnClickListener() {
			public void onClick(View v) {

			}
		};
		oks.setCustomerLogo(logo, label, listener);

		// 为EditPage设置一个背景的View
		//oks.setEditPageBackground(getPage());
		// 隐藏九宫格中的新浪微博
		// oks.addHiddenPlatform(SinaWeibo.NAME);

		// String[] AVATARS = {
		// 		"http://99touxiang.com/public/upload/nvsheng/125/27-011820_433.jpg",
		// 		"http://img1.2345.com/duoteimg/qqTxImg/2012/04/09/13339485237265.jpg",
		// 		"http://diy.qqjay.com/u/files/2012/0523/f466c38e1c6c99ee2d6cd7746207a97a.jpg",
		// 		"http://diy.qqjay.com/u2/2013/0422/fadc08459b1ef5fc1ea6b5b8d22e44b4.jpg",
		// 		"http://img1.2345.com/duoteimg/qqTxImg/2012/04/09/13339510584349.jpg",
		// 		"http://diy.qqjay.com/u2/2013/0401/4355c29b30d295b26da6f242a65bcaad.jpg" };
		// oks.setImageArray(AVATARS);              //腾讯微博和twitter用此方法分享多张图片，其他平台不可以

		// 启动分享
		oks.show(context);
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
