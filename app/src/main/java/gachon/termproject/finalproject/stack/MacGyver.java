package gachon.termproject.finalproject.stack;

import android.util.Log;

import java.util.ArrayList;

import gachon.termproject.finalproject.ArctObj.Coord;
import gachon.termproject.finalproject.ArctObj.NemoRoom;

public class MacGyver {

    public static Point[] getBorder(ArrayList<Point> points) {
        Point[] border = new Point[4]; // LT,LB,RB,RT
        Float minX = null, maxX = null, minY = null, maxY = null;

        for (Point p : points) {
            if (minX == null || p.x < minX) minX = p.x;
            if (maxX == null || p.x > maxX) maxX = p.x;
            if (minY == null || p.y < minY) minY = p.y;
            if (maxY == null || p.y > maxY) maxY = p.y;
        }

        border[0] = new Point(minX, minY);
        border[1] = new Point(minX, maxY);
        border[2] = new Point(maxX, maxY);
        border[3] = new Point(maxX, minY);

        return border;
    }

    public static Point[] getTriangle(ArrayList<Point> points, StackManager sm) {
        Point[] triangle = new Point[3];
        Float minX = null, maxX = null, minY = null, maxY = null;

        for (Point p : points) {
            if (minX == null || p.x < minX) minX = p.x;
            if (maxX == null || p.x > maxX) maxX = p.x;
            if (minY == null || p.y < minY) minY = p.y;
            if (maxY == null || p.y > maxY) maxY = p.y;
        }

        for (Object obj : sm.objStack) {
            if (obj instanceof NemoRoom) {
                int i = 0;
                Point[] room = new Point[4];
                for (Coord c : ((NemoRoom) obj).coords) {
                    room[i] = new Point(c.getPointX(), c.getPointY());
                    i++;
                }
                // 방 안에 문이 있다면
                if (minX >= room[0].x && maxX <= room[3].x && minY >= room[0].y && maxY <= room[1].y) {
                    //위쪽 내벽에 문
                    if (minY - room[0].y <= 10) {
                        if (minX - room[0].x < room[3].x - maxX) {
                            triangle[0] = new Point(minX, room[0].y);
                            triangle[1] = new Point(minX, maxY);
                            triangle[2] = new Point(maxX, room[0].y);
                        } else {
                            triangle[0] = new Point(maxX, room[0].y);
                            triangle[1] = new Point(maxX, maxY);
                            triangle[2] = new Point(minX, room[0].y);
                        }
                    } else if (minX - room[0].x <= 10) {
                        //왼쪽 내벽에 문
                        if (minY - room[0].y < room[1].y - maxY) {
                            triangle[0] = new Point(room[0].x, minY);
                            triangle[1] = new Point(maxX, minY);
                            triangle[2] = new Point(room[0].x, maxY);
                        } else {
                            triangle[0] = new Point(room[0].x, maxY);
                            triangle[1] = new Point(maxX, maxY);
                            triangle[2] = new Point(room[0].x, minY);
                        }
                    } else if (room[1].y - maxY <= 10) {
                        //아래쪽 내벽에 문
                        if (minX - room[1].x < room[2].x - maxX) {
                            triangle[0] = new Point(minX, room[1].y);
                            triangle[1] = new Point(minX, minY);
                            triangle[2] = new Point(maxX, room[1].y);
                        } else {
                            triangle[0] = new Point(maxX, room[1].y);
                            triangle[1] = new Point(maxX, minY);
                            triangle[2] = new Point(minX, room[1].y);
                        }
                    } else if (room[3].x - maxX <= 10) {
                        //오른쪽 내벽에 문
                        if (minY - room[3].y < room[2].y - maxY) {
                            triangle[0] = new Point(room[3].x, minY);
                            triangle[1] = new Point(minX, minY);
                            triangle[2] = new Point(room[3].x, maxY);
                        } else {
                            triangle[0] = new Point(room[3].x, maxY);
                            triangle[1] = new Point(minX, maxY);
                            triangle[2] = new Point(room[3].x, minY);
                        }
                    }

                    //위쪽 외벽에 문
                } else if (minX > room[0].x && maxX < room[3].x && minY < room[0].y && maxY <= room[1].y) {
                    if (room[0].y - maxY <= 10) {
                        if (minX - room[0].x < room[3].x - maxX) {
                            triangle[0] = new Point(minX, room[0].y);
                            triangle[1] = new Point(minX, minY);
                            triangle[2] = new Point(maxX, room[0].y);
                        } else {
                            triangle[0] = new Point(maxX, room[0].y);
                            triangle[1] = new Point(maxX, minY);
                            triangle[2] = new Point(minX, room[0].y);
                        }
                    }
                } else if (minX < room[0].x && maxX <= room[0].x && minY > room[0].y && maxY < room[1].y) {
                    if (room[0].x - maxX <= 10) {
                        //왼쪽 외벽에 문
                        if (minY - room[0].y < room[1].y - maxY) {
                            triangle[0] = new Point(room[0].x, minY);
                            triangle[1] = new Point(minX, minY);
                            triangle[2] = new Point(room[0].x, maxY);
                        } else {
                            triangle[0] = new Point(room[0].x, maxY);
                            triangle[1] = new Point(minX, maxY);
                            triangle[2] = new Point(room[0].x, minY);
                        }
                    }
                } else if (minX > room[1].x && maxX < room[2].x && minY >= room[1].y && maxY > minY) {
                    if (minY - room[1].y <= 10) {
                        //아래쪽 외벽에 문
                        if (minX - room[1].x < room[2].x - maxX) {
                            triangle[0] = new Point(minX, room[1].y);
                            triangle[1] = new Point(minX, maxY);
                            triangle[2] = new Point(maxX, room[1].y);
                        } else {
                            triangle[0] = new Point(maxX, room[1].y);
                            triangle[1] = new Point(maxX, maxY);
                            triangle[2] = new Point(minX, room[1].y);
                        }

                    }
                } else if (minX >= room[3].x && maxX > minX && minY > room[3].y && maxY < room[2].y) {
                    if (minX - room[3].x <= 10) {
                        //오른쪽 외벽에 문
                        if (minY - room[3].y < room[2].y - maxY) {
                            triangle[0] = new Point(room[3].x, minY);
                            triangle[1] = new Point(maxX, minY);
                            triangle[2] = new Point(room[3].x, maxY);
                        } else {
                            triangle[0] = new Point(room[3].x, maxY);
                            triangle[1] = new Point(maxX, maxY);
                            triangle[2] = new Point(room[3].x, minY);
                        }

                    }
                }
            }
        }
        return triangle;
    }

