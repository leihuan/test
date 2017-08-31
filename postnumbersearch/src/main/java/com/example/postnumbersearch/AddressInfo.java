package com.example.postnumbersearch;

/**
 * Created by lh on 2017/8/2.
 */
public class AddressInfo {
    String postnumber;
    String province;
    String city;
    String district;
    String address;
    String jd;

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getDistrict() {
        return district;
    }

    public String getPostnumber() {
        return postnumber;
    }

    public String getProvince() {
        return province;
    }

    public String getJd() {
        return jd;
    }

    public void setPostnumber(String postnumber) {
        this.postnumber = postnumber;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public void setJd(String jd) {
        this.jd = jd;
    }

    public void setProvince(String province) {
        this.province = province;
    }
}
