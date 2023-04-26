package com.joshlin.kids_picture_diary.list;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.joshlin.kids_picture_diary.EditActivity;
import com.joshlin.kids_picture_diary.R;
import com.joshlin.kids_picture_diary.db.Diary;
import com.joshlin.kids_picture_diary.db.DiaryManger;
import com.joshlin.kids_picture_diary.db.DiaryPdf;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private final Context context;
    private List<DiaryPdf> localDataSet;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final RecyclerView recyclePcitureListView;
        public final ImageButton btnEdit;
        public final ImageButton btnPdf;
        public final ImageButton btnDel;
        public final ImageButton btnCreate;
        public final FrameLayout createFrame;
        public final FrameLayout contentFrame;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            textView = (TextView) view.findViewById(R.id.textView);
            recyclePcitureListView = (RecyclerView) view.findViewById(R.id.recyclePictureList);
            btnEdit = (ImageButton) view.findViewById(R.id.btn_edit);
            btnPdf = (ImageButton) view.findViewById(R.id.btn_pdf);
            btnDel = (ImageButton) view.findViewById(R.id.btn_del);
            btnCreate = (ImageButton) view.findViewById(R.id.btn_create);
            createFrame = (FrameLayout) view.findViewById(R.id.createFrame);
            contentFrame = (FrameLayout) view.findViewById(R.id.contentFrame);
        }

        public TextView getTextView() {
            return textView;
        }

        public RecyclerView getPictureListView(){
            return recyclePcitureListView;
        }
    }

    /**
     * Initialize the dataset of the Adapter
     *
     * @param dataSet String[] containing the data to populate views to be used
     * by RecyclerView
     */
    public CustomAdapter(List<DiaryPdf> dataSet, Context ctx) {
        localDataSet = dataSet;
        context = ctx;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.text_row_item, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        if(position == 0){
            // create
            viewHolder.contentFrame.setVisibility(View.GONE);
            viewHolder.createFrame.setVisibility(View.VISIBLE);
            viewHolder.btnCreate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent switchActivityIntent = new Intent(context, EditActivity.class);
                    context.startActivity(switchActivityIntent);
                }
            });
            return;
        }

        DiaryPdf data = localDataSet.get(position-1);

        viewHolder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent switchActivityIntent = new Intent(context, EditActivity.class);
                switchActivityIntent.putExtra("id",data.cover.id);
                context.startActivity(switchActivityIntent);
                Log.i("BundleCoverID",""+data.cover.id);
            }
        });

        ArrayList<Diary> pages = new DiaryManger(context.getApplicationContext()).loadPages(data.cover.id);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = viewHolder.getPictureListView();
        recyclerView.setAdapter(new PictureAdapter(pages,context));
        recyclerView.setLayoutManager(layoutManager);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size()+1;
    }
}
