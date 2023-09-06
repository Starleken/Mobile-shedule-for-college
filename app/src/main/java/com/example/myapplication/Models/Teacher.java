package com.example.myapplication.Models;

import java.io.Serializable;
import java.text.MessageFormat;
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

    public String getFullName(){
        return MessageFormat.format("{0} {1} {2}", LastName, Name, Patronymic);
    }
}
