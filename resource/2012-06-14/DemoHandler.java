package com.samchen.samdemos.threads;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

public class DemoHandler extends Activity {

    final String TAG = "DemoHandler";
    private ProgressBar mProgress;
    private int mProgressStatus = 0;
    private Handler mHandler;
    boolean isRunning = false;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demothreads);

        Button btnRun = (Button) findViewById(R.id.button1);
        mProgress = (ProgressBar) findViewById(R.id.progressBar1);
        mHandler = new Handler();

        btnRun.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (!isRunning) {
                    startProgress(mProgress);
                    isRunning = true;
                } else{
                    Log.d(TAG, "thread is runing");
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.running),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void startProgress(View view) {
        // Do something long
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                for (mProgressStatus = 0; mProgressStatus <= 100; mProgressStatus++) {
                    //final int value = i;
                    SystemClock.sleep(500);
                   
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Log.d(TAG, "update mProgressStatus : "
                                    + mProgressStatus);
                            mProgress.setProgress(mProgressStatus);
                        }
                    });
                }
            }
        };
        new Thread(runnable).start();
    }
}

