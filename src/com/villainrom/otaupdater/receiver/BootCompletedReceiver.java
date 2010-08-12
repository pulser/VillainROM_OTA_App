package com.villainrom.otaupdater.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Starts our periodic check service on boot.
 * 
 * @author alankila
 */
public class BootCompletedReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		context.startService(new Intent("com.villainrom.otaupdate.PERIODIC_CHECK"));
	}
}
