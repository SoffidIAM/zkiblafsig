package es.caib.zkiblaf.applet;

import java.util.Vector;

public class PinCache {
	private static PinCache instance;

	/**
	 * Obtener el PinCache actual
	 * 
	 * @return pincache actual o uno nuevo si no existe
	 */
	protected static PinCache getInstance() {
		if (instance == null)
			instance = new PinCache();
		return instance;
	}

	Vector v = new Vector();

	public char[] getPassword(String cert) {

		// Expiracion maxima 30 minutos = 30 * 60 * 60 * 1000 = 108000000
		long maxTime = System.currentTimeMillis() - 108000000;
		PinEntry foundEntry = null;

		for (int i = 0; i < v.size(); i++) {
			PinEntry entry = (PinEntry) v.get(i);
			if (entry.date < maxTime && entry.password != null) {
				for (int j = 0; j < entry.password.length; j++) {
					entry.password[j] = '\0';
				}
				entry.password = null;
			}
			if (entry.cert.equals(cert))
				foundEntry = entry;
		}
		if (foundEntry == null) {
			foundEntry = new PinEntry();
			foundEntry.cert = cert;
			foundEntry.date = System.currentTimeMillis();
			v.add(foundEntry);
		}
		if (foundEntry.password != null)
			return foundEntry.password;
		PinDialog dialog = new PinDialog();
		dialog.setCertificate(cert);
		dialog.setVisible(true);
		char pass[] = dialog.getPassword();
		if (dialog.isSelected())
			foundEntry.password = pass;
		return pass;
	}

	class PinEntry {
		String cert;
		long date;
		char password[];
	}
}
