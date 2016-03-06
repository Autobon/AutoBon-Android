package cn.com.incardata.http.response;

/**
 * Created by zhangming on 2016/3/6.
 */
public class StartWorkEntity extends BaseEntity{
    private StartWork_Data data;

    public StartWork_Data getData() {
        return data;
    }

    public void setData(StartWork_Data data) {
        this.data = data;
    }
}
