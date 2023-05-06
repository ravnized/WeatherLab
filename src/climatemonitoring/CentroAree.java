package src.climatemonitoring;

import java.io.*;
import java.util.LinkedList;
import java.util.Objects;

/**
 * CentroAree Class created to manage all centroAree in filePathCentri
 */
public class CentroAree extends Login {

	static String filePathCentri = "data/CentroMonitoraggio.dati";
	int id;
	String nome;
	String indirizzo;
	String citta;

	/**
	 * Constructor to initialize the object
	 *
	 * @param id int
	 * @param nome String
	 * @param indirizzo String
	 * @param citta String
	 */
	public CentroAree(int id, String nome, String indirizzo, String citta) {
		this.id = id;
		this.nome = nome;
		this.indirizzo = indirizzo;
		this.citta = citta;
	}

	/**
	 * Function to write centri in the file
	 * Example: id;nome;indirizzo;città
	 *
	 * @param list LinkedList
	 * @return boolean if the operation is successful or not
	 */
	static boolean writeCentri(LinkedList<CentroAree> list) {

		Objects.requireNonNull(list, "list cannot be null");
		if (list.isEmpty()) return false;

		try {
			ClimateMonitor.createFile(filePathCentri);
			BufferedWriter writer = new BufferedWriter(new FileWriter(filePathCentri));
			for (CentroAree c : list) {
				writer.write(c.id + ";" + c.nome + ";" + c.indirizzo + ";" + c.citta);
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
	 * Function to read centri in the file
	 *
	 * @return the list of centri red
	 */
	public static LinkedList<CentroAree> readCentri() {
		LinkedList<CentroAree> list = new LinkedList<>();
		boolean fileCreated = ClimateMonitor.createFile(filePathCentri);
		if (!fileCreated) return list;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filePathCentri));
			String[] campiTotali;
			while (true) {
				int i = 0;
				String readerLiner = reader.readLine();
				if (readerLiner == null || readerLiner.equals("")) break;
				campiTotali = readerLiner.split(";");
				CentroAree centro = new CentroAree(Integer.parseInt(campiTotali[i]), campiTotali[i + 1], campiTotali[i + 2], campiTotali[i + 3]);
				list.addLast(centro);
			}
			reader.close();

			return list;

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Function to insert Centri check if the città' is present in CoordinateMonitoraggio
	 *
	 * @param centro CentroAree
	 * @return boolean true if is successful
	 */
	public static boolean insertCentro(CentroAree centro) {
		if (!isAutenticato()) {
			System.out.println("Non sei autenticato");
			return false;
		}
		Objects.requireNonNull(centro, "centro cannot be null");
		int sizeList = ClimateMonitor.cercaAreaGeografica(centro.citta, 0).size();
		if (sizeList == 0){
			System.out.println("Città non presente in CoordinateMonitoraggio");
			return false;
		};
		LinkedList<CentroAree> list = readCentri();
		assert list != null;
		for (CentroAree centroAree : list) {
			if (centroAree.id == centro.id) return false;
		}
		list.addLast(centro);
		return writeCentri(list);
	}

	/**
	 * Function to search centri in the file used in other function to check
	 *
	 * @param id int
	 * @return the centro if found
	 */
	public static CentroAree cercaCentro(int id) {
		LinkedList<CentroAree> list = readCentri();
		assert list != null;
		for (CentroAree c : list) {
			if (c.id == id) return c;
		}
		return null;
	}
}
