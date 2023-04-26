package com.joshlin.kids_picture_diary.db;

import java.util.List;

public class DiaryPdf {
    public final Diary cover;
    public final List<Diary> pages;

    public DiaryPdf(Diary cover, List<Diary> pages) {
        this.cover = cover;
        this.pages = pages;
        if (cover != null && pages != null) {
            this.pages.add(0, cover);
        }
    }
}
