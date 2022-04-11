package gachon.termproject.finalproject.ArctObj;

import java.util.ArrayList;

import gachon.termproject.finalproject.stack.Point;
import gachon.termproject.finalproject.stack.StackManager;

public class NemoRoom {
    public final int LT = 0, LB = 1, RB = 2, RT = 3;

    public int LCidx; // LT LB RB RT 순
    public Coord[] coords = new Coord[4];
    public Coord[] innerCords = new Coord[4];

    private int garo;
    private int sero;
    public int thickness = 200;

    public boolean isGaroSet = false;
    public boolean isSeroSet = false;

    public NemoRoom(Point[] border, Coord linkedCoord, int LCidx) {
        this.LCidx = LCidx;

        coords[LCidx] = linkedCoord;
        garo = (int) ((border[RB].x - border[LB].x) / StackManager.pointDivideMm) - 2*thickness;
        sero = (int) ((border[RB].y - border[RT].y) / StackManager.pointDivideMm) - 2*thickness;

        setCoords();
    }

    private void setCoords() {
        switch (LCidx) {
            case LT:
                innerCords[LT] = new Coord(coords[LCidx], thickness, thickness);
                innerCords[LB] = new Coord(innerCords[LT], 0, sero);
                innerCords[RB] = new Coord(innerCords[LT], garo, sero);
                innerCords[RT] = new Coord(innerCords[LT], garo, 0);

                coords[LB] = new Coord(innerCords[LT], 0 - thickness, sero + thickness);
                coords[RB] = new Coord(innerCords[LT], garo + thickness, sero + thickness);
                coords[RT] = new Coord(innerCords[LT], garo + thickness, 0 - thickness);
                break;
            case LB:
                innerCords[LB] = new Coord(coords[LCidx], thickness, -thickness);
                innerCords[LT] = new Coord(innerCords[LB], 0, -sero);
                innerCords[RB] = new Coord(innerCords[LB], garo, 0);
                innerCords[RT] = new Coord(innerCords[LB], garo, -sero);

                coords[LT] = new Coord(innerCords[LB], 0 - thickness, -sero - thickness);
                coords[RB] = new Coord(innerCords[LB], garo + thickness, 0 + thickness);
                coords[RT] = new Coord(innerCords[LB], garo + thickness, -sero - thickness);
                break;
            case RB:
                innerCords[RB] = new Coord(coords[LCidx], -thickness, -thickness);
                innerCords[LT] = new Coord(innerCords[RB], -garo, -sero);
                innerCords[LB] = new Coord(innerCords[RB], -garo, 0);
                innerCords[RT] = new Coord(innerCords[RB], 0, -sero);

                coords[LT] = new Coord(innerCords[RB], -garo - thickness, -sero - thickness);
                coords[LB] = new Coord(innerCords[RB], -garo - thickness, 0 + thickness);
                coords[RT] = new Coord(innerCords[RB], 0 + thickness, -sero - thickness);
                break;
            case RT:
                innerCords[RT] = new Coord(coords[LCidx], -thickness, thickness);
                innerCords[LT] = new Coord(innerCords[RT], -garo, 0);
                innerCords[LB] = new Coord(innerCords[RT], -garo, sero);
                innerCords[RB] = new Coord(innerCords[RT], 0, sero);

                coords[LT] = new Coord(innerCords[RT], -garo - thickness, 0 - thickness);
                coords[LB] = new Coord(innerCords[RT], -garo - thickness, sero + thickness);
                coords[RB] = new Coord(innerCords[RT], 0 + thickness, sero + thickness);
                break;
            default:
        }

    }

    private void setCoords2() {
        switch (LCidx) {
            case LT:
                innerCords[LT].setCoordByPoint(coords[LCidx], thickness, thickness);
                innerCords[LB].setCoordByPoint(innerCords[LT], 0, sero);
                innerCords[RB].setCoordByPoint(innerCords[LT], garo, sero);
                innerCords[RT].setCoordByPoint(innerCords[LT], garo, 0);

                coords[LB].setCoordByPoint(innerCords[LT], 0 - thickness, sero + thickness);
                coords[RB].setCoordByPoint(innerCords[LT], garo + thickness, sero + thickness);
                coords[RT].setCoordByPoint(innerCords[LT], garo + thickness, 0 - thickness);
                break;
            case LB:
                innerCords[LB].setCoordByPoint(coords[LCidx], thickness, -thickness);
                innerCords[LT].setCoordByPoint(innerCords[LB], 0, -sero);
                innerCords[RB].setCoordByPoint(innerCords[LB], garo, 0);
                innerCords[RT].setCoordByPoint(innerCords[LB], garo, -sero);

                coords[LT].setCoordByPoint(innerCords[LB], 0 - thickness, -sero - thickness);
                coords[RB].setCoordByPoint(innerCords[LB], garo + thickness, 0 + thickness);
                coords[RT].setCoordByPoint(innerCords[LB], garo + thickness, -sero - thickness);
                break;
            case RB:
                innerCords[RB].setCoordByPoint(coords[LCidx], -thickness, -thickness);
                innerCords[LT].setCoordByPoint(innerCords[RB], -garo, -sero);
                innerCords[LB].setCoordByPoint(innerCords[RB], -garo, 0);
                innerCords[RT].setCoordByPoint(innerCords[RB], 0, -sero);

                coords[LT].setCoordByPoint(innerCords[RB], -garo - thickness, -sero - thickness);
                coords[LB].setCoordByPoint(innerCords[RB], -garo - thickness, 0 + thickness);
                coords[RT].setCoordByPoint(innerCords[RB], 0 + thickness, -sero - thickness);
                break;
            case RT:
                innerCords[RT].setCoordByPoint(coords[LCidx], -thickness, thickness);
                innerCords[LT].setCoordByPoint(innerCords[RT], -garo, 0);
                innerCords[LB].setCoordByPoint(innerCords[RT], -garo, sero);
                innerCords[RB].setCoordByPoint(innerCords[RT], 0, sero);
                coords[LT].setCoordByPoint(innerCords[RT], -garo - thickness, 0 - thickness);
                coords[LB].setCoordByPoint(innerCords[RT], -garo - thickness, sero + thickness);
                coords[RB].setCoordByPoint(innerCords[RT], 0 + thickness, sero + thickness);
                break;
            default:
        }

    }

    public void setGaro(int garo) {
        this.garo = garo;
        this.isGaroSet = true;
        setCoords2();
    }

    public void setSero(int sero) {
        this.sero = sero;
        this.isSeroSet = true;
        setCoords2();
    }

    public int getGaro() {
        return garo;
    }

    public int getSero() {
        return sero;
    }
}
