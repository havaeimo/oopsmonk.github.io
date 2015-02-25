package com.samchen.samdemos.threads;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

public class DemoThreads extends Activity {
    final boolean isThread = true;
    final String TAG = "DemoThreads";
    private ProgressBar mProgress;
    private int mProgressStatus = 0;
    boolean isRunning = false;
   
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demothreads);

        Button btnRun = (Button) findViewById(R.id.button1);
        mProgress = (ProgressBar) findViewById(R.id.progressBar1);

        btnRun.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if(isRunning){
                    Log.d(TAG,"thread is runing");
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.running),
                            Toast.LENGTH_SHORT).show();
                    return;
                }else
                    isRunning = true;
               
                if (isThread) {
                    // thread example
                    new Thread(new Runnable() {
                        public void run() {
                            while (mProgressStatus < 100) {
                                // update the bar status
                                mProgress.post(new Runnable() {
                                    public void run() {
                                        mProgress.setProgress(mProgressStatus);
                                        Log.d(TAG, "update mProgressStatus : "
                                                + mProgressStatus);
                                    }
                                });

                                // do something long
                                SystemClock.sleep(500);
                                mProgressStatus++;
                            }
                        }

                    }).start();

                } else {
                    // non-thread example
                    while (mProgressStatus < 100) {
                        // update the bar status
                        mProgress.post(new Runnable() {
                            public void run() {
                                mProgress.setProgress(mProgressStatus);
                            }
                        });

                        // do something long
                        //SystemClock.sleep(500);
                        //if use SystemClock.sleep, system will be crash.
                        try{
                            Thread.sleep(500);
                        }catch (InterruptedException e){
                            Log.d(TAG,"InterruptedException!!!!!!!");
                            e.printStackTrace();
                        }
                        mProgressStatus++;
                       
                    }
                }

            }

        });
    }

}

