package gachon.termproject.finalproject.ArctObj;

import gachon.termproject.finalproject.stack.StackManager;

public class Coord {
    public boolean isLinking;
    private int x;
    private int y;

    private Coord linkingCoord;
    private int dx;
    private int dy;

    public Coord(int x, int y){
        this.isLinking = false;
        this.x = x;
        this.y = y;
    }

    public Coord(Coord linkingCoord, int dx, int dy){
        this.isLinking = true;
        this.linkingCoord = linkingCoord;
        this.dx = dx;
        this.dy = dy;
    }

    public void setCoordByPoint(Coord linkingCoord, int dx, int dy){
        this.isLinking = true;
        this.linkingCoord = linkingCoord;
        this.dx = dx;
        this.dy = dy;
    }

    public int getPointX(){
        if(isLinking == false) return x;
        return (int) (getX() * StackManager.pointDivideMm);
    }

    public int getPointY(){
        if(isLinking == false) return y;
        return (int) (getY() * StackManager.pointDivideMm);
    }

    public int getX(){
        if(isLinking) return linkingCoord.getX() + dx;
        else return x;
    }

    public int getY(){
        if(isLinking) return linkingCoord.getY() + dy;
        else return y;
    }


}
