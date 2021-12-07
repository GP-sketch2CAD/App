package gachon.termproject.finalproject.stack;

import java.util.ArrayList;

public class Digit {

    public int result;
    public Point[] points;
    public boolean check;
    Digit(){this.check=false; }

    Digit(Point[] points, int result){
        this.points = points;
        this.result=result;
        this.check=true;
    }
}
