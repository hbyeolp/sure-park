package com.example.hanbyeol.sure_park;

/**
 * Created by hanbyeol on 2016-07-27.
 */
public class ListViewCardItem {
    private String cardNum;
    private String cardName;
    private String cardDate;
    private String cardCode;

    public void setCardnum(String cardnum) {
        cardNum = cardnum ;
    }
    public void setCardname(String cardname) {
        cardName = cardname ;
    }
    public void setCarddate(String carddate) {
        cardDate = carddate ;
    }
    public void setCardcode(String cardcode) {
        cardCode = cardcode ;
    }
    public String getCardnum() {
        return this.cardNum;
    }
    public String getCardname() {
        return this.cardName;
    }
    public String getCarddate() {
        return this.cardDate;
    }
    public String getCardcode() {
        return this.cardCode;
    }
}
