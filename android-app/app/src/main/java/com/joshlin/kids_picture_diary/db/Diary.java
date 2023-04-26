package com.joshlin.kids_picture_diary.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class Diary {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "create_time")
    public long create_time;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "prompt")
    public String prompt;

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    public byte[] image;

    @ColumnInfo(name = "parent_id")
    public long parent_id;
}