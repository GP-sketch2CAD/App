package gachon.termproject.finalproject.ArctObj;

import gachon.termproject.finalproject.stack.MacGyver;
import gachon.termproject.finalproject.stack.Point;
import gachon.termproject.finalproject.stack.StackManager;

public class Door {
    public Coord[] coords = new Coord[4];
    public Coord[] doorCoords = new Coord[4];
    public int dcIdx;

    public int doorType = 0; // left == 0, right == 1
    public int degree;
    public int garo = 1000;
    public int sero = 200;
    public int doke = 40;
    public int frame = 50;


    public Door(Point[] triangle, NemoRoom room, int doorIdx) {
        int tempDis;


        if (MacGyver.CCW(triangle[0].x, triangle[0].y, triangle[1].x, triangle[1].y, triangle[2].x, triangle[2].y) > 0)
            this.doorType = 0;
        else this.doorType = 1;

        this.degree = getDoorDegree(triangle);
        if (degree == 90 && triangle[1].x < room.coords[0].getPointX()) this.degree = 270;

        Point lb = triangle[2];
        if (triangle[0].y > triangle[2].y || triangle[0].x < triangle[2].x) lb = triangle[0];

        if (doorIdx == 0) {
            tempDis = (int) ((lb.y - room.coords[1].getPointY()) / StackManager.pointDivideMm);
            coords[1] = new Coord(room.coords[1], 0, tempDis);
        } else if (doorIdx == 1) {
            tempDis = (int) ((lb.x - room.coords[1].getPointX()) / StackManager.pointDivideMm);
            coords[1] = new Coord(room.coords[1], tempDis, 0);
        } else if (doorIdx == 2) {
            tempDis = (int) ((lb.y - room.coords[2].getPointY()) / StackManager.pointDivideMm);
            coords[1] = new Coord(room.coords[2], -room.thickness, tempDis);
        } else {
            tempDis = (int) ((lb.x - room.coords[0].getPointX()) / StackManager.pointDivideMm);
            coords[1] = new Coord(room.coords[0], tempDis, room.thickness);
        }

        setCoords();
        setDoorCoords();

    }

    private void setCoords() {
        // linkedCoord = LB point
        if (degree % 180 == 0) {
            coords[0] = new Coord(coords[1], 0, 0 - sero);
            coords[2] = new Coord(coords[1], garo, 0);
            coords[3] = new Coord(coords[1], garo, 0 - sero);
        } else {
            coords[0] = new Coord(coords[1], 0, 0 - garo);
            coords[2] = new Coord(coords[1], sero, 0);
            coords[3] = new Coord(coords[1], sero, 0 - garo);
        }
    }

    private void setDoorCoords() {
        int doorLength = garo - 2 * frame;
        if (doorType == 0) { // left
            if (degree == 0) {
                dcIdx = 0;
                doorCoords[1] = new Coord(coords[0], 0, 0);

                doorCoords[0] = new Coord(doorCoords[1], 0, -doorLength);
                doorCoords[2] = new Coord(doorCoords[1], doke, 0);
                doorCoords[3] = new Coord(doorCoords[1], doke, -doorLength);
            } else if (degree == 90) {
                dcIdx = 3;
                doorCoords[0] = new Coord(coords[3], 0, 0);

                doorCoords[1] = new Coord(doorCoords[0], doke, 0);
                doorCoords[2] = new Coord(doorCoords[0], doorLength, doke);
                doorCoords[3] = new Coord(doorCoords[0], doorLength, 0);
            } else if (degree == 180) {
                dcIdx = 2;
                doorCoords[3] = new Coord(coords[2], 0, 0);

                doorCoords[0] = new Coord(doorCoords[3], -doke,0);
                doorCoords[1] = new Coord(doorCoords[3], -doke, doorLength);
                doorCoords[2] = new Coord(doorCoords[3], 0, doorLength);
            } else if (degree == 270) {
                dcIdx = 1;
                doorCoords[2] = new Coord(coords[1], 0, 0);

                doorCoords[0] = new Coord(doorCoords[2], -doorLength, -doke);
                doorCoords[1] = new Coord(doorCoords[2], -doorLength, 0);
                doorCoords[3] = new Coord(doorCoords[2], 0, -doke);
            }
        } else if (doorType == 1) { // right
            if (degree == 0) {
                dcIdx = 3;
                doorCoords[2] = new Coord(coords[3], 0, 0);

                doorCoords[0] = new Coord(doorCoords[2], -doke, -doorLength);
                doorCoords[1] = new Coord(doorCoords[2], -doke, 0);
                doorCoords[3] = new Coord(doorCoords[2], 0, -doorLength);
            } else if (degree == 90) {
                dcIdx = 2;
                doorCoords[1] = new Coord(coords[2], 0, 0);

                doorCoords[0] = new Coord(doorCoords[1], 0, -doke);
                doorCoords[2] = new Coord(doorCoords[1], doorLength, 0);
                doorCoords[3] = new Coord(doorCoords[1], doorLength, -doke);
            } else if (degree == 180) {
                dcIdx = 1;
                doorCoords[0] = new Coord(coords[1], 0, 0);

                doorCoords[1] = new Coord(doorCoords[0], 0, doorLength);
                doorCoords[2] = new Coord(doorCoords[0], doke, doorLength);
                doorCoords[3] = new Coord(doorCoords[0], doke, 0);
            } else if (degree == 270) {
                dcIdx = 3;
                doorCoords[3] = new Coord(coords[0], 0, 0);

                doorCoords[0] = new Coord(doorCoords[3], -doorLength,0);
                doorCoords[1] = new Coord(doorCoords[3], -doorLength, doke);
                doorCoords[2] = new Coord(doorCoords[3], 0, doke);
            }
        }
    }

    private int getDoorDegree(Point[] triangle) {
        float x, y, angle;
        x = triangle[1].x - triangle[0].x;
        y = triangle[1].y - triangle[0].y;

        angle = (float) (-y / Math.pow(x * x + y * y, 0.5));
        angle = (float) Math.acos(angle);
        return (int) (angle * (180 / Math.PI));
    }
}
