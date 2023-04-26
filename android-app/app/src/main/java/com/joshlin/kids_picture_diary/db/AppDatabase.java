package com.joshlin.kids_picture_diary.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;


@Database(entities = {Diary.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract DiaryDao diaryDao();
}