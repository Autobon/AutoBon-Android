package cn.com.incardata.adapter;

import android.content.Context;
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
import cn.com.incardata.autobon.R;
import cn.com.incardata.http.ImageLoaderCache;
import cn.com.incardata.http.NetURL;
import cn.com.incardata.http.response.OrderInfo_Construction;
import cn.com.incardata.http.response.OrderInfo_Data;
import cn.com.incardata.utils.DateCompute;

/**
 * Created by zhangming on 2016/3/21.
 * 账单详情适配器
 */
public class BillDetailAdapter extends BaseAdapter{
    private Context context;
    private List<OrderInfo_Data> mList;

    public BillDetailAdapter(Context context,List<OrderInfo_Data> mList){
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
            holder.tv_order_num = (TextView)convertView.findViewById(R.id.tv_order_num);
            holder.orderType = (TextView) convertView.findViewById(R.id.order_type);
            holder.tv_order_money =  (TextView)convertView.findViewById(R.id.tv_order_money);
            holder.iv_order_img = (ImageView)convertView.findViewById(R.id.iv_order_img);
            holder.tv_order_time = (TextView)convertView.findViewById(R.id.tv_order_time);
            holder.tv_work_item = (TextView)convertView.findViewById(R.id.tv_work_item);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        OrderInfo_Data data = mList.get(position);
        holder.tv_order_num.setText(context.getString(R.string.order_serial_number) + data.getOrderNum());  //订单编号
        holder.orderType.setText(MyApplication.getInstance().getSkill(data.getOrderType()));
        ImageLoaderCache.getInstance().loader(NetURL.IP_PORT + data.getPhoto(), holder.iv_order_img, 0);
        holder.tv_order_time.setText(DateCompute.getDate(data.getOrderTime()));  //施工时间

        OrderInfo_Construction cons;
        if (isMainTech(data.getMainTech().getId())) {
            cons = mList.get(position).getMainConstruct();
        }else {
            cons = mList.get(position).getSecondConstruct();
        }

        if (cons == null) return convertView;

        holder.tv_order_money.setText("￥"+String.valueOf(cons.getPayment()));//支付钱数量

        if (TextUtils.isEmpty(cons.getWorkItems())) return convertView;
        String item = cons.getWorkItems();
        if (TextUtils.isEmpty(item)){
            holder.tv_work_item.setText(null);
        }else {
            if (item.contains(",")){
                String[] items = item.split(",");
                String tempItem = "";
                for (String str : items){
                    tempItem += BillDetailActivity.workItems[Integer.parseInt(str)] + ",";
                }
                holder.tv_work_item.setText(tempItem.substring(0, tempItem.length() - 1));
            }else {
                holder.tv_work_item.setText(BillDetailActivity.workItems[1]);
            }
        }

        return convertView;
    }

    private boolean isMainTech(int id) {
        return (MyApplication.getInstance().getUserId() == id);
    }

    static class ViewHolder{
        TextView tv_order_money;
        TextView tv_order_num;
        TextView orderType;
        ImageView iv_order_img;
        TextView tv_order_time;
        TextView tv_work_item;
    }
}
