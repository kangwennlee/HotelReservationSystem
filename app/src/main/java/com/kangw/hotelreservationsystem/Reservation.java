package com.kangw.hotelreservationsystem;

public class Reservation {
    private String checkInDate;
    private String checkOutDate;
    private String numAdult;
    private String numChild;
    private String roomSelected;
    private String numRoom;
    private String totalPrice;
    private String reservationDate;
    private String reservationTime;

    public Reservation() {
    }

    public String getReservationTime() {
        return reservationTime;
    }

    public void setReservationTime(String reservationTime) {
        this.reservationTime = reservationTime;
    }

    public String getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(String reservationDate) {
        this.reservationDate = reservationDate;
    }

    public String getNumRoom() {
        return numRoom;
    }

    public void setNumRoom(String numRoom) {
        this.numRoom = numRoom;
    }

    public String getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(String checkInDate) {
        this.checkInDate = checkInDate;
    }

    public String getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(String checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public String getNumAdult() {
        return numAdult;
    }

    public void setNumAdult(String numAdult) {
        this.numAdult = numAdult;
    }

    public String getNumChild() {
        return numChild;
    }

    public void setNumChild(String numChild) {
        this.numChild = numChild;
    }

    public String getRoomSelected() {
        return roomSelected;
    }

    public void setRoomSelected(String roomSelected) {
        this.roomSelected = roomSelected;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String toShortString(){
        String string = "";

        String date = reservationDate.substring(0, 4) + "/" + reservationDate.substring(4, 6) + "/" + reservationDate.substring(6, 8);
        string+= "Reservation Date: " + date + " " + reservationTime + "\n";
        string+= "Check In Date: " + checkInDate + "\n";
        string+= "Check Out Date: " + checkOutDate + "\n";
        string+= "Number of Adult: " + numAdult + "\n";
        string += "Total Number of Room: " + numRoom + "\n";

        return string;
    }

    @Override
    public String toString() {
        String string = "";
        String date = reservationDate.substring(0, 4) + "/" + reservationDate.substring(4, 6) + "/" + reservationDate.substring(6, 8);
        string+= "Reservation Date: " + date + "\n";
        string+="Reservation Time: " + reservationTime + "\n";
        string+= "Check In Date: " + checkInDate + "\n";
        string+= "Check Out Date: " + checkOutDate + "\n";
        string+= "Number of Adult: " + numAdult + "\n";
        string += "Number of Children: " + numChild + "\n";
        string += "Room Selected: " + "\n";
        string += roomSelected;
        string += "Total Number of Room: " + numRoom + "\n";
        string += "Total Price: RM" + totalPrice + "\n";

        return string;
    }
}
