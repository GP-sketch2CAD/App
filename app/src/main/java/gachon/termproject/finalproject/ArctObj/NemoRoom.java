package gachon.termproject.finalproject.ArctObj;

import java.util.ArrayList;

import gachon.termproject.finalproject.stack.Point;

public class NemoRoom {
    public static final int LT = 0, LB = 1, RB = 2, RT = 3;

    public int LCidx; // LT LB RB RT 순
    public Coord[] coords = new Coord[4];

    private int garo = -1;
    private int sero = -1;
    public int thickness =  0; // TODO: 이거 그리는거 구현하고 고치기
    private int pGaro;
    private int pSero;

    public NemoRoom(Point[] border, Coord linkedCoord, int LCidx){
        this.LCidx = LCidx;

        coords[LCidx] = linkedCoord;
        pGaro = (int)(border[NemoRoom.RB].x - border[NemoRoom.LB].x);
        pSero = (int)(border[NemoRoom.RB].y - border[NemoRoom.RT].y);

        setCoords(pGaro, pSero);
    }

    private void setCoords(int dx, int dy){
        Coord linkedCoord = coords[LCidx];

        switch (LCidx){
            case NemoRoom.LT:
                coords[NemoRoom.LB] = new Coord(linkedCoord, 0 , dy);
                coords[NemoRoom.RB] = new Coord(linkedCoord, dx , dy);
                coords[NemoRoom.RT] = new Coord(linkedCoord, dx , 0);
                break;
            case NemoRoom.LB:
                coords[NemoRoom.LT] = new Coord(linkedCoord, 0 , -dy);

                coords[NemoRoom.RB] = new Coord(linkedCoord, dx , 0);
                coords[NemoRoom.RT] = new Coord(linkedCoord, dx , -dy);
                break;
            case NemoRoom.RB:
                coords[NemoRoom.LT] = new Coord(linkedCoord, -dx , -dy);
                coords[NemoRoom.LB] = new Coord(linkedCoord, -dx , 0);
                coords[NemoRoom.RT] = new Coord(linkedCoord, 0 , -dy);
                break;
            case NemoRoom.RT:
                coords[NemoRoom.LT] = new Coord(linkedCoord, -dx , 0);
                coords[NemoRoom.LB] = new Coord(linkedCoord, -dx , dy);
                coords[NemoRoom.RB] = new Coord(linkedCoord, 0 , dy);
                break;
            default:
        }

    }

    public void setGaro(int garo, float pointDivideMM){
        this.garo = garo;
        this.pGaro = (int)(pointDivideMM*garo);

        setCoords(pGaro,pSero);
    }

    public void setSero(int sero, float pointDivideMM){
        this.sero = sero;
        this.pSero = (int)(pointDivideMM*sero);

        setCoords(pGaro,pSero);
    }

}
