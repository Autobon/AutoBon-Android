package cn.com.incardata.autobon;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.incardata.adapter.BillAdapter;
import cn.com.incardata.http.Http;
import cn.com.incardata.http.NetURL;
import cn.com.incardata.http.OnResult;
import cn.com.incardata.http.response.BillEntity;
import cn.com.incardata.http.response.Bill_Data_Info;
import cn.com.incardata.utils.DateCompute;
import cn.com.incardata.utils.T;

/**
 * Created by zhangming on 2016/3/7.
 * 账单
 */
public class BillActivity extends BaseActivity implements View.OnClickListener{
    private ListView mList;
    private BillAdapter billAdapter;
    private Context context;

    private ImageView iv_back;
    private List<String> mYear;  //记录账单的年份
    private Map<String,List<Bill_Data_Info>> mapList;  //key代表年份,value代表月份账单集合
    private RelativeLayout rl_item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bill_activity);
        initView();
        initData();
        setListener();
    }

    private void initView(){
        context = this;
        iv_back = (ImageView) findViewById(R.id.iv_back);
        mList = (ListView) findViewById(R.id.bill_list_view);

        mYear = new ArrayList<String>();
        mapList = new HashMap<String,List<Bill_Data_Info>>();  //初始化
    }

    private void initData(){
        Http.getInstance().getTaskToken(NetURL.BILL_URL, BillEntity.class, new OnResult() {
            @Override
            public void onResult(Object entity) {
                if(entity == null){
                    T.show(context,context.getString(R.string.info_load_failure));
                    return;
                }
                BillEntity billEntity = (BillEntity) entity;
                if(billEntity.isResult()){
                    List<Bill_Data_Info> mList = billEntity.getData().getList();
                    Map<String,List<Bill_Data_Info>> mapData = new HashMap<String, List<Bill_Data_Info>>();
                    for(int i=0;i<mList.size();i++){
                        String year = ExtractBillYear(mList.get(i));
                        if(!mYear.contains(year)){
                            mYear.add(year);
                        }
                    }
                    for(int i=0;i<mYear.size();i++){
                        List<Bill_Data_Info> yearBillList = new ArrayList<Bill_Data_Info>();  //每年的账单信息列表记录
                        for(int j=0;j<mList.size();j++){
                            String year = ExtractBillYear(mList.get(j));
                            if(mYear.get(i).equals(year)){  //同一年
                                yearBillList.add(mList.get(j));
                            }
                        }
                        mapData.put(mYear.get(i),yearBillList);
                    }
                    setInitData(mapData);
                }else{
                    T.show(context,billEntity.getMessage());
                    return;
                }
            }
        });
    }

    private void setInitData(Map<String,List<Bill_Data_Info>> mapData){
        mapList.putAll(mapData);
        billAdapter = new BillAdapter(this,mapList);
        mList.setAdapter(billAdapter);
    }

    /**
     * 提取账单年份
     * @return year
     */
    private String ExtractBillYear(Bill_Data_Info info){
        long timeStamp = info.getBillMonth(); //时间戳
        String dateTime = DateCompute.timeStampToDate(timeStamp);  //年月日(yyyy-MM-dd HH:mm:ss)
        Log.i("test","dateTime===>"+dateTime);
        String year = dateTime.substring(0,4);  //年值取出
        return year;
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
