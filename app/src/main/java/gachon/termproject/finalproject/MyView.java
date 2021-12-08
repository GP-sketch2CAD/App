package gachon.termproject.finalproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.google.android.gms.tasks.OnFailureListener;

import java.util.ArrayList;

import gachon.termproject.finalproject.stack.Digit;
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

        Paint blackPaint = new Paint();
        blackPaint.setStrokeWidth(10);
        blackPaint.setColor(Color.BLACK);
        Log.w("2", "2");


        // 벽 그리기
        ArrayList<int[]> lines;
        ArrayList<float[]> rectangles = stackManager.getWallPoint();
        for(float[] rec: rectangles){
            canvas.drawRect(rec[0], rec[1], rec[2], rec[3], blackPaint);
        }

        // 기둥 그리기
        lines = stackManager.getColumnsPoint();
        Paint bluePaint = new Paint();
        bluePaint.setStrokeWidth(10);
        bluePaint.setColor(Color.BLUE);
        for(int[] rec: lines){
            canvas.drawRect(rec[0], rec[1], rec[2], rec[3], bluePaint);
        }

        //창문 그리기
        lines = stackManager.getWindowPoint();
        Paint yellowPaint = new Paint();
        yellowPaint.setStrokeWidth(5);
        yellowPaint.setColor(Color.YELLOW);
        for(int[] rec: lines){
            canvas.drawRect(rec[0], rec[1], rec[2], rec[3], yellowPaint);
        }

        // 문 그리기
        lines = stackManager.getDoorPoint();
        Paint greenPaint = new Paint();
        greenPaint.setStrokeWidth(5);
        greenPaint.setColor(Color.GREEN);
        for(int[] rec: lines){
            canvas.drawRect(rec[0], rec[1], rec[2], rec[3], greenPaint);
        }

        // 숫자 쓰기
        ArrayList<Digit> todigitDraw = stackManager.getDigitPoint();
        Paint t= new Paint();
        t.setTextSize(50);
        for(int i = 0; i < todigitDraw.size(); i++) {
            if (todigitDraw.get(i).check)
                canvas.drawText(Integer.toString(todigitDraw.get(i).result)
                        , (int) ((todigitDraw.get(i).points[0].x+todigitDraw.get(i).points[2].x)/2)
                        , (int) ((todigitDraw.get(i).points[0].y+todigitDraw.get(i).points[1].y)/2)
                        , t);
        }



        // drawStack 있는 것들
        ArrayList<Point> toDraw = stackManager.getDrawPoint();
        for (int i = 1; i < toDraw.size(); i++) {
            if (!toDraw.get(i).check)
                continue;
            c.drawLine(toDraw.get(i - 1).x, toDraw.get(i - 1).y, toDraw.get(i).x, toDraw.get(i).y, p1);
            canvas.drawLine(toDraw.get(i - 1).x, toDraw.get(i - 1).y, toDraw.get(i).x, toDraw.get(i).y, blackPaint);

        }

        // 실시간으로 그려지고 있는 것들
        for (int i = 1; i < points.size(); i++) {
            if (!points.get(i).check)
                continue;
            c.drawLine(points.get(i - 1).x, points.get(i - 1).y, points.get(i).x, points.get(i).y, p1);
            canvas.drawLine(points.get(i - 1).x, points.get(i - 1).y, points.get(i).x, points.get(i).y, blackPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        //Log.e("p3", String.valueOf(points));

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startTime = System.currentTimeMillis();
                points.add(new Point(x, y, false, color));
            case MotionEvent.ACTION_MOVE:
                points.add(new Point(x, y, true, color));
                break;
            case MotionEvent.ACTION_UP:
                endTime = System.currentTimeMillis();
                stackManager.push((ArrayList<Point>) points.clone(), startTime, endTime);
                points.clear();
                break;
        }
        invalidate();
        return true;
    }

    public int classifyDrawing(Point[] around) throws Throwable {
        Bitmap abc= Bitmap.createBitmap(bitmap,(int)around[0].x-10,(int)around[0].y-10,(int)(around[2].x-around[0].x+20), (int)(around[1].y-around[0].y+20));

        if (abc != null && this.digitClassifier.isInitialized()) {
            int a= this.digitClassifier.classify(abc);
            return a;
        }
        else return -1;
    }

}