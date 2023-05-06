package src.climatemonitoring;

import java.io.*;
import java.util.LinkedList;
import java.util.Objects;

/**
 * Class to manage the registration of the user
 */
public
class Registration {
	static String filepathOperatoriRegistrati = "data/OperatoriRegistrati.dati";
	String nome;
	String cognome;
	String codiceFiscale;
	String email;
	String userid;
	String password;
	int centroMonitoraggio;

	/**
	 * Constructor to create the object Registration
	 * @param nome String
	 * @param cognome String
	 * @param codiceFiscale String
	 * @param email String
	 * @param userid String
	 * @param password String
	 * @param centroMonitoraggio int
	 */
	public Registration(String nome, String cognome, String codiceFiscale, String email, String userid, String password, int centroMonitoraggio) {
		this.nome = nome;
		this.cognome = cognome;
		this.codiceFiscale = codiceFiscale;
		this.email = email;
		this.userid = userid;
		this.password = password;
		this.centroMonitoraggio = centroMonitoraggio;
	}

	/**
	 * Function to write the file with the user data
	 * Example: nome;cognome;codiceFiscale;email;userid;password;centroMonitoraggio
	 * @param list LinkedList
	 * @return boolean, true if the operation is successful
	 */
	static boolean writeRegistrati(LinkedList<Registration> list) {
		Objects.requireNonNull(list, "list cannot be null");
		if (list.isEmpty()) return false;

		try {
			ClimateMonitor.createFile(filepathOperatoriRegistrati);
			BufferedWriter writer = new BufferedWriter(new FileWriter(filepathOperatoriRegistrati));
			for (Registration r : list) {
				writer.write(r.nome + ";" + r.cognome + ";" + r.codiceFiscale + ";" + r.email + ";" + r.userid + ";" + r.password + ";" + r.centroMonitoraggio);
				writer.newLine();
			}
			writer.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Function that manage the read and write of the file
	 * @param utenteRegistrato Registration
	 * @return boolean
	 */
	public static boolean registrazione(Registration utenteRegistrato) {
		Objects.requireNonNull(utenteRegistrato, "utenteRegistrato cannot be null");

		LinkedList<Registration> list = readRegistrati();

		assert list != null;
		for (Registration r : list) {
			if (r.userid.equals(utenteRegistrato.userid)) return false;
		}
		list.addLast(utenteRegistrato);
		return writeRegistrati(list);
	}

	/**
	 * Function that read the file and return the list of the users registered
	 * @return list
	 */
	public static LinkedList<Registration> readRegistrati() {
		LinkedList<Registration> list = new LinkedList<>();
		boolean fileCreated = ClimateMonitor.createFile(filepathOperatoriRegistrati);
		if (!fileCreated) return list;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filepathOperatoriRegistrati));
			String[] campiTotali;
			while (true) {
				int i = 0;
				String readerLiner = reader.readLine();
				if (readerLiner == null || readerLiner.equals("")) break;
				campiTotali = readerLiner.split(";");
				Registration utenteRegistrato = new Registration(campiTotali[i], campiTotali[i + 1], campiTotali[i + 2], campiTotali[i + 3], campiTotali[i + 4], campiTotali[i + 5], Integer.parseInt(campiTotali[i + 6]));
				list.addLast(utenteRegistrato);
			}
			reader.close();

			return list;

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
