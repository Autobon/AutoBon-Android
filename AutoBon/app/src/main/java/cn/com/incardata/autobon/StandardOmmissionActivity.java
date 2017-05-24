package cn.com.incardata.autobon;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import cn.com.incardata.adapter.StandardCommissionAdapter;
import cn.com.incardata.http.response.StandardCommission;

/** 佣金标准界面
 * Created by yang on 2017/2/17.
 */
public class StandardOmmissionActivity extends BaseActivity {
    private ImageView iv_back;
    private ListView standard_commission_list;
    private StandardCommissionAdapter adapter;
    private List<StandardCommission> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standard_ommission);
        initView();
    }

    public void initView(){
        list = new ArrayList<>();

        iv_back = (ImageView) findViewById(R.id.iv_back);
        standard_commission_list = (ListView) findViewById(R.id.standard_commission_list);
        adapter = new StandardCommissionAdapter(getContext());
        standard_commission_list.setAdapter(adapter);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
