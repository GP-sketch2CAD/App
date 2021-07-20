package com.example.draw.Algorithms;

import com.example.draw.Aobject.XY;

import java.util.ArrayList;
import java.util.List;

public class Line2Straight {

    public Line2Straight() {
    }

    private static final double sqr(double x) {
        return Math.pow(x, 2);
    }

    private static final double distanceBetweenPoints(int vx, int vy, int wx, int wy) {
        return sqr(vx - wx) + sqr(vy - wy);
    }

    private static final double distanceToSegmentSquared(int px, int py, int vx, int vy, int wx, int wy) {
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

    private static final double perpendicularDistance(XY start, XY end, XY point) {
        return Math.sqrt(distanceToSegmentSquared(point.getX(), point.getY(), start.getX(), start.getY(), end.getX(), end.getX()));
    }

    public static final List<XY> douglasPeucker(List<XY> list, double epsilon) {
        // Find the point with the maximum distance
        double dMax, d;
        int index,start,end;
        XY startXY, endXY, pointXY;
        List<XY> resultList = new ArrayList<>();

        dMax = 0;
        index = 0;
        start = 0;
        end = list.size()-1;

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
            List<XY> result1 = douglasPeucker(list.subList(0,index+1), epsilon);
            List<XY> result2 = douglasPeucker(list.subList(index,end+1), epsilon);

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

