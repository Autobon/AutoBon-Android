package cn.com.incardata.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.com.incardata.autobon.BillDetailActivity;
import cn.com.incardata.autobon.R;
import cn.com.incardata.http.response.Bill_Data_Info;
import cn.com.incardata.utils.DateCompute;

/**
 * Created by zhangming on 2016/3/7.
 * 账单适配器(临时)
 */
public class BillAdapter extends BaseAdapter{
    private Context context;
    private RelativeLayout rl_item;
    private Map<String,List<Bill_Data_Info>> mapList;  //key代表年份,value代表月份账单集合
    private List<Bill_Data_Info> allYearBills;  //所有的账单信息
    private int allSize;  //账单的条目,即构造的ListView的数目
    private Map<String, Integer> mYear;  //记录年份
    private Map<String,String> monthMap;

    public BillAdapter(Context context, Map<String,List<Bill_Data_Info>> mapList){
        this.context = context;
        this.mapList = mapList;
        initData();
    }

    private void initData(){
        this.mYear = new HashMap<String, Integer>();
        allYearBills = new ArrayList<Bill_Data_Info>();

        Set<Map.Entry<String,List<Bill_Data_Info>>> keySets = this.mapList.entrySet();
        for(Map.Entry<String,List<Bill_Data_Info>> entry : keySets){
            List<Bill_Data_Info> dataList = entry.getValue();
            allYearBills.addAll(dataList);
            allSize += dataList.size();
        }

        monthMap = new HashMap<String,String>();
        monthMap.put("01","一月");
        monthMap.put("02","二月");
        monthMap.put("03","三月");
        monthMap.put("04","四月");
        monthMap.put("05","五月");
        monthMap.put("06","六月");
        monthMap.put("07","七月");
        monthMap.put("08","八月");
        monthMap.put("09","九月");
        monthMap.put("10","十月");
        monthMap.put("11","十一月");
        monthMap.put("12","十二月");
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
    public View getView(final int position, View convertView, ViewGroup parent) {
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
            holder.tv_all_money = (TextView) convertView.findViewById(R.id.tv_all_money);
            holder.rl_item = (RelativeLayout) convertView.findViewById(R.id.rl_item);
            convertView.setTag(holder);
        }
        long timeStamp = allYearBills.get(position).getBillMonth();  //时间戳
        String dateTime = DateCompute.timeStampToDate(timeStamp);  //年月日(yyyy-MM-dd HH:mm:ss)
        Log.i("test","dateTime===>"+dateTime);
        String year = dateTime.substring(0,4);  //年值取出

        if(mYear.containsKey(year)) {
            if (mYear.get(year) == position){
                convertView.findViewById(R.id.ll_bill_title).setVisibility(View.VISIBLE);
            }else{
                convertView.findViewById(R.id.ll_bill_title).setVisibility(View.GONE); //第一个年份标题显示,后面隐藏
            }
        }else{
            mYear.put(year, position);
            List<Bill_Data_Info> mList = this.mapList.get(year);  //获取一年的账单集合
            double sum = 0;
            for(int i=0;i<mList.size();i++){
                sum += mList.get(i).getSum();
            }
            holder.tv_all_money.setText("￥"+sum);
        }
        String month = dateTime.substring(5,7); //月值取出
        double money = allYearBills.get(position).getSum();  //获取价钱

        holder.tv_year.setText(year + context.getString(R.string.year_text));
        holder.tv_month.setText(monthMap.get(month));
        holder.tv_money.setText("￥"+String.valueOf(money));
        if(allYearBills.get(position).isPaid()){  //已结算
            holder.tv_status.setBackgroundResource(R.drawable.bill_pay);
            holder.tv_status.setText(context.getString(R.string.pay_text));
            holder.tv_status.setTextColor(context.getResources().getColor(R.color.main_white));
        }else{
            holder.tv_status.setBackgroundResource(R.drawable.bill_unpay);
            holder.tv_status.setText(context.getString(R.string.unpay_text));
            holder.tv_status.setTextColor(context.getResources().getColor(R.color.lightgray));
        }
        final int padding = context.getResources().getDimensionPixelOffset(R.dimen.dp10);
        holder.tv_status.setPadding(padding,0,padding,0);

        holder.rl_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, BillDetailActivity.class);
                intent.putExtra("BillID", allYearBills.get(position).getId());
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    static class ViewHolder{
        private TextView tv_year;
        private TextView tv_month;
        private TextView tv_money;
        private TextView tv_status;
        private TextView tv_all_money;
        private RelativeLayout rl_item;
    }
}
