package gachon.termproject.finalproject.ArctObj;

import java.util.ArrayList;

import gachon.termproject.finalproject.stack.Point;

public class NemoWindow {
    public static final int LT = 0, LB = 1, RB = 2, RT = 3;

    public Coord[] coords = new Coord[4];

    public NemoWindow(Point[] border){
        setCoords(border);
    }

    private void setCoords(Point[] border) {
        coords[0] = new Coord((int)border[0].x, (int)border[0].y);
        coords[1] = new Coord((int)border[1].x, (int)border[1].y);
        coords[2] = new Coord((int)border[2].x, (int)border[2].y);
        coords[3] = new Coord((int)border[3].x, (int)border[3].y);
    }

}
