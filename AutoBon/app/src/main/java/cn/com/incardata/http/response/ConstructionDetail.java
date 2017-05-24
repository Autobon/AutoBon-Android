package cn.com.incardata.http.response;

import java.util.List;

/** 工作完成 施工部位
 * Created by yang on 2016/11/3.
 */
public class ConstructionDetail {
    private int techId;
    private List<ProjectPositions> projectPositions;

    public int getTechId() {
        return techId;
    }

    public void setTechId(int techId) {
        this.techId = techId;
    }

    public List<ProjectPositions> getProjectPositions() {
        return projectPositions;
    }

    public void setProjectPositions(List<ProjectPositions> projectPositions) {
        this.projectPositions = projectPositions;
    }
}
