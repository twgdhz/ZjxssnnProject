package com.zjxfood.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.project.util.DensityUtils;
import com.umeng.analytics.MobclickAgent;
import com.zjxfood.common.Constants;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Hashtable;

public class MyQRcode extends AppActivity implements OnClickListener {
	private TextView mMyQRcodeNum; 
	private ImageView mMyQRcodeImage;
	private ImageView mBackImage;
	private Button mShareBtn;
	private Bitmap bitmap;
	private String path;
	private String content = "食尚男女正式上线啦，美食包罗万象，商品任您选购，快来点燃您疯狂的味蕾吧！即日下载，可获得100元现金大奖哦，快来扫扫我吧！";

	private TextView mTitleText;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_qrcode);
		setImmerseLayout(findViewById(R.id.head_layout));
//		ShareSDK.initSDK(this);
		init();
		// 获取数字推荐码
		mMyQRcodeNum = (TextView) findViewById(R.id.my_qrcode_num);
		mMyQRcodeNum.setText("推荐码：" + Constants.mUserCode);
		
		
		mBackImage = (ImageView) findViewById(R.id.my_qrcode_back_image);
		mBackImage.setOnClickListener(this);
		
		final int QR_WIDTH= DensityUtils.dp2px(getApplicationContext(), 200);
    	final int QR_HEIGHT=DensityUtils.dp2px(getApplicationContext(), 200);
        try {
            // 需要引入core包
            QRCodeWriter writer = new QRCodeWriter();
            String text = Constants.mUserCode;
            if (text == null || "".equals(text) || text.length() < 1) {
                return;
            }
            // 把输入的文本转为二维码
            BitMatrix martix = writer.encode(text, BarcodeFormat.QR_CODE,
                    QR_WIDTH, QR_HEIGHT);

            System.out.println("w:" + martix.getWidth() + "h:"
                    + martix.getHeight()+"二维码文本是："+text);

            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            BitMatrix bitMatrix = new QRCodeWriter().encode(text,
                    BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
            int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
            for (int y = 0; y < QR_HEIGHT; y++) {
                for (int x = 0; x < QR_WIDTH; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * QR_WIDTH + x] = 0xff000000;
                    } else {
                        pixels[y * QR_WIDTH + x] = 0xffffffff;
                    }
                }
            }
            bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT,
                    Bitmap.Config.ARGB_4444);

            bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
            
            mMyQRcodeImage.setImageBitmap(bitmap);
            
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

        } catch (WriterException e) {
            e.printStackTrace();
        }
	}
	
	private void init(){
		mMyQRcodeImage=(ImageView) findViewById(R.id.my_qrcode_image);
		mShareBtn = (Button) findViewById(R.id.my_share_btn);
		
		mShareBtn.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.my_qrcode_back_image:
			finish();
			break;

		case R.id.my_share_btn:
//			showShare();
//			Intent intent=new Intent(Intent.ACTION_SEND);
//			
//			if (path == null || path.equals("")) {
//	            intent.setType("text/plain"); // 纯文本  
//	        } else {  
//	            File f = new File(path);  
//	            if (f != null && f.exists() && f.isFile()) {
//	                intent.setType("image/*");  
//	                Uri u = Uri.fromFile(f);  
//	                intent.putExtra(Intent.EXTRA_STREAM, u);  
//	            }  
//	        }  
//			intent.setType("image/*");
//			intent.putExtra(Intent.EXTRA_SUBJECT, "Share");
////			intent.putExtra("sms_body", content);
//			intent.putExtra(Intent.EXTRA_TEXT, content);
//			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			startActivity(Intent.createChooser(intent, getTitle()));

			break;
		}
	}
//	private void showShare() { //boolean silent,String platform
//		ShareSDK.initSDK(this);
//		OnekeyShare oks = new OnekeyShare();
//		// 关闭sso授权
//		oks.disableSSOWhenAuthorize();
//		oks.setNotification(R.drawable.log_image, "222");
//		oks.setTitleUrl("http://ssnn.hexnews.com/ssnn.html");
//		oks.setTitle("食尚男女");
//		oks.setText(content);
//		oks.setImagePath(path);
//		oks.setUrl("http://ssnn.hexnews.com/ssnn.html");
//
//		oks.setShareContentCustomizeCallback(new ShareContentCustomizeDemo(){
//			@Override
//			public void onShare(Platform platform,
//					cn.sharesdk.framework.Platform.ShareParams paramsToShare) {
//				System.out.println(platform.getName().toString());
//				if ("Wechat".equals(platform.getName())) {
//					paramsToShare.setShareType(Platform.SHARE_WEBPAGE);;
//				}
//			}
//
//		});
//		// 启动分享GUI
//		oks.show(this);
//	}
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
