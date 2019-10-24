package com.example.fighterwar.Model;

import com.example.fighterwar.Model.Background;
import com.example.fighterwar.Model.Bullet;
import com.example.fighterwar.Model.Enemy;
import com.example.fighterwar.Model.FlyingObject;
import com.example.fighterwar.Model.MyFighter;

import java.util.Vector;
import java.util.concurrent.Semaphore;

public class Objects {
    public static int width, height; // 屏幕的宽高
    public static float screenScale; // 比例，用于适应不同屏幕

    public static Vector<FlyingObject> flyingObjects = new Vector<>(); // 所有飞行物的集合,添加进这个集合才能被画出来
    final public static Semaphore rmutex = new Semaphore(1, true);
    final public static Semaphore wmutex = new Semaphore(1, true);
    public static int readCount = 0;

    public static Vector<Enemy> enemies = new Vector<>(); // 敌人飞机的集合，添加进这个集合才能被子弹打中

    public static MyFighter myFighter;
    public static Background background;

}
