package cn.com.incardata.http.response;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zhangming on 2016/2/29.
 */
public class MyInfo_Data implements Parcelable {
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
    private int star;
    private int voteRate;
    private String skill;
    private String pushId;
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

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public int getVoteRate() {
        return voteRate;
    }

    public void setVoteRate(int voteRate) {
        this.voteRate = voteRate;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
        dest.writeInt(this.star);
        dest.writeInt(this.voteRate);
        dest.writeString(this.skill);
        dest.writeString(this.pushId);
        dest.writeString(this.status);
    }

    public MyInfo_Data() {
    }

    protected MyInfo_Data(Parcel in) {
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
        this.star = in.readInt();
        this.voteRate = in.readInt();
        this.skill = in.readString();
        this.pushId = in.readString();
        this.status = in.readString();
    }

    public static final Parcelable.Creator<MyInfo_Data> CREATOR = new Parcelable.Creator<MyInfo_Data>() {
        public MyInfo_Data createFromParcel(Parcel source) {
            return new MyInfo_Data(source);
        }

        public MyInfo_Data[] newArray(int size) {
            return new MyInfo_Data[size];
        }
    };
}
