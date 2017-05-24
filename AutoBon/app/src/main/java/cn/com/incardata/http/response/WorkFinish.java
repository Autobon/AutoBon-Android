package cn.com.incardata.http.response;

/**二期 工作完成提交
 * Created by yang on 2016/11/7.
 */
public class WorkFinish {
    private int orderId;
    private String afterPhotos;
    private ConstructionDetail[] constructionDetails;
    private ConsumeItem[] constructionWastes;


    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getAfterPhotos() {
        return afterPhotos;
    }

    public void setAfterPhotos(String afterPhotos) {
        this.afterPhotos = afterPhotos;
    }

    public ConstructionDetail[] getConstructionDetails() {
        return constructionDetails;
    }

    public void setConstructionDetails(ConstructionDetail[] constructionDetails) {
        this.constructionDetails = constructionDetails;
    }

    public ConsumeItem[] getConstructionWastes() {
        return constructionWastes;
    }

    public void setConstructionWastes(ConsumeItem[] constructionWastes) {
        this.constructionWastes = constructionWastes;
    }
}
