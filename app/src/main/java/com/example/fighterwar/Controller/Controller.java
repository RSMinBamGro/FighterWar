package com.example.fighterwar.Controller;

import com.example.fighterwar.Model.Background;
import com.example.fighterwar.Model.Bullet;
import com.example.fighterwar.Model.Enemy;
import com.example.fighterwar.Model.FlyingObject;
import com.example.fighterwar.Model.MyFighter;
import com.example.fighterwar.View.Frame;

import java.util.Vector;
import java.util.concurrent.Semaphore;

public class Controller {

    public static float screenScale; // 比例，用于适应不同屏幕

    public static Vector<FlyingObject> flyingObjects = new Vector<>(); // 所有飞行物的集合,添加进这个集合才能被画出来
    final public static Semaphore rmutex = new Semaphore(1, true);
    final public static Semaphore wmutex = new Semaphore(1, true);
    public static int readCount = 0;

    public static Vector<Enemy> enemies = new Vector<>(); // 敌人飞机的集合，添加进这个集合才能被子弹打中

    public static MyFighter myFighter;

}
