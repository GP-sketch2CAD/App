package com.example.draw.Aobject;

public class Wall {
    private XY leftTop;
    private XY rightBottom;
    private int thickness;
    private int length;

    public Wall(int leftTopX, int leftTopY, int rightBotX, int rightBotY){
        this.leftTop = new XY(leftTopX,leftTopY);
        this.rightBottom = new XY(rightBotX,rightBotY);
    }

    public Wall(XY leftTop, XY rightBottom){
        this.leftTop = leftTop;
        this.rightBottom = rightBottom;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setThickness(int thickness) {
        this.thickness = thickness;
    }
}
