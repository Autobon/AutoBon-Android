package cn.com.incardata.http.response;

/**
 * Created by zhangming on 2016/2/29.
 * 我的信息
 */
public class MyInfoEntity extends BaseEntity{
    private MyInfo_Data data;

    public MyInfo_Data getData() {
        return data;
    }

    public void setData(MyInfo_Data data) {
        this.data = data;
    }
}
