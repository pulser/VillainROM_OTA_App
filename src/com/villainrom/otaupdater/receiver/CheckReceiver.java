package com.villainrom.otaupdater.receiver;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.xml.sax.ContentHandler;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Xml;
import android.util.Xml.Encoding;

import com.villainrom.otaupdater.R;
import com.villainrom.otaupdater.activity.UpdateActivity;

/**
 * Starts our periodic check service on boot.
 * 
 * @author alankila
 */
public class CheckReceiver extends BroadcastReceiver {
	private ContentHandler contentHandler;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		String urlPattern = context.getString(R.string.url);
		String urlString = String.format(urlPattern, System.getProperty("ro.build.version.release"));
		
		try {
			URL url = new URL(urlString);
			URLConnection c = url.openConnection();
			c.setDoOutput(false);
			InputStream is = c.getInputStream();
			Xml.parse(is, Encoding.UTF_8, contentHandler);
		}
		catch (Exception e) {
			NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			Notification notification = new Notification(
					R.drawable.icon,
					"Update check failed",
					System.currentTimeMillis()
			);
			notification.flags |= Notification.FLAG_AUTO_CANCEL;
			notification.setLatestEventInfo(context, "Exception", e.getMessage(), null);
			nm.notify(UpdateActivity.NOTIFY_CHECK_FAILED_ID, notification);
		}
	}
}
