package com.example.draw;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.os.Bundle;


import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.draw.Aobject.XY;
import com.example.draw.Stack.StackManager;
import com.example.draw.Stack.StackData;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    Button penBtn, eraseBtn, undoBtn, redoBtn, convertBtn;
    LinearLayout container;
    StackManager stack;
    Converter converter;
    SeekBar seekBar;
    boolean stackChanged = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DrawView mv = new DrawView(getApplicationContext());


        stack = new StackManager();
        converter = new Converter(stack, mv);

//        penBtn = findViewById(R.id.btn_pen);
//        eraseBtn = findViewById(R.id.btn_erase);
        undoBtn = findViewById(R.id.btn_undo);
        redoBtn = findViewById(R.id.btn_redo);
        convertBtn = findViewById(R.id.btn_convert);
        container = findViewById(R.id.drawContainer);
        container.addView(mv);
        seekBar = findViewById(R.id.seekBar);


        undoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stack.undo();
                mv.invalidate();
            }
        });

        redoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stack.redo();
                mv.invalidate();
            }
        });

        convertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                converter.convert();
            }
        });

//        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                Log.d("seekbar", "seekBar: " + progress);
//                converter.setEpsilon(progress);
//                Toast.makeText(getApplicationContext(),"" + progress * 10,Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//
//            }
//        });


    }


    public class DrawView extends View {

        private Paint paint = new Paint();
        int x, y;
        StackData data;
        //여러가지의 그리기 명령을 모았다가 한번에 출력해주는
        //버퍼역할을 담당한다..


        public DrawView(Context context) {
            super(context);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            Path path = new Path();
            canvas.drawColor(Color.WHITE);

            paint.setColor(Color.BLACK);
            //STROKE속성을 이용하여 테두리...선...
            paint.setStyle(Paint.Style.STROKE);
            //두께
            paint.setStrokeWidth(3);


            if (stack.size() > 0) {
                ArrayList<StackData> sdList = stack.toArray();
                for (StackData sd : sdList) {
                    ArrayList<XY> xyList = sd.getXyList();
                    path.moveTo(xyList.get(0).getX(), xyList.get(0).getY());
                    for (int i = 1; i < xyList.size(); i++) {
                        path.lineTo(xyList.get(i).getX(), xyList.get(i).getY());
                    }
                }
            }

            //path객체가 가지고 있는 경로를 화면에 그린다...
            canvas.drawPath(path, paint);

        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            x = (int) event.getX();
            y = (int) event.getY();

            switch (event.getAction()) {
                // 처음 눌렀을 때
                case MotionEvent.ACTION_DOWN:
                    data = new StackData(System.currentTimeMillis());
                    stack.push(data);
                    data.addXY(x, y);
                    break;
                // 획을 움직이고 있을 때
                case MotionEvent.ACTION_MOVE:
                    data.addXY(x, y);
                    break;
                // 획이 끝났을 때
                case MotionEvent.ACTION_UP:
                    data.setLastTime(System.currentTimeMillis());
                    ArrayList list = (ArrayList) data.getXyList();


                    // 나중에 지우자
                    for (int i = 0; i < list.size(); i++) {
                        Log.i("draw", list.get(i).toString());
                    }
                    Log.i("draw", "start: " + data.getStartTime());
                    Log.i("draw", "last: " + data.getLastTime());
                    break;
            }
            invalidate();
            //View의 onDraw()를 호출하는 메소드...


            return true;
        }
    }
}