package cn.com.incardata.autobon;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import cn.com.incardata.adapter.BillAdapter;
import cn.com.incardata.http.response.BillEntity;

/**
 * Created by zhangming on 2016/3/7.
 * 账单
 */
public class BillActivity extends BaseActivity implements View.OnClickListener{
    private ListView mList;
    private BillAdapter billAdapter;
    private List<BillEntity> billList;
    private List<BillEntity> two_billList;

    private ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bill_activity);
        initView();
        initData();
        setListener();
    }

    private void initView(){
        billList = new ArrayList<BillEntity>();
        two_billList = new ArrayList<BillEntity>();

        iv_back = (ImageView) findViewById(R.id.iv_back);
        mList = (ListView) findViewById(R.id.bill_list_view);
        billAdapter = new BillAdapter(this,billList,two_billList);
        mList.setAdapter(billAdapter);
    }

    private void initData(){
        BillEntity be_one = new BillEntity();
        be_one.setMonth("二月");
        be_one.setPay(5790.00);
        BillEntity be_two = new BillEntity();
        be_two.setMonth("一月");
        be_two.setPay(2790.00);
        billList.add(be_one);
        billList.add(be_two);

        BillEntity be_three = new BillEntity();
        be_three.setMonth("十二月");
        be_three.setPay(6790.00);
        BillEntity be_four = new BillEntity();
        be_four.setMonth("十一月");
        be_four.setPay(3780.00);
        BillEntity be_five = new BillEntity();
        be_five.setMonth("十月");
        be_five.setPay(2780.00);
        two_billList.add(be_three);
        two_billList.add(be_four);
        two_billList.add(be_five);

        billAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
        }
    }

    private void setListener(){
        iv_back.setOnClickListener(this);
    }
}
