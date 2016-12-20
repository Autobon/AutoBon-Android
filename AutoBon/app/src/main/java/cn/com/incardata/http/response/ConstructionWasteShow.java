package cn.com.incardata.http.response;

import android.os.Parcel;
import android.os.Parcelable;

/** 报废材料实体类
 * Created by yang on 2016/12/8.
 */
public class ConstructionWasteShow implements Parcelable {
    private int id;
    private String techName;
    private int techId;
    private int orderId;
    private int project;
    private int position;
    private int total;
    private String projectName;
    private String postitionName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTechName() {
        return techName;
    }

    public void setTechName(String techName) {
        this.techName = techName;
    }

    public int getTechId() {
        return techId;
    }

    public void setTechId(int techId) {
        this.techId = techId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getProject() {
        return project;
    }

    public void setProject(int project) {
        this.project = project;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getPostitionName() {
        return postitionName;
    }

    public void setPostitionName(String postitionName) {
        this.postitionName = postitionName;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.techName);
        dest.writeInt(this.techId);
        dest.writeInt(this.orderId);
        dest.writeInt(this.project);
        dest.writeInt(this.position);
        dest.writeInt(this.total);
        dest.writeString(this.projectName);
        dest.writeString(this.postitionName);
    }

    public ConstructionWasteShow() {
    }

    protected ConstructionWasteShow(Parcel in) {
        this.id = in.readInt();
        this.techName = in.readString();
        this.techId = in.readInt();
        this.orderId = in.readInt();
        this.project = in.readInt();
        this.position = in.readInt();
        this.total = in.readInt();
        this.projectName = in.readString();
        this.postitionName = in.readString();
    }

    public static final Creator<ConstructionWasteShow> CREATOR = new Creator<ConstructionWasteShow>() {
        @Override
        public ConstructionWasteShow createFromParcel(Parcel source) {
            return new ConstructionWasteShow(source);
        }

        @Override
        public ConstructionWasteShow[] newArray(int size) {
            return new ConstructionWasteShow[size];
        }
    };
}
