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
import cn.com.incardata.http.response.StudyGardenData;

/**
 * 学习园地资料列表适配
 * <p>Created by wangyang on 2018/6/27.</p>
 */
public class StudyDataListAdapter extends BaseAdapter {
    private Context context;
    private List<StudyGardenData> list;

    public StudyDataListAdapter(Context context, List<StudyGardenData> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        if (list == null) return 0;
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        if (list == null) return null;
        return list.get(position);
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
            view = LayoutInflater.from(context).inflate(R.layout.item_study_data_list,null);
            holder.tv_name = (TextView) view.findViewById(R.id.tv_name);
            holder.tv_type = (TextView) view.findViewById(R.id.tv_type);
            holder.tv_remark = (TextView) view.findViewById(R.id.tv_remark);

            view.setTag(holder);
        }else {
            holder = (Holder) view.getTag();
        }

        if (!TextUtils.isEmpty(list.get(position).getFileName())){
            holder.tv_name.setText(list.get(position).getFileName());
        }else {
            holder.tv_name.setText("");
        }

        switch (list.get(position).getType()){
            case 1:
                holder.tv_type.setText("培训资料");
                break;
            case 2:
                holder.tv_type.setText("施工标准");
                break;
            case 3:
                holder.tv_type.setText("业务规则");
                break;
            default:
                holder.tv_type.setText("类型错误");
                break;
        }

        if (!TextUtils.isEmpty(list.get(position).getRemark())){
            holder.tv_remark.setText(list.get(position).getRemark());
        }else {
            holder.tv_remark.setText("");
        }

        return view;
    }

    private class Holder{
        private TextView tv_name;                                   //名称
        private TextView tv_type;                                   //类型
        private TextView tv_remark;                                 //备注
    }
}
