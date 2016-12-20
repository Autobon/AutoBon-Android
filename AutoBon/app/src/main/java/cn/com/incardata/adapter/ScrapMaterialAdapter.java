package cn.com.incardata.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.apache.http.client.methods.HttpOptions;

import cn.com.incardata.autobon.R;
import cn.com.incardata.http.response.ConstructionWasteShow;

/** 报废材料显示适配
 * Created by yang on 2016/12/8.
 */
public class ScrapMaterialAdapter extends BaseAdapter {
    private ConstructionWasteShow[] constructionWasteShows;
    private Activity context;

    public ScrapMaterialAdapter(ConstructionWasteShow[] constructionWasteShows, Activity context) {
        this.constructionWasteShows = constructionWasteShows;
        this.context = context;
    }

    @Override
    public int getCount() {
        return constructionWasteShows.length;
    }

    @Override
    public Object getItem(int position) {
        return constructionWasteShows[position];
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
            view = LayoutInflater.from(context).inflate(R.layout.scrap_material_list_item,viewGroup,false);
            holder.name = (TextView) view.findViewById(R.id.name);
            holder.project = (TextView) view.findViewById(R.id.project);
            holder.position = (TextView) view.findViewById(R.id.position);
            view.setTag(holder);
        }else{
            holder = (Holder) view.getTag();
        }

        ConstructionWasteShow constructionWasteShow = constructionWasteShows[position];
        holder.name.setText(constructionWasteShow.getTechName());
        holder.project.setText(constructionWasteShow.getProjectName());
        holder.position.setText(constructionWasteShow.getPostitionName() + " × " + constructionWasteShow.getTotal());

        return view;
    }



    class Holder{
        private TextView name;
        private TextView project;
        private TextView position;
    }
}
