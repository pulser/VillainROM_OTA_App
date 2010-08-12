package com.villainrom.otaupdater.activity;

import com.villainrom.otaupdater.R;
import com.villainrom.otaupdater.R.layout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class UpdateActivity extends Activity {
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