package cn.com.incardata.autobon;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import cn.com.incardata.utils.AutoCon;
import cn.com.incardata.utils.DateCompute;

public class ImmediateSuccessedActivity extends BaseActivity implements View.OnClickListener{
    private TextView today;
    private TextView orderNumber;
    private Button startWork;
    private CountDownTimer mTimer;

    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_immediate_successed);

        bundle = getIntent().getExtras();

        today = (TextView) findViewById(R.id.today);
        orderNumber = (TextView) findViewById(R.id.order_number);
        startWork = (Button) findViewById(R.id.start_work);

        today.setText(DateCompute.getWeekOfDate());
        orderNumber.setText(getString(R.string.order_serial_number) + "：" + bundle.getString("OrderNum"));

        findViewById(R.id.personal).setOnClickListener(this);
        findViewById(R.id.more).setOnClickListener(this);
        startWork.setOnClickListener(this);

        mTimer = new CountDownTimer(6000, 1000){

            @Override
            public void onTick(long millisUntilFinished) {
                startWork.setText(getString(R.string.tab_two_text) + "(" + millisUntilFinished / 1000 + ")");
            }

            @Override
            public void onFinish() {
                startWork.setText(getString(R.string.tab_two_text) + "(0)");
                finish();
            }
        };
        mTimer.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.personal:
                startActivity(MyInfoActivity.class);
                break;
            case R.id.more:
                startActivity(MoreActivity.class);
                break;
            case R.id.start_work:
                Intent intent = new Intent(getContext(), OrderReceiveActivity.class);
                intent.putExtra(OrderReceiveActivity.IsLocalData, false);
                intent.putExtra(AutoCon.ORDER_ID, bundle.getInt(AutoCon.ORDER_ID));
                startActivity(intent);
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTimer != null){
            mTimer.cancel();
            mTimer = null;
        }
    }
}
