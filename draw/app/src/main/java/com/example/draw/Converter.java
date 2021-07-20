package com.example.draw;

import android.view.View;

import com.example.draw.Algorithms.Line2Straight;
import com.example.draw.Aobject.XY;
import com.example.draw.Stack.StackData;
import com.example.draw.Stack.StackManager;

import java.util.ArrayList;
import java.util.List;

public class Converter {
    private StackManager stack;
    double epsilon = 250.;
    View view;

    Converter(StackManager stack, View view) {
        // 쓰레드로 개발하자 --> 일단은 버튼형식으로 개발하고 나중에 변경하기!
        // 3초에 한번씩 스택의 변경사항이 있는지 확인하기
        // stackManager에 변경사항이 있는지 알 수 있게 변경하자 --> enum?
        // stackData는 타입이 뭔지 알 수 있게 개발해야할 듯

        // 검사알고리즘
        // 1. 스택 매니저에 추가된? 있는지 확인하기()
        // 2. 변경된 사항이 있다면 변경된 것 확인하기

        // 확인 순서
        // 1. 숫자인지 아닌지 (사이즈도 고려해서 생각하자)
        // 2. 숫자가 아니라면 line2straight부터 하기 --> 곡선은 어떻게 할건지? (문, 곡선)
        //
        this.view = view;
        setStack(stack);
    }

    public void setStack(StackManager stack) {
        this.stack = stack;
    }

    public void convert() {
        List<XY> resultList;

        for (StackData data : stack.stack) {
           resultList = Line2Straight.douglasPeucker(data.getXyList(),epsilon);
           data.setXyList((ArrayList<XY>) resultList);
        }
        view.invalidate();
    }

    public void setEpsilon(int percent){
        this.epsilon = 10. * percent;
    }
}
