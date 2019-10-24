package com.example.fighterwar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.fighterwar.View.Frame;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();
        setContentView(new Frame(this));
    }

}
