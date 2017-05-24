package cn.com.incardata.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.apache.http.client.methods.HttpOptions;

import cn.com.incardata.autobon.R;
import cn.com.incardata.http.response.ProjectPosition;

/** 施工详情列表适配
 * Created by yang on 2016/12/2.
 */
public class WorkMessageItemAdapter extends BaseAdapter {
    private Activity activity;
    private ProjectPosition[] projectPositions;

    public WorkMessageItemAdapter(Activity activity,ProjectPosition[] projectPositions) {
        this.activity = activity;
        this.projectPositions = projectPositions;
    }

    @Override
    public int getCount() {
        return projectPositions.length;
    }

    @Override
    public Object getItem(int i) {
        return projectPositions[i];
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Holder holder = null;
        if (view == null){
            holder = new Holder();
            view = LayoutInflater.from(activity).inflate(R.layout.work_message_item_message,viewGroup,false);
            holder.workItem = (TextView) view.findViewById(R.id.work_item);
            holder.workType = (TextView) view.findViewById(R.id.work_type);
            view.setTag(holder);
        }else{
            holder = (Holder) view.getTag();
        }
        ProjectPosition projectPosition = projectPositions[i];
        holder.workType.setText(projectPosition.getProject() + "：");
        holder.workItem.setText(projectPosition.getPosition());
        return view;
    }

    class Holder{
        TextView workType;
        TextView workItem;
    }
}
