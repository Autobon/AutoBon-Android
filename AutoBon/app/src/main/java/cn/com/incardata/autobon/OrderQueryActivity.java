package cn.com.incardata.autobon;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import java.util.HashMap;
import java.util.Map;

import cn.com.incardata.http.response.SerializableMap;

/**
 * 订单查询界面
 * <p>Created by wangyang on 2019/9/20.</p>
 */
public class OrderQueryActivity extends BaseActivity implements View.OnClickListener {
    private EditText ed_license;                    //车牌号
    private EditText ed_vin;                        //车架号

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_query);

        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        ed_license = (EditText) findViewById(R.id.ed_license);
        ed_vin = (EditText) findViewById(R.id.ed_vin);


        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.btn_query).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_query:
                query();
                break;
        }
    }

    /**
     * 查询
     */
    private void query() {
        Map<String, String> param = new HashMap<>();

        String licenseStr = ed_license.getText().toString().trim();
        if (!TextUtils.isEmpty(licenseStr)) {
            param.put("license", licenseStr);
        }

        String vinStr = ed_vin.getText().toString().trim();
        if (!TextUtils.isEmpty(vinStr)) {
            param.put("vin", vinStr);
        }

        Intent intent = new Intent();
        SerializableMap map = new SerializableMap();
        map.setMap(param);
        Bundle bundle = new Bundle();
        bundle.putSerializable("map", map);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }
}
