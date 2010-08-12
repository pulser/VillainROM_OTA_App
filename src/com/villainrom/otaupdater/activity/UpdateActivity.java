package com.villainrom.otaupdater.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.villainrom.otaupdater.R;

public class UpdateActivity extends Activity {
    public static final int NOTIFY_SERVICE_ID = 1;
	public static final int NOTIFY_CHECK_FAILED_ID = 2;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        /* HACK: mostly used for development purposes. Ensures that the service
         * is running when the application is being displayed. */
        startService(new Intent("com.villainrom.otaupdate.PERIODIC_CHECK"));
    }
}