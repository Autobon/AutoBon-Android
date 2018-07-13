package cn.com.incardata.http.response;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 团队列表数据
 * <p>Created by wangyang on 2018/6/26.</p>
 */
public class TeamListData implements Parcelable {
    private int id;
    private String name;
    private int managerId;
    private String managerName;
    private String managerPhone;

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

    public int getManagerId() {
        return managerId;
    }

    public void setManagerId(int managerId) {
        this.managerId = managerId;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getManagerPhone() {
        return managerPhone;
    }

    public void setManagerPhone(String managerPhone) {
        this.managerPhone = managerPhone;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeInt(this.managerId);
        dest.writeString(this.managerName);
        dest.writeString(this.managerPhone);
    }

    public TeamListData() {
    }

    protected TeamListData(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.managerId = in.readInt();
        this.managerName = in.readString();
        this.managerPhone = in.readString();
    }

    public static final Parcelable.Creator<TeamListData> CREATOR = new Parcelable.Creator<TeamListData>() {
        @Override
        public TeamListData createFromParcel(Parcel source) {
            return new TeamListData(source);
        }

        @Override
        public TeamListData[] newArray(int size) {
            return new TeamListData[size];
        }
    };
}
