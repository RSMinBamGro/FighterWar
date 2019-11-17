package com.example.fighterwar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.fighterwar.Controller.BackgroundController;
import com.example.fighterwar.Controller.Controller;
import com.example.fighterwar.Model.Background;
import com.example.fighterwar.Model.MyFighter;
import com.example.fighterwar.View.Frame;


public class MainActivity extends AppCompatActivity {

    Frame frame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();
        Frame frame = new Frame(this);

        Log.d("MainActivity", frame.width + " " + frame.height);

        setContentView(frame);


        BackgroundController bc = new BackgroundController(new Background(frame.getContext(), frame.width, frame.height, 0, 0),
                                                            new Background(frame.getContext(), frame.width, frame.height, 0, -frame.height));

        Controller.myFighter = new MyFighter(frame.getContext(), frame.width / 2, frame.height);

    }

}
