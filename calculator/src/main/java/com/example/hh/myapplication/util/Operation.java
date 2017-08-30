package com.example.hh.myapplication.util;

/**
 * Created by lh on 2017/1/17.
 */
public abstract class Operation {

    private Constant.OperationType type;

    public Operation( ){
    }

    public void setType(Constant.OperationType type) {
        this.type = type;
    }

    public  float getResult(float value1, float value2){
        float result = 0.0f;
        switch (type){
            case ADD:
                result = value1 + value2;
                break;
            case SUBTRACT:
                result = value1 - value2;
                break;
            case MULTIPLY:
                result = value1 * value2;
                break;
            case DIVIDE:
                result = value1/value2;
        }
        return result;
    }
    public abstract void setCalData();
}
