package com.example.fighterwar.Model;

import android.content.Context;
import android.graphics.BitmapFactory;

import com.example.fighterwar.Controller.Controller;
import com.example.fighterwar.R;

public class Bullet extends FlyingObject implements Runnable {

    public Bullet (Context context) {
        super(context);

        width = 15;
        height = 30;

        setX(Controller.myFighter.getRect().left + Controller.myFighter.getObjWidth() / 2 - width / 2);
        setY(Controller.myFighter.getRect().top);

        img = BitmapFactory.decodeResource(getResources(), R.mipmap.bomb);

        Controller.flyingObjects.add(this);

        new Thread(this).start();
    }

    @Override
    public void run () {
        while (true) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            setY(rect.top - 6);

            if (rect.top + height <= 0) break;
        }

        try {
            Controller.wmutex.acquire();

            Controller.flyingObjects.remove(this); // 不能在while循环内执行，否则该线程会持续执行，资源得不到释放，程序会越来越卡
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            Controller.wmutex.release();
        }


    }
}
