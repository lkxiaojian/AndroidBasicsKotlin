package com.zky.basics.main.entity.bean;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.Bitmap;
import com.zky.basics.common.BR;

/**
 * Created by lk
 * Date 2019-11-06
 * Time 14:58
 * Detail:
 */
public class SplashViewBean extends BaseObservable {
    private String name;
    private String paw;
    private String timeMeesage;
    private String rgPhone;
    private String rgName;
    private String rgImageCode;
    private String rgCode;
    private String rgLevel;
    private String rgProvince;
    private String rgCity;
    private String rgTwon;
    private String rgSchool;
    private String rgPaw;
    private String rgqrPaw;
    private Bitmap rgImageUrl;
    private int rgErrorImageUrl;
    private int levelIndel;
    private boolean writeLevel;
    private boolean writeProvince;
    private boolean writeCity;
    private boolean writeTwon;
    private boolean writeSchool;


    public SplashViewBean() {
    }

    public SplashViewBean(String name, String paw, String timeMeesage, String rgPhone, String rgName, String rgImageCode, String rgCode, String rgLevel, String rgProvince, String rgCity, String rgTwon, String rgSchool, String rgPaw, String rgqrPaw, Bitmap rgImageUrl, int rgErrorImageUrl, int levelIndel, boolean writeLevel, boolean writeProvince, boolean writeCity, boolean writeTwon, boolean writeSchool) {
        this.name = name;
        this.paw = paw;
        this.timeMeesage = timeMeesage;
        this.rgPhone = rgPhone;
        this.rgName = rgName;
        this.rgImageCode = rgImageCode;
        this.rgCode = rgCode;
        this.rgLevel = rgLevel;
        this.rgProvince = rgProvince;
        this.rgCity = rgCity;
        this.rgTwon = rgTwon;
        this.rgSchool = rgSchool;
        this.rgPaw = rgPaw;
        this.rgqrPaw = rgqrPaw;
        this.rgImageUrl = rgImageUrl;
        this.rgErrorImageUrl = rgErrorImageUrl;
        this.levelIndel = levelIndel;
        this.writeLevel = writeLevel;
        this.writeProvince = writeProvince;
        this.writeCity = writeCity;
        this.writeTwon = writeTwon;
        this.writeSchool = writeSchool;
    }

    @Bindable
    public boolean isWriteProvince() {
        return writeProvince;
    }

    public void setWriteProvince(boolean writeProvince) {
        this.writeProvince = writeProvince;
        notifyPropertyChanged(BR.writeProvince);

    }
    @Bindable
    public boolean isWriteCity() {
        return writeCity;
    }

    public void setWriteCity(boolean writeCity) {
        this.writeCity = writeCity;
        notifyPropertyChanged(BR.writeCity);

    }
    @Bindable
    public boolean isWriteTwon() {
        return writeTwon;
    }

    public void setWriteTwon(boolean writeTwon) {
        this.writeTwon = writeTwon;
        notifyPropertyChanged(BR.writeTwon);

    }
    @Bindable
    public boolean isWriteSchool() {
        return writeSchool;
    }

    public void setWriteSchool(boolean writeSchool) {
        this.writeSchool = writeSchool;
        notifyPropertyChanged(BR.writeSchool);

    }

    @Bindable
    public boolean isWriteLevel() {
        return writeLevel;
    }

    public void setWriteLevel(boolean writeLevel) {
        this.writeLevel = writeLevel;
        notifyPropertyChanged(BR.writeLevel);
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;

    }

    @Bindable
    public String getPaw() {
        return paw;
    }

    public void setPaw(String paw) {
        this.paw = paw;
    }

    @Bindable
    public String getTimeMeesage() {
        return timeMeesage;
    }

    public void setTimeMeesage(String timeMeesage) {
        this.timeMeesage = timeMeesage;
        notifyPropertyChanged(BR.timeMeesage);

    }

    @Bindable
    public String getRgPhone() {
        return rgPhone;
    }

    public void setRgPhone(String rgPhone) {
        this.rgPhone = rgPhone;
        notifyPropertyChanged(BR.rgPhone);
    }

    @Bindable
    public String getRgName() {
        return rgName;
    }

    public void setRgName(String rgName) {
        this.rgName = rgName;
        notifyPropertyChanged(BR.rgName);

    }

    @Bindable
    public String getRgImageCode() {
        return rgImageCode;
    }

    public void setRgImageCode(String rgImageCode) {
        this.rgImageCode = rgImageCode;
        notifyPropertyChanged(BR.rgImageCode);
    }

    @Bindable
    public String getRgCode() {
        return rgCode;
    }

    public void setRgCode(String rgCode) {
        this.rgCode = rgCode;
        notifyPropertyChanged(BR.rgCode);
    }

    @Bindable
    public String getRgLevel() {
        return rgLevel;
    }

    public void setRgLevel(String rgLevel) {
        this.rgLevel = rgLevel;
        notifyPropertyChanged(BR.rgLevel);
//        this.setWriteLevel(false);
    }

    @Bindable
    public String getRgProvince() {
        return rgProvince;
    }

    public void setRgProvince(String rgProvince) {
        this.rgProvince = rgProvince;
        notifyPropertyChanged(BR.rgProvince);
    }

    @Bindable
    public String getRgCity() {
        return rgCity;
    }

    public void setRgCity(String rgCity) {
        this.rgCity = rgCity;
        notifyPropertyChanged(BR.rgCity);
    }

    @Bindable
    public String getRgTwon() {
        return rgTwon;
    }

    public void setRgTwon(String rgTwon) {
        this.rgTwon = rgTwon;
        notifyPropertyChanged(BR.rgTwon);

    }

    @Bindable
    public String getRgSchool() {
        return rgSchool;
    }

    public void setRgSchool(String rgSchool) {
        this.rgSchool = rgSchool;
        notifyPropertyChanged(BR.rgSchool);

    }

    @Bindable
    public String getRgPaw() {
        return rgPaw;
    }

    public void setRgPaw(String rgPaw) {
        this.rgPaw = rgPaw;
        notifyPropertyChanged(BR.rgPaw);

    }

    @Bindable
    public String getRgqrPaw() {
        return rgqrPaw;
    }

    public void setRgqrPaw(String rgqrPaw) {
        this.rgqrPaw = rgqrPaw;
        notifyPropertyChanged(BR.rgqrPaw);

    }

    @Bindable
    public Bitmap getRgImageUrl() {
        return rgImageUrl;
    }

    public void setRgImageUrl(Bitmap rgImageUrl) {
        this.rgImageUrl = rgImageUrl;
        notifyPropertyChanged(BR.rgImageUrl);

    }

    @Bindable
    public Integer getRgErrorImageUrl() {
        return rgErrorImageUrl;
    }

    public void setRgErrorImageUrl(int rgErrorImageUrl) {
        this.rgErrorImageUrl = rgErrorImageUrl;

    }

    @Bindable
    public Integer getLevelIndel() {
        return levelIndel;
    }

    public void setLevelIndel(int levelIndel) {
        this.levelIndel = levelIndel;
        notifyPropertyChanged(BR.levelIndel);

    }


}
