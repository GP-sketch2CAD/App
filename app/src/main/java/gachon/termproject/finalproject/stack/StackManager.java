package gachon.termproject.finalproject.stack;

import android.os.Handler;
import android.util.Log;

import java.util.ArrayDeque;
import java.util.ArrayList;

import gachon.termproject.finalproject.ArctObj.Coord;
import gachon.termproject.finalproject.ArctObj.Door;
import gachon.termproject.finalproject.ArctObj.NemoColumn;
import gachon.termproject.finalproject.ArctObj.NemoRoom;
import gachon.termproject.finalproject.ArctObj.NemoWindow;
import gachon.termproject.finalproject.MyView;

public class StackManager {

    private long timeThreshold = 750; // 일단 1초로 잡고 나중에 수정
    public static double pointDivideMm = 0.1;
    public static Coord initialCord = new Coord(0, 0);

    Converter converter = new Converter();
    ArrayDeque<DrawElement> drawStack;
    ArrayDeque<Object> objStack;
    ArrayList<Point[]> bt = new ArrayList<Point[]>();
    boolean isruq;
    MyView myView;

    public StackManager() {
        this.drawStack = new ArrayDeque<DrawElement>();
        this.objStack = new ArrayDeque<Object>();
    }

    public void setView(MyView myView) {
        this.myView = myView;
    }

    public void push(ArrayList<Point> points, long startT, long endT) {
        DrawElement newElement = new DrawElement(points, startT, endT);

        if (drawStack.size() == 0) {
            bt.clear();
            drawStack.push(newElement);
            Point[] around = MacGyver.getBorder(newElement.points);
            bt.add(around);
        } else {
            DrawElement lastElement = drawStack.pop();

            if (newElement.startTime - lastElement.endTime < timeThreshold) {
                isruq = false;

                for (Point p : lastElement.points) {
                    for (Point p2 : newElement.points) {
                        if ((((int) (p.x) == (int) (p2.x)) && ((int) (p.y) == (int) (p2.y)))) {
                            isruq = true;
                        }
                    }
                    System.out.println(isruq);
                }
                if (!(isruq)) {
                    Log.e("resultText", "1111111111");
                    Point[] around = MacGyver.getBorder(newElement.points);
                    bt.add(around);
                    lastElement.addPoints(points);
                    lastElement.endTime = newElement.endTime;
                    drawStack.push(lastElement);
                } else {
                    bt.remove(bt.size() - 1);
                    lastElement.addPoints(points);
                    lastElement.endTime = newElement.endTime;
                    drawStack.push(lastElement);
                    Point[] around = MacGyver.getBorder(lastElement.points);
                    bt.add(around);
                }
//


            } else {
                drawStack.push(lastElement);
                drawStack.push(newElement);
            }

        }

        // 1.5초 뒤 변환
        callConvertDraw2Obj(1500);


    }

    public Object pop() {
        if (drawStack.size() > 0) return drawStack.pop();
        else if (objStack.size() > 0) return objStack.pop();
        else return null;
    }

    public ArrayList<Point> getDrawPoint() {
        ArrayList<Point> allPoints = new ArrayList<Point>();

        for (DrawElement de : drawStack) {
            allPoints.addAll(de.points);
        }

        return allPoints;
    }

    public ArrayList<Digit> getDigitPoint() {
        ArrayList<Digit> result = new ArrayList<>();

        for (Object obj : objStack) {
            if (obj instanceof Digit) {

                result.add((Digit) obj);
            }
        }
        return result;
    }

