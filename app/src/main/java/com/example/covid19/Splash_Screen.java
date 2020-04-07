package com.example.covid19;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;


public class Splash_Screen extends AppCompatActivity {

    private ProgressBar splash_progress_bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash__screen);

        splash_progress_bar = findViewById(R.id.splash_progress);

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                while(splash_progress_bar.getProgress() != 100){
                    splash_progress_bar.incrementProgressBy(20);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Intent intent = new Intent(Splash_Screen.this, statsTemp.class);
                startActivity(intent);
                finish();
            }
        });
        t1.start();
    }
}
