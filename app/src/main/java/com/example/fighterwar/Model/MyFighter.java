package com.example.fighterwar.Model;

import android.content.Context;
import android.graphics.BitmapFactory;

import com.example.fighterwar.Controller.Controller;
import com.example.fighterwar.R;

public class MyFighter extends FlyingObject implements Runnable {

    public MyFighter (Context context) {
        super(context);

        width = 75;
        height = 90;

        setX(Controller.width / 2 - width / 2);
        setY(Controller.height - height);

        img = BitmapFactory.decodeResource(getResources(), R.mipmap.me);

        Controller.flyingObjects.add(this);

        new Thread(this).start();
    }

    @Override
    public void run () {
        while (true) {
            try {
                Thread.sleep(90);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            new Bullet(getContext());
        }
    }

}
