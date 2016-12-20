package cn.com.incardata.adapter;

import android.content.Context;
import android.content.SyncRequest;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import java.util.List;

import cn.com.incardata.autobon.R;

/**
 * Created by yang on 2016/11/15.
 */
public class DialogListViewAdapter extends BaseAdapter {
    private List<Integer> list;
    private Context context;

    public DialogListViewAdapter(List<Integer> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null){
            holder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.dialog_listview_item,viewGroup,false);
            holder.tv_number = (TextView) view.findViewById(R.id.tv_number);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }
        holder.tv_number.setText(String.valueOf(list.get(position)));

        return view;
    }



    class ViewHolder{
        private TextView tv_number;
    }
}
