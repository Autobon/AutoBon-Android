package cn.com.incardata.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import cn.com.incardata.application.MyApplication;
import cn.com.incardata.autobon.MyOrderActivity;
import cn.com.incardata.autobon.R;
import cn.com.incardata.http.ImageLoaderCache;
import cn.com.incardata.http.NetURL;
import cn.com.incardata.http.response.OrderInfo;
import cn.com.incardata.http.response.OrderInfo_Construction;
import cn.com.incardata.http.response.OrderInfo_Data;
import cn.com.incardata.utils.DateCompute;

/** 已完成订单列表适配
 * Created by wanghao on 16/3/21.
 */
public class OrderFinishedAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<OrderInfo> mList;
    private boolean isMainResponsible;

    private int main_orange_color;
    private int darkgray_color;
    private String RMB;

    public OrderFinishedAdapter(Context context, boolean isMainResponsible, ArrayList<OrderInfo> mList) {
        this.context = context;
        this.isMainResponsible = isMainResponsible;
        this.mList = mList;
        main_orange_color = context.getResources().getColor(R.color.main_orange);
        darkgray_color = context.getResources().getColor(R.color.darkgray);
        RMB = context.getResources().getString(R.string.RMB);
    }

    @Override
    public int getCount() {
        if (mList == null) return 0;
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Activity activity = (Activity) context;
        Holder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.finished_list_item, parent, false);
            holder = new Holder();

            holder.money = (TextView) convertView.findViewById(R.id.money);
            holder.moneyState = (TextView) convertView.findViewById(R.id.money_state);
            holder.orderNum = (TextView) convertView.findViewById(R.id.order_number);
            holder.tv_license = (TextView) convertView.findViewById(R.id.tv_license);
            holder.tv_vin = (TextView) convertView.findViewById(R.id.tv_vin);
            holder.buttons[0] = (TextView) convertView.findViewById(R.id.btn1);
            holder.buttons[1] = (TextView) convertView.findViewById(R.id.btn2);
            holder.buttons[2] = (TextView) convertView.findViewById(R.id.btn3);
            holder.buttons[3] = (TextView) convertView.findViewById(R.id.btn4);
//            holder.orderImage = (ImageView) convertView.findViewById(R.id.order_image);
            holder.workTime = (TextView) convertView.findViewById(R.id.work_time);
//            holder.workItem = (TextView) convertView.findViewById(R.id.work_item);

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        holder.orderNum.setText(R.string.order_serial_number);
        holder.orderNum.append(mList.get(position).getOrderNum());
        holder.tv_license.setText(R.string.license_str);
        holder.tv_vin.setText(R.string.vin_str);
        if (!TextUtils.isEmpty(mList.get(position).getLicense())){
            holder.tv_license.append(mList.get(position).getLicense());
        }else {
            holder.tv_license.append("");
        }
        if (!TextUtils.isEmpty(mList.get(position).getVin())){
            holder.tv_vin.append(mList.get(position).getVin());
        }else {
            holder.tv_vin.append("");
        }

        holder.workTime.setText(DateCompute.getDate(mList.get(position).getStartTime()));
        String[] types = (mList.get(position).getType()).split(",");
        for (int i = 0; i < 4; i++){
            if (i < types.length){
                holder.buttons[i].setVisibility(View.VISIBLE);
                holder.buttons[i].setText(getProject(types[i]));
            }else {
                holder.buttons[i].setVisibility(View.INVISIBLE);
            }
        }
//        type = type.substring(0,type.length() - 1);


//        holder.workItem.setText(type);
//        if (mList.get(position).getPayStatus() == 0){
//
//        }else if (mList.get(position).getPayStatus() == 0)


        if (mList.get(position).getStatus().equals("CANCELED")) {
            holder.money.setText(activity.getResources().getString(R.string.RMB) + 0);
            holder.moneyState.setText(R.string.yetcancel);
            holder.moneyState.setTextColor(activity.getResources().getColor(R.color.darkgray));
            holder.workTime.setText(R.string.no);
//            holder.workItem.setText("无");
        } else if (mList.get(position).getStatus().equals("GIVEN_UP")) {
            holder.money.setText(activity.getResources().getString(R.string.RMB) + 0);
            holder.moneyState.setText(R.string.yetrenounce);
            holder.moneyState.setTextColor(activity.getResources().getColor(R.color.darkgray));
            holder.workTime.setText(R.string.no);
//            holder.workItem.setText("无");
        } else if (mList.get(position).getStatus().equals("EXPIRED")) {
            holder.money.setText(activity.getResources().getString(R.string.RMB) + 0);
            holder.moneyState.setText(R.string.yetovertime);
            holder.moneyState.setTextColor(activity.getResources().getColor(R.color.darkgray));
            holder.workTime.setText(R.string.no);
//            holder.workItem.setText("无");
        } else {
            if (mList.get(position).getPayStatus() == null || mList.get(position).getPayStatus() == 0){
                holder.money.setVisibility(View.GONE);
                holder.moneyState.setText(R.string.no_calculate);
                holder.moneyState.setTextColor(context.getResources().getColor(R.color.gray_A3));
            }else if(mList.get(position).getPayStatus() == 1){
                holder.money.setVisibility(View.VISIBLE);
                holder.money.setText(activity.getString(R.string.count) + RMB + mList.get(position).getPayment());
                holder.moneyState.setText(R.string.pay_wait);
                holder.moneyState.setTextColor(context.getResources().getColor(R.color.gray_A3));
                holder.money.setTextColor(context.getResources().getColor(R.color.gray_A3));
            }else if (mList.get(position).getPayStatus() == 2){
                holder.money.setVisibility(View.VISIBLE);
                holder.money.setText(activity.getString(R.string.count) + RMB + mList.get(position).getPayment());
                holder.moneyState.setText(R.string.pay_done);
                holder.moneyState.setTextColor(context.getResources().getColor(R.color.main_orange));
                holder.money.setTextColor(context.getResources().getColor(R.color.gray_A3));
            }
        }

        return convertView;
    }

    public String getProject(String type) {
        if ("1".equals(type)) {
            return context.getString(R.string.ge);
        } else if ("2".equals(type)) {
            return context.getString(R.string.yin);
        } else if ("3".equals(type)) {
            return context.getString(R.string.che);
        } else if ("4".equals(type)) {
            return context.getString(R.string.mei);
        } else
            return null;
    }

    private class Holder {
        TextView money;
        TextView moneyState;
        TextView orderNum;
        TextView tv_license;
        TextView tv_vin;
        TextView[] buttons = new TextView[4];
//        ImageView orderImage;
        TextView workTime;

    }
}
