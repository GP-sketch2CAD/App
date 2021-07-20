package com.example.draw.Aobject;

public class Number {
    public XY xy;
    int size;

    public Number(int x, int y, int size){
        this.xy = new XY(x,y);
        this.size = size;
    }
}
