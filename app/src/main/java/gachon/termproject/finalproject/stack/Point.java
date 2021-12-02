package gachon.termproject.finalproject.stack;

public class Point implements Cloneable{
    public float x;
    public float y;
    public boolean check;
    public int color;

    public Point(float x, float y, boolean check,int color)
    {
        this.x = x;
        this.y = y;
        this.check = check;
        this.color = color;
    }

    @Override
    public Point clone(){
        try {
            return (Point)super.clone();
        } catch (Exception e){
          return null;
        }

    }
    public float getPointX()
    {
        return this.x;
    }
}
