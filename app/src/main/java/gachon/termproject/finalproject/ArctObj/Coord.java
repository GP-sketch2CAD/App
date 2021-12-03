package gachon.termproject.finalproject.ArctObj;

public class Coord {
    public boolean isLinking;
    private int x;
    private int y;

    private Coord linkingCoord;
    public int dx;
    public int dy;

    public Coord(int x, int y){
        this.isLinking = false;
        this.x = x;
        this.y = y;
    }

    public Coord(Coord linkingCoord, int dx, int dy){
        this.isLinking = true;
        this.linkingCoord = linkingCoord;
        this.dx = dx;
        this.dy = dy;
    }

    public void setCoord(Coord linkingCoord, int dx, int dy){
        this.isLinking = true;
        this.linkingCoord = linkingCoord;
        this.dx = dx;
        this.dy = dy;
    }

    public int getX(){
        if(isLinking) return linkingCoord.getX() + dx;
        else return x;
    }

    public int getY(){
        if(isLinking) return linkingCoord.getY() + dy;
        else return y;
    }


}
