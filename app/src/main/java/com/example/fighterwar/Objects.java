package com.example.fighterwar;

import android.graphics.Bitmap;

import java.util.Vector;

public class Objects {
    public static int width, height; // 屏幕的宽高
    public static float screenScale; // 比例，用于适应不同屏幕
    public static Vector<FlyingObject> flyingObjects = new Vector<>(); // 所有飞行物的集合,添加进这个集合才能被画出来
    public static Vector<Enemy> enemies = new Vector<>(); // 敌人飞机的集合，添加进这个集合才能被子弹打中
    public static MyFighter myFighter; // 我的灰机
    public static Background background; // 背景

}
