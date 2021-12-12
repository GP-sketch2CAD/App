package gachon.termproject.finalproject.stack;


import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import gachon.termproject.finalproject.ArctObj.Coord;
import gachon.termproject.finalproject.ArctObj.Door;
import gachon.termproject.finalproject.ArctObj.NemoColumn;
import gachon.termproject.finalproject.ArctObj.NemoRoom;
import gachon.termproject.finalproject.ArctObj.NemoWindow;


public class Converter {
    double epsilon = 50.;

    Converter() {

    }

    public Object convertPoints2Obj(ArrayList<Point> points, StackManager stackManager) {
            Object obj = (ArrayList<Point>) Line2Straight.douglasPeucker((List<Point>) points, epsilon);

            if (isDoor(points, stackManager)) {
                Log.e("arctOBJ", "door");
                Point[] triangle = MacGyver.getTriangle(points, stackManager);
                float minX, minY, maxX, maxY;
                int doorDisThreshold = 30;

                for (Object room : stackManager.objStack) {
                    if (room instanceof NemoRoom) {
                        NemoRoom nr = (NemoRoom) room;

                        minX = nr.coords[0].getPointX();
                        minY = nr.coords[0].getPointY();
                        maxX = nr.coords[2].getPointX();
                        maxY = nr.coords[2].getPointY();

                        if (Math.abs(minY - triangle[0].y) < doorDisThreshold) {
                            // 위에 있는 경우
                            obj = new Door(triangle, nr, 3);
                            break;
                        } else if (Math.abs(minX - triangle[0].x) < doorDisThreshold) {
                            // 왼쪽에 있는 경우
                            obj = new Door(triangle, nr, 0);
                            break;
                        } else if (Math.abs(maxY - triangle[0].y) < doorDisThreshold) {
                            // 아래에 있는 경우
                            obj = new Door(triangle, nr, 1);
                            break;
                        } else if (Math.abs(maxX - triangle[0].x) < doorDisThreshold) {
                            // 오른쪽에 있는 경우
                            obj = new Door(triangle, nr, 2);
                            break;
                        }
                    }
                }
            } else if (isWindow(points, stackManager)) {
                Log.e("arctOBJ", "window");
                Point[] border = MacGyver.getBorder(points);

                float[] xSun1 = new float[]{border[0].x, border[0].y, border[3].x, border[3].y};
                float[] xSun2 = new float[]{border[1].x, border[1].y, border[2].x, border[2].y};
                float[] ySun1 = new float[]{border[0].x, border[0].y, border[1].x, border[1].y};
                float[] ySun2 = new float[]{border[2].x, border[2].y, border[3].x, border[3].y};

                for (Object room : stackManager.objStack) {
                    if (room instanceof NemoRoom) {
                        NemoRoom nr = (NemoRoom) room;
                        float[] left = new float[]{nr.coords[0].getPointX(), nr.coords[0].getPointY(), nr.coords[1].getPointX(), nr.coords[1].getPointY()};
                        float[] bot = new float[]{nr.coords[1].getPointX(), nr.coords[1].getPointY(), nr.coords[2].getPointX(), nr.coords[2].getPointY()};
                        float[] right = new float[]{nr.coords[2].getPointX(), nr.coords[2].getPointY(), nr.coords[3].getPointX(), nr.coords[3].getPointY()};
                        float[] top = new float[]{nr.coords[0].getPointX(), nr.coords[0].getPointY(), nr.coords[3].getPointX(), nr.coords[3].getPointY()};

                        if (MacGyver.isCross(xSun1, left) && MacGyver.isCross(xSun2, left)) {
                            obj = new NemoWindow(border, nr, 0);
                            break;
                        } else if (MacGyver.isCross(xSun1, right) && MacGyver.isCross(xSun2, right)) {
                            obj = new NemoWindow(border, nr, 2);
                            break;
                        } else if (MacGyver.isCross(ySun1, bot) && MacGyver.isCross(ySun2, bot)) {
                            obj = new NemoWindow(border, nr, 1);
                            break;
                        } else if (MacGyver.isCross(ySun1, top) && MacGyver.isCross(ySun2, top)) {
                            obj = new NemoWindow(border, nr, 3);
                            break;
                        }
                    }
                }
            } else if (isColumn(points)) {
                Log.e("arctOBJ", "column");
                int LCidx;
                Point[] border = MacGyver.getBorder(points);
                int g = (int) ((border[0].x) / StackManager.pointDivideMm);
                int s = (int) ((border[0].y) / StackManager.pointDivideMm);
                Coord linkC = new Coord(StackManager.initialCord, g, s);

                LCidx = MacGyver.getShortestColumnCord(stackManager, border, linkC);
                obj = new NemoColumn(border, linkC, LCidx);

            } else if (isOverlap(points, stackManager)) {
                obj = null;

            } else if (isWall(points)) {
                if (((ArrayList<Point>) Line2Straight.douglasPeucker((List<Point>) points, epsilon)).size() > 4 &&
                        ((ArrayList<Point>) Line2Straight.douglasPeucker((List<Point>) points, epsilon)).size() < 7) {
                    int LCidx;
                    Point[] border = MacGyver.getBorder(points);
                    int g = (int) ((border[0].x) / StackManager.pointDivideMm);
                    int s = (int) ((border[0].y) / StackManager.pointDivideMm);
                    Coord linkC = new Coord(StackManager.initialCord, g, s);

                    LCidx = MacGyver.getShortestRoomCord(stackManager, border, linkC);
                    obj = new NemoRoom(border, linkC, LCidx);
                }else
                    obj = null;
            }
        return obj;
        }


