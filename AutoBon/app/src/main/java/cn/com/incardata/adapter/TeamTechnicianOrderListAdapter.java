package cn.com.incardata.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.com.incardata.autobon.R;
import cn.com.incardata.http.response.OrderInfo;
import cn.com.incardata.utils.DateCompute;

/**
 * 团队技师订单列表适配
 * <p>Created by wangyang on 2018/6/27.</p>
 */
public class TeamTechnicianOrderListAdapter extends BaseAdapter {
    private Context context;
    private List<OrderInfo> mList;

    public TeamTechnicianOrderListAdapter(Context context, List<OrderInfo> mList) {
        this.context = context;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        if (mList == null) return 0;
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        if (mList == null) return null;
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_team_technician_order_list, parent, false);
            holder = new Holder();

            holder.status = (TextView) convertView.findViewById(R.id.status);
            holder.orderNum = (TextView) convertView.findViewById(R.id.order_number);
            holder.buttons[0] = (TextView) convertView.findViewById(R.id.btn1);
            holder.buttons[1] = (TextView) convertView.findViewById(R.id.btn2);
            holder.buttons[2] = (TextView) convertView.findViewById(R.id.btn3);
            holder.buttons[3] = (TextView) convertView.findViewById(R.id.btn4);
            holder.workTime = (TextView) convertView.findViewById(R.id.work_time);

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        holder.orderNum.setText(R.string.order_serial_number);
        holder.orderNum.append(mList.get(position).getOrderNum());
        holder.workTime.setText(DateCompute.getDate(mList.get(position).getAgreedStartTime()));
        String[] types = (mList.get(position).getType()).split(",");
        for (int i = 0; i < 4; i++) {
            if (i < types.length) {
                holder.buttons[i].setVisibility(View.VISIBLE);
                holder.buttons[i].setText(getProject(types[i]));
            } else {
                holder.buttons[i].setVisibility(View.INVISIBLE);
            }
        }

        if ("TAKEN_UP".equals(mList.get(position).getStatus())) {//进入开始工作
            holder.status.setText(context.getResources().getString(R.string.receiver_text));
            holder.status.setTextColor(context.getResources().getColor(R.color.main_orange));
        } else if ("IN_PROGRESS".equals(mList.get(position).getStatus())) {//进入签到
            holder.status.setText(context.getResources().getString(R.string.yetdepart));
            holder.status.setTextColor(context.getResources().getColor(R.color.main_orange));
        } else if ("SIGNED_IN".equals(mList.get(position).getStatus())) {
            //进入工作前照片上传
            holder.status.setText(R.string.yetsignin);
            holder.status.setTextColor(context.getResources().getColor(R.color.main_orange));
        } else if ("AT_WORK".equals(mList.get(position).getStatus())) {
            //进入工作后照片上传
            holder.status.setText(R.string.working);
            holder.status.setTextColor(context.getResources().getColor(R.color.main_orange));
        } else if ("FINISHED".equals(mList.get(position).getStatus())) {
            holder.status.setText(R.string.uncommented);
        } else if ("COMMENTED".equals(mList.get(position).getStatus())) {
            holder.status.setText(R.string.yetcommented);
            holder.status.setTextColor(context.getResources().getColor(R.color.main_orange));
        } else if ("CANCELED".equals(mList.get(position).getStatus())) {//撤单
            holder.status.setText(context.getResources().getString(R.string.yetcancel));
            holder.status.setTextColor(context.getResources().getColor(R.color.darkgray));
        } else if ("EXPIRED".equals(mList.get(position).getStatus()) || "CANCELED".equals(mList.get(position).getStatus())) {//撤单
            holder.status.setText(context.getResources().getString(R.string.yetovertime));
            holder.status.setTextColor(context.getResources().getColor(R.color.darkgray));
        } else if ("GIVEN_UP".equals(mList.get(position).getStatus())) {
            holder.status.setText(context.getResources().getString(R.string.yetrenounce));
            holder.status.setTextColor(context.getResources().getColor(R.color.darkgray));
        } else {
            holder.status.setText("状态错误");
            holder.status.setTextColor(context.getResources().getColor(R.color.darkgray));
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
        TextView status;                                    //订单状态
        TextView orderNum;                                  //订单编号
        TextView[] buttons = new TextView[4];               //施工项目
        TextView workTime;                                  //预约施工时间
    }
}
