package com.example.petr.textmachine;

import java.util.ArrayList;



public class AlarmData {

    int hour;
    int min;
    int id;

    boolean monday;
    boolean tuesday;
    boolean wednesday;
    boolean thursday;
    boolean friday;
    boolean saturday;
    boolean sunday;

    boolean enable;

    String smsText;

    String tel;

    String description;

    public AlarmData(int hour,int min,boolean monday,boolean tuesday, boolean wednesday,boolean thursday,
    boolean friday,boolean saturday,boolean sunday,String smsText,String tel, String description){
        this.description = description;
        this.enable = true;
        this.hour = hour;
        this.min = min;
        this.monday = monday;
        this. tuesday = tuesday;
        this.wednesday = wednesday;
        this.thursday = thursday;
        this.friday = friday;
        this.saturday = saturday;
        this. sunday = sunday;
        this.smsText = smsText;
        this.tel = tel;

    }

    public AlarmData(int hour,int min,int monday,int tuesday, int wednesday,int thursday,
                     int friday,int saturday,int sunday,String smsText,String tel, String description){
        this.description = description;
        this.enable = true;
        this.hour = hour;
        this.min = min;
        this.monday =   monday == 1 ? true : false;
        this.tuesday =  tuesday == 1 ? true : false;
        this.wednesday= wednesday == 1 ? true : false;
        this.thursday = thursday == 1 ? true : false;
        this.friday =   friday == 1 ? true : false;
        this.saturday = saturday == 1 ? true : false;
        this.sunday =   sunday == 1 ? true : false;
        this.smsText = smsText;
        this.tel = tel;

    }




    public void saveToDB(){
        MyDb.getInstance().insertSMS(this.hour,this.min,this.monday?1:0,this.tuesday?1:0,this.wednesday?1:0,this.thursday?1:0,this.friday?1:0,
                this.saturday?1:0,this.sunday?1:0, this.smsText, this.tel, this.description, this.enable?1:0);
    }

}
