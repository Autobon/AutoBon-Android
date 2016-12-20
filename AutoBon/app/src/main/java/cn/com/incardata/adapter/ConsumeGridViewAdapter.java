package cn.com.incardata.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import cn.com.incardata.autobon.R;
import cn.com.incardata.http.response.ConstructionPosition;
import cn.com.incardata.http.response.ConsumeItem;
import cn.com.incardata.http.response.GetOrderProjectItem;
import cn.com.incardata.utils.T;
import cn.com.incardata.view.ListViewDialog;
import cn.com.incardata.view.wheel.widget.WheelPopupWindow;

/**报废材料适配
 * Created by yang on 2016/11/4.
 */
public class ConsumeGridViewAdapter extends BaseAdapter {
//    private List<ConstructionPosition> items;
    private ConstructionPosition[] items;
    private Activity context;
    private List<ConsumeItem> consumeItems;
    private int checkId;
    private List<String> list;
    private int check;
    private int pojectId;


    public List<ConsumeItem> getConsumeItems() {
        return consumeItems;
    }

    public void setConsumeItems(List<ConsumeItem> consumeItems) {
        this.consumeItems = consumeItems;
    }

    public ConsumeGridViewAdapter(ConstructionPosition[] items,int pojectId, Activity context) {
        this.items = items;
        this.context = context;
        this.pojectId = pojectId;
        consumeItems = new ArrayList<ConsumeItem>();
    }

    @Override
    public int getCount() {
        return items.length;
    }

    @Override
    public Object getItem(int position) {
        return items[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
       list = new ArrayList<String>();
        for (int i = 0; i <= 5; i++){
            list.add(String.valueOf(i));
        }
        final ConstructionPosition item = items[position];
        View view1 = View.inflate(context, R.layout.rg_tab_grid_item, null);
        Button button = (Button) view1.findViewById(R.id.rg_btn);
        if (item.getTotal() == 0){
            button.setText(item.getName());
            button.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.corner_default_btn));
            button.setTextColor(context.getResources().getColor(R.color.gray_A3));
        }else {
            button.setText(item.getName() + "×" + item.getTotal());
            button.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.corner_choice_btn));
            button.setTextColor(context.getResources().getColor(R.color.main_white));
            if (button.getText().toString().length() > 6){
                String text = button.getText().toString().trim();
                button.setText(text.substring(0,2) + "··" + text.substring(text.length() - 3,text.length()));
            }
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check = position;
                showPopupWindow(list,item.getTotal());
            }
        });
        return view1;
    }


    private WheelPopupWindow popup;
    private void showPopupWindow(List<String> list,int curIndex){
        if (popup == null) {
            popup = new WheelPopupWindow(context);
            popup.init();
            popup.setListener(checkedListener);
        }
        popup.setData(list,curIndex);
        popup.showAtLocation(context.findViewById(R.id.scroll_finish), Gravity.BOTTOM, 0, 0);
    }

    private WheelPopupWindow.OnCheckedListener checkedListener = new WheelPopupWindow.OnCheckedListener() {
        @Override
        public void onChecked(int index) {
            ConsumeItem consumeItem = new ConsumeItem();
            items[check].setTotal(index);
            notifyDataSetChanged();
//            T.show(context,list.get(index));
//            switch (type){
//                case BRAND:
//                    if (index == (Integer) carBrand.getTag()) return;
//                    if (brandList != null && !brandList.isEmpty()) {
//                        carBrand.setText(brand.get(index));
//                        carBrand.setTag(index);
//                    }
//                    getSeries(brandList.get(index).getBrandCode());
//                    break;
//                case SERIES:
//                    if (seriesList != null && !seriesList.isEmpty()){
//                        carSeries.setText(series.get(index));
//                        carSeries.setTag(index);
//                    }
//                    break;
//                case ENGINE:
//                    engineType.setText(list.get(index));
//                    engineType_unit.setText(index == 0 ? "L" : "T");
//                    break;
//            }

        }
    };




}
