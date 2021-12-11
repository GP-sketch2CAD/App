package gachon.termproject.finalproject.stack;

import java.util.ArrayList;

import gachon.termproject.finalproject.ArctObj.Coord;
import gachon.termproject.finalproject.ArctObj.NemoColumn;
import gachon.termproject.finalproject.ArctObj.NemoRoom;
import gachon.termproject.finalproject.ArctObj.NemoWindow;

public class Digit {

    public int result;
    public Point[] points;
    public boolean check;
    public boolean isset;

    Digit() {
        this.check = false;
    }

    Digit(Point[] points, int result) {
        this.points = points;
        this.result = result;
        this.check = true;
        this.isset=false;
    }

    public void digitinput(StackManager stackManager) {
        NemoRoom R=searchNemoroomlength(stackManager);
        if(R==null && !isset)
        {
            NemoColumn C=searchNemocolumnlength(stackManager);
            if(C==null&& !isset)
            {
                NemoWindow W=searchNemoWindowlength(stackManager);
            }
        }
    }

    public Coord cen(Coord A, Coord B) {
        Coord P =new Coord((A.getPointX()+B.getPointX())/2,(A.getPointY()+B.getPointY())/2);

        return P;
    }

    void setNemoroomlength(NemoRoom a ,boolean isg)
    {
        if (!isg) {
            a.setSero(result);
            a.isSeroSet = true;
            isset = true;

            points[0].y = (a.coords[0].getPointY());
            points[1].y = (a.coords[1].getPointY());
            points[2].y = (a.coords[1].getPointY());
            points[3].y = (a.coords[0].getPointY());

        } else {
            a.setGaro(result);
            a.isGaroSet = true;
            isset = true;


            points[0].x = (a.coords[0].getPointX())-50;
            points[1].x = (a.coords[0].getPointX())-50;
            points[2].x = (a.coords[2].getPointX())-50;
            points[3].x = (a.coords[2].getPointX())-50;
        }
    }

    void setNemocolumnlength(NemoColumn a ,boolean isg)
    {
        if (!isg) {
            a.setSero(result);
            a.isSeroSet = true;
            isset = true;

            points[0].y = (a.coords[0].getPointY());
            points[1].y = (a.coords[1].getPointY());
            points[2].y = (a.coords[1].getPointY());
            points[3].y = (a.coords[0].getPointY());

        } else {
            a.setGaro(result);
            a.isGaroSet = true;
            isset = true;


            points[0].x = (a.coords[0].getPointX()) - 50;
            points[1].x = (a.coords[0].getPointX()) - 50;
            points[2].x = (a.coords[2].getPointX()) - 50;
            points[3].x = (a.coords[2].getPointX()) - 50;
        }
    }
    void setNemowindowlength(NemoWindow a ,boolean isg)
    {
        if (a.degree==90) {
            a.setDis(result);
            a.isDisSet = true;
            isset = true;

            points[0].y = (a.coords[0].getPointY());
            points[1].y = (a.coords[1].getPointY());
            points[2].y = (a.coords[1].getPointY());
            points[3].y = (a.coords[0].getPointY());

        } else if(a.degree==0){
            a.setDis(result);
            a.isDisSet = true;
            isset = true;


            points[0].x = (a.coords[0].getPointX()) - 50;
            points[1].x = (a.coords[0].getPointX()) - 50;
            points[2].x = (a.coords[2].getPointX()) - 50;
            points[3].x = (a.coords[2].getPointX()) - 50;
        }
    }

    NemoRoom searchNemoroomlength(StackManager stackManager)
    {
        Point cc=new Point((points[0].x + points[2].x) / 2,(points[0].y + points[1].y) / 2);
        float minDistance=-1,preD, dis;
        boolean isg=false;
        NemoRoom a=null;

        for (Object obj : stackManager.objStack) {
            if (obj instanceof NemoRoom) {
                if (!(((NemoRoom) obj).isGaroSet && ((NemoRoom) obj).isSeroSet)) {
                    Coord[] center=new Coord[4];
                    for(int i=0;i<4;i++) {
                        center[i] = cen(((NemoRoom) obj).coords[i], ((NemoRoom) obj).coords[(i + 1) % 4]);

                        dis = MacGyver.getDistance(center[i], cc);
                        if ((minDistance < 0 || minDistance > dis)) {
                            preD=minDistance;
                            minDistance = dis;
                            if(i==0|| i==2)
                            {
                                if(((NemoRoom) obj).isSeroSet){
                                    minDistance=preD;
                                    continue;
                                }
                                isg=false;
                            }
                            else
                            {
                                if(((NemoRoom) obj).isGaroSet){
                                    minDistance=preD;
                                    continue;
                                }
                                isg=true;
                            }
                            a=(NemoRoom)obj;


                        }
                    }


                }
            }
        }
        if(a!=null) {
            setNemoroomlength(a,isg);
        }
        return a;
    }

    NemoColumn searchNemocolumnlength(StackManager stackManager)
    {
        Point cc=new Point((points[0].x + points[2].x) / 2,(points[0].y + points[1].y) / 2);
        float minDistance=-1,preD, dis;
        boolean isg=false;
        NemoColumn a=null;

        for (Object obj : stackManager.objStack) {
            if (obj instanceof NemoColumn) {
                if (!(((NemoColumn) obj).isGaroSet && ((NemoColumn) obj).isSeroSet)) {
                    Coord[] center=new Coord[4];
                    for(int i=0;i<4;i++) {
                        center[i] = cen(((NemoColumn) obj).coords[i], ((NemoColumn) obj).coords[(i + 1) % 4]);

                        dis = MacGyver.getDistance(center[i], cc);
                        if ((minDistance < 0 || minDistance > dis)) {
                            preD=minDistance;
                            minDistance = dis;
                            if(i==0|| i==2)
                            {
                                if(((NemoColumn) obj).isSeroSet){
                                    minDistance=preD;
                                    continue;
                                }
                                isg=false;
                            }
                            else
                            {
                                if(((NemoColumn) obj).isGaroSet){
                                    minDistance=preD;
                                    continue;
                                }
                                isg=true;
                            }
                            a=(NemoColumn)obj;


                        }
                    }


                }
            }
        }
        if(a!=null) {
            setNemocolumnlength(a,isg);
        }
        return a;
    }

    NemoWindow searchNemoWindowlength(StackManager stackManager)
    {
        Point cc=new Point((points[0].x + points[2].x) / 2,(points[0].y + points[1].y) / 2);
        float minDistance=-1,preD, dis;
        boolean isg=false;
        NemoWindow a=null;

        for (Object obj : stackManager.objStack) {
            if (obj instanceof NemoWindow) {
                if (!(((NemoWindow) obj).isDisSet)) {
                    Coord[] center=new Coord[4];
                    for(int i=0;i<4;i++) {
                        center[i] = cen(((NemoWindow) obj).coords[i], ((NemoWindow) obj).coords[(i + 1) % 4]);

                        dis = MacGyver.getDistance(center[i], cc);
                        if ((minDistance < 0 || minDistance > dis)) {
                            preD=minDistance;
                            minDistance = dis;

                            a=(NemoWindow)obj;


                        }
                    }


                }
            }
        }
        if(a!=null) {
            setNemowindowlength(a,isg);
        }
        return a;
    }

}


