package com.samchen.samdemos.threads;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

public class DemoAsyncTask extends Activity {

    final String TAG = "DemoAsyncTask";
    private ProgressBar mProgress;
    private int mProgressStatus = 0;
    boolean isRunning = false;
    private updateTask mTask;

    /** Called when the activity is first created. */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demothreads);

        mProgress = (ProgressBar) findViewById(R.id.progressBar1);

        Button btnRun = (Button) findViewById(R.id.button1);
        btnRun.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (!isRunning) {
                    mTask = new updateTask();
                    mTask.execute();
                    isRunning = true;
                } else {
                    Log.d(TAG, "thread is runing");
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.running), Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });

    }

    private class updateTask extends AsyncTask<Void, Integer, Void> {

        protected void onPostExecute(Void result) {
           
        }

        protected void onProgressUpdate(Integer... value) {
            Log.d(TAG, "onProgressUpdate : " + value);
            mProgress.setProgress(value[0]);
        }

        protected void onPreExecute() {
            // TODO Auto-generated method stub
            mProgressStatus = 0;
        }

        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub

            while (mProgressStatus < 100) {
                Log.d(TAG, "doInBackground mProgressStatus : "
                        + mProgressStatus);
                // do something long
                publishProgress(mProgressStatus);
                SystemClock.sleep(500);
                mProgressStatus++;
            }
            return null;
        }
    }

}

