package com.tp4.gradesappfirebase;

public class Student {
    private String name;
    private String cin;

    private float noteAndroid;
    private float noteAngular;
    private float noteUX;
    private float noteDB;
    private float noteC;
    private float noteBigData;

    public Student(String name, String cin, float noteAndroid, float noteAngular, float noteUX, float noteDB, float noteC, float noteBigData) {
        this.name = name;
        this.cin = cin;
        this.noteAndroid = noteAndroid;
        this.noteAngular = noteAngular;
        this.noteUX = noteUX;
        this.noteDB = noteDB;
        this.noteC = noteC;
        this.noteBigData = noteBigData;
    }

    public Student() {

    }

    public String getCin() {
        return this.cin;
    }

    public String getName() {
        return name;
    }

    public float getNoteAndroid() {
        return noteAndroid;
    }

    public float getNoteAngular() {
        return noteAngular;
    }

    public float getNoteUX() {
        return noteUX;
    }

    public float getNoteDB() {
        return noteDB;
    }

    public float getNoteC() {
        return noteC;
    }

    public float getNoteBigData() {
        return noteBigData;
    }
}