    public ArrayList<float[]> getWallPoint() {
        ArrayList<float[]> result = new ArrayList<>();
        NemoRoom room;
        for (Object obj : objStack) {
            if (obj instanceof NemoRoom) {
                room = (NemoRoom) obj;
                float outMaxX, outMaxY, outMinX, outMinY, inMaxX, inMaxY, inMinX, inMinY;
                outMinX = (float) (room.coords[0].getPointX());
                outMinY = (float) (room.coords[0].getPointY());
                outMaxX = (float) (room.coords[2].getPointX());
                outMaxY = (float) (room.coords[2].getPointY());
                inMinX = (float) (room.innerCords[0].getPointX());
                inMinY = (float) (room.innerCords[0].getPointY());
                inMaxX = (float) (room.innerCords[2].getPointX());
                inMaxY = (float) (room.innerCords[2].getPointY());

                result.add(new float[]{outMinX, outMinY, outMaxX, inMinY});
                result.add(new float[]{outMinX, outMinY, inMinX, inMaxY});
                result.add(new float[]{outMinX, inMaxY, outMaxX, outMaxY});
                result.add(new float[]{inMaxX, outMinY, outMaxX, outMaxY});
            }
        }

        return result;
    }

    public ArrayList<int[]> getColumnsPoint() {
        ArrayList<int[]> result = new ArrayList<>();

        for (Object obj : objStack) {
            if (obj instanceof NemoColumn) {
                int[] temp = new int[]{((NemoColumn) obj).coords[0].getPointX(),
                        ((NemoColumn) obj).coords[0].getPointY(),
                        ((NemoColumn) obj).coords[2].getPointX(),
                        ((NemoColumn) obj).coords[2].getPointY()};
                result.add(temp);
            }
        }
        return result;
    }

    public ArrayList<int[]> getWindowPoint() {
        ArrayList<int[]> result = new ArrayList<>();

        for (Object obj : objStack) {
            if (obj instanceof NemoWindow) {
                int[] temp = new int[]{((NemoWindow) obj).coords[0].getPointX(),
                        ((NemoWindow) obj).coords[0].getPointY(),
                        ((NemoWindow) obj).coords[2].getPointX(),
                        ((NemoWindow) obj).coords[2].getPointY()};
                result.add(temp);
            }
        }
        return result;
    }

    public ArrayList<int[]> getDoorPoint() {
        ArrayList<int[]> result = new ArrayList<>();

        for (Object obj : objStack) {
            if (obj instanceof Door) {
                int[] temp = new int[]{((Door) obj).coords[0].getPointX(),
                        ((Door) obj).coords[0].getPointY(),
                        ((Door) obj).coords[2].getPointX(),
                        ((Door) obj).coords[2].getPointY()};
                result.add(temp);


                temp = new int[]{((Door) obj).doorCoords[0].getPointX(),
                        ((Door) obj).doorCoords[0].getPointY(),
                        ((Door) obj).doorCoords[2].getPointX(),
                        ((Door) obj).doorCoords[2].getPointY()};
                result.add(temp);
            }
        }
        return result;
    }

    private void callConvertDraw2Obj(long time) {
        // 1.5초 뒤 변환
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                convertDraw2Obj();
            }
        }, time);
    }

    private void convertDraw2Obj() {
        long lt;

        if (drawStack.size() == 0) return;
        for (DrawElement de : drawStack) {
            lt = System.currentTimeMillis() - de.endTime;
            if (lt < 2 * timeThreshold) {
                callConvertDraw2Obj(1000);
                return;
            }
            Point[] size = MacGyver.getBorder(de.points);
            float recSize = (size[2].x - size[0].x) * (size[1].y - size[0].y);
            System.out.println(recSize);
            if (recSize < 20000) {
                ArrayList<Integer> result = new ArrayList<>();
                int total = 0;
                for (int i = 0; i < bt.size(); i++) {
                    System.out.println(bt.get(i)[0].x);
                    try {
                        int num;
                        num = myView.classifyDrawing(bt.get(i));
                        if (num != -1) {
                            result.add(num);
                            total = (int) (total + result.get(i) * Math.pow(10, bt.size() - 1 - i));
                        } else {
                            objStack.push(converter.convertPoints2Obj(de.points, this));
                            break;
                        }
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                }
                if (total > 0) {
                    Digit digit = new Digit(MacGyver.getBorder(de.points), total);
                    objStack.push(digit);
                    System.out.println(total);
                }
            } else {
                Object temp = converter.convertPoints2Obj(de.points, this);
                if (temp == null) {
                    drawStack.pollFirst();
                    continue;
                }
                objStack.push(temp);
            }
            drawStack.pollFirst();
        }
        myView.invalidate();
    }


}