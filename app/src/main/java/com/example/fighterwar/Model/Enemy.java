package com.example.fighterwar.Model;

import android.content.Context;

import com.example.fighterwar.Controller.Controller;

public class Enemy extends FlyingObject {

    public Enemy (Context context) {
        super(context);

        width = height = 200 * (int) Controller.screenScale;//凡是涉及到像素，都乘一下分辨率比例

        setX(0);
        setY(0);

        Controller.flyingObjects.add(this);
        Controller.enemies.add(this);
    }

}
