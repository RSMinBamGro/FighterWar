package com.example.fighterwar.View;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.graphics.*;

import com.example.fighterwar.Model.Background;
import com.example.fighterwar.Model.Bullet;
import com.example.fighterwar.Model.MyFighter;
import com.example.fighterwar.Model.Objects;

import java.io.ObjectStreamException;

public class Frame extends View {
    private Paint painter = new Paint(); // 画笔
    private Point position_down; // 点触坐标
    private Point position_myFighter; // 我的战机坐标

    public Frame (Context context) {
        super(context);

        painter.setTextSize(50 * Objects.screenScale);
        painter.setColor(Color.WHITE);

        position_down = new Point();
        position_myFighter = new Point();

        // 注册点触事件监听器以控制玩家飞机
        setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) { //

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    position_down.set((int)event.getX(), (int)event.getY());

                    position_myFighter.x = Objects.myFighter.getRect().left;
                    position_myFighter.y = Objects.myFighter.getRect().top;
                }

                int delta_x = (int) event.getX() - position_down.x;
                int delta_y = (int) event.getY() - position_down.y;

                Objects.myFighter.setX(position_myFighter.x + delta_x);
                Objects.myFighter.setY(position_myFighter.y + delta_y);

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

        canvas.drawBitmap(Objects.background.getImg(), null, Objects.background.getRect(), painter);

        try {
            Objects.rmutex.acquire();

            if(Objects.readCount == 0)
                Objects.wmutex.acquire();

            Objects.readCount ++;

            Objects.rmutex.release();

            for (int i = 0; i < Objects.flyingObjects.size(); i ++)
                canvas.drawBitmap(Objects.flyingObjects.get(i).getImg(), null, Objects.flyingObjects.get(i).getRect(), painter);

            Objects.rmutex.acquire();

            Objects.readCount --;

            Objects.rmutex.release();

            if (Objects.readCount == 0)
                Objects.wmutex.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    @Override
    protected void onSizeChanged (int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Objects.width = w;
        Objects.height = h;

        // 获取手机分辨率与 960 * 540 的比例
        Objects.screenScale = (float) (Math.sqrt(Objects.width * Objects.height) / Math.sqrt(960 * 540));

        // 捕获屏幕大小之后才能创建背景对象
        Objects.background = new Background(getContext());
        Objects.myFighter = new MyFighter(getContext());
        Bullet bullet = new Bullet(getContext());
    }

}
