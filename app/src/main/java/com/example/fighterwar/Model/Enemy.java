package com.example.fighterwar.Model;

import android.content.Context;

public class Enemy extends FlyingObject {

    public Enemy (Context context) {
        super(context);

        width = height = 200 * (int) Objects.screenScale;//凡是涉及到像素，都乘一下分辨率比例

        setX(0);
        setY(0);

        Objects.flyingObjects.add(this);
        Objects.enemies.add(this);
    }

}
