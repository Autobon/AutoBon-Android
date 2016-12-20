package cn.com.incardata.http.response;

import android.os.Parcel;
import android.os.Parcelable;

/** 我的信息
 * Created by yang on 2016/12/1.
 */
public class MyMessage implements Parcelable {

//    private Technician technician;
//    //    private String starRate;
////    private String balance;
////    private String unpaidOrders;
////    private String totalOrders;
////    private String commentCount;


    private int id;
    private String phone;
    private String name;
    private String gender;
    private String avatar;
    private String idNo;
    private String idPhoto;
    private String bank;
    private String bankAddress;
    private String bankCardNo;
    private String verifyAt;
    private long requestVerifyAt;
    private String verifyMsg;
    private long lastLoginAt;
    private String lastLoginIp;
    private long createAt;
//    private String starRate;
//    private String balance;
//    private String unpaidOrders;
//    private String totalOrders;
//    private String commentCount;
    private String skill;
    private String pushId;
    private String reference;
    private int filmLevel;
    private int filmWorkingSeniority;
    private int carCoverLevel;
    private int carCoverWorkingSeniority;
    private int colorModifyLevel;
    private int colorModifyWorkingSeniority;
    private int beautyLevel;
    private int beautyWorkingSeniority;
    private String resume;
    private int workStatus;
    private String status;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getIdPhoto() {
        return idPhoto;
    }

    public void setIdPhoto(String idPhoto) {
        this.idPhoto = idPhoto;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getBankAddress() {
        return bankAddress;
    }

    public void setBankAddress(String bankAddress) {
        this.bankAddress = bankAddress;
    }

    public String getBankCardNo() {
        return bankCardNo;
    }

    public void setBankCardNo(String bankCardNo) {
        this.bankCardNo = bankCardNo;
    }

    public String getVerifyAt() {
        return verifyAt;
    }

    public void setVerifyAt(String verifyAt) {
        this.verifyAt = verifyAt;
    }

    public long getRequestVerifyAt() {
        return requestVerifyAt;
    }

    public void setRequestVerifyAt(long requestVerifyAt) {
        this.requestVerifyAt = requestVerifyAt;
    }

    public String getVerifyMsg() {
        return verifyMsg;
    }

    public void setVerifyMsg(String verifyMsg) {
        this.verifyMsg = verifyMsg;
    }

    public long getLastLoginAt() {
        return lastLoginAt;
    }

    public void setLastLoginAt(long lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    public long getCreateAt() {
        return createAt;
    }

    public void setCreateAt(long createAt) {
        this.createAt = createAt;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public int getFilmLevel() {
        return filmLevel;
    }

    public void setFilmLevel(int filmLevel) {
        this.filmLevel = filmLevel;
    }

    public int getFilmWorkingSeniority() {
        return filmWorkingSeniority;
    }

    public void setFilmWorkingSeniority(int filmWorkingSeniority) {
        this.filmWorkingSeniority = filmWorkingSeniority;
    }

    public int getCarCoverLevel() {
        return carCoverLevel;
    }

    public void setCarCoverLevel(int carCoverLevel) {
        this.carCoverLevel = carCoverLevel;
    }

    public int getCarCoverWorkingSeniority() {
        return carCoverWorkingSeniority;
    }

    public void setCarCoverWorkingSeniority(int carCoverWorkingSeniority) {
        this.carCoverWorkingSeniority = carCoverWorkingSeniority;
    }

    public int getColorModifyLevel() {
        return colorModifyLevel;
    }

    public void setColorModifyLevel(int colorModifyLevel) {
        this.colorModifyLevel = colorModifyLevel;
    }

    public int getColorModifyWorkingSeniority() {
        return colorModifyWorkingSeniority;
    }

    public void setColorModifyWorkingSeniority(int colorModifyWorkingSeniority) {
        this.colorModifyWorkingSeniority = colorModifyWorkingSeniority;
    }

    public int getBeautyLevel() {
        return beautyLevel;
    }

    public void setBeautyLevel(int beautyLevel) {
        this.beautyLevel = beautyLevel;
    }

    public int getBeautyWorkingSeniority() {
        return beautyWorkingSeniority;
    }

    public void setBeautyWorkingSeniority(int beautyWorkingSeniority) {
        this.beautyWorkingSeniority = beautyWorkingSeniority;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public int getWorkStatus() {
        return workStatus;
    }

    public void setWorkStatus(int workStatus) {
        this.workStatus = workStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.phone);
        dest.writeString(this.name);
        dest.writeString(this.gender);
        dest.writeString(this.avatar);
        dest.writeString(this.idNo);
        dest.writeString(this.idPhoto);
        dest.writeString(this.bank);
        dest.writeString(this.bankAddress);
        dest.writeString(this.bankCardNo);
        dest.writeString(this.verifyAt);
        dest.writeLong(this.requestVerifyAt);
        dest.writeString(this.verifyMsg);
        dest.writeLong(this.lastLoginAt);
        dest.writeString(this.lastLoginIp);
        dest.writeLong(this.createAt);
        dest.writeString(this.skill);
        dest.writeString(this.pushId);
        dest.writeString(this.reference);
        dest.writeInt(this.filmLevel);
        dest.writeInt(this.filmWorkingSeniority);
        dest.writeInt(this.carCoverLevel);
        dest.writeInt(this.carCoverWorkingSeniority);
        dest.writeInt(this.colorModifyLevel);
        dest.writeInt(this.colorModifyWorkingSeniority);
        dest.writeInt(this.beautyLevel);
        dest.writeInt(this.beautyWorkingSeniority);
        dest.writeString(this.resume);
        dest.writeInt(this.workStatus);
        dest.writeString(this.status);
    }

    public MyMessage() {
    }

    protected MyMessage(Parcel in) {
        this.id = in.readInt();
        this.phone = in.readString();
        this.name = in.readString();
        this.gender = in.readString();
        this.avatar = in.readString();
        this.idNo = in.readString();
        this.idPhoto = in.readString();
        this.bank = in.readString();
        this.bankAddress = in.readString();
        this.bankCardNo = in.readString();
        this.verifyAt = in.readString();
        this.requestVerifyAt = in.readLong();
        this.verifyMsg = in.readString();
        this.lastLoginAt = in.readLong();
        this.lastLoginIp = in.readString();
        this.createAt = in.readLong();
        this.skill = in.readString();
        this.pushId = in.readString();
        this.reference = in.readString();
        this.filmLevel = in.readInt();
        this.filmWorkingSeniority = in.readInt();
        this.carCoverLevel = in.readInt();
        this.carCoverWorkingSeniority = in.readInt();
        this.colorModifyLevel = in.readInt();
        this.colorModifyWorkingSeniority = in.readInt();
        this.beautyLevel = in.readInt();
        this.beautyWorkingSeniority = in.readInt();
        this.resume = in.readString();
        this.workStatus = in.readInt();
        this.status = in.readString();
    }

    public static final Creator<MyMessage> CREATOR = new Creator<MyMessage>() {
        @Override
        public MyMessage createFromParcel(Parcel source) {
            return new MyMessage(source);
        }

        @Override
        public MyMessage[] newArray(int size) {
            return new MyMessage[size];
        }
    };
}
