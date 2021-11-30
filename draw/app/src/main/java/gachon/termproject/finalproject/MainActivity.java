package gachon.termproject.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.util.Log;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    class Point{
        float x;
        float y;
        boolean check;
        int color;

        public Point(float x, float y, boolean check,int color)
        {
            this.x = x;
            this.y = y;
            this.check = check;
            this.color = color;
        }
    }

    class MyView extends View
    {
        public MyView(Context context) { super(context); }

        @Override
        protected void onDraw(Canvas canvas) {
            Paint p = new Paint();
            p.setStrokeWidth(10);
            Log.w("2", "2");
            Log.e("p1", String.valueOf(points));
            for(int i=1 ; i<points.size() ; i++)
            {
                if(!points.get(i).check)
                    continue;
                canvas.drawLine(points.get(i-1).x,points.get(i-1).y,points.get(i).x,points.get(i).y,p);
                Log.e("p2", String.valueOf(points));
            }
        }
        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float x = event.getX();
            float y = event.getY();
            Log.e("p3", String.valueOf(points));

            switch (event.getAction())
            {
                case MotionEvent.ACTION_DOWN:
                    points.add(new Point(x,y,false , color));
                case MotionEvent.ACTION_MOVE :
                    points.add(new Point(x,y,true , color));
                    break;
                case MotionEvent.ACTION_UP :
                    master.add((ArrayList<Point>)points.clone());
                    break;
            }
            invalidate();
            return true;
        }
    }

    ArrayList<Point> points = new ArrayList<Point>();
    ArrayList<Point> save_points = new ArrayList<Point>();
    ArrayList<ArrayList<Point>> master = new ArrayList(); //undo용
    Button clearbtn;
    LinearLayout drawlinear;
    int color = Color.BLACK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final MyView m = new MyView(this);

        findViewById(R.id.llog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("points", String.valueOf(points));
                Log.e("save_points",String.valueOf(save_points));
                Log.e("master", master.size()+ "");
                for(int i = 0; i<master.size();i++){
                    Log.w("rkqt" + i, String.valueOf(master.get(i)));
                }
            }
        });


        clearbtn = findViewById(R.id.clear_btn);
        drawlinear = findViewById(R.id.draw_linear);
        clearbtn.setOnClickListener(new View.OnClickListener() { //지우기 버튼 눌렸을때
            @Override
            public void onClick(View v){
                if(master.size() == 0) return;
                if(master.size() == 1) {
                    points.clear();
                }
                points = master.get(master.size()-2);
                master.remove(master.size()-1);
                m.invalidate();
            }
        });
        drawlinear.addView(m);
    }
}