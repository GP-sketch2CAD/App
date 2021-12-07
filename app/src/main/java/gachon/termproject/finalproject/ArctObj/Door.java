package gachon.termproject.finalproject.ArctObj;
import gachon.termproject.finalproject.stack.Point;

public class Door {
    public Coord[] coords = new Coord[3];
    public Door (Point[] triangle){
        setCoords(triangle);
    }

    private void setCoords(Point[] triangle){
        coords[0] = new Coord((int)triangle[0].x, (int)triangle[0].y);
        coords[1] = new Coord((int)triangle[1].x, (int)triangle[1].y);
        coords[2] = new Coord((int)triangle[2].x, (int)triangle[2].y);
    }
}
