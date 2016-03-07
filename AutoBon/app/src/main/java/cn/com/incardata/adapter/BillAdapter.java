package cn.com.incardata.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import cn.com.incardata.autobon.R;
import cn.com.incardata.http.response.BillEntity;

/**
 * Created by zhangming on 2016/3/7.
 * 账单适配器(临时)
 */
public class BillAdapter extends BaseAdapter{
    private Context context;
    //TODO 临时数据
    private List<BillEntity> one_billList,two_billList;
    private static int allBillListSize;

    public BillAdapter(Context context,List<BillEntity>...billLists){
        this.context = context;
        this.one_billList = billLists[0];
        this.two_billList = billLists[1];
        allBillListSize = billLists.length;
    }

    @Override
    public int getCount() {
        return one_billList.size()+two_billList.size()+allBillListSize;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BillEntity billEntity;
        if (position == 0){
            return getTitleView("2016");
        }else if(position == (one_billList.size()+1)){
            return getTitleView("2015");
        }else if(position <= one_billList.size()){
            billEntity = one_billList.get(position-1);
        }else{
            billEntity = two_billList.get(position-1-one_billList.size()-1);
        }

        ViewHolder holder;
        if (convertView != null && convertView instanceof RelativeLayout) {
            holder = (ViewHolder) convertView.getTag();
        }else{
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.bill_item, parent, false);
            holder.tv_month = (TextView) convertView.findViewById(R.id.tv_month);
            holder.tv_money = (TextView) convertView.findViewById(R.id.tv_money);
            convertView.setTag(holder);
        }
        holder.tv_month.setText(billEntity.getMonth());
        holder.tv_money.setText(String.valueOf(billEntity.getPay()));

        return convertView;
    }

    private View getTitleView(String year){
        View view = LayoutInflater.from(context).inflate(R.layout.bill_title_item,null);
        TextView tv_year = (TextView) view.findViewById(R.id.tv_year);
        TextView tv_money = (TextView) view.findViewById(R.id.tv_money);
        tv_year.setText(year);
        double all_money = 0.0;
        for(int i=0;i<one_billList.size();i++){
            double money = one_billList.get(i).getPay();
            all_money+=money;
        }
        tv_money.setText(String.valueOf(all_money));
        return view;
    }

    static class ViewHolder{
        private TextView tv_month;
        private TextView tv_money;
    }
}
