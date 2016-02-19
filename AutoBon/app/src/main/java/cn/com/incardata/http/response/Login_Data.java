package cn.com.incardata.http.response;

/**
 * Created by wanghao on 16/2/19.
 */
public class Login_Data {
    private int id;

    private String phone; // 手机号

    private String password; // 密码

    private String name; // 姓名

    private String gender; // 性别

    private String avatar; // 头像

    private String idNo; // 身份证编号

    private String idPhoto; // 身份证正面照片

    private String bank; // 银行卡归属银行

    private String bankAddress; // 开户行地址

    private String bankCardNo; // 银行卡号码

    private long verifyAt; // 认证通过日期

    private long lastLoginAt; // 最后登录时间

    private String lastLoginIp; // 最后登录IP

    private long createAt; // 注册时间

    private int star; // 技师星级

    private float voteRate; // 技师好评率

    private String skill; // 技师技能

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public long getVerifyAt() {
        return verifyAt;
    }

    public void setVerifyAt(long verifyAt) {
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

    public float getVoteRate() {
        return voteRate;
    }

    public void setVoteRate(float voteRate) {
        this.voteRate = voteRate;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }
}
