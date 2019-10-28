package cn.com.incardata.http.response;

import java.util.List;

/**
 * 商户报价产品列表数据
 * <p>Created by wangyang on 2019/10/9.</p>
 */
public class ProductList_Data {
    private List<ProductData> list;
    public List<ProductData> getList() {
        return list;
    }

    public void setList(List<ProductData> list) {
        this.list = list;
    }
}
