package com.jackmeng.handwarmer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private boolean alreadyRunning = false;
    public Warmer warmer;
    public Thread worker = new Thread();
    public Thread slave = new Thread();
    public Thread secondary = new Thread();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView textView = (TextView) findViewById(R.id.statusText);
        final TextView cpuTemp = (TextView) findViewById(R.id.statusText2);

        final Button button = (Button) findViewById(R.id.button);

        secondary = new Thread(() -> {
            while (true) {
                cpuTemp.setText(warmer.cpuTemperature() + " temp.");
                System.out.println(warmer.cpuTemperature());
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        secondary.start();

        button.setOnClickListener(view -> {
            slave = new Thread(() -> {
                if (alreadyRunning) {
                    if (worker.isAlive() || !worker.isInterrupted()) {
                        worker.interrupt();
                    }
                    worker = new Thread();
                    warmer.stop();
                    button.setText("Start");
                    textView.setText("Off");
                    alreadyRunning = false;

                } else {
                    worker = new Thread(() -> {
                        warmer.start();
                    });
                    worker.start();
                    button.setText("Stop");
                    textView.setText("On");
                    alreadyRunning = true;
                }
            });
            slave.start();

        });
    }
}