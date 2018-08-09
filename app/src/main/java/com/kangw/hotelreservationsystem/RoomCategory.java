package com.kangw.hotelreservationsystem;

public class RoomCategory {
    private String roomType;
    private String roomDesc;
    private String roomPrice;
    private String roomFree;
    private String numRoomSelected;

    public RoomCategory() {
    }

    public RoomCategory(String roomType, String roomDesc, String roomPrice, String roomFree, String numRoomSelected) {
        this.roomType = roomType;
        this.roomDesc = roomDesc;
        this.roomPrice = roomPrice;
        this.roomFree = roomFree;
        this.numRoomSelected = numRoomSelected;
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

    public String getNumRoomSelected() {
        return numRoomSelected;
    }

    public void setNumRoomSelected(String numRoomSelected) {
        this.numRoomSelected = numRoomSelected;
    }
}
