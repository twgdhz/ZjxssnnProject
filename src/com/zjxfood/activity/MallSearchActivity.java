package com.zjxfood.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.zjxfood.application.ExitApplication;

public class MallSearchActivity extends AppActivity implements OnClickListener {

    private EditText mSearchEdit;
    private TextView mSearchText;
    private ImageView mBackImage;
    private String mGroup = "", mType = "", mShouCode = "", mProportion = "",mTypeName;
    private Bundle mBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mall_list_search_layout);
        setImmerseLayout(findViewById(R.id.head_layout));
        ExitApplication.getInstance().addMyActivity(this);
        ExitApplication.getInstance().addMallActivity(this);
        ExitApplication.getInstance().addActivity(this);

        init();
        mBundle = getIntent().getExtras();
        if (mBundle != null) {
            mGroup = mBundle.getString("group");
            mType = mBundle.getString("type");
            mShouCode = mBundle.getString("shouCode");
            mProportion = mBundle.getString("proportion");
            mTypeName = mBundle.getString("typeName");
        }
    }

    private void init() {
        mSearchEdit = (EditText) findViewById(R.id.mall_title_search);
        mSearchText = (TextView) findViewById(R.id.mall_search_text);
        mBackImage = (ImageView) findViewById(R.id.mall_search_back_image);

        mSearchText.setOnClickListener(this);
        mBackImage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.mall_search_text:
                if (!(mSearchEdit.getText().toString().equals(""))) {
                    intent.setClass(getApplicationContext(), MallActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("name", mSearchEdit.getText().toString());
                    bundle.putString("group", mGroup);
                    bundle.putString("titleName", mSearchEdit.getText().toString());
                    bundle.putString("type", mType);
                    bundle.putString("shouCode", mShouCode);
                    bundle.putString("proportion", mProportion);
                    bundle.putString("typeName", mTypeName);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "搜索内容不能为空！", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.mall_search_back_image:
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        System.gc();
        super.onDestroy();
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
