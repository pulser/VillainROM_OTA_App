package com.villainrom.otaupdater.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Used to validate that update packages are ours before using them.
 * 
 * @author alankila
 */
public class CheckSignature {
	/** The trusted certificate */
	private final Certificate trustAnchor;

	/**
	 * Update.zip validator. Android actually uses jarsigner/signapk
	 * style files, so we parse the content as a .jar.
	 * 
	 * @param anchor The root of the certificate chain.
	 */
	public CheckSignature(Certificate anchor) {
		trustAnchor = anchor;
	}
	
	/**
	 * Validates the jar file.
	 * 
	 * @param jarFile File to validate
	 * @return true on success
	 * @throws IOException
	 * @throws SecurityException
	 */
	public void validateZip(File jarFile) throws IOException, SecurityException {
		byte[] buf = new byte[4096];

		JarFile jar = new JarFile(jarFile);
		Enumeration<JarEntry> entries = jar.entries();
		while (entries.hasMoreElements()) {
			JarEntry je = entries.nextElement();
			if (je == null) {
				break;
			}

			/* Directory does not need to be signed */
			if (je.isDirectory()) {
				continue;
			}
			
			/* The exceptions that don't need to be signed either. */
			String name = je.getName();
			if (name.equals("META-INF/MANIFEST.MF") || name.equals("META-INF/CERT.SF") || name.equals("META-INF/CERT.RSA")) {
				continue;
			}

			/* Read one byte -- this is apparently necessary to cause validation. (CHECK) */
			InputStream entryFile = jar.getInputStream(je);
			while (entryFile.read(buf) > 0) {
			}

			/* Validate that we trust the certificate used to sign the file. */
			Certificate[] certs = je.getCertificates();
			try {
				validateCertificate(certs);
			} catch (SecurityException e) {
				throw new SecurityException("Failed to validate: " + name, e);
			}
		}
	}

	/**
	 * We don't do chain-of-trust stuff here. It's either signed by
	 * just 1 certificate, our main certificate, or we throw it into the river.
	 * 
	 * @param certificate The chain to validate
	 * 
	 * @return true if valid
	 */
	private void validateCertificate(Certificate[] certificate) throws SecurityException {
		if (certificate == null) {
			throw new SecurityException("No signer associated in security manifest");
		}

		if (certificate.length != 1) {
			throw new SecurityException("Only 1 signer expected, now has: " + certificate.length);
		}
		
		if (! certificate[0].equals(trustAnchor)) {
			throw new SecurityException("File not signed by our certificate, but by: " + certificate[0]);
		}
	}
	
	public static void main(String[] args) throws Exception {
		if (args.length != 2) {
			System.out.println("Usage: program <x509cert in der> <update.zip>");
			return;
		}
		Certificate x = CertificateFactory.getInstance("X509").generateCertificate(new FileInputStream(new File(args[0])));
		new CheckSignature(x).validateZip(new File(args[1]));
		System.out.println("Success!");
	}
}
