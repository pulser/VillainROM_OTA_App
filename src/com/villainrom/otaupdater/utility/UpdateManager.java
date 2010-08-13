package com.villainrom.otaupdater.utility;

import java.io.File;
import java.io.InputStream;
import java.io.PrintWriter;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.villainrom.otaupdater.R;
import com.villainrom.otaupdater.RecoverySystem;

public class UpdateManager {
	final String appliedUpdatesPath;
	final String recoveryCommandPath;
	
	final Context context;
	
	public UpdateManager(Context context) {
		this.context = context;
		
		appliedUpdatesPath = context.getString(R.string.path_applied_updates);
		recoveryCommandPath = context.getString(R.string.path_recovery_command);
	}

	public List<String> getAppliedUpdates() {
		List<String> files = new ArrayList<String>();

		/* might not exist yet. */
		File[] list = new File(appliedUpdatesPath).listFiles();
		if (list != null) {
			for (File f : list) {
				files.add(f.getName());
			}
		}
		return files;
	}
	
	public void applyUpdate(File update) throws Exception {
		InputStream certStream = UpdateManager.class.getClassLoader().getResourceAsStream("ota_x509.pem");
		Certificate cert = CertificateFactory.getInstance("X.509").generateCertificate(certStream);
		CheckSignature cs = new CheckSignature(cert);
		cs.validateZip(update);
		RecoverySystem.installPackage(context, update);
	}

	@SuppressWarnings("unused")
	private void applyUpdateTheHardWay(File update) throws Exception {
		Process p = Runtime.getRuntime().exec("su");

		PrintWriter pw = new PrintWriter(p.getOutputStream());
		pw.println("set -e");
		pw.println("mkdir -p " + new File(recoveryCommandPath).getParent());
		pw.println("echo boot-recovery" + " > " + recoveryCommandPath);
		pw.println("echo --update_package=" + update.getAbsolutePath().replaceAll("^/cache", "CACHE:") + " >> " + recoveryCommandPath);
		pw.println("reboot recovery");
		pw.close();
		
		if (p.waitFor() != 0) {
			throw new Exception("Failed to write update command sequence.");
		}
	}
	
}