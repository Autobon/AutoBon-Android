package cn.com.incardata.autobon_shops;

import android.os.Bundle;
import android.os.CountDownTimer;

import cn.com.incardata.application.MyApplication;

public class WelcomeActivity extends BaseActivity {
    private static final int DURATION = 2000;//持续时长（秒）
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        MyApplication.setIsSkipNewOrder(true);
        startCount();
    }

    private void startCount() {
        new CountDownTimer(DURATION, DURATION){

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                startActivity(LoginActivity.class);
                finish();
            }
        }.start();
    }
}