    private boolean isDoor(ArrayList<Point> points, StackManager sm) {
        if (((ArrayList<Point>) Line2Straight.douglasPeucker((List<Point>) points, epsilon)).size() == 3 && !sm.objStack.isEmpty()) {
            Point[] door = MacGyver.getTriangle(points, sm);
            if (door[0] != null && door[1] != null && door[2] != null) {
                return true;
            }
        }
        return false;
    }

    private boolean isWindow(ArrayList<Point> points, StackManager sm) {
        int LB = 0, LT = 1, RT = 2, RB = 3;
        Point[] border = MacGyver.getBorder(points);
        for (Object obj : sm.objStack) {
            if (obj instanceof NemoRoom) {
                int count = 0;
                int i = 0;
                Point[] room = new Point[4];
                for (Coord c : ((NemoRoom) obj).coords) {
                    room[i] = new Point(c.getPointX(), c.getPointY());
                    i++;
                }
                for (int a = 0; a < 4; a++) {
                    if ((border[a].x > room[LT].x && border[a].x < room[RT].x) && (border[a].y < room[LT].y && border[a].y > room[LB].y)) {
                        count++;
                    }
                }

                if(count == 2) return true;

            }
        }
        return false;
    }

    private boolean isColumn(ArrayList<Point> points) {
        Point[] border = MacGyver.getBorder(points);
        ArrayList<Point> sLine = (ArrayList<Point>) Line2Straight.douglasPeucker(points, epsilon / 2);
        int check = 0;

        float[] LTRB = new float[]{border[0].x, border[0].y, border[2].x, border[2].y};
        float[] RTLB = new float[]{border[3].x, border[3].y, border[1].x, border[1].y};

        for (int i = 0; i < sLine.size() - 1; i++) {
            if (sLine.get(i).check == false && sLine.get(i + 1).check == true) {
                float[] ml = new float[]{sLine.get(i).x, sLine.get(i).y, sLine.get(i + 1).x, sLine.get(i + 1).y};
                if (MacGyver.isCross(LTRB, ml)) check++;
                if (MacGyver.isCross(RTLB, ml)) check++;
            }
        }

        if (check > 2) return true;
        return false;
    }

    private boolean isWall(ArrayList<Point> points) {
        // TODO: 일단 넓이로 좀 해볼까.....
        return true;
    }

