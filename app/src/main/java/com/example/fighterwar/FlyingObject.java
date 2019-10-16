package com.example.fighterwar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.View;

public class FlyingObject extends View{

    protected Rect rect = new Rect(); // 用来确定位置
    protected int hp; // 生命
    protected int width, height; // 宽高
    protected Bitmap img;

    public FlyingObject (Context context) {
        super(context);
    }


    public void setX (int x) {
        rect.left = x;
        rect.right = x + width;
    }

    public void setY (int y) {
        rect.top = y;
        rect.bottom = y + height;
    }

    public Rect getRect () {
        return rect;
    }

    public int getObjHeight () {
        return height;
    }

    public int getObjWidth () {
        return width;
    }

    public Bitmap getImg() { return img; }
}
