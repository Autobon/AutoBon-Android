package cn.com.incardata.autobon;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

/**
 * Created by zhangming on 2016/3/21.
 * 账单详情
 */
public class BillDetailActivity extends Activity{
    private ListView mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bill_order_activity);
        initView();
    }

    private void initView(){
        mList = (ListView) findViewById(R.id.bill_list_view);
    }
}
