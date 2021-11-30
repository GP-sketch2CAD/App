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

import gachon.termproject.finalproject.stack.Point;
import gachon.termproject.finalproject.stack.StackManager;

public class MainActivity extends AppCompatActivity {

    StackManager stackManager = new StackManager();
    ArrayList<Point> points = new ArrayList<Point>();
    Button clearbtn;
    LinearLayout drawlinear;
    int color = Color.BLACK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final MyView myView = new MyView(this);

//        findViewById(R.id.llog).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.e("points", String.valueOf(points));
//                Log.e("save_points", String.valueOf(save_points));
//                Log.e("master", master.size() + "");
//                for (int i = 0; i < master.size(); i++) {
//                    Log.w("rkqt" + i, String.valueOf(master.get(i)));
//                }
//            }
//        });


        clearbtn = findViewById(R.id.clear_btn);
        drawlinear = findViewById(R.id.draw_linear);
        clearbtn.setOnClickListener(new View.OnClickListener() { //undo 버튼 눌렸을때
            @Override
            public void onClick(View v) {
                stackManager.pop();
                myView.invalidate();
            }
        });
        drawlinear.addView(myView);
    }

    class MyView extends View {
        long startTime;
        long endTime;

        public MyView(Context context) {
            super(context);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            Paint p = new Paint();
            p.setStrokeWidth(10);
            Log.w("2", "2");

            ArrayList<Point> toDraw = stackManager.getAllPoints();
            for (int i = 1; i < toDraw.size(); i++) {
                if (!toDraw.get(i).check)
                    continue;
                canvas.drawLine(toDraw.get(i - 1).x, toDraw.get(i - 1).y, toDraw.get(i).x, toDraw.get(i).y, p);

            }
            for (int i = 1; i < points.size(); i++) {
                if (!points.get(i).check)
                    continue;
                canvas.drawLine(points.get(i - 1).x, points.get(i - 1).y, points.get(i).x, points.get(i).y, p);

            }
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float x = event.getX();
            float y = event.getY();
            Log.e("p3", String.valueOf(points));

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    startTime = System.currentTimeMillis();
                    points.add(new Point(x, y, false, color));
                case MotionEvent.ACTION_MOVE:
                    points.add(new Point(x, y, true, color));
                    break;
                case MotionEvent.ACTION_UP:
                    endTime = System.currentTimeMillis();
                    stackManager.push((ArrayList<Point>) points.clone(), startTime, endTime);
                    points.clear();
                    break;
            }
            invalidate();
            return true;
        }
    }
}