    private boolean isOverlap(ArrayList<Point> points, StackManager stackManager) {
        //border에 x와 y좌표가 기준사각형의 x와 y보다 작으면 안에있는 사각형, border의 x와 y가 기준 사각형의 x와 y보다 크면 보다 큰 사각형
        Point[] border = MacGyver.getBorder(points);
        int LB = 0, LT = 1, RT = 2, RB = 3;

        int ccount = 0;

        Point[] room = new Point[4];

        if (stackManager.objStack.size() == 0) return false;

        for (Object obj : stackManager.objStack) {
            if (obj instanceof NemoRoom) {
                int count = 0;
                int i = 0;
                //Point[] room = new Point[4];
                for (Coord c : ((NemoRoom) obj).coords) {
                    room[i] = new Point(c.getPointX(), c.getPointY());
                    i++;
                }

                for (int a = 0; a < 4; a++) {
                    if ((border[a].x > room[LT].x && border[a].x < room[RT].x) && (border[a].y < room[LT].y && border[a].y > room[LB].y)) {
                        count++;
                    }
                }
                if (count == 0) {
                    if ((border[LT].y < room[LT].y && border[LT].y > room[LB].y) && (border[LT].x < room[LT].x && border[RT].x > room[RT].x))
                        count = 1;
                    if ((border[RT].x > room[LT].x && border[RT].x < room[RT].x) && (border[LT].y > room[LT].y && border[LB].y < room[LB].y))
                        count = 1;
                    if ((border[LB].y < room[LT].y && border[LB].y > room[LB].y) && (border[LT].x < room[LT].x && border[RT].x > room[RT].x))
                        count = 1;
                    if ((border[LT].x > room[LT].x && border[LT].x < room[RT].x) && (border[LT].y > room[LT].y && border[LB].y < room[LB].y))
                        count = 1;
                }

                if (count == 1) {
                    Log.e("Overlap!", "Nope");
                    return true;
                } else {
                    ccount++;
                    continue;
                }
            }

        }
        if (ccount == stackManager.objStack.size()) {
            return false;
        }
        //Log.e("Overlap!!!!!!!!!", "Nope");
        return false;
    }

}

class Line2Straight {

    public Line2Straight() {
    }

    private static final double sqr(float x) {
        return Math.pow(x, 2);
    }

    private static final double distanceBetweenPoints(float vx, float vy, float wx, float wy) {
        return sqr(vx - wx) + sqr(vy - wy);
    }

    private static final double distanceToSegmentSquared(float px, float py, float vx, float vy, float wx, float wy) {
        final double l2 = distanceBetweenPoints(vx, vy, wx, wy);
        if (l2 == 0)
            return distanceBetweenPoints(px, py, vx, vy);
        final double t = ((px - vx) * (wx - vx) + (py - vy) * (wy - vy)) / l2;
        if (t < 0)
            return distanceBetweenPoints(px, py, vx, vy);
        if (t > 1)
            return distanceBetweenPoints(px, py, wx, wy);
        return distanceBetweenPoints(px, py, (int) (vx + t * (wx - vx)), (int) (vy + t * (wy - vy)));
    }

    private static final double perpendicularDistance(int px, int py, int vx, int vy, int wx, int wy) {
        return Math.sqrt(distanceToSegmentSquared(px, py, vx, vy, wx, wy));
    }

    private static final double perpendicularDistance(Point start, Point end, Point point) {
        return Math.sqrt(distanceToSegmentSquared(point.x, point.y, start.x, start.y, end.x, end.y));
    }

    public static final List<Point> douglasPeucker(List<Point> list, double epsilon) {
        // Find the point with the maximum distance
        double dMax, d;
        int index, start, end;
        Point startXY, endXY, pointXY;
        List<Point> resultList = new ArrayList<>();

        dMax = 0;
        index = 0;
        start = 0;
        end = list.size() - 1;

        startXY = list.get(start);
        endXY = list.get(end);

        for (int i = start + 1; i < end - 1; i++) {
            pointXY = list.get(i);
            d = perpendicularDistance(startXY, endXY, pointXY);

            if (d > dMax) {
                index = i;
                dMax = d;
            }
        }

        // If max distance is greater than epsilon, recursively simplify
        if (dMax > epsilon) {
            // Recursive call
            List<Point> result1 = douglasPeucker(list.subList(0, index + 1), epsilon);
            List<Point> result2 = douglasPeucker(list.subList(index, end + 1), epsilon);

            resultList.addAll(result1);
            resultList.addAll(result2);
        } else {
            if ((end - start) > 0) {
                resultList.add(list.get(start));
                resultList.add(list.get(end));
            } else {
                resultList.add(list.get(start));
            }
        }

        ArrayList<Point> uniqueList = new ArrayList<>();
        for (Point p : resultList) {
            if (!uniqueList.contains(p)) uniqueList.add(p);
        }
        return uniqueList;
    }


}