    public static int getShortestRoomCord(StackManager stackManager, Point[] border, Coord cord) {
        // 이건 가장 가까운 점을 찾아주고
        // 보더 좀 정리해줌

        int idx = 0, i, dx, dy, threshold = 50;
        float minDistance = -1, dis;

        for (Object obj : stackManager.objStack) {
            if (obj instanceof NemoRoom) {
                for (Coord c : ((NemoRoom) obj).coords) {
                    for (i = 0; i < 4; i++) {
                        dis = getDistance(c, border[i]);
                        if (dis < threshold && (minDistance < 0 || minDistance > dis)) {
                            idx = i;
                            minDistance = dis;

                            // 링커포인트에 왼쪽 오른쪽 위 아래 어디로 연결되어있는 건지 확인을 해야해
                            if (isBorderLeftOnRoom((NemoRoom) obj, border, threshold)) {
                                Log.e("getShortestRoomCord", "Left");
                                dx = (int) (0 + ((NemoRoom) obj).thickness);
                                dy = 0;
                                cord.setCoordByPoint(c, dx, dy);
                            } else if (isBorderRightOnRoom((NemoRoom) obj, border, threshold)) {
                                Log.e("getShortestRoomCord", "RIGHT");
                                dx = (int) (0 - ((NemoRoom) obj).thickness);
                                dy = 0;
                                cord.setCoordByPoint(c, dx, dy);
                            } else if (isBorderUnderRoom((NemoRoom) obj, border, threshold)) {
                                Log.e("getShortestRoomCord", "DOWN");
                                dx = 0;
                                dy = (int) (0 - ((NemoRoom) obj).thickness);
                                cord.setCoordByPoint(c, dx, dy);
                            } else if (isBorderOnRoom((NemoRoom) obj, border, threshold)) {
                                Log.e("getShortestRoomCord", "UP");
                                dx = 0;
                                dy = (int) (0 + ((NemoRoom) obj).thickness);
                                cord.setCoordByPoint(c, dx, dy);
                            } else if (isBorderInRoom((NemoRoom) obj, border, threshold)) {
                                Log.e("getShortestRoomCord", "IN");
                                if (Math.abs(c.getPointX() - border[i].x) < threshold / 2) dx = 0;
                                else dx = (int) (border[i].x - c.getPointX());
                                if (Math.abs(c.getPointY() - border[i].y) < threshold / 2) dy = 0;
                                else dy = (int) (border[i].y - c.getPointY());
                                cord.setCoordByPoint(c, dx, dy);
                            } else {
                                Log.e("Error", "some error in find left right up down");
                            }
                        }

                        if (dis < threshold / 2) {
                            border[i].x = c.getPointX();
                            border[i].y = c.getPointY();

                            switch (i) {
                                case 0:
                                    border[1].x = border[i].x;
                                    border[3].y = border[i].y;
                                    break;
                                case 1:
                                    border[0].x = border[i].x;
                                    border[2].y = border[i].y;
                                    break;
                                case 2:
                                    border[3].x = border[i].x;
                                    border[1].y = border[i].y;
                                    break;
                                case 3:
                                    border[2].x = border[i].x;
                                    border[0].y = border[i].y;
                                    break;
                            }

                        }
                    }
                }
            }
        }
        return idx;
    }

