package cn.com.incardata.http.response;

import java.util.List;

/**
 * Created by zhangming on 2016/3/14.
 */
public class WorkItemEntity extends BaseEntity{
    private List<WorkItem_Data> data;

    public List<WorkItem_Data> getData() {
        return data;
    }

    public void setData(List<WorkItem_Data> data) {
        this.data = data;
    }
}
