package com.villainrom.otaupdater.activity;

import java.util.Arrays;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.villainrom.otaupdater.R;
import com.villainrom.otaupdater.utility.Update;

public class SelectUpdateActivity extends Activity {
	private Update[] updateList;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_update);
        
        updateList = (Update[]) getIntent().getParcelableArrayExtra("update");

        Spinner update = (Spinner) findViewById(R.id.Update);
        update.setAdapter(new ArrayAdapter<Update>(this, R.id.Description, Arrays.asList(updateList)));
    }
    
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    }
}