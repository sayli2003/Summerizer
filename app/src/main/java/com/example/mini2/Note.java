package com.example.mini2;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;


public class Note implements Parcelable {
    String Title;
    String Summery;
    String NoteID;
    public Note(Parcel in) {

    }
    public Note(String key,String value) {
        Log.v("INSIDE NOTE",value);
    }
    public int describeContents() {
        return 0;
    }
    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };
    public String getTitle(){
        return Title;
    }
    public String getSummery(){
        return Summery;
    }
    Note(String noteid,String title,String summery){
        this.Title=title;
        this.NoteID=noteid;
        this.Summery=summery;

    }
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Title);
        dest.writeString(Summery);
    }


}
