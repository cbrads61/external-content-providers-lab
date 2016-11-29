package com.colinbradley.externalcontentproviderslab;

/**
 * Created by colinbradley on 11/28/16.
 */

public class Event {
    String mName;
    String mDate;
    long mID;

    public Event (String name, String date, long id){
        mName = name;
        mDate = date;
        mID = id;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }

    public long getmID() {
        return mID;
    }

    public void setmID(long mID) {
        this.mID = mID;
    }
}
