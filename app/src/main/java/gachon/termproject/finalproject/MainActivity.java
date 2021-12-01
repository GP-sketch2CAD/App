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
    Button undobtn;
    LinearLayout drawlinear;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final MyView myView = new MyView(this, stackManager);
        stackManager.setView(myView);

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


        undobtn = findViewById(R.id.undo_btn);
        drawlinear = findViewById(R.id.draw_linear);
        undobtn.setOnClickListener(new View.OnClickListener() { //undo 버튼 눌렸을때
            @Override
            public void onClick(View v) {
                stackManager.pop();
                myView.invalidate();
            }
        });
        drawlinear.addView(myView);
    }
}

