package com.kangw.hotelreservationsystem;

public class RoomView {
    private String roomType;
    private String roomDesc;
    private String roomPrice;

    public RoomView() {
    }

    public RoomView(String roomType, String roomDesc, String roomPrice) {
        this.roomType = roomType;
        this.roomDesc = roomDesc;
        this.roomPrice = roomPrice;
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
}
