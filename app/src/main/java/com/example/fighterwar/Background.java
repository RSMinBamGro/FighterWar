package com.example.fighterwar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class Background extends FlyingObject /*implements Runnable*/ {

    public Background (Context context) {
        super(context);

        width = Objects.width;
        height = Objects.height;

        setX(0);
        setY(0);

        img = BitmapFactory.decodeResource(getResources(), R.mipmap.background1);
    }

//    // 控制背景滚动
//    @Override
//    public void run () {
//        while (true) {
//            try {
//                Thread.sleep(10);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//            // 如果背景越界则从头开始
//            if (rect.top + 2 <= 0)
//                setY(rect.top + 2);
//            else
//                setY(-Objects.height);
//        }
//    }
}
