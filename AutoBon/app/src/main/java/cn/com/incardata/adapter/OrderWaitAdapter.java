package cn.com.incardata.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.com.incardata.application.MyApplication;
import cn.com.incardata.autobon.R;
import cn.com.incardata.http.ImageLoaderCache;
import cn.com.incardata.http.NetURL;
import cn.com.incardata.http.response.Order;
import cn.com.incardata.utils.DateCompute;

/**
 * Created by wanghao on 16/3/9.
 */
public class OrderWaitAdapter extends BaseAdapter{
    private Context context;
    private List<Order> orderList;

    public OrderWaitAdapter(Context context, List<Order> orderList){
        this.context = context;
        this.orderList = orderList;
    }

    @Override
    public int getCount() {
        if (orderList == null) return 0;
        return orderList.size();
    }

    @Override
    public Object getItem(int i) {
        return orderList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        Holder holder = null;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.order_wait_list_item, viewGroup, false);
            holder = new Holder();

            holder.orderType = (TextView) view.findViewById(R.id.order_type);
            holder.operate = (Button) view.findViewById(R.id.order_operate);
            holder.orderNumber = (TextView) view.findViewById(R.id.order_number);
            holder.orderTime = (TextView) view.findViewById(R.id.order_time);
            holder.orderImage = (ImageView) view.findViewById(R.id.order_image);
//            holder.hideBg = (TextView) view.findViewById(R.id.order_hide_text);

            view.setTag(holder);
        }else {
            holder = (Holder) view.getTag();
        }

        holder.orderType.setText(MyApplication.getInstance().getSkill(orderList.get(i).getOrderType()));
        holder.orderNumber.setText(R.string.order_serial_number);
        holder.orderNumber.append(orderList.get(i).getOrderNum());
        holder.orderTime.setText(R.string.order_time);
        holder.orderTime.append(DateCompute.getDate(orderList.get(i).getOrderTime()));

        holder.operate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null){
                    mListener.onClickOrder(i);
                }
            }
        });

        ImageLoaderCache.getInstance().loader(NetURL.IP_PORT + orderList.get(i).getPhoto(), holder.orderImage, 0);
        return view;
    }

    private class Holder{
        TextView orderType;
        Button operate;
        TextView orderNumber;
        TextView orderTime;
        ImageView orderImage;
        TextView hideBg;
    }

    private OnClickOrderListener mListener;

    public void setOnClickOrderListener(OnClickOrderListener mListener){
        this.mListener = mListener;
    }

    public interface OnClickOrderListener{
        void onClickOrder(int position);
    }
}
