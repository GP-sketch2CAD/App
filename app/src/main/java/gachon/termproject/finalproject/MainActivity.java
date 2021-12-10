package gachon.termproject.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import gachon.termproject.finalproject.stack.StackManager;

public class MainActivity extends AppCompatActivity {

    StackManager stackManager = new StackManager();
    Button undoBtn, exportBtn;
    LinearLayout drawlinear;
    Exporter exporter = new Exporter(stackManager);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final MyView myView = new MyView(this, stackManager);
        stackManager.setView(myView);

        exportBtn = findViewById(R.id.export);
        exportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exporter.obj2json();
                exporter.connect2Server(getFilesDir());
            }
        });


        undoBtn = findViewById(R.id.undo_btn);
        drawlinear = findViewById(R.id.draw_linear);
        undoBtn.setOnClickListener(new View.OnClickListener() { //undo 버튼 눌렸을때
            @Override
            public void onClick(View v) {
                stackManager.pop();
                myView.invalidate();
            }
        });
        drawlinear.addView(myView);
    }
}

