package cn.com.incardata.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import cn.com.incardata.autobon.MyOrderActivity;
import cn.com.incardata.autobon.R;
import cn.com.incardata.http.ImageLoaderCache;
import cn.com.incardata.http.NetURL;
import cn.com.incardata.http.response.OrderInfo_Construction;
import cn.com.incardata.http.response.OrderInfo_Data;
import cn.com.incardata.utils.DateCompute;

/**
 * Created by wanghao on 16/3/21.
 */
public class OrderFinishedAdapter extends BaseAdapter{
    private Context context;
    private ArrayList<OrderInfo_Data> mList;
    private boolean isMainResponsible;

    private int main_orange_color;
    private int darkgray_color;
    private String RMB;

    public OrderFinishedAdapter(Context context, boolean isMainResponsible, ArrayList<OrderInfo_Data> mList){
        this.context = context;
        this.isMainResponsible = isMainResponsible;
        this.mList = mList;
        main_orange_color =  context.getResources().getColor(R.color.main_orange);
        darkgray_color = context.getResources().getColor(R.color.darkgray);
        RMB = context.getResources().getString(R.string.RMB);
    }

    @Override
    public int getCount() {
        if (mList == null) return 10;
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
        Holder holder = null;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.finished_list_item, parent, false);
            holder = new Holder();

            holder.money = (TextView) convertView.findViewById(R.id.money);
            holder.moneyState = (TextView) convertView.findViewById(R.id.money_state);
            holder.orderNum = (TextView) convertView.findViewById(R.id.order_number);
            holder.orderImage = (ImageView) convertView.findViewById(R.id.order_image);
            holder.workTime = (TextView) convertView.findViewById(R.id.work_time);
            holder.workItem = (TextView) convertView.findViewById(R.id.work_item);

            convertView.setTag(holder);
        }else {
            holder = (Holder) convertView.getTag();
        }

        holder.orderNum.setText(R.string.order_serial_number);
        holder.orderNum.append(mList.get(position).getOrderNum());
        ImageLoaderCache.getInstance().loader(NetURL.IP_PORT + mList.get(position).getPhoto(), holder.orderImage, 0);
        holder.workTime.setText(DateCompute.getDate(mList.get(position).getOrderTime()));

        OrderInfo_Construction comment;
        if (isMainResponsible) {
            comment = mList.get(position).getMainConstruct();
        }else {
            comment = mList.get(position).getSecondConstruct();
        }
        holder.money.setText(RMB + comment.getPayment());
        if (comment.getPayStatus() == 2){
            holder.moneyState.setText(R.string.pay_done);
            holder.moneyState.setTextColor(main_orange_color);
        }else {
            holder.moneyState.setText(R.string.pay_wait);
            holder.moneyState.setTextColor(darkgray_color);
        }

        String item = comment.getWorkItems();
        if (TextUtils.isEmpty(item)){
            holder.workItem.setText(null);
        }else {
            if (item.contains(",")){
                String[] items = item.split(",");
                String tempItem = "";
                for (String str : items){
                    tempItem += MyOrderActivity.workItems[Integer.parseInt(str)] + ",";
                }
                holder.workItem.setText(tempItem.substring(0, tempItem.length() - 1));
            }else {
                holder.workItem.setText(MyOrderActivity.workItems[1]);
            }
        }
        return convertView;
    }

    private class Holder{
        TextView money;
        TextView moneyState;
        TextView orderNum;
        ImageView orderImage;
        TextView workTime;
        TextView workItem;
    }
}
