package src.climatemonitoring;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Objects;

public class ParametriClimatici extends Login {

    enum climateCategory {
        Vento,
        Umidita,
        Pressione,
        Temperatura,
        Precipitazioni,
        AltitudineGhiacciai,
        MassaGhiacciai,
    }

    static String filepathParametriClimatici = "data/ParametriClimatici.dati";
    static String pattern = "dd/MM/yyyy HH:mm:ss";
    int score = 1;
    String notes = "";
    int id_centro;
    String areaInteresse;
    String dataRilevazione;

    String climateCategoryToString;
    public ParametriClimatici(int id_centro, String areaInteresse, String dataRilevazione, int climate, int score, String notes) {
        this.score = score;
        this.notes = notes;
        this.id_centro = id_centro;
        this.areaInteresse = areaInteresse;
        this.dataRilevazione = dataRilevazione;
        this.climateCategoryToString = climateCategory.values()[climate].toString();

    }

    static boolean writeParametri(LinkedList<ParametriClimatici> list) {
        Objects.requireNonNull(list, "list cannot be null");
        if (list.isEmpty()) return false;
        File file;
        try {
            file = new File(filepathParametriClimatici);
            if (!file.exists()) file.createNewFile();
            BufferedWriter writer = new BufferedWriter(new FileWriter(filepathParametriClimatici));
            for (ParametriClimatici r : list) {
                writer.write(r.id_centro + ";" + r.areaInteresse + ";" + r.dataRilevazione + ";" + r.climateCategoryToString + ";" + r.score + ";" + r.notes);
                writer.newLine();
            }
            writer.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static LinkedList<ParametriClimatici> readParametri() {
        DateFormat df = new SimpleDateFormat(pattern);
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

                ParametriClimatici parametriClimatici = new ParametriClimatici(Integer.parseInt(campiTotali[i]), campiTotali[i + 1], campiTotali[i + 2], climateCategory.valueOf(campiTotali[i+3]).ordinal(), Integer.parseInt(campiTotali[i + 4]), campiTotali[i + 5]);
                list.addLast(parametriClimatici);

            }
            reader.close();

            return list;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static HashMap<String, String> arrayResponseCreate(String success, String message) {
        HashMap<String, String> arrayResponse = new HashMap<>();
        arrayResponse.put("success", success);
        arrayResponse.put("error", message);
        return arrayResponse;

    }

    public static HashMap<String, String> inserisciParametriClimatici(ParametriClimatici parametriClimatici) {
        HashMap<String, String> arrayResponse = new HashMap<>();
        boolean responseWrite = false;
        if (!isAutenticato()) {
            arrayResponse = arrayResponseCreate("false", "Utente non autenticato");
            return arrayResponse;
        }
        if (CentroAree.cercaCentro(parametriClimatici.id_centro) == null){
            arrayResponse = arrayResponseCreate("false", "Centro non trovato");
            return arrayResponse;
        }
        if (ClimateMonitor.cercaAreaGeografica(parametriClimatici.areaInteresse,0).size() == 0){
            arrayResponse = arrayResponseCreate("false", "Area non trovata");
            return arrayResponse;
        }
        Objects.requireNonNull(parametriClimatici, "parametriClimatici cannot be null");
        LinkedList<ParametriClimatici> list = readParametri();
        list.addLast(parametriClimatici);
        responseWrite = writeParametri(list);
        if (responseWrite) arrayResponse = arrayResponseCreate("true", "Parametri inseriti correttamente");
        else arrayResponse = arrayResponseCreate("false", "Errore nell'inserimento dei parametri");
        return arrayResponse;
    }

    public static void main(String[] args) {
        Login login = new Login("ravnized", "ravnized");
        login.login();
        HashMap<String, String> response;
        LocalDateTime dateNow = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        String formatDateTime = dateNow.format(formatter);
        response = inserisciParametriClimatici(new ParametriClimatici(1, "Roma", formatDateTime, 3, 5, "minchia se piove"));
        System.out.println(response.get("error"));
    }
}
