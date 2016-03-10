package cn.com.incardata.http.response;

/**
 * Created by zhangming on 2016/3/10.
 */
public class OrderInfo_Data {
    private MyInfo_Data mainTech;
    private MyInfo_Data secondTech;
    private OrderInfo_Construction construction;
    private String comment;
    private Order order;

    public MyInfo_Data getMainTech() {
        return mainTech;
    }

    public void setMainTech(MyInfo_Data mainTech) {
        this.mainTech = mainTech;
    }

    public MyInfo_Data getSecondTech() {
        return secondTech;
    }

    public void setSecondTech(MyInfo_Data secondTech) {
        this.secondTech = secondTech;
    }

    public OrderInfo_Construction getConstruction() {
        return construction;
    }

    public void setConstruction(OrderInfo_Construction construction) {
        this.construction = construction;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
