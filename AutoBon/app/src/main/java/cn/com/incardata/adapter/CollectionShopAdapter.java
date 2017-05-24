package cn.com.incardata.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import cn.com.incardata.autobon.R;
import cn.com.incardata.http.response.CollectionShop_Data;

/**
 * 已收藏的商户列表适配
 * Created by yang on 2017/5/19.
 */
public class CollectionShopAdapter extends BaseAdapter {
    private Context context;
    private List<CollectionShop_Data> list;

    public CollectionShopAdapter(Context context, List<CollectionShop_Data> list) {
        this.context = context;
        this.list = list;
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
    public View getView(final int position, View view, ViewGroup viewGroup) {
        Holder holder = null;
        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.list_item_collection_shop,null);
            holder = new Holder();
            holder.shop_name = (TextView) view.findViewById(R.id.shop_name);
            holder.shop_corporation = (TextView) view.findViewById(R.id.shop_corporation);
            holder.shop_phone = (TextView) view.findViewById(R.id.shop_phone);
            holder.delete = (Button) view.findViewById(R.id.delete);
            view.setTag(holder);
        }else {
            holder = (Holder) view.getTag();
        }

        if (!TextUtils.isEmpty(list.get(position).getCooperator().getFullname())){
            holder.shop_name.setText(list.get(position).getCooperator().getFullname());
        }else {
            holder.shop_name.setText(context.getString(R.string.no));
        }

        if (!TextUtils.isEmpty(list.get(position).getCooperator().getCorporationName())){
            holder.shop_corporation.setText(list.get(position).getCooperator().getCorporationName());
        }else {
            holder.shop_corporation.setText(context.getString(R.string.no));
        }

        if (!TextUtils.isEmpty(list.get(position).getCooperator().getContactPhone())){
            holder.shop_phone.setText(list.get(position).getCooperator().getContactPhone());
        }else {
            holder.shop_phone.setText(context.getString(R.string.no));
        }

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listeren != null){
                    listeren.onClick(position);
                }
            }
        });

        return view;
    }


    private class Holder{
        private TextView shop_name;                     //商户名称
        private TextView shop_corporation;              //商户法人
        private TextView shop_phone;                    //联系电话
        private Button delete;                          //移除按钮
    }

    private ClickListeren listeren;

    public void setListeren(ClickListeren listeren) {
        this.listeren = listeren;
    }

    public interface ClickListeren{
        void onClick(int position);
    }
}
