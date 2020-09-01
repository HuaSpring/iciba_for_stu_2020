package com.fspt.practice.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.fspt.practice.R;
import com.fspt.practice.base.BaseActivity;
import com.fspt.practice.global.Constants;
import com.fspt.practice.utils.LoginCheckUtils;
import com.fspt.practice.utils.SPHelper;

/**
 * Create by Spring on 2020/9/1 13:53
 */
public class SplashLoginActivity extends BaseActivity {

    private ImageView ivBack;
    private TextView tvTitle;
    private RelativeLayout rlTitle;
    private EditText edtUserName;//用户名
    private EditText edtIds;//学号
    private Button btnLogin;//登录
    private CheckBox cbSaveMsg;//是否记住密码
    //    private AppAction mAppAction;//用于登录云平台的对象
    private EditText edtIp;//ip
    private EditText edtProject;//项目标识

    private LinearLayout llLogin;//登录信息模块
    private LinearLayout llRight;//右半部分登陆信息设置所在的LinearLayout


    SPHelper spHelper;
    private boolean isSetAccount = false;//是否要设置账号，是为true

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
        loadData();
        addListener();
    }

    @Override
    public void loadData() {

        spHelper = SPHelper.getInstant(this);
        if (spHelper.getBooleanFromSP(this, Constants.ISSAVEMSG)) {
            if (isSetAccount) {
                //如果是设置账号密码并且记住了登录信息，设置checkbox选中状态，填充用户名密码等信息
                cbSaveMsg.setChecked(true);
                edtUserName.setText(spHelper.getStringFromSP(this, Constants.USERNAME));
                edtIds.setText(spHelper.getStringFromSP(this, Constants.IDS));
            } else {
                //如果记住了登录信息但是不是要设置用户名密码的，在功能选择页面登陆
                startIntent(true);
            }
        }
    }


    @Override
    public void initView() {
        isSetAccount = getIntent().getBooleanExtra("isSetAccount", false);

        ivBack = (ImageView) findViewById(R.id.ivBack);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        rlTitle = (RelativeLayout) findViewById(R.id.rlTitle);
        edtUserName = (EditText) findViewById(R.id.edtUserName);
        edtIds = (EditText) findViewById(R.id.edt_ids);
        llLogin = (LinearLayout) findViewById(R.id.llLogin);
        llRight = (LinearLayout) findViewById(R.id.llRight);


        btnLogin = (Button) findViewById(R.id.btnLogin);
        cbSaveMsg = (CheckBox) findViewById(R.id.cbSaveMsg);
        ivBack.setImageResource(R.mipmap.icon);//设置图标
        tvTitle.setText("实训周  金山词霸翻译");
        rlTitle.getBackground().setAlpha(180);//设置标题栏透明度
        llRight.getBackground().setAlpha(180);//设置右侧面板透明度


    }

    @Override
    public void addListener() {

    }


    //点击事件
    public void MyClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:
                judgeAndLogin();
                break;
            case R.id.rbLogin://登录模式
                llLogin.setVisibility(View.VISIBLE);//显示
                break;
        }
    }


    /**
     * 自定义方法，用于页面跳转
     *
     * @param isGotoLogin 是否要在功能选择页面登陆
     */
    private void startIntent(boolean isGotoLogin) {
        // TODO 3.6.1：1-2:判断如果不是要设置用户名密码的，附加数据表明要在功能选择页面登陆，跳转到功能选择页面并结束当前Activity
        Intent intent = new Intent(SplashLoginActivity.this, PracticeMainActivity.class);
        if (isGotoLogin) {
            intent.putExtra("isGoToLogin", true);
        }
        startActivity(intent);
        finish();
    }


    private void judgeAndLogin() {
        String UserName = edtUserName.getText().toString().trim();
        String ids = edtIds.getText().toString().trim();
        Log.d("HHHH", "judgeAndLogin: " + UserName + " ids " + ids);
        if (ids.isEmpty()) {
            Toast.makeText(this, "请填写您的学号", Toast.LENGTH_SHORT).show();
            return;
        }
        login(UserName, ids);
    }


    private void login(String userName, String ids) {
        boolean b = LoginCheckUtils.checkLogin(SplashLoginActivity.this, ids);
        if (b) {
            if (cbSaveMsg.isChecked()) {
                spHelper.putData2SP(getBaseContext(), Constants.ISSAVEMSG, true);
                spHelper.putData2SP(getBaseContext(), Constants.USERNAME, userName);
                spHelper.putData2SP(getBaseContext(), Constants.IDS, ids);
            }
            startIntent(false);
            Toast.makeText(SplashLoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(SplashLoginActivity.this, "该学号不存在，请检查", Toast.LENGTH_SHORT).show();
        }
    }

}
