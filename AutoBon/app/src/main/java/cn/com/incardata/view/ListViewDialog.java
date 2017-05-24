package cn.com.incardata.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import cn.com.incardata.adapter.DialogListViewAdapter;
import cn.com.incardata.autobon.R;

/** 弃用
 * Created by yang on 2016/11/15.
 */
public class ListViewDialog extends Dialog {
    private Context context;
    private ListView lv_dialog;
    private List<Integer> list;
    private DialogListViewAdapter dialogListViewAdapter;
    private int position = -1;


    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public ListViewDialog(Context context, List<Integer> list) {
        super(context);
        this.context = context;
        this.list = list;
        initView();
    }


    public void initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_listview);
        lv_dialog = (ListView) findViewById(R.id.lv_dialog);


        Window window = getWindow();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();

        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER;
        params.height = (int) (height * 0.4); // 高度设置为屏幕的0.6
        params.width = (int) (width); // 宽度设置为屏幕的0.65
        window.setAttributes(params);

        dialogListViewAdapter = new DialogListViewAdapter(list, context);
        lv_dialog.setAdapter(dialogListViewAdapter);
//        lv_dialog.setOnItemClickListener(onItemClickListener);
    }

//    private AdapterView.OnItemClickListener onItemClickListener;
    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener){
//        this.onItemClickListener = onItemClickListener;
        lv_dialog.setOnItemClickListener(onItemClickListener);
    }
}
