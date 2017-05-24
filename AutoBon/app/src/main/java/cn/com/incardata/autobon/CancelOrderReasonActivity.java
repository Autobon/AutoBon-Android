package cn.com.incardata.autobon;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;

import cn.com.incardata.application.MyApplication;
import cn.com.incardata.http.Http;
import cn.com.incardata.http.NetURL;
import cn.com.incardata.http.OnResult;
import cn.com.incardata.http.response.BaseEntity;
import cn.com.incardata.http.response.DropOrderEntity;
import cn.com.incardata.utils.AutoCon;
import cn.com.incardata.utils.T;

/** 放弃理由
 * Created by yang on 2016/12/7.
 */
public class CancelOrderReasonActivity extends BaseActivity implements View.OnClickListener {
    private ImageView back;
    private EditText reason;
    private ImageView img1,img2,img3;
    private LinearLayout ll1,ll2,ll3;
    private Button submit;
    private int check = 1;

    private int orderId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_order);
        initView();
    }

    public void initView(){
        orderId = getIntent().getIntExtra(AutoCon.ORDER_ID,-1);
        back = (ImageView) findViewById(R.id.iv_back);
        img1 = (ImageView) findViewById(R.id.img1);
        img2 = (ImageView) findViewById(R.id.img2);
        img3 = (ImageView) findViewById(R.id.img3);
        ll1 = (LinearLayout) findViewById(R.id.ll1);
        ll2 = (LinearLayout) findViewById(R.id.ll2);
        ll3 = (LinearLayout) findViewById(R.id.ll3);
        reason = (EditText) findViewById(R.id.ed_reason);
        submit = (Button) findViewById(R.id.submit);
        ll1.setOnClickListener(this);
        ll2.setOnClickListener(this);
        ll3.setOnClickListener(this);
        back.setOnClickListener(this);
        submit.setOnClickListener(this);

        if (orderId == -1){
            T.show(getContext(),R.string.dataUploadFailed);
            return;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll1:
                check = 1;
                img1.setImageResource(R.mipmap.radio_select);
                img2.setImageResource(R.mipmap.radio_default);
                img3.setImageResource(R.mipmap.radio_default);
                reason.setVisibility(View.GONE);
                break;
            case R.id.ll2:
                check = 2;
                img1.setImageResource(R.mipmap.radio_default);
                img2.setImageResource(R.mipmap.radio_select);
                img3.setImageResource(R.mipmap.radio_default);
                reason.setVisibility(View.GONE);
                break;
            case R.id.ll3:
                check = 3;
                img1.setImageResource(R.mipmap.radio_default);
                img2.setImageResource(R.mipmap.radio_default);
                img3.setImageResource(R.mipmap.radio_select);
                reason.setVisibility(View.VISIBLE);
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.submit:
                cancelOrder();
                break;
        }
    }


    public void cancelOrder(){
        String json = "";
        if (check == 1){
            json = getString(R.string.grab_error_order);
        }else if (check == 2){
            json = getString(R.string.temporary_be_engaged);
        }else {
            String reasonStr = reason.getText().toString().trim();
            if (TextUtils.isEmpty(reasonStr)){
                T.show(getContext(),getString(R.string.input_cancel_reason));
                return;
            }else {
                json = reasonStr;
            }
        }
        showDialog(getString(R.string.processing));
        Http.getInstance().putTaskToken(NetURL.getDropOrderV2(orderId) + "?reason=" + json, "", DropOrderEntity.class, new OnResult() {
            @Override
            public void onResult(Object entity) {
                cancelDialog();
                if (entity == null){
                    T.show(getContext(), R.string.operate_failed_agen);
                    return;
                }
                if (entity instanceof DropOrderEntity && ((DropOrderEntity) entity).isStatus()){
                    MyApplication.isRefresh = true;
                    T.show(getContext(),((DropOrderEntity) entity).getMessage());
                    Intent intent = new Intent(CancelOrderReasonActivity.this,MainAuthorizedActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    T.show(getContext(), ((DropOrderEntity) entity).getMessage());
                    return;
                }
            }
        });
    }
}
