package cn.com.incardata.adapter;

import android.content.Context;

import java.util.List;

import cn.com.incardata.view.wheel.widget.adapters.AbstractWheelTextAdapter;

/**
 * Created by wanghao on 16/5/6.
 */
public class ArrayWheelAdapter extends AbstractWheelTextAdapter{
    private List<String> mlist;

    public ArrayWheelAdapter(Context context, int itemResource, int itemTextResource) {
        super(context, itemResource, itemTextResource);
    }

    @Override
    protected CharSequence getItemText(int index) {
        if (index >= 0 && index < mlist.size()) {
            String item = mlist.get(index);
            if (item instanceof CharSequence) {
                return (CharSequence) item;
            }
            return item.toString();
        }
        return null;
    }

    @Override
    public int getItemsCount() {
        return mlist == null ? 0 : mlist.size();
    }

    public void updateData(List<String> mlist){
        this.mlist = mlist;
        notifyDataInvalidatedEvent();
    }
}
