package cn.com.incardata.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.com.incardata.autobon.R;
import cn.com.incardata.http.response.TeamListData;

/**
 * 我的团队列表适配
 * <p>Created by wangyang on 2018/6/26.</p>
 */
public class MyTeamAdapter extends BaseAdapter {
    private Context context;
    private List<TeamListData> list;

    public MyTeamAdapter(Context context, List<TeamListData> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        if (list != null){
            return list.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (list != null){
            return list.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        Holder holder = null;
        if (view == null){
            holder = new Holder();
            view = LayoutInflater.from(context).inflate(R.layout.item_my_team_list,null);
            holder.tv_team_name = (TextView) view.findViewById(R.id.tv_team_name);
            holder.tv_captain_name = (TextView) view.findViewById(R.id.tv_captain_name);
            holder.tv_captain_phone = (TextView) view.findViewById(R.id.tv_captain_phone);

            view.setTag(holder);
        }else {
            holder = (Holder) view.getTag();
        }

        if (!TextUtils.isEmpty(list.get(position).getName())){
            holder.tv_team_name.setText(list.get(position).getName());
        }else {
            holder.tv_team_name.setText("");
        }
        if (!TextUtils.isEmpty(list.get(position).getManagerName())){
            holder.tv_captain_name.setText(list.get(position).getManagerName());
        }else {
            holder.tv_captain_name.setText("");
        }
        if (!TextUtils.isEmpty(list.get(position).getManagerPhone())){
            holder.tv_captain_phone.setText(list.get(position).getManagerPhone());
        }else {
            holder.tv_captain_phone.setText("");
        }

        return view;
    }


    private class Holder{
        private TextView tv_team_name;                                  //团队名称
        private TextView tv_captain_name;                               //队长姓名
        private TextView tv_captain_phone;                              //队长电话
    }
}
