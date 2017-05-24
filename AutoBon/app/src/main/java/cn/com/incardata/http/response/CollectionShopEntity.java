package cn.com.incardata.http.response;

import java.util.List;

/** 查询已收藏的商户列表数据
 * Created by yang on 2017/5/19.
 */
public class CollectionShopEntity {
    private int page;
    private int totalElements;
    private int totalPages;
    private int pageSize;
    private int count;
    private List<CollectionShop_Data> list;

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

    public List<CollectionShop_Data> getList() {
        return list;
    }

    public void setList(List<CollectionShop_Data> list) {
        this.list = list;
    }
}
