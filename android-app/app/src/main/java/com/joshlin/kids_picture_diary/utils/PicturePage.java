package com.joshlin.kids_picture_diary.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.joshlin.kids_picture_diary.EditActivity;
import com.joshlin.kids_picture_diary.db.Diary;
import com.joshlin.kids_picture_diary.db.DiaryManger;
import com.joshlin.kids_picture_diary.db.DiaryPdf;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import android.util.Base64;

public class PicturePage {
    private final ImageView pictureView;
    private final TextView pageNumText;
    private final TextView ideaText;
    private final Context ctx;
    private final ImageButton nextBtn;
    private final ImageButton prevBtn;
    private final ImageButton reDrawBtn;
    private final ProgressBar loadingPB;
    private final EditActivity editActivity;
    public long coverId = 0;
    public int pageNumber = 0;
    public DiaryPdf pdf = null;
    public DiaryManger db;

    public PicturePage(
            EditActivity activity,
            long coverId,
            ImageView pictureView,
            TextView pageNumText,
            TextView ideaText,
            ImageButton nextBtn,
            ImageButton prevBtn,
            ImageButton reDrawBtn,
            ProgressBar loadingPB
    ) {
        this.editActivity = activity;
        this.ctx = activity.getApplicationContext();
        this.db = new DiaryManger(ctx);
        this.coverId = coverId;
        this.pageNumber = 0;
        this.pictureView = pictureView;
        this.pageNumText = pageNumText;
        this.ideaText = ideaText;
        this.nextBtn = nextBtn;
        this.prevBtn = prevBtn;
        this.reDrawBtn = reDrawBtn;
        this.loadingPB = loadingPB;
        if (coverId > 0) {
            pdf = db.loadPdf(coverId);
        }
        Log.i("CoverID",""+coverId);
    }

    public void next() {
        if (pdf == null || pdf.pages.size() <= pageNumber) return;
        pageNumber++;
        update();
    }

    public void prev() {
        if (pageNumber <= 0) return;
        pageNumber--;
        update();
    }

    public void reDraw() {
        if (pdf == null || pdf.pages.size() <= pageNumber) {
            return;
        }
        this.sendPost("");
    }

    private void setBtnEnabled(ImageButton btn,Boolean enabled){
        btn.setImageAlpha(enabled ? 0xFF : 0x3F);
    }

    public void update() {
        Log.i("pdfIsNull",""+(pdf==null));
        setBtnEnabled(nextBtn,true);
        setBtnEnabled(prevBtn,true);
        setBtnEnabled(reDrawBtn,true);
        pageNumText.setText(new Integer(pageNumber + 1).toString());
        if (pageNumber == 0) {
            setBtnEnabled(prevBtn,false);
        }
        if (pdf == null || pdf.pages.size() <= pageNumber) {
//            Log.i("OutOfRange","size="+pdf.pages.size()+"|pageNumber="+pageNumber);
            pictureView.setImageResource(0);
            ideaText.setText("");
            setBtnEnabled(nextBtn,false);
            setBtnEnabled(reDrawBtn,false);
            return;
        }
        Diary d = pdf.pages.get(pageNumber);
        Log.i("SettingTitle",d.title);
        Bitmap bmp = BitmapFactory.decodeByteArray(d.image, 0, d.image.length);
        pictureView.setImageBitmap(bmp);
        ideaText.setText(d.title);
    }

    public void getImageFromIdea(String idea) {
        this.sendPost(idea);
    }

    private String toUtf8(String s) throws UnsupportedEncodingException {
        return s;
//        return URLEncoder.encode(s, "UTF-8");
    }

    public void onResult(JSONObject obj, String idea) throws JSONException {
        Diary page = new Diary();
        page.prompt = obj.getString("prompt");
        page.image = Base64.decode(obj.getString("image"), Base64.DEFAULT);
        page.title = idea;
        if(pdf!=null){
            page.parent_id = pdf.cover.id;
        }


        if (pdf == null) {
            Log.i("add", idea);
            // new cover
            Diary added = db.addPage(page);
            ArrayList list = new ArrayList();
            pdf = new DiaryPdf(added, list);
        } else if (pdf.pages.size() <= pageNumber) {
            Log.i("add", idea);
            // append
            Diary added = db.addPage(page);
            pdf.pages.add(added);
        } else {
            Log.i("update", idea);
            //update
            Diary edit = pdf.pages.get(pageNumber);
            if (idea != "") {
                edit.title = idea;
            }
            edit.prompt = page.prompt;
            edit.image = page.image;
            db.update(edit);
        }
        update();
    }

    public void sendPost(String idea) {
        loadingPB.setVisibility(View.VISIBLE);
        Log.i("showloading", "loadingPB.setVisibility(View.VISIBLE);");

        String urlAddress = "http://10.0.0.197:3000/apis/sd/drawImageFromIdea";

        String str = idea;
        try {
            if (idea == "") {
                str = pdf.pages.get(pageNumber).prompt;
                urlAddress = "http://10.0.0.197:3000/apis/sd/drawImage";
            }
            byte[] bytesEncoded = Base64.encode(str.getBytes(),Base64.DEFAULT);
            JSONArray params = new JSONArray();
            params.put("");
            params.put(new String(bytesEncoded) );
            String body = params.toString();
            Log.i("body", body);
            Job job = new Job();
            job.doRequest(urlAddress, body, new Runnable() {
                @Override
                public void run() {
                    editActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                onResult(job.result.getJSONObject("data"), idea);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            loadingPB.setVisibility(View.GONE);
                            update();
                        }
                    });

                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


