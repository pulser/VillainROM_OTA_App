package com.villainrom.otaupdater;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * This service accesses the VillainROM servers to discover new updates,
 * and informs the user when new updates are found.
 * 
 * @author alankila
 */
public class PeriodicCheckService extends Service {
	/** You can't connect to this service. */
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}
