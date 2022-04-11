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
    //캔버스 위에 그린 그림을 object로 변환
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

                        //기존 방의 왼선과 그린 그림의 위, 아래선이 만나면 왼쪽 창문
                    if (MacGyver.isCross(xSun1, left) && MacGyver.isCross(xSun2, left)) {
                        obj = new NemoWindow(border, nr, 0);
                        break;
                        //기존 방의 오른선과 그린 그림의 위, 아래선이 만나면 오른쪽 창문
                    } else if (MacGyver.isCross(xSun1, right) && MacGyver.isCross(xSun2, right)) {
                        obj = new NemoWindow(border, nr, 2);
                        break;
                        //기존 방의 윗선과 그린 그림의 왼, 오른선이 만나면 위쪽 창문
                    } else if (MacGyver.isCross(ySun1, bot) && MacGyver.isCross(ySun2, bot)) {
                        obj = new NemoWindow(border, nr, 1);
                        break;
                        //기존 방의 아랫선과 그린 그림의 왼, 오른선이 만나면 아래쪽 창문
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

        } else {
            // nemo일 때 보강을 좀 해야할 듯 (어떻게 하면 네모 형태로 그려졌을 때만 방이 만들어질까)
            if (((ArrayList<Point>) Line2Straight.douglasPeucker((List<Point>) points, epsilon)).size() >= 4 &&
                    ((ArrayList<Point>) Line2Straight.douglasPeucker((List<Point>) points, epsilon)).size() < 7) {
                int LCidx;
                Point[] border = MacGyver.getBorder(points);
                int g = (int) ((border[0].x) / StackManager.pointDivideMm);
                int s = (int) ((border[0].y) / StackManager.pointDivideMm);
                Coord linkC = new Coord(StackManager.initialCord, g, s);

                LCidx = MacGyver.getShortestRoomCord(stackManager, border, linkC);
                obj = new NemoRoom(border, linkC, LCidx);
            } else
                obj = null;
        }
        return obj;
    }

    //기존 object에 방이 있어야 하며 그림이 더귤라스 파커에 의해 점 3개가 나오면 문으로 인식
    private boolean isDoor(ArrayList<Point> points, StackManager sm) {
        if (((ArrayList<Point>) Line2Straight.douglasPeucker((List<Point>) points, epsilon)).size() == 3
                && !sm.objStack.isEmpty()) {
            Point[] door = MacGyver.getTriangle(points, sm);
            if (door[0] != null && door[1] != null && door[2] != null) {
                return true;
            }
        }
        return false;
    }
    // 그림의 border를 가져와 기존 방의 벽과 비교하여 벽의 양옆으로 두 개의 점씩 있으면 창문으로 인식.
    private boolean isWindow(ArrayList<Point> points, StackManager sm) {
        int LB = 0, LT = 1, RT = 2;
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
                // 문 그릴 때 벽을 벽 통과해서 그리면 문이 창문으로 인식되는 경우가 있어서 우선 점 4개 이상이면 적어도 사각형을 나타내는 거라서 추가 조건 넣음.
                // 위에 nemo보강하면 여기도 같이 바꿔줘야 할듯
                if (count == 2 && ((ArrayList<Point>) Line2Straight.douglasPeucker((List<Point>) points, epsilon)).size() >3) return true;
            }
        }
        return false;
    }
    // 그림의 border 내부에 3개 이상의 대각선이 그려지면 기둥으로 인식
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

    /* //isWall의 역할이 없음!!!!!!
    private boolean isWall(ArrayList<Point> points) {
        // TODO: 일단 넓이로 좀 해볼까.....
        return true;
    }
    */

    //border의 x,y좌표가 기존 방의 x, y좌표 보다 작으면 안에있는 사각형, 반대면 큰 사각형
    private boolean isOverlap(ArrayList<Point> points, StackManager stackManager) {
        Point[] border = MacGyver.getBorder(points);
        int LB = 0, LT = 1, RT = 2;
        int ccount = 0;
        Point[] room = new Point[4];
        if (stackManager.objStack.size() == 0) return false;

        for (Object obj : stackManager.objStack) {
            if (obj instanceof NemoRoom) {
                int count = 0;
                int i = 0;
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
                }
            }
        }
        if (ccount == stackManager.objStack.size()) {
            return false;
        }
        return false;
    }
}
// 그림의 좌표를 얻기 위해 더귤라스 파커 알고리즘을 이용.
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
