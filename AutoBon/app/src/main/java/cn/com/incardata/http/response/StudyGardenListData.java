package cn.com.incardata.http.response;

import java.util.List;

/**
 * 学习园地数据解析类
 * <p>Created by wangyang on 2018/6/27.</p>
 */
public class StudyGardenListData {
    private int page;
    private int totalElements;
    private int totalPages;
    private int pageSize;
    private int count;
    private List<StudyGardenData> list;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

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

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<StudyGardenData> getList() {
        return list;
    }

    public void setList(List<StudyGardenData> list) {
        this.list = list;
    }
}
