package com.example.myapplication;

public class TimeControlSettings
{
    String name;
    long timeControlP1;
    long timeControlP2;
    long incrementP1;
    long incrementP2;

    public TimeControlSettings(String n, long t1, long t2, long incP1, long incP2)
    {
        name = n;
        timeControlP1 = t1;
        timeControlP2 = t2;
        incrementP1 = incP1;
        incrementP2 = incP2;
    }
}
