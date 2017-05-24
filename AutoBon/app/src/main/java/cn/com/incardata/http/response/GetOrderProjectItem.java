package cn.com.incardata.http.response;

/** 获取施工项目和部位 二期
 * Created by yang on 2016/11/14.
 */
public class GetOrderProjectItem {
    private int id;
    private String name;
    private ConstructionPosition[] constructionPositions;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ConstructionPosition[] getConstructionPositions() {
        return constructionPositions;
    }

    public void setConstructionPositions(ConstructionPosition[] constructionPositions) {
        this.constructionPositions = constructionPositions;
    }
}
