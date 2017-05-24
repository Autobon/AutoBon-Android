package cn.com.incardata.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.com.incardata.autobon.R;
import cn.com.incardata.http.response.StandardCommission;

/**
 * 佣金标准适配
 * Created by yang on 2017/2/17.
 */
public class StandardCommissionAdapter extends BaseAdapter {
    private Context context;
    //    private List<StandardCommission> list;
    private String[] workName;
    private String[] workMoney;


    public StandardCommissionAdapter(Context context) {
        this.context = context;
        workName = context.getResources().getStringArray(R.array.work_name);
        workMoney = context.getResources().getStringArray(R.array.work_money);
    }

    @Override
    public int getCount() {
        return workName.length;
    }

    @Override
    public Object getItem(int position) {
        return workName[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        Holder holder = null;
        if (view == null) {
            holder = new Holder();
            view = LayoutInflater.from(context).inflate(R.layout.standard_commission_list_item, viewGroup, false);
            holder.work_name = (TextView) view.findViewById(R.id.work_name);
            holder.work_money = (TextView) view.findViewById(R.id.work_money);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }

        holder.work_name.setText(workName[position]);
        holder.work_money.setText(workMoney[position]);
        return view;
    }

    class Holder {
        private TextView work_name, work_money;
    }
}
