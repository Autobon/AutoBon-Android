package cn.com.incardata.http.response;

import java.util.List;

/**
 * 查询团队技师列表解析类
 * <p>Created by wangyang on 2018/6/27.</p>
 */
public class TeamTechnicianListData {

    private int totalElements;
    private int totalPages;
    private int size;
    private int count;
    private List<TeamTechnicianData> content;

    public int getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(int totalElements) {
        this.totalElements = totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<TeamTechnicianData> getContent() {
        return content;
    }

    public void setContent(List<TeamTechnicianData> content) {
        this.content = content;
    }
}
