package com.joshlin.kids_picture_diary.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;

public class StrokeTextView extends AppCompatTextView {

    private TextView borderText;///用于描边的TextView
    TextPaint tp1; //borderText的Paint

    private int mStrokeColor = Color.BLACK;
    private int strokeWidth = 2;

    public StrokeTextView(Context context){
        super(context);
        borderText = new TextView(context);
        borderText.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        borderText.setTextColor(this.mStrokeColor);  //设置描边颜色
        borderText.setGravity(getGravity());
        tp1 = borderText.getPaint();
        tp1.setStrokeWidth(this.strokeWidth); //设置描边宽度
        tp1.setStyle(Paint.Style.STROKE); //设置画笔样式为描边
        tp1.setStrokeJoin(Paint.Join.ROUND); //连接方式为圆角

    }

    //设置描边的颜色和宽度
    public void setStroke(int color,int strokeWidth) {
        tp1.setStrokeWidth(strokeWidth);
        borderText.setTextColor(color);  //设置描边颜色
    }

    @Override
    public void setLineSpacing(float add, float mult) {
        super.setLineSpacing(add, mult);
        borderText.setLineSpacing(add, mult);
    }

    @Override
    public void setMaxWidth(int maxPixels) {
        super.setMaxWidth(maxPixels);
        borderText.setMaxWidth(maxPixels);
    }

    public void setLayoutParams(ViewGroup.LayoutParams params) {
        super.setLayoutParams(params);
        borderText.setLayoutParams(params);
    }

    @Override
    public void setGravity(int gravity) {
        super.setGravity(gravity);
        borderText.setGravity(gravity);
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        super.setPadding(left, top, right, bottom);
        borderText.setPadding(left,top,right,bottom);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        borderText.measure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void setTextSize(int unit, float size) {
        super.setTextSize(unit, size);
        borderText.setTextSize(unit,size);
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        borderText.layout(left, top, right, bottom);
    }

    //必须使用这个方法设置字体
    public void setText2(CharSequence text) {
        setText(text);
        borderText.setText(getText().toString());
    }

    protected void onDraw(Canvas canvas) {
        borderText.draw(canvas);
        super.onDraw(canvas);
    }
}