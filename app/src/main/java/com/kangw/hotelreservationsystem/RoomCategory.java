package com.kangw.hotelreservationsystem;

import java.util.ArrayList;

public class RoomCategory {
    private String roomType;
    private String roomDesc;
    private String roomPrice;
    private String roomFree;
    private ArrayList<String> searchCriteria;

    public RoomCategory() {
    }

    public RoomCategory(String roomType, String roomDesc, String roomPrice, String roomFree, ArrayList<String> searchCriteria) {
        this.roomType = roomType;
        this.roomDesc = roomDesc;
        this.roomPrice = roomPrice;
        this.roomFree = roomFree;
        this.searchCriteria = searchCriteria;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getRoomDesc() {
        return roomDesc;
    }

    public void setRoomDesc(String roomDesc) {
        this.roomDesc = roomDesc;
    }

    public String getRoomPrice() {
        return roomPrice;
    }

    public void setRoomPrice(String roomPrice) {
        this.roomPrice = roomPrice;
    }

    public String getRoomFree() {
        return roomFree;
    }

    public void setRoomFree(String roomFree) {
        this.roomFree = roomFree;
    }

    public ArrayList<String> getSearchCriteria() {
        return searchCriteria;
    }

    public void setSearchCriteria(ArrayList<String> searchCriteria) {
        this.searchCriteria = searchCriteria;
    }
}
