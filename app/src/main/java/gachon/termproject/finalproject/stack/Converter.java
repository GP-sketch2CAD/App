package gachon.termproject.finalproject.stack;

import java.util.ArrayList;
import java.util.List;

public class Converter {
    double epsilon = 50.;
    Converter() {

    }

    public Object convertPoints2Obj(ArrayList<Point> points) {
        ArrayList<Point> temp = null;

        if (isNumber(points)) {

        } else {
            // 변환 한번 해주자
             temp = (ArrayList<Point>) Line2Straight.douglasPeucker((List<Point>) points,epsilon);

            if (isDoor(points)) {

            } else if (isWindow(points)) {

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

        return resultList;
    }


}
