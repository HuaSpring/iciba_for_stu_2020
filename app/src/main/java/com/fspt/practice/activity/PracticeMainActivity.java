package com.fspt.practice.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;

import com.fspt.practice.R;
import com.fspt.practice.base.BaseActivity;
import com.fspt.practice.bean.FanyiBase;
import com.fspt.practice.bean.Fanyi_CN_T_EN_Bean;
import com.fspt.practice.bean.Fanyi_Eng_T_CN_Bean;
import com.fspt.practice.global.Constants;
import com.fspt.practice.net.TranslateApiService;
import com.fspt.practice.utils.CommonUtils;
import com.fspt.practice.utils.LoginCheckUtils;
import com.fspt.practice.utils.SPHelper;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class PracticeMainActivity extends BaseActivity implements View.OnClickListener {
    private ImageView ivBack;
    private TextView tvTitle;
    private ImageView ivRight;
    private RelativeLayout rlTitle;
    private SPHelper spHelper;//数据 存储工具
    private boolean isLogin = false;

    private TranslateApiService apiService = null;
    private TranslateApiService apiServicerx = null;
    private TextView tv_result;// Translate result


    public static final String FANYI_URL = "http://fy.iciba.com/";
    private EditText ed_word;
    private String defaultEN = "apple";
    private String defaultZH = "苹果";

    /**
     * 查询 苹果，返回 apple等
     * {"status":1,"content":{"from":"zh-CN","to":"en-US","out":"apple","vendor":"ciba","err_no":0}}
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        loadData();
        addListener();

    }


    @Override
    public void loadData() {

    }

    @Override
    public void initView() {
        ivBack = (ImageView) findViewById(R.id.ivBack);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        ivRight = (ImageView) findViewById(R.id.ivRight);
        rlTitle = (RelativeLayout) findViewById(R.id.rlTitle);
        rlTitle.getBackground().setAlpha(180);//设置透明度
        ivRight.setVisibility(View.VISIBLE);//设置显示
        spHelper = SPHelper.getInstant(this);//获取实例
        ivBack.setImageResource(R.mipmap.icon);//消失不可见

        String ids = spHelper.getStringFromSP(this, Constants.IDS);
        tvTitle.setText("金山翻译 Copyright By Author @ " + LoginCheckUtils.getNameByID(PracticeMainActivity.this, ids));

        ed_word = (EditText) findViewById(R.id.ed_word);
        tv_result = (TextView) findViewById(R.id.tv_result);
//        mAppAction = new AppActionImpl(this);//实例化云平台操作类
        //接收登录页面传来的是否要登陆的信息
        // TODO  3.6.1：2、接收登录页面传来的是否要登陆的信息，决定是否在功能选择页面登录
        isLogin = getIntent().getBooleanExtra("isGoToLogin", false);
        if (isLogin) {
            String UserName = spHelper.getStringFromSP(this, Constants.USERNAME);
            login(UserName, ids);
            Log.d("HHHH", UserName + ":" + ids);
        }

        CommonUtils.autoSetClickListener(getWindow().getDecorView(), AppCompatButton.class, this, 500);
        CommonUtils.autoSetClickListener(getWindow().getDecorView(), AppCompatImageView.class, this, 500);
    }

    @Override
    public void addListener() {

    }


    /**
     * 设置登录信息
     */
    private void setLoginMSG() {
        Intent intent = new Intent(PracticeMainActivity.this, SplashLoginActivity.class);
        intent.putExtra("isSetAccount", true);
        startActivity(intent);
    }


    private void login(String userName, String ids) {
        boolean b = LoginCheckUtils.checkLogin(PracticeMainActivity.this, ids);
        if (b) {
            Toast.makeText(PracticeMainActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(PracticeMainActivity.this, "该学号不存在，请检查", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(PracticeMainActivity.this, SplashLoginActivity.class);
            intent.putExtra("isSetAccount", true);
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivRight:
                //点击右上角设置按钮跳到设置登陆信息页面
                setLoginMSG();
                //结束Activity
                finish();
                break;
            case R.id.btn_zh_cn:
                getFanyi_ZH_EN();
                break;
            case R.id.btn_en_zh:
                getFanyi_EN_ZH_Normal();
                break;
            case R.id.btn_rxjava:
                rxFanyi_EN_ZH_RX();
                break;
            default:
                break;
        }
    }


    // 4. 创建 Reprofit 实例
    private Retrofit createRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(FANYI_URL)
                // 设置OKHttpClient,如果不设置会提供一个默认的
                .client(new OkHttpClient())
                // 添加Gson配置转化库
                .addConverterFactory(GsonConverterFactory.create())
                // 添加对Rxjava2的适配(只用Retrofit访问网络不需要添加对RxJava的适配)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit;
    }


    //普通方式 getFanyi_Cn_En()
    /*该方法用于调用在接口“ApiService”中定义的以Call对象返回的登陆方法
    主要步骤有：
    （1）判断接口实例apiService是否为空，当为空时获取Retrofit实例。
    （2）通过Retrofit提供的动态代理方式创建apiService的实例。
    （3）使用apiService实例调用ApiService中的getFanyi_Cn_En()方法，并传入相应的参数来得到请求对象Call的实例call。
    （4）使用call对象的“call.enqueue()”方法发起网络请求。
    （5）接收返回的结果。
    */
    public void getFanyi_ZH_EN() {
        if (apiService == null) {
            // 获取Retrofit实例
            Retrofit retrofit = createRetrofit();
            // 通过代理方式创建ApiService 得到一个接口的实例
            apiService = retrofit.create(TranslateApiService.class);
        }
        // 调用方法返回Call请求对象
        String word = ed_word.getText().toString().trim();
        word = TextUtils.isEmpty(word) ? defaultZH : word;
        Call<FanyiBase<Fanyi_CN_T_EN_Bean.ContentBean>> call = apiService.getFanyiResult_CN_EN(word);
        call.enqueue(new Callback<FanyiBase<Fanyi_CN_T_EN_Bean.ContentBean>>() {
            @Override
            public void onResponse(Call<FanyiBase<Fanyi_CN_T_EN_Bean.ContentBean>> call, Response<FanyiBase<Fanyi_CN_T_EN_Bean.ContentBean>> response) {
                if (response.code() == 200) {
                    assert response.body() != null;
                    tv_result.setText(response.body().getContent().getOut());
                }
            }

            @Override
            public void onFailure(Call<FanyiBase<Fanyi_CN_T_EN_Bean.ContentBean>> call, Throwable t) {
                Log.d("HHHH", "onFailure: " + t.getMessage());
            }
        });
    }


    public void getFanyi_EN_ZH_Normal() {
        if (apiService == null) {
            // 获取Retrofit实例
            Retrofit retrofit = createRetrofit();
            // 通过代理方式创建ApiService 得到一个接口的实例
            apiService = retrofit.create(TranslateApiService.class);
        }
        // 调用方法返回Call请求对象
        String word = ed_word.getText().toString().trim();
        word = TextUtils.isEmpty(word) ? defaultEN : word;
        Call<FanyiBase<Fanyi_Eng_T_CN_Bean.ContentBean>> call = apiService.getFanyiResult_EN_ZH(word);
        call.enqueue(new Callback<FanyiBase<Fanyi_Eng_T_CN_Bean.ContentBean>>() {
            @Override
            public void onResponse(Call<FanyiBase<Fanyi_Eng_T_CN_Bean.ContentBean>> call, Response<FanyiBase<Fanyi_Eng_T_CN_Bean.ContentBean>> response) {
                if (response.code() == 200) {
                    assert response.body() != null;
                    Fanyi_Eng_T_CN_Bean.ContentBean value = response.body().getContent();
                    List<String> word_mean = value.getWord_mean();
                    if (word_mean != null && word_mean.size() > 0) {
                        StringBuilder sb = new StringBuilder();
                        for (String s : word_mean)
                            sb.append(s);
                        tv_result.setText(sb);
                    } else {
                        tv_result.setText("Unknow");
                    }
                }
            }

            @Override
            public void onFailure(Call<FanyiBase<Fanyi_Eng_T_CN_Bean.ContentBean>> call, Throwable t) {
                Log.d("HHHH", "onFailure: " + t.getMessage());
            }
        });
    }

    /*该方法用于调用接口ApiService中定义的以Observable模式返回的登陆方法，实现云平台的登录。
        在“Retrofit+Rxjava”按钮单击事件中调用该方法。
     */
    public void rxFanyi_EN_ZH_RX() {
        if (apiServicerx == null) {
            // 获取Retrofit实例
            Retrofit retrofit = createRetrofit();
            // 通过代理方式创建ApiService 得到一个接口的实例
            apiServicerx = retrofit.create(TranslateApiService.class);
        }
        // 创建一个被观察者对象 /System.err: android.os.NetworkOnMainThreadException  要指定Schedulers.io 线程
        String word = ed_word.getText().toString().trim();
        word = TextUtils.isEmpty(word) ? defaultEN : word;
        apiServicerx.getFanyiResult_EN_ZH_RX(word).subscribeOn(Schedulers.newThread()).subscribe(new Observer<FanyiBase<Fanyi_Eng_T_CN_Bean.ContentBean>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(FanyiBase<Fanyi_Eng_T_CN_Bean.ContentBean> response) {
                Fanyi_Eng_T_CN_Bean.ContentBean value = response.getContent();
                List<String> word_mean = value.getWord_mean();
                if (word_mean != null && word_mean.size() > 0) {
                    StringBuilder sb = new StringBuilder();
                    for (String s : word_mean)
                        sb.append(s);
                    tv_result.setText(sb);
                } else {
                    tv_result.setText("Unknow");
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.d("HHHH", "RX onFailure: " + e.getMessage());
                e.printStackTrace();
            }

            @Override
            public void onComplete() {
            }
        });
    }


}