package com.example.kwlee.customitemviewtest;

public class MyItem {
    int mIcon; // image resource
    String nName; // text
    String nAge;  // text

    MyItem(int aIcon, String aName, String aAge) {
        mIcon = aIcon;
        nName = aName;
        nAge = aAge;
    }

    @Override
    public String toString() {
        return "MyItem{" +
                "mIcon=" + mIcon +
                ", nName='" + nName + '\'' +
                ", nAge='" + nAge + '\'' +
                '}';
    }

    //alt+insert 키 누르면 getter +setter 생성
    public int getmIcon() {
        return mIcon;
    }

    public void setmIcon(int mIcon) {
        this.mIcon = mIcon;
    }

    public String getnName() {
        return nName;
    }

    public void setnName(String nName) {
        this.nName = nName;
    }

    public String getnAge() {
        return nAge;
    }

    public void setnAge(String nAge) {
        this.nAge = nAge;
    }
}