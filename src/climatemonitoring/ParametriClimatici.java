package src.climatemonitoring;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Objects;

/**
 * Class of Parametri Climatici
 */
public class ParametriClimatici extends Login {

	static String filepathParametriClimatici = "data/ParametriClimatici.dati";
	static String pattern = "dd/MM/yyyy HH:mm:ss";
	int score = 1;
	String notes = "";
	int id_centro;
	String areaInteresse;
	String dataRilevazione;
	String climateCategoryToString;

	/**
	 * Constructor for the object ParamatriClimatici
	 * @param id_centro int
	 * @param areaInteresse String
	 * @param dataRilevazione String
	 * @param climate int
	 * @param score int
	 * @param notes String
	 */
	public ParametriClimatici(int id_centro, String areaInteresse, String dataRilevazione, int climate, int score, String notes) {
		this.score = score;
		this.notes = notes;
		this.id_centro = id_centro;
		this.areaInteresse = areaInteresse;
		this.dataRilevazione = dataRilevazione;
		this.climateCategoryToString = climateCategory.values()[climate].toString();

	}

	/**
	 * Function that write weather params in the file
	 * @param list LinkedList
	 * @return boolean
	 */
	static boolean writeParametri(LinkedList<ParametriClimatici> list) {
		Objects.requireNonNull(list, "list cannot be null");
		if (list.isEmpty()) return false;
		File file;
		try {
			file = new File(filepathParametriClimatici);
			if (!file.exists()) file.createNewFile();
			BufferedWriter writer = new BufferedWriter(new FileWriter(filepathParametriClimatici));
			for (ParametriClimatici r : list) {
				writer.write(r.id_centro + ";" + r.areaInteresse + ";" + r.dataRilevazione + ";" + r.climateCategoryToString + ";" + r.score + ";" + (!r.notes.isEmpty() ? r.notes : "_"));
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
	 * Function that read the params in the file
	 * @return LinkedList of the params
	 */
	public static LinkedList<ParametriClimatici> readParametri() {
		LinkedList<ParametriClimatici> list = new LinkedList<>();
		File file = new File(filepathParametriClimatici);
		if (!file.exists()) return list;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filepathParametriClimatici));
			String[] campiTotali;
			while (true) {
				int i = 0;
				String readerLiner = reader.readLine();
				if (readerLiner == null || readerLiner.equals("")) break;
				campiTotali = readerLiner.split(";");

				ParametriClimatici parametriClimatici = new ParametriClimatici(Integer.parseInt(campiTotali[i]), campiTotali[i + 1], campiTotali[i + 2], climateCategory.valueOf(campiTotali[i + 3]).ordinal(), Integer.parseInt(campiTotali[i + 4]), campiTotali[i + 5]);
				list.addLast(parametriClimatici);

			}
			reader.close();

			return list;

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Function to manage HashMap
	 * @param success String
	 * @param message String
	 * @return HashMap
	 */
	public static HashMap<String, String> arrayResponseCreate(String success, String message) {
		HashMap<String, String> arrayResponse = new HashMap<>();
		arrayResponse.put("success", success);
		arrayResponse.put("error", message);
		return arrayResponse;

	}

	/**
	 * Function to manage the writing and reading of the file
	 * @param parametriClimatici ParametriClimatici
	 * @return HashMap
	 */
	public static HashMap<String, String> inserisciParametriClimatici(ParametriClimatici parametriClimatici) {
		HashMap<String, String> arrayResponse = new HashMap<>();
		boolean responseWrite = false;
		if (!isAutenticato()) {
			arrayResponse = arrayResponseCreate("false", "Utente non autenticato");
			return arrayResponse;
		}
		if (CentroAree.cercaCentro(parametriClimatici.id_centro) == null) {
			arrayResponse = arrayResponseCreate("false", "Centro non trovato");
			return arrayResponse;
		}
		if (ClimateMonitor.cercaAreaGeografica(parametriClimatici.areaInteresse, 0).size() == 0) {
			arrayResponse = arrayResponseCreate("false", "Area non trovata");
			return arrayResponse;
		}
		Objects.requireNonNull(parametriClimatici, "parametriClimatici cannot be null");
		LinkedList<ParametriClimatici> list = readParametri();
		assert list != null;
		list.addLast(parametriClimatici);
		responseWrite = writeParametri(list);
		if (responseWrite) arrayResponse = arrayResponseCreate("true", "Parametri inseriti correttamente");
		else arrayResponse = arrayResponseCreate("false", "Errore nell'inserimento dei parametri");
		return arrayResponse;
	}

	/**
	 * Function to get current time and date
	 * @return String
	 */
	public static String dateNow() {
		LocalDateTime dateNow = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		return dateNow.format(formatter);
	}


	/**
	 * Enum for the Climate Category
	 */
	enum climateCategory {
		Vento,
		Umidita,
		Pressione,
		Temperatura,
		Precipitazioni,
		AltitudineGhiacciai,
		MassaGhiacciai,
	}
}
