package gachon.termproject.finalproject.stack;

import java.util.ArrayList;
import java.util.Stack;

public class StackManager {

    private long timeThreshold = 500; // 일단 1초로 잡고 나중에 수정
    Stack<DrawElement> drawStack;
    Stack<Object> objStack;


    public StackManager() {
        this.drawStack = new Stack<DrawElement>();
        this.objStack = new Stack<Object>();
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
        // TODO: convert 함수 만들고 얼마 뒤에 콜하기
    }

    public Object pop() {
        if (!drawStack.empty()) return drawStack.pop();
        else if (!objStack.empty()) return objStack.pop();
        else return null;
    }

    public ArrayList<Point> getAllPoints() {
        ArrayList<Point> allPoints = new ArrayList<Point>();

        // TODO: 나중에 obj도 추가해줘야함
        for (DrawElement de : drawStack) {
            allPoints.addAll(de.points);
        }

        return allPoints;
    }

}
