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
import cn.com.incardata.http.response.TeamTechnicianData;

/**
 * 团队技师列表适配
 * <p>Created by wangyang on 2018/6/27.</p>
 */
public class TeamTechnicianAdapter extends BaseAdapter {
    private Context context;
    private List<TeamTechnicianData> list;

    public TeamTechnicianAdapter(Context context, List<TeamTechnicianData> list) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_team_technician_list, parent, false);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.phone = (TextView) convertView.findViewById(R.id.phone);
            holder.status = (TextView) convertView.findViewById(R.id.status);
            holder.filmLevelNum = (TextView) convertView.findViewById(R.id.filmLevelNum);
            holder.carCoverLevelNum = (TextView) convertView.findViewById(R.id.carCoverLevelNum);
            holder.colorModifyLevelNum = (TextView) convertView.findViewById(R.id.colorModifyLevelNum);
            holder.beautyLevelNum = (TextView) convertView.findViewById(R.id.beautyLevelNum);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        TeamTechnicianData technicianMessage = list.get(position);
        if (!TextUtils.isEmpty(technicianMessage.getName())){
            holder.name.setText(technicianMessage.getName());
        }else {
            holder.name.setText("");
        }
        if (!TextUtils.isEmpty(technicianMessage.getPhone())){
            holder.phone.setText(technicianMessage.getPhone());
        }else {
            holder.phone.setText("");
        }

        switch (technicianMessage.getWorkStatus()){
            case 1:
                holder.status.setText("可接单");
                break;
            case 2:
                holder.status.setText("工作中");
                break;
            case 3:
                holder.status.setText("休息中");
                break;
            default:
                holder.status.setText("状态错误");
                break;
        }
        holder.filmLevelNum.setText(technicianMessage.getFilmLevel() + "星");
        holder.carCoverLevelNum.setText(technicianMessage.getCarCoverLevel() + "星");
        holder.colorModifyLevelNum.setText(technicianMessage.getColorModifyLevel() + "星");
        holder.beautyLevelNum.setText(technicianMessage.getBeautyLevel() + "星");

        return convertView;
    }

    static class ViewHolder {
        private TextView name;                                  //姓名
        private TextView phone;                                 //手机号
        private TextView status;                                //工作状态
        private TextView filmLevelNum;                          //隔热膜星级
        private TextView carCoverLevelNum;                      //隐形车衣星级
        private TextView colorModifyLevelNum;                   //车身改色星级
        private TextView beautyLevelNum;                        //美容清洁星级
    }

}
