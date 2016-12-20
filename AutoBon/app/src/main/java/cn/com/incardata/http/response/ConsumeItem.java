package cn.com.incardata.http.response;

/** 二期 材料耗损实体类
 * Created by yang on 2016/11/7.
 */
public class ConsumeItem {
    private String techId;
    private String project;
    private String position;
    private String total = "0";


    public String getTechId() {
        return techId;
    }

    public void setTechId(String techId) {
        this.techId = techId;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
