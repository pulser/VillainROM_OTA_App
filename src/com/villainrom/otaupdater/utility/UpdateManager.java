package com.villainrom.otaupdater.utility;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.villainrom.otaupdater.R;

public class UpdateManager {
	final String appliedUpdatesPath;
	final String updateLocationPath;
	final String recoveryCommandPath;
	
	public UpdateManager(Context context) {
		appliedUpdatesPath = context.getString(R.string.path_applied_updates);
		updateLocationPath = context.getString(R.string.path_download_to);
		recoveryCommandPath = context.getString(R.string.path_recovery_command);
	}

	public List<String> getAppliedUpdates() {
		File[] list = new File(appliedUpdatesPath).listFiles();
		List<String> files = new ArrayList<String>();
		for (File f : list) {
			files.add(f.getName());
		}
		return files;
	}
	
	public void applyUpdate(String update) throws IOException {
		PrintWriter f = new PrintWriter(new FileOutputStream(new File(recoveryCommandPath)));
		String commands = "";
		commands += "apply " + updateLocationPath + "/" + update + "\n";
		commands += "touch " + appliedUpdatesPath + "/" + update + "\n";
		f.print(commands);
		f.close();
	}
}