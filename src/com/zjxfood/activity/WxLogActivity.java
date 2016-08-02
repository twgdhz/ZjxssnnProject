package com.zjxfood.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.zjxfood.common.Constants;
import com.zjxfood.http.XutilsUtils;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/6/6.
 * 微信登录
 */
public class WxLogActivity extends AppActivity implements View.OnClickListener {
    private RelativeLayout mTitleLayout;
    private TextView mTitleText;
    private ImageView mBackImage;
    private EditText mPhone, mCode, mTuijianma;
    private TextView mGetCode, mCodeTime;
    private Button mButton;
    private CheckBox mCheckBox;
    private Bundle mBundle;
    private String mUnionid, mOpenid, mName, mHeadUrl;
    private boolean isPhone = true;
    private boolean isClick = true;
    private String code;// 验证码
    private int n = 60;
    private Timer mTimer;
    private HashMap<String, Object> mMaps;
    private HashMap<String, Object> mMaps2;
    private HashMap<String, Object> mLogMaps, mLogMaps2;
    private FrameLayout mTjLayout;
    private TextView mTjText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wx_reg_layout);
        setImmerseLayout(findViewById(R.id.head_layout));
        init();
        mBundle = getIntent().getExtras();
        if (mBundle != null) {
            mUnionid = mBundle.getString("unionId");
            mOpenid = mBundle.getString("mOpenid");
            mName = mBundle.getString("nickname");
            mHeadUrl = mBundle.getString("headimgurl");
        }
        mPhone.addTextChangedListener(textWatcher);

    }

    private void init() {
        mTjText = (TextView) findViewById(R.id.invitation_code_text_phone);
        mTjLayout = (FrameLayout) findViewById(R.id.invitation_code_frame_layout);
        mPhone = (EditText) findViewById(R.id.wx_user_name_edit);
        mCode = (EditText) findViewById(R.id.wx_user_code_edit);
        mTuijianma = (EditText) findViewById(R.id.wx_invitation_code_edit);
        mGetCode = (TextView) findViewById(R.id.wx_get_code_text);
        mButton = (Button) findViewById(R.id.wx_user_log_submit);
        mCodeTime = (TextView) findViewById(R.id.wx_get_code_text_time);

        mTitleLayout = (RelativeLayout) findViewById(R.id.wx_title_layout);
        mTitleText = (TextView) mTitleLayout.findViewById(R.id.add_bank_info_text);
        mTitleText.setText("帐号绑定");
        mBackImage = (ImageView) mTitleLayout.findViewById(R.id.mall_back_info_image);

        mBackImage.setOnClickListener(this);
        mGetCode.setOnClickListener(this);
        mButton.setOnClickListener(this);
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            isRegPhone();
        }
    };

    // 注册
    private void register() {
        StringBuilder sb = new StringBuilder();
        sb.append("username=" + mPhone.getText() + "&vcode=" + mCode.getText() + "&unionid=" + mUnionid + "&openid=" + mOpenid + "&headimage=" + mHeadUrl + "&nickname=" + mName + "&tjcode" + mTuijianma.getText());
        XutilsUtils.get(Constants.wxRegister, sb,
                new RequestCallBack<String>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> res) {
                        Log.i("msg", res.result + "===============");
                        try {
                            mMaps = Constants.getJsonObject(res.result);
                            Toast.makeText(getApplicationContext(), mMaps + "", Toast.LENGTH_LONG).show();
                            if (mMaps != null && mMaps.size() > 0) {
//                            if(mMaps.get("Code")!=null && mMaps.get("Code").toString().equals("200")){
                                mMaps2 = Constants.getJsonObject(mMaps.get("Data").toString());
                                if (mMaps2 != null && mMaps2.size() > 0) {
                                    Constants.mUserName = mMaps2.get("Username")
                                            .toString();
                                    Constants.mId = mMaps2.get("Id").toString();
                                    Constants.mUserCode = mMaps2.get("Usercode")
                                            .toString();
                                    Constants.mPayPassword = mMaps2
                                            .get("Paypassword").toString();
                                    Constants.mShMoney = mMaps2.get("Shmoney")
                                            .toString();
                                    Constants.mPassWord = "null";
                                    Constants.mFid = mMaps2.get("Fid").toString();
                                    Constants.mIsjh = mMaps2.get("Isjh")
                                            .toString();
                                    Constants.headPath = mMaps2.get("Avatar")
                                            .toString();
                                    Constants.UserLevelMemo = mLogMaps.get("UserLevelMemo")
                                            .toString();
                                    if (!(mMaps2.get("Realname").equals("null"))) {
                                        Constants.mRealname = URLDecoder.decode(mMaps2
                                                        .get("Realname").toString(),
                                                "UTF-8");
                                    } else {
                                        Constants.mRealname = mMaps2
                                                .get("Username").toString();
                                    }
                                    Constants.onLine = 1;
                                    handler.sendEmptyMessageDelayed(5, 0);
                                    if (mMaps.get("Message") != null) {
                                        Toast.makeText(getApplicationContext(), mMaps.get("Message").toString(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                            handler.sendEmptyMessageDelayed(6, 0);
                            if (mMaps.get("Message") != null) {
                                Toast.makeText(getApplicationContext(), mMaps.get("Message").toString(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mall_back_info_image:
                finish();
                break;
            case R.id.wx_user_log_submit:
                if (!mPhone.getText().toString().equals("")) {
                    if (!mCode.getText().toString().equals("") && mCode.getText().toString().equals(code)) {
                        register();
                    } else {
                        Toast.makeText(getApplicationContext(), "验证码错误", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "手机好吗不能为空", Toast.LENGTH_LONG).show();
                }

                break;
            case R.id.wx_get_code_text:
                if (!mPhone.getText().toString().equals("")) {
//                    isRegPhone();
                    if (checkPhoneNum(mPhone.getText().toString())) {
                        sendCode();
                    } else {
                        Toast.makeText(getApplicationContext(), "手机号码格式不正确！",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "手机好吗不能为空", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void isRegPhone() {
        if (checkPhoneNum(mPhone.getText().toString())) {
            StringBuilder sb = new StringBuilder();
            sb.append("username=" + mPhone.getText().toString());
            XutilsUtils.get(Constants.isReg2, sb,
                    new RequestCallBack<String>() {
                        @Override
                        public void onFailure(HttpException arg0, String arg1) {
                            isPhone = true;
                        }

                        @Override
                        public void onSuccess(ResponseInfo<String> res) {
                            if ((res.result).equals("true")) {
                                isPhone = true;
                                mTjText.setVisibility(View.GONE);
                                mTjLayout.setVisibility(View.GONE);
                            } else {
                                isPhone = false;
                                mTjText.setVisibility(View.VISIBLE);
                                mTjLayout.setVisibility(View.VISIBLE);
                            }
                        }
                    });
        }
    }

    // 发送验证码
    private void sendCode() {
        if (true) {
            if (isClick) {
                Random random = new Random();
                String result = "";
                for (int i = 0; i < 6; i++) {
                    result += random.nextInt(10);
                }
                code = result;
                Log.i("code", code + "===========code============");
                mTimer = new Timer();
                TimerTask task = new TimerTask() {
                    public void run() {
                        n--;
                        if (n <= 0) {
                            handler.sendEmptyMessageDelayed(2, 0);
                        } else {
                            handler.sendEmptyMessageDelayed(1, 0);
                        }
                    }
                };
                mTimer.schedule(task, 60, 1000);
                StringBuilder sb = new StringBuilder();
                sb.append("mobile=" + mPhone.getText().toString() + "&code=" + code);
                XutilsUtils.get(Constants.sendCode3, sb,
                        new RequestCallBack<String>() {
                            @Override
                            public void onFailure(HttpException arg0, String arg1) {
                            }

                            @Override
                            public void onSuccess(ResponseInfo<String> res) {
                                if (res.result.equals("true")) {
                                    handler.sendEmptyMessageDelayed(2, 0);
                                } else {
                                    Log.i("codejson", "=========验证码发送失败============");
                                }
                            }
                        });
            }
        } else {
            Toast.makeText(getApplicationContext(), "手机号码已经被注册！",
                    Toast.LENGTH_SHORT).show();
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Intent intent = new Intent();
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    mCodeTime.setText(n + "");
                    mCodeTime.setVisibility(View.VISIBLE);
                    mGetCode.setTextColor(getResources().getColor(R.color.gray));
                    break;
                case 2:
                    if (mTimer != null) {
                        mTimer.cancel();
                        mTimer.purge();
                        mTimer = null;
                        mGetCode.setTextColor(getResources().getColor(
                                R.color.main_title_color));
                        isClick = true;
                        n = 60;
                        mCodeTime.setVisibility(View.GONE);
                    }
                    break;
                case 3:
                    Toast.makeText(getApplicationContext(), "验证码发送成功！",
                            Toast.LENGTH_SHORT).show();
                    mGetCode.setTextColor(getResources().getColor(R.color.gray));
                    isClick = false;
                    break;
                case 4:
                    if (mMaps2 != null && mMaps2.size() > 0) {
//                        isRegClick = true;
                        // 注册成功后跳转到这里 并跳转至登录页面MyUserLogActivity
                        intent.setClass(getApplicationContext(),
                                MyUserLogActivity.class);
                        Bundle bundle = new Bundle();

                        bundle.putString("userName", mPhone.getText().toString());

                        bundle.putString("Id", mMaps2.get("Id").toString());
                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();
                        Toast.makeText(getApplicationContext(),
                                mMaps.get("Message").toString(),
                                Toast.LENGTH_SHORT).show();
                    } else {
//                        isRegClick = true;
                        Toast.makeText(getApplicationContext(), mMaps.get("Message").toString(),
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 5:
// 跳转我的界面
                    intent.setClass(getApplicationContext(), MyNewActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("name", mPhone.getText().toString());
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                    break;
                case 6:
                    intent.setClass(getApplicationContext(),
                            MyUserLogActivity.class);
                    startActivity(intent);
                    finish();
                    break;
            }
        }
    };

    /**
     * 判段手机号格式是否正确
     *
     * @param phoneNum
     * @return boolean
     */
    public boolean checkPhoneNum(String phoneNum) {
        Pattern pattern = Pattern.compile("^1\\d{10}$");
        Matcher matcher = pattern.matcher(phoneNum);

        if (matcher.matches()) {
            return true;
        }
        return false;
    }
}
