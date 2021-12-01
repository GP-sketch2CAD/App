package gachon.termproject.finalproject.stack;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Converter {
    double epsilon = 50.;

    Converter() {

    }

    public Object convertPoints2Obj(ArrayList<Point> points) {
        ArrayList<Point> temp = null;

        if (isNumber(points)) {

        } else if (isDoor(points)) {
            // 나중에 모델 만들어야할듯
            // 더글라스파커랑 상극이야
        } else {
            // 변환 한번 해주자
            temp = (ArrayList<Point>) Line2Straight.douglasPeucker((List<Point>) points, epsilon);
            temp = Line2Nemo.nemoNemo(temp);

            if (isWindow(points)) {

            } else if (isColumn(points)) {

            } else if (isWall(points)) {

            }
        }

        return temp;
    }

    private boolean isDoor(ArrayList<Point> points) {
        return false;
    }

    private boolean isWindow(ArrayList<Point> points) {
        return false;
    }

    private boolean isColumn(ArrayList<Point> points) {
        return false;
    }

    private boolean isWall(ArrayList<Point> points) {
        return false;
    }

    private boolean isNumber(ArrayList<Point> points) {

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