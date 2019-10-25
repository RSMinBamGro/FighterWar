package com.example.fighterwar.View;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.graphics.*;

import com.example.fighterwar.Controller.Controller;
import com.example.fighterwar.Model.Background;
import com.example.fighterwar.Model.Bullet;
import com.example.fighterwar.Model.MyFighter;

public class Frame extends View {
    private Paint painter = new Paint(); // 画笔
    private Point position_down; // 点触坐标
    private Point position_myFighter; // 我的战机坐标

    public Frame (Context context) {
        super(context);

        painter.setTextSize(50 * Controller.screenScale);
        painter.setColor(Color.WHITE);

        position_down = new Point();
        position_myFighter = new Point();

        // 注册点触事件监听器以控制玩家飞机
        setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) { //

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    position_down.set((int)event.getX(), (int)event.getY());

                    position_myFighter.x = Controller.myFighter.getRect().left;
                    position_myFighter.y = Controller.myFighter.getRect().top;
                }

                int delta_x = (int) event.getX() - position_down.x;
                int delta_y = (int) event.getY() - position_down.y;

                Controller.myFighter.setX(position_myFighter.x + delta_x);
                Controller.myFighter.setY(position_myFighter.y + delta_y);

                return true;
            }


        });

        new Thread(new Update()).start(); // 新建线程，令画布自动重绘

    }

    private class Update implements Runnable {
        @Override
        public void run () {
            while (true) {
                try {
                    Thread.sleep(10); // 每 10 ms 刷新一次画布
                } catch (InterruptedException e){
                    e.printStackTrace();
                }

                postInvalidate(); // 刷新画布
            }
        }
    }

    @Override
    protected void onDraw (Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(Controller.background1.getImg(), null, Controller.background1.getRect(), painter);

        try {
            if(Controller.readCount == 0)
                Controller.wmutex.acquire();


            Controller.rmutex.acquire();

            Controller.readCount ++;

            Controller.rmutex.release();

            for (int i = 0; i < Controller.flyingObjects.size(); i ++)
                canvas.drawBitmap(Controller.flyingObjects.get(i).getImg(), null, Controller.flyingObjects.get(i).getRect(), painter);

            Controller.rmutex.acquire();

            Controller.readCount --;

            Controller.rmutex.release();

            if (Controller.readCount == 0)
                Controller.wmutex.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    @Override
    protected void onSizeChanged (int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Controller.width = w;
        Controller.height = h;

        // 获取手机分辨率与 960 * 540 的比例
        Controller.screenScale = (float) (Math.sqrt(Controller.width * Controller.height) / Math.sqrt(960 * 540));

        // 捕获屏幕大小之后才能创建背景对象
        Controller.background1 = new Background(getContext());
        Controller.myFighter = new MyFighter(getContext());
        Bullet bullet = new Bullet(getContext());
    }

}
