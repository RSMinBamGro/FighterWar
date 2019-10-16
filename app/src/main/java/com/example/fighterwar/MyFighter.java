package com.example.fighterwar;

import android.content.Context;
import android.graphics.BitmapFactory;

public class MyFighter extends FlyingObject {

    private int speed;

    public MyFighter (Context context) {
        super(context);

        width = 75;
        height = 90;

//        setX(Objects.width / 2 - width / 2);
//        setY(Objects.height - height);
        setX(0);
        setY(0);

        img = BitmapFactory.decodeResource(getResources(), R.mipmap.me);

        Objects.flyingObjects.add(this);
    }

}
