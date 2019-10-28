package cn.com.incardata.http.response;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 报价列表数据
 * <p>Created by wangyang on 2019/10/9.</p>
 */
public class ProductData implements Parcelable {
    private int id;
    private int coopId;
    private String name;
    private int type;
    private String brand;
    private String code;
    private String model;
    private int constructionPosition;
    private String constructionPositionName;
    private int workingHours;
    private int constructionCommission;
    private int starLevel;
    private int scrapCost;
    private int warranty;
    private int price;

    private int technicianId = -1;
    private int total = 0;
    private boolean isCheck = false;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCoopId() {
        return coopId;
    }

    public void setCoopId(int coopId) {
        this.coopId = coopId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getConstructionPosition() {
        return constructionPosition;
    }

    public void setConstructionPosition(int constructionPosition) {
        this.constructionPosition = constructionPosition;
    }

    public String getConstructionPositionName() {
        return constructionPositionName;
    }

    public void setConstructionPositionName(String constructionPositionName) {
        this.constructionPositionName = constructionPositionName;
    }

    public int getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(int workingHours) {
        this.workingHours = workingHours;
    }

    public int getConstructionCommission() {
        return constructionCommission;
    }

    public void setConstructionCommission(int constructionCommission) {
        this.constructionCommission = constructionCommission;
    }

    public int getStarLevel() {
        return starLevel;
    }

    public void setStarLevel(int starLevel) {
        this.starLevel = starLevel;
    }

    public int getScrapCost() {
        return scrapCost;
    }

    public void setScrapCost(int scrapCost) {
        this.scrapCost = scrapCost;
    }

    public int getWarranty() {
        return warranty;
    }

    public void setWarranty(int warranty) {
        this.warranty = warranty;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getTechnicianId() {
        return technicianId;
    }

    public void setTechnicianId(int technicianId) {
        this.technicianId = technicianId;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.coopId);
        dest.writeString(this.name);
        dest.writeInt(this.type);
        dest.writeString(this.brand);
        dest.writeString(this.code);
        dest.writeString(this.model);
        dest.writeInt(this.constructionPosition);
        dest.writeString(this.constructionPositionName);
        dest.writeInt(this.workingHours);
        dest.writeInt(this.constructionCommission);
        dest.writeInt(this.starLevel);
        dest.writeInt(this.scrapCost);
        dest.writeInt(this.warranty);
        dest.writeInt(this.price);
        dest.writeInt(this.technicianId);
        dest.writeInt(this.total);
        dest.writeByte(this.isCheck ? (byte) 1 : (byte) 0);
    }

    public ProductData() {
    }

    protected ProductData(Parcel in) {
        this.id = in.readInt();
        this.coopId = in.readInt();
        this.name = in.readString();
        this.type = in.readInt();
        this.brand = in.readString();
        this.code = in.readString();
        this.model = in.readString();
        this.constructionPosition = in.readInt();
        this.constructionPositionName = in.readString();
        this.workingHours = in.readInt();
        this.constructionCommission = in.readInt();
        this.starLevel = in.readInt();
        this.scrapCost = in.readInt();
        this.warranty = in.readInt();
        this.price = in.readInt();
        this.technicianId = in.readInt();
        this.total = in.readInt();
        this.isCheck = in.readByte() != 0;
    }

    public static final Creator<ProductData> CREATOR = new Creator<ProductData>() {
        @Override
        public ProductData createFromParcel(Parcel source) {
            return new ProductData(source);
        }

        @Override
        public ProductData[] newArray(int size) {
            return new ProductData[size];
        }
    };
}
