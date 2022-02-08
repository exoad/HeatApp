package com.jackmeng.handwarmer;

import android.app.ActivityManager;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.TextView;

public abstract class Warmer extends AppCompatActivity {

    private static Thread[] threads = new Thread[2];

    private static void init() {
        Arrays.fill(threads, 0, threads.length, new Thread());
    }


    public static float cpuTemperature()
    {
        Process process;
        try {
            process = Runtime.getRuntime().exec("cat sys/class/thermal/thermal_zone0/temp");
            process.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = reader.readLine();
            if(line!=null) {
                float temp = Float.parseFloat(line);
                return temp / 1000.0f;
            }else{
                return 51.0f;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0f;
        }
    }

    public static void start() {
        init();
        // make each thread perform a task to warm up the cpu but not a while-true loop
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {
                ArrayList<Long> all = new ArrayList<>();

                while(true) {
                    long ran = (long) ((long) (Math.random() * 999) * Math.cos(Math.floor(Math.random() * Math.cos(Math.log((Math.random() % 2 == 0 ? Integer.MAX_VALUE : Integer.MIN_VALUE))))));
                    all.add(ran);
                }
            });

            threads[i].start();

        }

    }

    public void stop() {
        for(Thread s : threads) {
            s.interrupt();
        }
    }

}
