package cn.com.incardata.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.com.incardata.application.MyApplication;
import cn.com.incardata.autobon.BillDetailActivity;
import cn.com.incardata.autobon.EnlargementActivity;
import cn.com.incardata.autobon.R;
import cn.com.incardata.http.ImageLoaderCache;
import cn.com.incardata.http.NetURL;
import cn.com.incardata.http.response.BillOrderList;
import cn.com.incardata.http.response.OrderInfo_Construction;
import cn.com.incardata.http.response.OrderInfo_Data;
import cn.com.incardata.utils.DateCompute;

/**
 * Created by zhangming on 2016/3/21.
 * 账单详情适配器
 */
public class BillDetailAdapter extends BaseAdapter{
    private Context context;
    private List<BillOrderList> mList;

    public BillDetailAdapter(Context context,List<BillOrderList> mList){
        this.context = context;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.bill_detail_item, parent, false);
            holder.money = (TextView) convertView.findViewById(R.id.money);
            holder.bill_source = (TextView) convertView.findViewById(R.id.bill_source);
            holder.orderNum = (TextView) convertView.findViewById(R.id.order_number);
            holder.buttons[0] = (TextView) convertView.findViewById(R.id.btn1);
            holder.buttons[1] = (TextView) convertView.findViewById(R.id.btn2);
            holder.buttons[2] = (TextView) convertView.findViewById(R.id.btn3);
            holder.buttons[3] = (TextView) convertView.findViewById(R.id.btn4);
            holder.workTime = (TextView) convertView.findViewById(R.id.work_time);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        BillOrderList billOrderList = mList.get(position);
        if (billOrderList.getSource() == 1){
            holder.bill_source.setVisibility(View.VISIBLE);
        }else {
            holder.bill_source.setVisibility(View.GONE);
        }
        if (billOrderList.getOrderNum() == null){
            holder.orderNum.setText(R.string.order_serial_number);
        }else {
            holder.orderNum.setText(R.string.order_serial_number);
            holder.orderNum.append(billOrderList.getOrderNum());
        }
        holder.workTime.setText(DateCompute.getDate(billOrderList.getCreateDate()));
        if (billOrderList.getPayment() == null){
            holder.money.setText("合计:" + context.getResources().getString(R.string.RMB) + 0);
        }else {
            holder.money.setText("合计:" + context.getResources().getString(R.string.RMB) + billOrderList.getPayment());
        }

        if (billOrderList.getProject1() != null){
            holder.buttons[0].setVisibility(View.VISIBLE);
            holder.buttons[0].setText(getProject(String.valueOf(billOrderList.getProject1())));
            holder.buttons[1].setVisibility(View.INVISIBLE);
            holder.buttons[2].setVisibility(View.INVISIBLE);
            holder.buttons[3].setVisibility(View.INVISIBLE);
        }
        if (billOrderList.getProject2() != null){
            holder.buttons[0].setVisibility(View.VISIBLE);
            holder.buttons[1].setVisibility(View.VISIBLE);
            holder.buttons[0].setText(getProject(String.valueOf(billOrderList.getProject1())));
            holder.buttons[1].setText(getProject(String.valueOf(billOrderList.getProject2())));
            holder.buttons[2].setVisibility(View.INVISIBLE);
            holder.buttons[3].setVisibility(View.INVISIBLE);
        }
        if (billOrderList.getProject3() != null){
            holder.buttons[0].setVisibility(View.VISIBLE);
            holder.buttons[1].setVisibility(View.VISIBLE);
            holder.buttons[2].setVisibility(View.VISIBLE);
            holder.buttons[0].setText(getProject(String.valueOf(billOrderList.getProject1())));
            holder.buttons[1].setText(getProject(String.valueOf(billOrderList.getProject2())));
            holder.buttons[2].setText(getProject(String.valueOf(billOrderList.getProject3())));
            holder.buttons[3].setVisibility(View.INVISIBLE);
        }
        if (billOrderList.getProject4() != null){
            holder.buttons[0].setVisibility(View.VISIBLE);
            holder.buttons[1].setVisibility(View.VISIBLE);
            holder.buttons[2].setVisibility(View.VISIBLE);
            holder.buttons[3].setVisibility(View.VISIBLE);
            holder.buttons[0].setText(getProject(String.valueOf(billOrderList.getProject1())));
            holder.buttons[1].setText(getProject(String.valueOf(billOrderList.getProject2())));
            holder.buttons[2].setText(getProject(String.valueOf(billOrderList.getProject3())));
            holder.buttons[3].setText(getProject(String.valueOf(billOrderList.getProject4())));
        }



        return convertView;
    }

    public String getProject(String type) {
        if ("1".equals(type)) {
            return "隔热膜";
        } else if ("2".equals(type)) {
            return "隐形车衣";
        } else if ("3".equals(type)) {
            return "车身改色";
        } else if ("4".equals(type)) {
            return "美容清洁";
        } else
            return null;
    }


    static class ViewHolder{

        TextView money,bill_source;
        TextView orderNum;
        TextView[] buttons = new TextView[4];
        TextView workTime;
    }
}
