package com.villainrom.otaupdater.activity;

import android.app.Activity;
import android.os.Bundle;

import com.villainrom.otaupdater.R;
import com.villainrom.otaupdater.utility.Update;
import com.villainrom.otaupdater.utility.UpdateManager;

public class ApplyActivity extends Activity {
	private UpdateManager updateManager;
	
	private Update update;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apply);

        updateManager = new UpdateManager(this);
       
        update = (Update) getIntent().getParcelableExtra("update");

		bindUI();
    }
    
    private void bindUI() {
    }
    
    
}