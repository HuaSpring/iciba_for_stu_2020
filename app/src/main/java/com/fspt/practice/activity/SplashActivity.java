package com.fspt.practice.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.viewpager.widget.ViewPager;

import com.fspt.practice.R;
import com.fspt.practice.adapter.SplashPageAdapter;
import com.fspt.practice.base.BaseActivity;
import com.fspt.practice.global.Constants;
import com.fspt.practice.utils.SPHelper;

import java.util.ArrayList;

public class SplashActivity extends BaseActivity {
    private int imgs[] = {R.mipmap.pic_welcome_01, R.mipmap.pic_welcome_02, R.mipmap.pic_welcome_03, R.mipmap.pic_welcome_04};//向导页面的四张图片
    private ViewPager vp;
    private ArrayList<ImageView> list;
    private Button btnEnterLogin;
    SPHelper spHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_splash);

        spHelper = SPHelper.getInstant(this);

        checkIsEnter();
        initView();
        loadData();
        addListener();

    }

    /**
     * 检查是否进入过向导页面
     */
    private void checkIsEnter() {
        //获取是否进入过向导页面
        boolean isEnter = spHelper.getBooleanFromSP(SplashActivity.this, Constants.MODULE_PRACTICE, Constants.FIRST_SHOW);
        //如果进入过，直接跳入登陆页面
        if (isEnter) {
            enterLogin();
        }
    }


    public void initView() {
        vp = (ViewPager) findViewById(R.id.vp);
        btnEnterLogin = (Button) findViewById(R.id.btn_enter_login);
    }

    @Override
    public void loadData() {
        //加载图片，将图片放在ImageView中，然后将ImageView加载到ViewPager中
        list = new ArrayList<ImageView>();
        for (int resId : imgs) {
            ImageView iv = new ImageView(this);
            iv.setBackgroundResource(resId);
            list.add(iv);
        }
        vp.setAdapter(new SplashPageAdapter(list));
    }

    @Override
    public void addListener() {
        //设置ViewPager滑动监听
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //设置按钮在向导页，最后一页显示
                if (position == imgs.length - 1) {
                    btnEnterLogin.setVisibility(View.VISIBLE);
                } else {
                    btnEnterLogin.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //开始体验按钮,点击进入登陆页面
        btnEnterLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterLogin();
                //记录已经进入过向导页面了
                spHelper.putData2SP(SplashActivity.this, Constants.MODULE_PRACTICE, Constants.FIRST_SHOW, true);
            }
        });
    }

    /**
     * 进入登陆页面
     */
    private void enterLogin() {
        //resetting 用于区分是从登陆页面进入，还是从主页面进入的
        Intent intent = new Intent(this, SplashLoginActivity.class);
        intent.putExtra("resetting", false);
        startActivity(intent);
        this.finish();
    }

}
