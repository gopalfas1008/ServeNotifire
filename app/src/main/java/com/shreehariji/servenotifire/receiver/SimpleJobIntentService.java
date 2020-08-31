package com.shreehariji.servenotifire.receiver;

import android.annotation.SuppressLint;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.JobIntentService;

import com.shreehariji.servenotifire.tools.ServerAutoCheckerScheduler;

public class SimpleJobIntentService extends JobIntentService {
    /**
     * Unique job ID for this service.
     */
    static final int JOB_ID = 1000;

    @SuppressLint("NewApi")
    public static void setUpdateJob(Context context) {
        JobInfo job =
                new JobInfo.Builder(
                        JOB_ID,
                        new ComponentName(context, SimpleJobIntentService.class))
                        .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                        .setMinimumLatency(15*60*1000L)
                        .setOverrideDeadline((long) (15*60*1000L * 1.05))
                        .setRequiresCharging(false)
                        .build();

        JobScheduler mJobService = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);

        // Job을 등록한다.
        mJobService.schedule(job);
    }

    /**
     * Convenience method for enqueuing work in to this service.
     */
    static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, SimpleJobIntentService.class, JOB_ID, work);
    }

    @Override
    protected void onHandleWork(Intent intent) {
        // We have received work to do.  The system or framework is already
        // holding a wake lock for us at this point, so we can just go.
        Log.i("SimpleJobIntentService", "Executing work: " + intent);
        ServerAutoCheckerScheduler.InitiateAllAlarms(getApplicationContext());
        TestJobService.scheduleJob(getApplicationContext()); // res
        Log.i("SimpleJobIntentService", "Completed service @ " + SystemClock.elapsedRealtime());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        toast("All work complete");
    }

    final Handler mHandler = new Handler();

    // Helper for showing tests
    void toast(final CharSequence text) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(SimpleJobIntentService.this, text, Toast.LENGTH_SHORT).show();
            }
        });
    }
}