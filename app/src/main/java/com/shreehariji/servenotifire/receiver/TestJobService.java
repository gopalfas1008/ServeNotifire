package com.shreehariji.servenotifire.receiver;


import android.annotation.SuppressLint;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.shreehariji.servenotifire.tools.ServerAutoCheckerScheduler;

/**
 * JobService to be scheduled by the JobScheduler.
 * start another service
 */

@SuppressLint("NewApi")
public class TestJobService extends JobService {
    private static final String TAG = "SyncService";
    public static void scheduleJob(Context context) {
        ComponentName serviceComponent = new ComponentName(context, TestJobService.class);
        JobInfo.Builder builder = new JobInfo.Builder(0, serviceComponent);
        builder.setMinimumLatency(15*60 * 1000); // wait at least
        builder.setOverrideDeadline(15*60* 1000); // maximum delay
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED); // require unmetered network
        builder.setRequiresDeviceIdle(false); // device should be idle
        builder.setRequiresCharging(false); // we don't care if the device is charging or not
        JobScheduler jobScheduler = context.getSystemService(JobScheduler.class);
        jobScheduler.schedule(builder.build());
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d("MyJob=","Do Work Continue");
        ServerAutoCheckerScheduler.InitiateAllAlarms(getApplicationContext());
        TestJobService.scheduleJob(getApplicationContext()); // reschedule the job
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return true;
    }

}