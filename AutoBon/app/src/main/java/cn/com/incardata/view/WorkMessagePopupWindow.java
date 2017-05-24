package cn.com.incardata.view;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;

import cn.com.incardata.adapter.ScrapMaterialAdapter;
import cn.com.incardata.adapter.WorkMessageAdapter;
import cn.com.incardata.autobon.R;
import cn.com.incardata.http.response.ConstructionWasteShow;
import cn.com.incardata.http.response.OrderConstructionShow;

/** 施工详情弹窗
 * Created by yang on 2016/12/2.
 */
public class WorkMessagePopupWindow extends PopupWindow implements View.OnClickListener {
    private Activity activity;
    private ListView work_message_list,scrap_material_list;
    private OrderConstructionShow[] orderConstructionShows;
    private WorkMessageAdapter workMessageAdapter;
    private ScrapMaterialAdapter scrapMaterialAdapter;
    private ConstructionWasteShow[] constructionWasteShows;
    private ImageView iv_back;

    public WorkMessagePopupWindow(Activity activity,OrderConstructionShow[] orderConstructionShows,ConstructionWasteShow[] constructionWasteShows) {
        this.activity = activity;
        this.orderConstructionShows = orderConstructionShows;
        this.constructionWasteShows = constructionWasteShows;
    }


    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            int desiredWidth= View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }


    public void init(){
        View view = LayoutInflater.from(activity).inflate(R.layout.work_message,null,false);
        this.setContentView(view);
        work_message_list = (ListView) view.findViewById(R.id.work_message_list);
        workMessageAdapter = new WorkMessageAdapter(activity,orderConstructionShows);
        work_message_list.setAdapter(workMessageAdapter);
        setListViewHeightBasedOnChildren(work_message_list);
        scrap_material_list = (ListView) view.findViewById(R.id.scrap_material_list);
        scrapMaterialAdapter = new ScrapMaterialAdapter(constructionWasteShows,activity);
        scrap_material_list.setAdapter(scrapMaterialAdapter);
        setListViewHeightBasedOnChildren(scrap_material_list);
        iv_back = (ImageView) view.findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closePopupWindow();
            }
        });


        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth((int) (activity.getWindowManager().getDefaultDisplay().getWidth() * 0.9));
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight((int) (activity.getWindowManager().getDefaultDisplay().getHeight() * 0.9));
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.SharePop);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);

        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                closePopupWindow();
            }
        });


    }
    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
        WindowManager.LayoutParams params=activity.getWindow().getAttributes();
        params.alpha=0.7f;
        activity.getWindow().setAttributes(params);
    };


    /**
     * 关闭窗口
     */
    public void closePopupWindow()
    {
        WindowManager.LayoutParams params = activity.getWindow().getAttributes();
        params.alpha=1f;
        activity.getWindow().setAttributes(params);
        dismiss();
    }

    @Override
    public void onClick(View view) {

    }
}
