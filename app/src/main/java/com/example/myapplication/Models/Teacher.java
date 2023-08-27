package com.example.myapplication.Models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Date;


public class Teacher implements Serializable {
    public int id;
    public String Name;
    public String LastName;
    public String Patronymic;
    public String Phone;
    public Date DateFired;
    public String Image;
    public Study Study;

}
