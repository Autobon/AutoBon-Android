package cn.com.incardata.view;

import android.content.Context;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.widget.TextView;

/**
 * Created by yang on 2016/12/12.
 */
public class ListTextView extends TextView {

    public ListTextView(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        Layout layout = getLayout();
        if (layout != null){
//            int height = Flo
        }
    }
}