    public static int getShortestColumnCord(StackManager stackManager, Point[] border, Coord cord) {
        // 이건 가장 가까운 점을 찾아주고
        // 보더 좀 정리해줌

        int idx = 0, i, dx, dy, threshold = 100;
        float minDistance = -1, dis;

        for (Object obj : stackManager.objStack) {
            if (obj instanceof NemoRoom) {
                for (int c = 0; c < 4; c++) {
                    for (i = 0; i < 4; i++) {
                        dis = getDistance(((NemoRoom) obj).coords[c], border[i]);
                        if (minDistance < 0 || minDistance > dis) {
                            idx = i;
                            minDistance = dis;

                            if (dis < threshold) {
                                if (isBorderLeftOnRoom((NemoRoom) obj, border, threshold) ||
                                        isBorderRightOnRoom((NemoRoom) obj, border, threshold) ||
                                        isBorderUnderRoom((NemoRoom) obj, border, threshold) ||
                                        isBorderOnRoom((NemoRoom) obj, border, threshold)) {
                                    cord.setCoordByPoint(((NemoRoom) obj).coords[c], 0, 0);
                                } else if (isBorderInRoom((NemoRoom) obj, border, threshold)) {
                                    Log.e("getShortestRoomCord", "IN");
                                    if (Math.abs(((NemoRoom) obj).innerCords[c].getPointX() - border[i].x) < threshold)
                                        dx = 0;
                                    else
                                        dx = (int) (border[i].x - ((NemoRoom) obj).innerCords[c].getPointX());
                                    if (Math.abs(((NemoRoom) obj).innerCords[c].getPointY() - border[i].y) < threshold)
                                        dy = 0;
                                    else
                                        dy = (int) (border[i].y - ((NemoRoom) obj).innerCords[c].getPointY());
                                    cord.setCoordByPoint(((NemoRoom) obj).innerCords[c], dx, dy);
                                }
                            }
                        }
//
                        if (dis < threshold / 2) {
                            border[i].x = ((NemoRoom) obj).coords[c].getPointX();
                            border[i].y = ((NemoRoom) obj).coords[c].getPointY();

                            switch (i) {
                                case 0:
                                    border[1].x = border[i].x;
                                    border[3].y = border[i].y;
                                    break;
                                case 1:
                                    border[0].x = border[i].x;
                                    border[2].y = border[i].y;
                                    break;
                                case 2:
                                    border[3].x = border[i].x;
                                    border[1].y = border[i].y;
                                    break;
                                case 3:
                                    border[2].x = border[i].x;
                                    border[0].y = border[i].y;
                                    break;
                            }
                        }
                    }
                }
            }
        }
        return idx;
    }


    public static float getDistance(Coord coord, Point point) {
        return (float) Math.sqrt(Math.pow(coord.getPointX() - point.x, 2) + Math.pow(coord.getPointY() - point.y, 2));
    }

    public static boolean isBorderLeftOnRoom(NemoRoom room, Point[] border, int threshold) {
        int cx;

        for (Coord c : room.coords) {
            cx = c.getPointX();
            for (Point p : border) {
                if (cx < (int) p.x - threshold) return false;
            }
        }
        return true;
    }

    public static boolean isBorderRightOnRoom(NemoRoom room, Point[] border, int threshold) {
        int cx;

        for (Coord c : room.coords) {
            cx = c.getPointX();
            for (Point p : border) {
                if (cx > (int) p.x + threshold) return false;
            }
        }
        return true;
    }

    public static boolean isBorderUnderRoom(NemoRoom room, Point[] border, int threshold) {
        int cy;

        for (Coord c : room.coords) {
            cy = c.getPointY();
            for (Point p : border) {
                if (cy > (int) p.y + threshold) return false;
            }
        }
        return true;
    }

    public static boolean isBorderOnRoom(NemoRoom room, Point[] border, int threshold) {
        int cy;

        for (Coord c : room.coords) {
            cy = c.getPointY();
            for (Point p : border) {
                if (cy < (int) p.y - threshold) return false;
            }
        }
        return true;
    }

    public static boolean isBorderInRoom(NemoRoom room, Point[] border, int threshold) {
        threshold /= 2;
        if (room.coords[0].getPointX() > border[0].x + threshold) return false;
        if (room.coords[0].getPointY() > border[0].y + threshold) return false;
        if (room.coords[2].getPointX() < border[0].x - threshold) return false;
        if (room.coords[2].getPointY() < border[0].y - threshold) return false;
        return true;
    }


    public static boolean isCross(float[] p, float[] q) {

        int lp = CCW(p[0], p[1], p[2], p[3], q[0], q[1]) * CCW(p[0], p[1], p[2], p[3], q[2], q[3]);
        int lq = CCW(q[0], q[1], q[2], q[3], p[0], p[1]) * CCW(q[0], q[1], q[2], q[3], p[2], p[3]);

        if (lp < 0 && lq < 0) return true;
        return false;
    }

    public static int CCW(float ax, float ay, float bx, float by, float cx, float cy) {
        float ccw = ax * by + bx * cy + cx * ay;
        ccw -= ay * bx + by * cx + cy * ax;

        if (ccw > 0) return 1;
        else if (ccw < 0) return -1;
        else return 0;
    }
}
