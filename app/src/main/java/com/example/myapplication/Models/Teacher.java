package com.example.myapplication.Models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.myapplication.Interfaces.ISearchable;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Date;


public class Teacher implements Serializable, ISearchable {
    public int id;
    public String Name;
    public String LastName;
    public String Patronymic;
    public String Phone;
    public Date DateFired;
    public String Image;
    public Study Study;

    @Override
    public String GetText() {
        return MessageFormat.format("{0} {1} {2}", LastName, Name, Patronymic);
    }
}
