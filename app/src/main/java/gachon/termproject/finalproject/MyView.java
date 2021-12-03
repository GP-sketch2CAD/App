package gachon.termproject.finalproject;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

import gachon.termproject.finalproject.stack.Point;
import gachon.termproject.finalproject.stack.StackManager;

public class MyView extends View {
    ArrayList<Point> points = new ArrayList<Point>();
    StackManager stackManager;
    int color = Color.BLACK;

    long startTime;
    long endTime;

    public MyView(Context context, StackManager stackManager) {
        super(context);
        this.stackManager = stackManager;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint p = new Paint();
        p.setStrokeWidth(10);

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
            canvas.drawLine(toDraw.get(i - 1).x, toDraw.get(i - 1).y, toDraw.get(i).x, toDraw.get(i).y, p);

        }

        // 실시간으로 그려지고 있는 것들
        for (int i = 1; i < points.size(); i++) {
            if (!points.get(i).check)
                continue;
            canvas.drawLine(points.get(i - 1).x, points.get(i - 1).y, points.get(i).x, points.get(i).y, p);
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
                stackManager.push((ArrayList<Point>) points.clone(), startTime, endTime);
                points.clear();
                break;
        }
        invalidate();
        return true;
    }
}
