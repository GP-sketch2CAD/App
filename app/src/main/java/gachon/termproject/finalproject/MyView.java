package gachon.termproject.finalproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import gachon.termproject.finalproject.stack.Point;
import gachon.termproject.finalproject.stack.StackManager;

public class MyView extends View {
    ArrayList<Point> points = new ArrayList<Point>();
    StackManager stackManager;
    Bitmap bitmap = null;
    private DigitClassifier digitClassifier = new DigitClassifier(this);
    int color = Color.BLACK;

    long startTime;
    long endTime;

    public MyView(Context context, StackManager stackManager) {
        super(context);
        this.stackManager = stackManager;
        this.digitClassifier.initialize().addOnFailureListener((OnFailureListener)null);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        bitmap = Bitmap.createBitmap(this.getWidth(), this.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas c=new Canvas(bitmap);
        Paint p1 = new Paint();
        p1.setStrokeWidth(10);
        p1.setColor(Color.WHITE);
        Paint p = new Paint();
        p.setStrokeWidth(10);
        Log.w("2", "2");


        // 벽 그리기
        ArrayList<int[]> lines = stackManager.getWallPoint();
        for(int[] line: lines){
            canvas.drawLine(line[0], line[1], line[2], line[3], p);
        }

        // 기둥 그리기
        lines = stackManager.getColumnsPoint();
        Paint b = new Paint();
        b.setStrokeWidth(10);
        b.setColor(Color.BLUE);
        for(int[] rec: lines){
            canvas.drawRect(rec[0], rec[1], rec[2], rec[3], b);
        }


        // drawStack 있는 것들
        ArrayList<Point> toDraw = stackManager.getDrawPoint();
        for (int i = 1; i < toDraw.size(); i++) {
            if (!toDraw.get(i).check)
                continue;
            c.drawLine(toDraw.get(i - 1).x, toDraw.get(i - 1).y, toDraw.get(i).x, toDraw.get(i).y, p);
            canvas.drawLine(toDraw.get(i - 1).x, toDraw.get(i - 1).y, toDraw.get(i).x, toDraw.get(i).y, p1);

        }

        // 실시간으로 그려지고 있는 것들
        for (int i = 1; i < points.size(); i++) {
            if (!points.get(i).check)
                continue;
            c.drawLine(points.get(i - 1).x, points.get(i - 1).y, points.get(i).x, points.get(i).y, p);
            canvas.drawLine(points.get(i - 1).x, points.get(i - 1).y, points.get(i).x, points.get(i).y, p1);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        Log.e("p3", String.valueOf(points));

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startTime = System.currentTimeMillis();
                points.add(new Point(x, y, false, color));
            case MotionEvent.ACTION_MOVE:
                points.add(new Point(x, y, true, color));
                break;
            case MotionEvent.ACTION_UP:
                endTime = System.currentTimeMillis();
                this.classifyDrawing();
                stackManager.push((ArrayList<Point>) points.clone(), startTime, endTime);
                points.clear();
                break;
        }
        invalidate();
        return true;
    }

    private void classifyDrawing() {
        Bitmap abc= Bitmap.createBitmap(bitmap,0,0,this.getWidth(), this.getHeight());

        if (abc != null && this.digitClassifier.isInitialized()) {
            this.digitClassifier.classifyAsync(abc).addOnSuccessListener((OnSuccessListener)(new OnSuccessListener() {
                // $FF: synthetic method
                // $FF: bridge method
                public void onSuccess(Object var1) {
                    this.onSuccess((String)var1);
                }

                public final void onSuccess(String resultText) {
                    Log.e("resultText", resultText);
                }
            })).addOnFailureListener((OnFailureListener)(new OnFailureListener() {
                public final void onFailure(Exception e) {

                    Log.e("MainActivity", "Error classifying drawing.", (Throwable)e);
                }
            }));
        }

    }

}
