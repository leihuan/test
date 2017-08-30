package com.example.hh.myapplication.util;

/**
 * Created by hh on 2017/1/17.
 */
public class HouseInfo {

    public String houseLong, houseWidth, houseHeight, doorHeight, doorWidth, doorCount, windowHeight, windowWidth, windowCount;
    /**单价*/
    public String mPerPrice;
    /**材料长度*/
    public String mPerMaterialLong;
    /**材料宽度*/
    public String mPerMaterialWidth;
    /**材料规格*/
    public String mSize;

    //    private String floorLong,floorWidth,floorPerPrice,wallTileLong,wallTileWidth,wallTilePerPrice,
//    wallPaperSize,wallpaperPerPrice,wallCount, floorTileLong,floorTileWidth,floorTilePerPrice,clothWidth,clothPerPrice;
    public void HouseInfo() {
    }

    public String getmSize() {
        return mSize;
    }

    public void setmSize(String mSize) {
        this.mSize = mSize;
    }

    public String getmPerMaterialLong() {
        return mPerMaterialLong;
    }

    public String getmPerMaterialWidth() {
        return mPerMaterialWidth;
    }

    public void setmPerMaterialLong(String mPerMaterialLong) {
        this.mPerMaterialLong = mPerMaterialLong;
    }

    public void setmPerMaterialWidth(String mPerMaterialWidth) {
        this.mPerMaterialWidth = mPerMaterialWidth;
    }

    public String getmPerPrice() {
        return mPerPrice;
    }

    public void setmPerPrice(String mPerPrice) {
        this.mPerPrice = mPerPrice;
    }

    public void setDoorCount(String doorCount) {
        this.doorCount = doorCount;
    }

    public void setDoorHeight(String doorHeight) {
        this.doorHeight = doorHeight;
    }

    public void setDoorWidth(String doorWidth) {
        this.doorWidth = doorWidth;
    }

    public void setHouseHeight(String houseHeight) {
        this.houseHeight = houseHeight;
    }

    public void setHouseLong(String houseLong) {
        this.houseLong = houseLong;
    }

    public void setHouseWidth(String houseWidth) {
        this.houseWidth = houseWidth;
    }

    public void setWindowCount(String windowCount) {
        this.windowCount = windowCount;
    }

    public void setWindowHeight(String windowHeight) {
        this.windowHeight = windowHeight;
    }

    public void setWindowWidth(String windowWidth) {
        this.windowWidth = windowWidth;
    }

    public String getDoorCount() {
        return doorCount;
    }

    public String getDoorHeight() {
        return doorHeight;
    }

    public String getDoorWidth() {
        return doorWidth;
    }

    public String getHouseHeight() {
        return houseHeight;
    }

    public String getHouseLong() {
        return houseLong;
    }

    public String getHouseWidth() {
        return houseWidth;
    }

    public String getWindowCount() {
        return windowCount;
    }

    public String getWindowHeight() {
        return windowHeight;
    }

    public String getWindowWidth() {
        return windowWidth;
    }

}
