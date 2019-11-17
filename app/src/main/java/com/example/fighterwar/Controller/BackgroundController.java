package com.example.fighterwar.Controller;

import com.example.fighterwar.Model.Background;

public class BackgroundController implements Runnable {

    public static Background background1, background2;

    public BackgroundController (Background b1, Background b2) {
        background1 = b1;
        background2 = b2;

        new Thread(this).start();
    }

    @Override
    public void run () {
        while (true) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // 如果背景越界则从头开始
            if (background1.getRect().top + 2 <= background1.getObjHeight())
                background1.setY(background1.getRect().top + 2);
            else
                background1.setY(-background1.getObjHeight() + 2);

            if (background2.getRect().top + 2 <= background2.getObjHeight())
                background2.setY(background2.getRect().top + 2);
            else
                background2.setY(-background2.getObjHeight() + 2);
        }
    }
}