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
import cn.com.incardata.http.response.OrderInfo;
import cn.com.incardata.utils.DateCompute;

/** 未被抢的单列表适配
 * Created by wanghao on 16/3/9.
 */
public class OrderWaitAdapter extends BaseAdapter{
    private Context context;
    private List<OrderInfo> orderList;

    public OrderWaitAdapter(Context context, List<OrderInfo> orderList){
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
    public View getView(final int position, View view, ViewGroup viewGroup) {
        Holder holder = null;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.order_wait_list_item, viewGroup, false);
            holder = new Holder();

//            holder.orderType = (TextView) view.findViewById(R.id.order_type);
            holder.operate = (Button) view.findViewById(R.id.order_operate);
            holder.orderNumber = (TextView) view.findViewById(R.id.order_number);
            holder.orderTime = (TextView) view.findViewById(R.id.order_time);
            holder.types[0] = (TextView) view.findViewById(R.id.btn1);
            holder.types[1] = (TextView) view.findViewById(R.id.btn2);
            holder.types[2] = (TextView) view.findViewById(R.id.btn3);
            holder.types[3] = (TextView) view.findViewById(R.id.btn4);

//            holder.orderImage = (ImageView) view.findViewById(R.id.order_image);
//            holder.hideBg = (TextView) view.findViewById(R.id.order_hide_text);

            view.setTag(holder);
        }else {
            holder = (Holder) view.getTag();
        }


        String[] type = (orderList.get(position).getType()).split(",");
        for (int i = 0; i < 4; i++){
            if (i < type.length){
                holder.types[i].setVisibility(View.VISIBLE);
                holder.types[i].setText(getProject(type[i]));
            }else {
                holder.types[i].setVisibility(View.INVISIBLE);
            }
        }
//        holder.orderType.setText(type);
        holder.orderNumber.setText(R.string.order_serial_number);
        holder.orderNumber.append(orderList.get(position).getOrderNum());
        holder.orderTime.setText(R.string.order_time);
        holder.orderTime.append(DateCompute.getDate(orderList.get(position).getAgreedStartTime()));

        holder.operate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null){
                    mListener.onClickOrder(position);
                }
            }
        });

//        ImageLoaderCache.getInstance().loader(NetURL.IP_PORT + orderList.get(i).getPhoto(), holder.orderImage, 0);
        return view;
    }

    private class Holder{
//        TextView orderType;
        Button operate;
        TextView orderNumber;
        TextView orderTime;

        private TextView[] types = new TextView[4];
//        ImageView orderImage;
//        TextView hideBg;
    }

    private OnClickOrderListener mListener;

    public void setOnClickOrderListener(OnClickOrderListener mListener){
        this.mListener = mListener;
    }

    public interface OnClickOrderListener{
        void onClickOrder(int position);
    }

    public String getProject(String type){
        if ("1".equals(type)){
            return "隔热膜";
        }else if ("2".equals(type)){
            return "隐形车衣";
        }else if ("3".equals(type)){
            return "车身改色";
        }else if ("4".equals(type)){
            return "美容清洁";
        }else
            return null;
    }
}
