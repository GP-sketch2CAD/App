package com.example.draw.Stack;

import android.util.Log;

import java.util.ArrayList;
import java.util.Stack;

public class StackManager {
    Stack<StackData> tempStack;
    public Stack<StackData> stack;

    public StackManager() {
        super();
        stack = new Stack<>();
        tempStack = new Stack<>();
    }

    // push
    public StackData push(StackData item) {
        stack.push(item);
        if (tempStack.size() > 0) resetRedo();
        return item;
    }

    // pop
    public StackData pop() {
        if (stack.size() > 0) return stack.pop();
        return null;
    }

    // undo
    // pop 해서 임시스택에 push
    // 새로운 정보(푸쉬되었을 때) 임시스택 release
    public boolean undo() {
        if (isUndoPossible() == false) return false;

        tempStack.push(stack.pop());
        return true;
    }

    // redo
    // 임시스택 pop -> 메인스택 push
    public boolean redo() {
        if (isRedoPossible() == false) return false;

        stack.push(tempStack.pop());
        return true;
    }

    // erase
    // 위치로 검색 --> 해당 데이터 삭제

    // reset temp Stack
    private int resetRedo() {
        tempStack.removeAllElements();
        return 0;
    }

    public boolean isUndoPossible() {
        if (stack.size() > 0) return true;
        else return false;
    }

    public boolean isRedoPossible() {
        if (tempStack.size() > 0) return true;
        else return false;
    }

    public ArrayList<StackData> toArray() {
        ArrayList<StackData> ar = new ArrayList<>();
        for (int i = 0; i < stack.size(); i++) {
            ar.add(stack.get(i));
        }
        return ar;
    }

    public int size() {
        return stack.size();
    }


}