class Line2Nemo {

    public static ArrayList<Point> nemoNemo(ArrayList<Point> points) {
        ArrayList<Point> result = new ArrayList<>();
        float[] xAxis = {1, 0}, yAxis = {0, 1}, vec;
        int angleThreshold = 20;
        int tempAngle;

        if (points.size() < 4) return points;

        result.add(points.get(0));
        for (int i = 1; i < points.size() - 1; i++) {
            tempAngle = getAngle(points.get(i - 1), points.get(i), points.get(i + 1));
            if (90 + angleThreshold >= tempAngle && tempAngle >= 90 - angleThreshold) {
                // 직각에 가까운 경우
                vec = new float[]{(points.get(i).x - points.get(i - 1).x), (points.get(i).y - points.get(i - 1).y)};
                if (getAngle(xAxis, vec) < angleThreshold) {
                    points.get(i).y = points.get(i - 1).y;
                } else if (getAngle(yAxis, vec) < angleThreshold) {
                    points.get(i).x = points.get(i - 1).x;
                }
            } else if (tempAngle <= angleThreshold) {
                // 직선에 가까운 정도
                points.set(i, points.get(i - 1));
                continue;
            }
            result.add(points.get(i));
        }
        Point temp = new Point(points.get(0).x, points.get(0).y, true, points.get(0).color);
        result.add(temp);
        return result;
    }

    private static int getAngle(Point a, Point b, Point c) {
        float[] v1 = {(a.x - b.x), (a.y - b.y)};
        float[] v2 = {(c.x - b.x), (c.y - b.y)};
        float radian = (float) Math.acos(dot(v1, v2) / (vecSize(v1) * vecSize(v2)));
        int angle = Math.abs((int) radian2Angle(radian));

        if (angle > 180) return angle - 180;
        return angle;
    }

    private static float getAngle(float[] v1, float[] v2) {
        float radian = (float) Math.acos(dot(v1, v2) / (vecSize(v1) * vecSize(v2)));
        int angle = Math.abs((int) radian2Angle(radian));

        if (angle > 180) return angle - 180;
        return angle;
    }

    private static float dot(float[] v1, float[] v2) {
        return (v1[0] * v2[0] + v1[1] * v2[1]);
    }

    private static float vecSize(float[] vec) {
        return (float) Math.sqrt(vec[0] * vec[0] + vec[1] * vec[1]);
    }

    private static float getRadian(float degree) {
        return (float) Math.PI * (degree / 180);
    }

    private static float radian2Angle(float radian) {
        return radian * 180 / (float) Math.PI;
    }

    private static Point getSusun(Point a, Point b, Point c) {

        double xx = 0.0;
        double yy = 0.0;


        // Ax와 Bx 가 같을때(y에 평행)
        if (a.x == b.x) {
            xx = a.x;
            yy = c.y;
        }

        // Ay와 By 가 같을때(x에 평행)

        else if (a.y == b.y) {
            xx = c.x;
            yy = a.y;
        } else {
            // 직선의 방정식(1) y= m1x + k1
            // 점A와 B를 지나는 직선의 기울기
            double m1 = (b.y - a.y) / (b.x - a.x);
            double k1 = -m1 * a.x + a.y;
            // (1)과 직교하고 화면중심을 지나는 직선의 방정식 y= m2x + k2
            // 직교하기 때문에 기울기의 곱은 -1 이됨 m1 * m2 = -1
            double m2 = -1 / m1;
            double k2 = -m2 * c.x + c.y;
            // 두 직선의 교점을 찾는다 m1x + k1 = m2x + k2
            xx = (k2 - k1) / (m1 - m2);
            yy = m1 * xx + k1;
        }
        return new Point((float) xx, (float) yy, b.check, b.color);
    }
}