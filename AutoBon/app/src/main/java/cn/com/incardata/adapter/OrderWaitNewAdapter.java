package cn.com.incardata.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;

import java.util.List;

import cn.com.incardata.autobon.R;
import cn.com.incardata.http.response.CollectionShop_Data;
import cn.com.incardata.http.response.OrderInfo;
import cn.com.incardata.utils.BaiduMapUtil;
import cn.com.incardata.utils.DateCompute;
import cn.com.incardata.utils.DecimalUtil;

/** 未被抢的单列表适配
 * Created by wanghao on 16/3/9.
 */
public class OrderWaitNewAdapter extends BaseAdapter{
    private Context context;
    private List<OrderInfo> orderList;
    private List<CollectionShop_Data> collectionShopList;
    private LatLng techLocation;

    public OrderWaitNewAdapter(Context context, List<OrderInfo> orderList, List<CollectionShop_Data> collectionShopList){
        this.context = context;
        this.orderList = orderList;
        this.collectionShopList = collectionShopList;
    }

    public void setTechLocation(LatLng techLocation) {
        this.techLocation = techLocation;
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
            view = LayoutInflater.from(context).inflate(R.layout.order_wait_list_item_new, viewGroup, false);
            holder = new Holder();

            holder.operate = (Button) view.findViewById(R.id.order_operate);
            holder.order_info = (TextView) view.findViewById(R.id.order_info);

            view.setTag(holder);
        }else {
            holder = (Holder) view.getTag();
        }

//        if (collectionShopList != null && collectionShopList.size() > 0){
//            for (int i = 0; i < collectionShopList.size(); i++){
//                if (orderList.get(position).getCoopId() == collectionShopList.get(i).getCooperator().getId()){
//                    holder.img_collection.setImageDrawable(context.getResources().getDrawable(R.mipmap.img_rank));
//                    break;
//                }else {
//                    holder.img_collection.setImageDrawable(context.getResources().getDrawable(R.mipmap.img_rank_default));
//                }
//            }
//        }else {
//            holder.img_collection.setImageDrawable(context.getResources().getDrawable(R.mipmap.img_rank_default));
//        }

        String type = "";
        Log.e("orderType",orderList.get(position).getType() + "");
        String[] types = (orderList.get(position).getType()).split(",");
        for (int i = 0; i < types.length; i++){
            type = type + getProject(types[i]) + ",";
        }
        type = type.substring(0,type.length() - 1);
        String shopName = orderList.get(position).getCoopName();
        String address = orderList.get(position).getAddress();
        double distance;
        if (techLocation != null){
            if (!TextUtils.isEmpty(orderList.get(position).getLatitude()) && !TextUtils.isEmpty(orderList.get(position).getLongitude())){
                distance = BaiduMapUtil.getDistance(techLocation, new LatLng(Double.parseDouble(orderList.get(position).getLatitude()), Double.parseDouble(orderList.get(position).getLongitude())));
                distance = DecimalUtil.DoubleDecimal1(distance/1000);
            }else {
                distance = 0;
            }
        }else {
            distance = 0;
        }
        String workTimeStr = DateCompute.getDate(orderList.get(position).getAgreedStartTime());
        String agreeEndTime = DateCompute.getDate(orderList.get(position).getAgreedEndTime());

//        String orderInfoStr = "施工项目：" + type + "，商户名称：" + shopName + "，地址：" + address + "，距离"
//                + distance + "公里，预约施工时间：" + workTimeStr + "，最迟交车时间：" + agreeEndTime + "。";

        String orderInfoStr = "施工项目：" + type + "\n商户名称：" + shopName + "\n地址：" + address + "\n距离："
                + distance + "公里\n预约施工时间：" + workTimeStr + "\n最迟交车时间：" + agreeEndTime;
        holder.order_info.setText(orderInfoStr);

        holder.operate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null){
                    mListener.onClickOrder(position);
                }
            }
        });

        return view;
    }

    private class Holder{
        Button operate;
        TextView order_info;
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
