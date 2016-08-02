package com.zjxfood.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.umeng.analytics.MobclickAgent;
import com.zjxfood.common.Constants;

import java.util.Hashtable;

/**
 * 我的二维码
 * @author zjx
 *
 */
public class CodeGenerationActivity extends AppActivity{

	private ImageView mImageView;
	private int QR_WIDTH = 200;
	private int QR_HEIGHT = 200;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.code_generation_layout);
        setImmerseLayout(findViewById(R.id.head_layout));
		init();
		createImage();
	}
	private void createImage() {
        try {
            // 需要引入core包
            QRCodeWriter writer = new QRCodeWriter();
//
            String text = Constants.mId;

            Log.i("TAG", "生成的文本：" + Constants.mId);
            if (text == null || "".equals(text) || text.length() < 1) {
                return;
            }
            // 把输入的文本转为二维码  
            BitMatrix martix = writer.encode(text, BarcodeFormat.QR_CODE,
            		200, 200);

            System.out.println("w:" + martix.getWidth() + "h:"
                    + martix.getHeight());

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

            Bitmap bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT,
                    Bitmap.Config.ARGB_8888);

            bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
            mImageView.setImageBitmap(bitmap);

        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
	private void init(){
		mImageView = (ImageView) findViewById(R.id.code_gener_image);
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
