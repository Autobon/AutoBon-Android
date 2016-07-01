package cn.com.incardata.http.response;

import android.os.Parcel;
import android.os.Parcelable;

/**商户信息
 * Created by wanghao on 16/6/29.
 */
public class OrderInfo_Cooperator implements Parcelable {
    private int id;
    private String fullname;
    private String businessLicense;
    private String corporationName;
    private String corporationIdNo;
    private String bussinessLicensePic;
    private String corporationIdPicA;
    private String corporationIdPicB;
    private String longitude;
    private String latitude;
    private String invoiceHeader;
    private String taxIdNo;
    private String postcode;
    private String province;
    private String city;
    private String district;
    private String address;
    private String contact;
    private String contactPhone;
    private double createTime;
    private int statusCode;
    private int orderNum;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getBusinessLicense() {
        return businessLicense;
    }

    public void setBusinessLicense(String businessLicense) {
        this.businessLicense = businessLicense;
    }

    public String getCorporationName() {
        return corporationName;
    }

    public void setCorporationName(String corporationName) {
        this.corporationName = corporationName;
    }

    public String getCorporationIdNo() {
        return corporationIdNo;
    }

    public void setCorporationIdNo(String corporationIdNo) {
        this.corporationIdNo = corporationIdNo;
    }

    public String getBussinessLicensePic() {
        return bussinessLicensePic;
    }

    public void setBussinessLicensePic(String bussinessLicensePic) {
        this.bussinessLicensePic = bussinessLicensePic;
    }

    public String getCorporationIdPicA() {
        return corporationIdPicA;
    }

    public void setCorporationIdPicA(String corporationIdPicA) {
        this.corporationIdPicA = corporationIdPicA;
    }

    public String getCorporationIdPicB() {
        return corporationIdPicB;
    }

    public void setCorporationIdPicB(String corporationIdPicB) {
        this.corporationIdPicB = corporationIdPicB;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getInvoiceHeader() {
        return invoiceHeader;
    }

    public void setInvoiceHeader(String invoiceHeader) {
        this.invoiceHeader = invoiceHeader;
    }

    public String getTaxIdNo() {
        return taxIdNo;
    }

    public void setTaxIdNo(String taxIdNo) {
        this.taxIdNo = taxIdNo;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public double getCreateTime() {
        return createTime;
    }

    public void setCreateTime(double createTime) {
        this.createTime = createTime;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.fullname);
        dest.writeString(this.businessLicense);
        dest.writeString(this.corporationName);
        dest.writeString(this.corporationIdNo);
        dest.writeString(this.bussinessLicensePic);
        dest.writeString(this.corporationIdPicA);
        dest.writeString(this.corporationIdPicB);
        dest.writeString(this.longitude);
        dest.writeString(this.latitude);
        dest.writeString(this.invoiceHeader);
        dest.writeString(this.taxIdNo);
        dest.writeString(this.postcode);
        dest.writeString(this.province);
        dest.writeString(this.city);
        dest.writeString(this.district);
        dest.writeString(this.address);
        dest.writeString(this.contact);
        dest.writeString(this.contactPhone);
        dest.writeDouble(this.createTime);
        dest.writeInt(this.statusCode);
        dest.writeInt(this.orderNum);
    }

    public OrderInfo_Cooperator() {
    }

    protected OrderInfo_Cooperator(Parcel in) {
        this.id = in.readInt();
        this.fullname = in.readString();
        this.businessLicense = in.readString();
        this.corporationName = in.readString();
        this.corporationIdNo = in.readString();
        this.bussinessLicensePic = in.readString();
        this.corporationIdPicA = in.readString();
        this.corporationIdPicB = in.readString();
        this.longitude = in.readString();
        this.latitude = in.readString();
        this.invoiceHeader = in.readString();
        this.taxIdNo = in.readString();
        this.postcode = in.readString();
        this.province = in.readString();
        this.city = in.readString();
        this.district = in.readString();
        this.address = in.readString();
        this.contact = in.readString();
        this.contactPhone = in.readString();
        this.createTime = in.readDouble();
        this.statusCode = in.readInt();
        this.orderNum = in.readInt();
    }

    public static final Parcelable.Creator<OrderInfo_Cooperator> CREATOR = new Parcelable.Creator<OrderInfo_Cooperator>() {
        @Override
        public OrderInfo_Cooperator createFromParcel(Parcel source) {
            return new OrderInfo_Cooperator(source);
        }

        @Override
        public OrderInfo_Cooperator[] newArray(int size) {
            return new OrderInfo_Cooperator[size];
        }
    };
}
