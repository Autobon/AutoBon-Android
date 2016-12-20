package cn.com.incardata.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import cn.com.incardata.application.MyApplication;
import cn.com.incardata.autobon.EnlargementActivity;
import cn.com.incardata.autobon.OrderReceiveActivity;
import cn.com.incardata.autobon.R;
import cn.com.incardata.autobon.WorkBeforeActivity;
import cn.com.incardata.autobon.WorkFinishActivity;
import cn.com.incardata.autobon.WorkSignInActivity;
import cn.com.incardata.fragment.ForceStartWorkDialogFragment;
import cn.com.incardata.http.ImageLoaderCache;
import cn.com.incardata.http.NetURL;
import cn.com.incardata.http.response.OrderInfo;
import cn.com.incardata.http.response.OrderInfo_Data;
import cn.com.incardata.http.response.UnfinishOrder;
import cn.com.incardata.utils.AutoCon;
import cn.com.incardata.utils.DateCompute;

/**
 * 已抢订单
 * Created by wanghao on 16/3/9.
 */
public class OrderUnfinishedAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<OrderInfo> mList;

    public OrderUnfinishedAdapter(Context context, ArrayList<OrderInfo> mList) {
        this.context = context;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        if (mList == null) return 0;
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        Holder holder = null;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_unfinished_item, viewGroup, false);
            holder = new Holder();

            holder.operate = (Button) view.findViewById(R.id.order_operate);
//            holder.orderType = (TextView) view.findViewById(R.id.order_type);
            holder.orderNumber = (TextView) view.findViewById(R.id.order_number);
            holder.orderTime = (TextView) view.findViewById(R.id.order_time);
            holder.types[0] = (TextView) view.findViewById(R.id.btn1);
            holder.types[1] = (TextView) view.findViewById(R.id.btn2);
            holder.types[2] = (TextView) view.findViewById(R.id.btn3);
            holder.types[3] = (TextView) view.findViewById(R.id.btn4);
            holder.warn = (ImageView) view.findViewById(R.id.warn);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }

        String[] type = (mList.get(position).getType()).split(",");
//
        for (int i = 0; i < 4; i++){
            if (i < type.length){
                holder.types[i].setVisibility(View.VISIBLE);
                holder.types[i].setText(getProject(type[i]));
            }else {
                holder.types[i].setVisibility(View.INVISIBLE);
            }
        }


        holder.orderNumber.setText(R.string.order_serial_number);
        holder.orderNumber.append(mList.get(position).getOrderNum());
        holder.orderTime.setText(R.string.order_time);
        holder.orderTime.append(DateCompute.getDate(mList.get(position).getAgreedStartTime()));
        if ((mList.get(position).getAgreedStartTime() - System.currentTimeMillis()) < 60 * 60 * 1000 && (mList.get(position).getAgreedStartTime() - System.currentTimeMillis()) > 0){
            holder.warn.setVisibility(View.VISIBLE);
        }else {
            holder.warn.setVisibility(View.GONE);
        }

        if ("TAKEN_UP".equals(mList.get(position).getStatus())) {//进入开始工作
            holder.operate.setText(R.string.start_work);
        } else {
            holder.operate.setText(R.string.inputOrder);
        }

        holder.operate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onClickOrder(position);
                }
            }
        });

        return view;
    }

    private class Holder {
        Button operate;
        TextView orderNumber;
        TextView orderTime;
        private TextView[] types = new TextView[4];
        ImageView warn;
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

    private class ImageOnclick extends AsInnerOnclick {
        public ImageOnclick(int position) {
            super(position);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, EnlargementActivity.class);
            Bundle bundle = new Bundle();
            bundle.putStringArray("IMAGE_URL", new String[]{mList.get(getPosition()).getPhoto()});
            intent.putExtras(bundle);
            context.startActivity(intent);
//            context.overridePendingTransition(R.anim.anim_image_enter, R.anim.anim_image_quit);
        }
    }

    ;

    private OnClickOrderListener mListener;

    public void setOnClickOrderListener(OnClickOrderListener mListener) {
        this.mListener = mListener;
    }

    public interface OnClickOrderListener {
        void onClickOrder(int position);
    }
}
