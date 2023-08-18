package com.example.myapplication.Models;

import com.example.myapplication.Models.Audience.Audience;
import com.example.myapplication.Models.Audience.Group;

import java.util.Date;

public class Pair {
    public int id;
    public Date dateStart;
    public Date dateEnd;
    public Audience audience;
    public TeacherSubject teacherSubject;
    public DayOfWeek dayOfWeek;
    public Time time;
    public TypeOfPair typeOfPair;
    public Group group;

}
