package cn.com.incardata.http.response;

/**
 * Created by zhangming on 2016/2/28.
 */
public class AddContactEntity extends BaseEntity{
    private AddContact_data data;

    public AddContact_data getData() {
        return data;
    }

    public void setData(AddContact_data data) {
        this.data = data;
    }
}
