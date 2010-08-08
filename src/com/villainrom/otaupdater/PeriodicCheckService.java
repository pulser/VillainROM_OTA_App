package com.villainrom.otaupdater;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * This service accesses the VillainROM servers to discover new updates,
 * and informs the user when new updates are found.
 * 
 * @author alankila
 */
public class PeriodicCheckService extends Service {
	private static final String TAG = PeriodicCheckService.class.getSimpleName();

	/** You can't connect to this service. */
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(TAG, "Starting");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.i(TAG, "Stopping");
	}
}
