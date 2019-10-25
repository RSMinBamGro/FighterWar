package com.example.fighterwar.Model;

import android.content.Context;
import android.graphics.BitmapFactory;

import com.example.fighterwar.Controller.Controller;
import com.example.fighterwar.R;

public class Background extends FlyingObject /*implements Runnable*/ {

    public Background (Context context) {
        super(context);

        width = Controller.width;
        height = Controller.height;

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
//                setY(-Controller.height);
//        }
//    }
}
