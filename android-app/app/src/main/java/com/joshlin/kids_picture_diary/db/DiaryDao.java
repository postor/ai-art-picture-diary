package com.joshlin.kids_picture_diary.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface DiaryDao {
    @Query("SELECT * FROM diary")
    List<Diary> getAll();

    @Query("SELECT * FROM diary WHERE parent_id = 0 ORDER BY id DESC")
    List<Diary> loadCovers();

    @Query("SELECT * FROM diary WHERE parent_id=:parent_id ORDER BY id ASC")
    List<Diary> loadPages(long parent_id);

    @Query("SELECT * FROM diary WHERE id=:id")
    Diary getOne(long id);

    @Insert
    long[] insertAll(Diary... users);

    @Delete
    void delete(Diary user);

    @Update
    void update(Diary diary);
}