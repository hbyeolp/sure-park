package com.example.hanbyeol.sure_park;

import android.provider.BaseColumns;

/**
 * Created by hanbyeol on 2016-07-26.
 */
public class DataBases {
    public static final class CardCreateDB implements BaseColumns{
        public static final String _CREATE =
                "create table card" +
                "(_id integer primary key autoincrement," +
                "cardNumber text not null, cardFirstname text not null, cardLastname text not null, cardExpirationMonth text not null, cardExpirationYear text not null, cardValidationCode text not null);";
    }
    public static final class ReservationCreateDB implements BaseColumns {
        public static final String _CREATE =
                "create table reservation" +
                        "(_id integer primary key autoincrement," +
                        "email text not null, parkinglotname text not null, parkingLotID text not null, reservationTime text not null, carSize text not null, entranceTime text not null, exitTime text not null);";
    }
}
