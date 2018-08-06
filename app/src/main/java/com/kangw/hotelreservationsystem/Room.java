package com.kangw.hotelreservationsystem;

public class Room {
    private String roomID;
    private String roomType;
    private String status;

    public Room() {
    }

    public Room(String roomID, String roomType, String status) {
        this.roomID = roomID;
        this.roomType = roomType;
        this.status = status;
    }

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
