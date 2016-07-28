package com.example.hanbyeol.sure_park;

/**
 * Created by hanbyeol on 2016-07-27.
 */
public class ListViewReservationItem {
    private String parkinglotID;
    private String email;
    private String carSize;
    private String reservationTime;
    private String entranceTime;
    private String exitTime;

    public void setParkinglotID(String parkinglotid) {
        parkinglotID = parkinglotid;
    }
    public void setEmail(String e_mail) {
        email = e_mail;
    }
    public void setCarSize(String carsize) {
        carSize = carsize;
    }
    public void setReservationTime(String reservationtime) {
        reservationTime = reservationtime;
    }
    public void setEntranceTime(String entrancetime) {
        entranceTime = entrancetime;
    }
    public void setExitTime(String exittime) {
        exitTime = exittime;
    }

    public String getParkinglotID() {
        return this.parkinglotID;
    }
    public String getEmail() {
        return this.email;
    }
    public String getCarSize() {
        return this.carSize;
    }
    public String getReservationTime() {
        return this.reservationTime;
    }
    public String getEntranceTime() {
        return this.entranceTime;
    }
    public String getExitTime() {
        return this.exitTime;
    }
}
