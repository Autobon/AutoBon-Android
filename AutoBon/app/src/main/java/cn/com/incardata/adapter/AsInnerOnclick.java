package cn.com.incardata.adapter;

import android.view.View;

/**
 * Created by wanghao on 16/6/28.
 */
public abstract class AsInnerOnclick implements View.OnClickListener {
    private int position;

    public AsInnerOnclick() {
    }

    public AsInnerOnclick(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
