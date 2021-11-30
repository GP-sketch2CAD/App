package gachon.termproject.finalproject.stack;

import java.util.ArrayList;

public class DrawElement {
    public long startTime;
    public long endTime;
    public ArrayList<Point> points;

    DrawElement(ArrayList<Point> points, long startTime, long endTime){
        this.startTime = startTime;
        this.endTime = endTime;
        this.points = points;
    }

    public void addPoints(ArrayList<Point> addPoints){
        this.points.addAll(addPoints);
    }
}

