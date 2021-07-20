package com.example.draw.Stack;

import com.example.draw.Aobject.XY;

import java.util.ArrayList;
import java.util.List;

public class StackData {
    protected long startTime;
    protected long lastTime;
    protected ArrayList<XY> xyList;
    protected Object object;

    public StackData() {
        xyList = new ArrayList<>();
        object = null;
    }

    public StackData(long startTime) {
        this();
        this.startTime = startTime;
    }

    public void addXY(int x, int y) {
        XY newXY = new XY(x, y);
        xyList.add(newXY);
    }

    // getter
    public long getStartTime() {
        return startTime;
    }

    public long getLastTime() {
        return lastTime;
    }

    public ArrayList<XY> getXyList() {
        return this.xyList;
    }

    // setter
    public void setLastTime(long lastTime) {
        this.lastTime = lastTime;
    }


    public void setXyList(ArrayList<XY> xyList) {
        this.xyList = xyList;
    }
}
