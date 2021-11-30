package gachon.termproject.finalproject.stack;

import android.os.Handler;

import java.util.ArrayList;
import java.util.Stack;

import gachon.termproject.finalproject.MainActivity;
import gachon.termproject.finalproject.MyView;

public class StackManager {

    private long timeThreshold = 500; // 일단 1초로 잡고 나중에 수정
    Converter converter = new Converter();
    Stack<DrawElement> drawStack;
    Stack<Object> objStack;
    MyView myView;

    public StackManager() {
        this.drawStack = new Stack<DrawElement>();
        this.objStack = new Stack<Object>();
    }
    public void setView(MyView myView){
        this.myView = myView;
    }

    public void push(ArrayList<Point> points, long startT, long endT) {
        DrawElement newElement = new DrawElement(points, startT, endT);

        if (drawStack.empty()) {
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
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                convertDraw2Obj();
            }
        }, 1500);


    }

    public Object pop() {
        if (!drawStack.empty()) return drawStack.pop();
        else if (!objStack.empty()) return objStack.pop();
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

    private void convertDraw2Obj() {
        while (!drawStack.empty()) {
            // TODO: 다시 수정하기
            DrawElement de = drawStack.pop();
            objStack.push(converter.convertPoints2Obj(de.points));
        }
        myView.invalidate();
    }

}
