package com.example.fighterwar.Model;

import android.content.Context;
import android.graphics.BitmapFactory;

import com.example.fighterwar.Controller.Controller;
import com.example.fighterwar.R;

public class Background extends FlyingObject {

    public Background (Context context, int width, int height, int posX, int posY) {
        super(context);

        this.width = width;
        this.height = height;

        setX(0);
        setY(0);

        img = BitmapFactory.decodeResource(getResources(), R.mipmap.background1);

    }

}
