package gachon.termproject.finalproject.stack;

import android.graphics.Bitmap;
import android.os.Handler;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Stack;

import gachon.termproject.finalproject.ArctObj.NemoColumn;
import gachon.termproject.finalproject.ArctObj.NemoRoom;
import gachon.termproject.finalproject.MainActivity;
import gachon.termproject.finalproject.MyView;

public class StackManager {

    private long timeThreshold = 750; // 일단 1초로 잡고 나중에 수정
    Converter converter = new Converter();
    ArrayDeque<DrawElement> drawStack;
    ArrayDeque<Object> objStack;
    MyView myView;

    public StackManager() {
        this.drawStack = new ArrayDeque<DrawElement>();
        this.objStack = new ArrayDeque<Object>();
    }

    public void setView(MyView myView) {
        this.myView = myView;
    }

    public void push(ArrayList<Point> points, long startT, long endT) {
        DrawElement newElement = new DrawElement(points, startT, endT);

        if (drawStack.size() == 0) {
            drawStack.push(newElement);
        } else {
            DrawElement lastElement = drawStack.pop();

            if (newElement.startTime - lastElement.endTime < timeThreshold) {
                lastElement.addPoints(points);
                lastElement.endTime = newElement.endTime;
                drawStack.push(lastElement);
            } else {
                drawStack.push(lastElement);
                drawStack.push(newElement);
            }

        }

        // 1.5초 뒤 변환
        callConvertDraw2Obj(1500);


    }

    public Object pop() {
        if (drawStack.size() > 0) return drawStack.pop();
        else if (objStack.size() > 0) return objStack.pop();
        else return null;
    }

    public ArrayList<Point> getAllPoints() {
        ArrayList<Point> allPoints = new ArrayList<Point>();

        // TODO: 나중에 수정하기
        for (Object obj : objStack) {
            allPoints.addAll((ArrayList<Point>) obj);
        }

        for (DrawElement de : drawStack) {
            allPoints.addAll(de.points);
        }

        return allPoints;
    }

    public ArrayList<Point> getDrawPoint() {
        ArrayList<Point> allPoints = new ArrayList<Point>();

        for (DrawElement de : drawStack) {
            allPoints.addAll(de.points);
        }

        return allPoints;
    }

    public ArrayList<int[]> getWallPoint() {
        ArrayList<int[]> result = new ArrayList<>();

        for (Object obj : objStack) {
            if (obj instanceof NemoRoom) {
                for (int i = 0; i < 3; i++) {
                    int[] temp = new int[]{((NemoRoom) obj).coords[i].getX(),
                            ((NemoRoom) obj).coords[i].getY(),
                            ((NemoRoom) obj).coords[i + 1].getX(),
                            ((NemoRoom) obj).coords[i + 1].getY()};
                    result.add(temp);
                }
                result.add(new int[]{((NemoRoom) obj).coords[3].getX(),
                        ((NemoRoom) obj).coords[3].getY(),
                        ((NemoRoom) obj).coords[0].getX(),
                        ((NemoRoom) obj).coords[0].getY()});
            }
        }

        return result;
    }

    public ArrayList<int[]> getColumnsPoint() {
        ArrayList<int[]> result = new ArrayList<>();

        for (Object obj : objStack) {
            if (obj instanceof NemoColumn) {
                int[] temp = new int[]{((NemoColumn) obj).coords[0].getX(),
                        ((NemoColumn) obj).coords[0].getY(),
                        ((NemoColumn) obj).coords[2].getX(),
                        ((NemoColumn) obj).coords[2].getY()};
                result.add(temp);
            }
        }
        return result;
    }


    private void callConvertDraw2Obj(long time) {
        // 1.5초 뒤 변환
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                convertDraw2Obj();
            }
        }, time);
    }

    private void convertDraw2Obj() {
        long lt;

        if (drawStack.size() == 0) return;
        for (DrawElement de : drawStack) {
            lt = System.currentTimeMillis() - de.endTime;
            if (lt < 2 * timeThreshold) {
                callConvertDraw2Obj(1000);
                return;
            }
            objStack.push(converter.convertPoints2Obj(de.points, this));
            drawStack.pollFirst();
        }
        myView.invalidate();
    }

}
