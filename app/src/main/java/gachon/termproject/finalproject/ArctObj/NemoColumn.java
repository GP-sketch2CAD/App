package gachon.termproject.finalproject.ArctObj;

import gachon.termproject.finalproject.stack.Point;
import gachon.termproject.finalproject.stack.StackManager;

public class NemoColumn {
    public final int LT = 0, LB = 1, RB = 2, RT = 3;

    public int LCidx; // LT LB RB RT 순
    public Coord[] coords = new Coord[4];

    private int garo;
    private int sero;

    public boolean isGaroSet = false;
    public boolean isSeroSet = false;

    public NemoColumn(Point[] border, Coord linkedCoord, int LCidx) {
        this.LCidx = LCidx;

        coords[LCidx] = linkedCoord;
        garo = (int) ((border[RB].x - border[LB].x) / StackManager.pointDivideMm);
        sero = (int) ((border[RB].y - border[RT].y) / StackManager.pointDivideMm);

        setCoords();
    }

    private void setCoords() {
        switch (LCidx) {
            case LT:
                coords[LB] = new Coord(coords[LT], 0, sero);
                coords[RB] = new Coord(coords[LT], garo, sero);
                coords[RT] = new Coord(coords[LT], garo, 0);
                break;
            case LB:

                coords[LT] = new Coord(coords[LB], 0, -sero);
                coords[RB] = new Coord(coords[LB], garo, 0);
                coords[RT] = new Coord(coords[LB], garo, -sero);
                break;
            case RB:
                coords[LT] = new Coord(coords[RB], -garo, -sero);
                coords[LB] = new Coord(coords[RB], -garo, 0);
                coords[RT] = new Coord(coords[RB], 0, -sero);
                break;
            case RT:
                coords[LT] = new Coord(coords[RT], -garo, 0);
                coords[LB] = new Coord(coords[RT], -garo, sero);
                coords[RB] = new Coord(coords[RT], 0, sero);
                break;
            default:
        }

    }

    public void setGaro(int garo) {
        this.garo = garo;
        this.isGaroSet = true;
        setCoords();
    }

    public void setSero(int sero) {
        this.sero = sero;
        this.isSeroSet = true;
        setCoords();
    }

    public int getGaro() {
        return garo;
    }

    public int getSero() {
        return sero;
    }


}
