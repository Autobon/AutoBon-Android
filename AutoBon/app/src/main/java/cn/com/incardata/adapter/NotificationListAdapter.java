package cn.com.incardata.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.com.incardata.autobon.R;
import cn.com.incardata.http.response.Message_Data;
import cn.com.incardata.utils.DateCompute;

/**
 * 通知消息列表
 * Created by wanghao on 16/4/14.
 */
public class NotificationListAdapter extends BaseAdapter{
    private Context context;
    private List<Message_Data.Message_Data_List> mList;

    public NotificationListAdapter(Context context, List<Message_Data.Message_Data_List> mList){
        this.context = context;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.notification_list_item, parent, false);
            holder = new Holder();
            holder.messageTitle = (TextView) convertView.findViewById(R.id.message_title);
            holder.messageContent = (TextView) convertView.findViewById(R.id.message_content);
            holder.messageTime = (TextView) convertView.findViewById(R.id.message_time);

            convertView.setTag(holder);
        }else {
            holder = (Holder) convertView.getTag();
        }

        holder.messageTitle.setText(mList.get(position).getTitle());
        holder.messageContent.setText(mList.get(position).getContent());
        holder.messageTime.setText(DateCompute.getDate(mList.get(position).getPublishTime()));
        return convertView;
    }

    private class Holder{
        TextView messageTitle;
        TextView messageContent;
        TextView messageTime;
    }
}
