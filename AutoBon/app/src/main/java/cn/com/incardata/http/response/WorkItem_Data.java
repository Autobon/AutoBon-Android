package cn.com.incardata.http.response;

/**
 * Created by zhangming on 2016/3/14.
 */
public class WorkItem_Data {
    private int seat;
    private String name;
    private int id;

    private boolean isFocus;  //增加是否选中状态标志

    public WorkItem_Data(){
        setFocus(false);
    }

    public boolean isFocus() {
        return isFocus;
    }

    public void setFocus(boolean focus) {
        isFocus = focus;
    }

    public int getSeat() {
        return seat;
    }

    public void setSeat(int seat) {
        this.seat = seat;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
