package cn.com.incardata.autobon;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;

import cn.com.incardata.application.MyApplication;
import cn.com.incardata.service.AutobonService;

public class WelcomeActivity extends BaseActivity {
    private static final int DURATION = 2000;//持续时长（秒）
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        MyApplication.setIsSkipNewOrder(true);
        startCount();

        if (MyApplication.getService() == null){
            initBaidu();
        }
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

    ServiceConnection sc;
    private void initBaidu() {
        Intent intent = new Intent(this, AutobonService.class);
        sc = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                AutobonService mService = ((AutobonService.LocalBinder)service).getService();
                MyApplication.setService(mService);
                mService.startBDLocal();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        bindService(intent, sc, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (sc != null){
            unbindService(sc);
        }
    }
}
