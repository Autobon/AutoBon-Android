package cn.com.incardata.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.com.incardata.autobon.R;
import cn.com.incardata.http.ImageLoaderCache;
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
            holder.tv_order_money =  (TextView)convertView.findViewById(R.id.tv_order_money);
            holder.iv_order_img = (ImageView)convertView.findViewById(R.id.iv_order_img);
            holder.tv_order_time = (TextView)convertView.findViewById(R.id.tv_order_time);
            holder.tv_work_item = (TextView)convertView.findViewById(R.id.tv_work_item);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        OrderInfo_Data data = mList.get(position);
        String orderNum = data.getOrderNum();  //订单编号
        int payment = data.getMainConstruct().getPayment();  //支付钱数量
        String photoUrl = data.getPhoto();  //照片url
        String orderTime = DateCompute.timeStampToDate(data.getOrderTime());  //施工时间
        String workItems = data.getMainConstruct().getWorkItems();  //施工部位

        holder.tv_order_num.setText(orderNum);
        holder.tv_order_money.setText("￥"+String.valueOf(payment));
        ImageLoaderCache.getInstance().loader(photoUrl,holder.iv_order_img,false);
        holder.tv_order_time.setText(orderTime);
        holder.tv_work_item.setText(workItems);

        return convertView;
    }

    static class ViewHolder{
        TextView tv_order_money;
        TextView tv_order_num;
        ImageView iv_order_img;
        TextView tv_order_time;
        TextView tv_work_item;
    }
}
