package cn.com.incardata.http.response;

import android.os.Parcel;
import android.os.Parcelable;

/** 查询已收藏的商户列表界面
 * Created by yang on 2017/5/19.
 */
public class CollectionShop_Data implements Parcelable {
    private int id;
    private int technicianId;
    private long createTime;
    private Cooperator cooperator;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTechnicianId() {
        return technicianId;
    }

    public void setTechnicianId(int technicianId) {
        this.technicianId = technicianId;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public Cooperator getCooperator() {
        return cooperator;
    }

    public void setCooperator(Cooperator cooperator) {
        this.cooperator = cooperator;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.technicianId);
        dest.writeLong(this.createTime);
        dest.writeParcelable(this.cooperator, flags);
    }

    public CollectionShop_Data() {
    }

    protected CollectionShop_Data(Parcel in) {
        this.id = in.readInt();
        this.technicianId = in.readInt();
        this.createTime = in.readLong();
        this.cooperator = in.readParcelable(Cooperator.class.getClassLoader());
    }

    public static final Creator<CollectionShop_Data> CREATOR = new Creator<CollectionShop_Data>() {
        @Override
        public CollectionShop_Data createFromParcel(Parcel source) {
            return new CollectionShop_Data(source);
        }

        @Override
        public CollectionShop_Data[] newArray(int size) {
            return new CollectionShop_Data[size];
        }
    };
}
