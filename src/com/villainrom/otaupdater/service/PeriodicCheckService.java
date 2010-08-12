package com.villainrom.otaupdater.service;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.villainrom.otaupdater.activity.UpdateActivity;

/**
 * This service accesses the VillainROM servers to discover new updates,
 * and informs the user when new updates are found. It reschedules itself
 * once per day.
 * 
 * @author alankila
 */
public class PeriodicCheckService extends Service {
	private static final String TAG = PeriodicCheckService.class.getSimpleName();

	Timer timer;
	
	/** You can't connect to this service. */
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(TAG, "Starting");

		startForeground(UpdateActivity.NOTIFY_SERVICE_ID, new Notification());

		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				sendBroadcast(new Intent("com.villainrom.otaupdater.CHECK"));
			}
		};
		task.run();
		
		timer = new Timer();
		timer.scheduleAtFixedRate(task, Calendar.getInstance().getTime(), 86400 * 1000);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.i(TAG, "Stopping");
		stopForeground(true);
		
		timer.cancel();
	}
}
