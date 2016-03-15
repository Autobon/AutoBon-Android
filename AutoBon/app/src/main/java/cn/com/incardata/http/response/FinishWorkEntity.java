package cn.com.incardata.http.response;

/**
 * Created by zhangming on 2016/3/15.
 */
public class FinishWorkEntity extends BaseEntity{
    private FinishWork_Data data;

    public FinishWork_Data getData() {
        return data;
    }

    public void setData(FinishWork_Data data) {
        this.data = data;
    }
}
