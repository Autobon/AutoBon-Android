package cn.com.incardata.http.response;

import android.os.Parcel;

/**
 * Created by zhangming on 2016/3/10.
 * 订单详细信息
 */
public class OrderInfoEntity extends BaseEntity{
    private OrderInfo_Data data;

    public OrderInfo_Data getData() {
        return data;
    }

    public void setData(OrderInfo_Data data) {
        this.data = data;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeParcelable(this.data, flags);
    }

    public OrderInfoEntity() {
    }

    protected OrderInfoEntity(Parcel in) {
        super(in);
        this.data = in.readParcelable(OrderInfo_Data.class.getClassLoader());
    }

    public static final Creator<OrderInfoEntity> CREATOR = new Creator<OrderInfoEntity>() {
        @Override
        public OrderInfoEntity createFromParcel(Parcel source) {
            return new OrderInfoEntity(source);
        }

        @Override
        public OrderInfoEntity[] newArray(int size) {
            return new OrderInfoEntity[size];
        }
    };
}
