package com.joshlin.kids_picture_diary.db;

import android.content.Context;

import androidx.room.Room;

import java.util.ArrayList;
import java.util.List;


public class DiaryManger {
    final Context context;
    final AppDatabase db;
    private final DiaryDao dao;

    public DiaryManger(Context context){
        this.context = context;
        this.db = Room.databaseBuilder(context,
                AppDatabase.class, "diary").allowMainThreadQueries().build();
        this.dao = db.diaryDao();
    }



    public ArrayList<DiaryPdf> loadAll(){
        ArrayList<DiaryPdf>  rtn = new ArrayList<DiaryPdf> ();
        for (Diary cover:dao.loadCovers()) {
            rtn.add(new DiaryPdf(cover,dao.loadPages(cover.id)));
        }
        return rtn;
    }

    public DiaryPdf loadPdf(long id) {
        return new DiaryPdf(dao.getOne(id),dao.loadPages(id));
    }

    public ArrayList<Diary> loadPages(long coverId){
        ArrayList<Diary>  rtn = new ArrayList<Diary> ();
        rtn.add(dao.getOne(coverId));
        for (Diary page:dao.loadPages(coverId)) {
            rtn.add(page);
        }
        return rtn;
    }

    public Diary addPage(Diary page){
        return dao.getOne(dao.insertAll(page)[0]);
    }
    public void update(Diary page){ dao.update(page);}
}
