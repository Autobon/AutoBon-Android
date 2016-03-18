package cn.com.incardata.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.com.incardata.autobon.R;
import cn.com.incardata.http.response.Bill_Data_Info;
import cn.com.incardata.utils.DateCompute;

/**
 * Created by zhangming on 2016/3/7.
 * 账单适配器(临时)
 */
public class BillAdapter extends BaseAdapter{
    private Context context;
    private Map<String,List<Bill_Data_Info>> mapList;  //key代表年份,value代表月份账单集合
    private List<Bill_Data_Info> allYearBills;  //所有的账单信息
    private int allSize;  //账单的条目,即构造的ListView的数目
    private List<String> mYear;  //记录年份

    public BillAdapter(Context context, Map<String,List<Bill_Data_Info>> mapList){
        this.context = context;
        this.mapList = mapList;
        initData();
    }

    private void initData(){
        this.mYear = new ArrayList<String>();
        allYearBills = new ArrayList<Bill_Data_Info>();
        Set<Map.Entry<String,List<Bill_Data_Info>>> keySets = this.mapList.entrySet();
        for(Map.Entry<String,List<Bill_Data_Info>> entry : keySets){
            List<Bill_Data_Info> dataList = entry.getValue();
            allYearBills.addAll(dataList);
            allSize += dataList.size();
        }
    }

    @Override
    public int getCount() {
        return allSize;
    }

    @Override
    public Object getItem(int position) {
        return allYearBills.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        }else{
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.bill_all_item, parent, false);
            holder.tv_year = (TextView) convertView.findViewById(R.id.tv_year);
            holder.tv_month = (TextView) convertView.findViewById(R.id.tv_month);
            holder.tv_money = (TextView) convertView.findViewById(R.id.tv_money);
            holder.tv_status = (TextView) convertView.findViewById(R.id.tv_status);
            convertView.setTag(holder);
        }
        long timeStamp = allYearBills.get(position).getBillMonth();  //时间戳
        String dateTime = DateCompute.timeStampToDate(timeStamp);  //年月日(yyyy-MM-dd HH:mm:ss)
        Log.i("test","dateTime===>"+dateTime);
        String year = dateTime.substring(0,4);  //年值取出
        if(!mYear.contains(year)){
            mYear.add(year);
        }else{
            View view = convertView.findViewById(R.id.ll_bill_title); //第一个年份标题显示,后面隐藏
            view.setVisibility(View.GONE);
        }
        String month = dateTime.substring(5,7); //月值取出
        double money = allYearBills.get(position).getSum();  //获取价钱

        holder.tv_year.setText(year);
        holder.tv_month.setText(month);
        holder.tv_money.setText(String.valueOf(money));
        if(allYearBills.get(position).isPayed()){  //已结算
            holder.tv_status.setText(context.getString(R.string.pay_text));
            holder.tv_status.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bill_pay));
        }else{
            holder.tv_status.setText(context.getString(R.string.unpay_text));
            holder.tv_status.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bill_unpay));
        }
        return convertView;
    }

    static class ViewHolder{
        private TextView tv_year;
        private TextView tv_month;
        private TextView tv_money;
        private TextView tv_status;
    }
}
