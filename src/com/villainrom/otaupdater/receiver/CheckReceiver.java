package com.villainrom.otaupdater.receiver;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.villainrom.otaupdater.R;
import com.villainrom.otaupdater.activity.UpdateActivity;
import com.villainrom.otaupdater.utility.Update;

/**
 * Check for updates
 * 
 * @author alankila
 */
public class CheckReceiver extends BroadcastReceiver {	
	@Override
	public void onReceive(Context context, Intent intent) {
		String urlPattern = context.getString(R.string.url);
		String urlString = String.format(urlPattern, System.getProperty("ro.build.version.release"));

		List<Update> updates = new ArrayList<Update>();
		
		try {
			URL url = new URL(urlString);
			URLConnection c = url.openConnection();
			c.setDoOutput(false);
			InputStream is = c.getInputStream();
			
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
			NodeList nl = doc.getElementsByTagName("update");
			for (int i = 0; i < nl.getLength(); i ++) {				
				Node n = nl.item(i);
				if (! (n instanceof Element)) {
					continue;
				}

				Update u = new Update();
				
				String attribute = n.getNodeName();
				if ("name".equals(attribute)
						|| "description".equals(attribute)
						|| "url".equals(attribute)) {
					Update.class.getField(attribute).set(u, n.getNodeValue());
					continue;
				}
					
				if ("dependencies".equals(attribute)) {
					putDependencies(n, u);
				}
				
				updates.add(u);
			}
			
			if (updates.size() != 0) {
				Intent foundUpdates = new Intent("com.villainrom.otaupdater.SHOW");
				foundUpdates.putExtra("update", updates.toArray(new Update[updates.size()]));
				context.sendBroadcast(foundUpdates);
			}
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

	private static void putDependencies(Node node, Update u) {
		NodeList nl = ((Element) node).getElementsByTagName("update");
		for (int i = 0; i < nl.getLength(); i ++) {
			u.dependencyUpdateNames.add(nl.item(i).getNodeValue());
		}
	}
}
