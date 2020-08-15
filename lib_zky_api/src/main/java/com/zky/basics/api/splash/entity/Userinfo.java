package com.zky.basics.api.splash.entity;

/**
 * Created by lk
 * Date 2019-11-11
 * Time 14:01
 * Detail:
 */
public class Userinfo {
    private String token;
    private String code;
    private String userName;
    private String password;
    private String province;
    private String city;
    private String county;
    private String college;
    private int accountLevel;
    private int accountStatus;
    private int isAdmin;
    private String phone;
    private String headImg;
    private String createDate;

    private String provinceName;
    private String cityName;
    private String countyName;
    private String regionName;
    private String schoolName;


//    private String typeLevel;
//
//
//    public String getTypeLevel() {
//        // 0-中央、2-省（自治区）、3-市（自治州）、4-县（区）、5-学校
//        String l = "";
//        if ("0".equals(accountLevel)) {
//            l = "中央";
//
//        } else if ("2".equals(accountLevel)) {
//            l = "省（自治区）";
//        } else if ("3".equals(accountLevel)) {
//            l = "市（自治州）";
//        } else if ("4".equals(accountLevel)) {
//            l = "县（区）";
//        } else if ("5".equals(accountLevel)) {
//            l = "学校";
//        }
//        return l;
//    }
//
//    public void setTypeLevel(String typeLevel) {
//        this.typeLevel = typeLevel;
////        notifyPropertyChanged(BR.writeTwon);
//
//
//    }


    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public int getAccountLevel() {
        return accountLevel;
    }

    public void setAccountLevel(int accountLevel) {
        this.accountLevel = accountLevel;
    }

    public int getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(int accountStatus) {
        this.accountStatus = accountStatus;
    }

    public int getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(int isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }


}
