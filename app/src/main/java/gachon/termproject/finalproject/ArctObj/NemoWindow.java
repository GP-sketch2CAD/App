package gachon.termproject.finalproject.ArctObj;

import java.util.ArrayList;

import gachon.termproject.finalproject.stack.Point;
import gachon.termproject.finalproject.stack.StackManager;

public class NemoWindow {
    public Coord[] coords = new Coord[4];

    private int dis; // TODO: 이거 설정해주는 함수 만들어야해

    public int garo;
    public int sero = 200;
    public int degree;
    public int frame_garo = 50;
    public int frame_sero = 50;
    public int windowType = 0;


    public boolean isDisSet = false;

    public NemoWindow(Point[] border) {
        setCoords(border);
    }

    public NemoWindow(Point[] border, NemoRoom room, int winIdx) {
        int tempDis;
        if (winIdx % 2 == 0) {
            // 창문이 세로일때
            this.degree = 90;
            this.dis = (int) ((border[1].y - border[0].y) / StackManager.pointDivideMm);
            if (winIdx == 0) {
                // 왼쪽 창문일 경우
                tempDis = (int) ((room.coords[1].getPointY() - border[1].y) / StackManager.pointDivideMm);
                coords[1] = new Coord(room.innerCords[1], -200, -tempDis);
            } else { // winIdx == 2
                // 오른족 창문일 경우
                tempDis = (int) ((room.innerCords[2].getPointY() - border[1].y) / StackManager.pointDivideMm);
                coords[1] = new Coord(room.innerCords[2], 0, -tempDis);
            }
        } else {
            // 창문이 가로일때
            this.degree = 0;
            this.dis = (int) ((border[3].x - border[0].x) / StackManager.pointDivideMm);

            if (winIdx == 1) {
                // 아래 창문일 경우
                tempDis = (int) ((room.coords[1].getPointX() - border[1].x) / StackManager.pointDivideMm);
                coords[1] = new Coord(room.innerCords[1], -tempDis - room.thickness, 200);
            } else { // winIdx == 3
                // 윗 창문일 경우
                tempDis = (int) ((room.innerCords[0].getPointX() - border[0].x) / StackManager.pointDivideMm);
                coords[1] = new Coord(room.innerCords[0], -tempDis, 0);
            }

        }
        setCoords();
    }


    private void setCoords(Point[] border) {
        coords[0] = new Coord((int) border[0].x, (int) border[0].y);
        coords[1] = new Coord((int) border[1].x, (int) border[1].y);
        coords[2] = new Coord((int) border[2].x, (int) border[2].y);
        coords[3] = new Coord((int) border[3].x, (int) border[3].y);
    }

    private void setCoords() {
        // linkedCoord = LB point
        if (degree == 0) {
            coords[0] = new Coord(coords[1], 0, 0 - sero);
            coords[2] = new Coord(coords[1], dis, 0);
            coords[3] = new Coord(coords[1], dis, 0 - sero);
        } else {
            coords[0] = new Coord(coords[1], 0, 0 - dis);
            coords[2] = new Coord(coords[1], sero, 0);
            coords[3] = new Coord(coords[1], sero, 0 - dis);
        }
    }

    public void setDis(int dis) {
        this.dis = dis;
        this.isDisSet = true;
        setCoords();
    }

}
