package src.climatemonitoring;




import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Scanner;

public class ClimateMonitor {

    String name;
    String country;
    String country_id;
    String latitude;
    String longitude;
    static String filePathCoordinate = "data/CoordinateMonitoraggio.dati";


    public ClimateMonitor(String name, String country_id, String country, String latitude, String longitude) {
        this.name = name;
        this.country = country;
        this.country_id = country_id;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static HashMap<String, String> arrayResponseCreate(String success, String message) {
        HashMap<String, String> arrayResponse = new HashMap<>();
        arrayResponse.put("success", success);
        arrayResponse.put("error", message);
        return arrayResponse;

    }

    public static LinkedList<ClimateMonitor> readClimateCoordinates() {
        LinkedList<ClimateMonitor> list = new LinkedList<>();
        File file = new File(filePathCoordinate);

            if (!file.exists()){
                System.out.println("File non esistente");
                return list;
            }

            try {
                BufferedReader reader = new BufferedReader(new FileReader(filePathCoordinate));
                String[] field;
                while (true) {
                    int index = 0;
                    String readerLiner = reader.readLine();
                    if (readerLiner == null || readerLiner.equals("")) break;
                    field = readerLiner.split(",");
                    ClimateMonitor climate = new ClimateMonitor(field[index], field[index + 1], field[index + 2], field[index + 3], field[index + 4]);
                    list.addLast(climate);
                }
                reader.close();

                return list;

            } catch (IOException e) {
                e.printStackTrace();
            }
            return list;

    }

    public static LinkedList<ClimateMonitor> cercaAreaGeografica(String nomeArea, int scelta) {
        Objects.requireNonNull(nomeArea);
        nomeArea = nomeArea.replaceAll("\\s", "");
        LinkedList<ClimateMonitor> areaFoundList = new LinkedList<>();
        if (nomeArea.equals("")) {
            return areaFoundList;
        }
        LinkedList<ClimateMonitor> climateList = readClimateCoordinates();
        if (climateList.size() == 0) {
            return areaFoundList;
        }
        if (scelta == 0){
            for (ClimateMonitor climate : climateList) {
                if (climate.name.equals(nomeArea)) {
                    areaFoundList.addLast(new ClimateMonitor(climate.name, climate.country_id, climate.country, climate.latitude, climate.longitude));
                }
            }
        }else if (scelta == 1) {
            for (ClimateMonitor climate : climateList) {
                if (climate.country.equals(nomeArea)) {
                    areaFoundList.addLast(new ClimateMonitor(climate.name, climate.country_id, climate.country, climate.latitude, climate.longitude));
                }
            }
        }

        return areaFoundList;
    }

    public static LinkedList<ClimateMonitor> cercaAreaGeografica(String lat, String lon, int scelta) {
        Objects.requireNonNull(lat);
        Objects.requireNonNull(lon);
        LinkedList<ClimateMonitor> areaFoundList = new LinkedList<>();
        if (lat.equals("") || lon.equals("")) {
            return areaFoundList;
        }
        LinkedList<ClimateMonitor> climateList = readClimateCoordinates();
        if (climateList.size() == 0) {
            return areaFoundList;
        }
        if (scelta == 0){
            for (ClimateMonitor climate : climateList) {
                //replace double quotes with empty string
                climate.latitude = climate.latitude.replaceAll("\\s", "");
                climate.longitude = climate.longitude.replaceAll("\\s", "");
                //System.out.println("LatGet: " + climateLatArr[0] + " " + "LatPassata: " +lat + " " + "LongGet: " + climateLongArr[0] + " " + "LongPassata: " + lon);
                if(climate.latitude.equals(lat) && climate.longitude.equals(lon)){
                    areaFoundList.addLast(new ClimateMonitor(climate.name, climate.country_id, climate.country, climate.latitude, climate.longitude));
                }
            }
        } else {
            for (ClimateMonitor climate : climateList) {
                //System.out.println("LatGet: " + climateLatArr[0] + " " + "LatPassata: " +lat + " " + "LongGet: " + climateLongArr[0] + " " + "LongPassata: " + lon);
                String[] climateLongArr;
                climateLongArr = climate.longitude.split("\\.");
                climateLongArr[0] = climateLongArr[0].replaceAll("\\s", "");
                String[] climateLatArr;
                climateLatArr = climate.latitude.split("\\.");
                //System.out.println("LatGet: " + climateLatArr[0] + " " + "LatPassata: " +lat + " " + "LongGet: " + climateLongArr[0] + " " + "LongPassata: " + lon);
                if(climateLatArr[0].contains(lat) && climateLongArr[0].contains(lon) && climateLatArr[0].length() == lat.length() && climateLongArr[0].length() == lon.length()){
                    areaFoundList.addLast(new ClimateMonitor(climate.name, climate.country_id, climate.country, climate.latitude, climate.longitude));
            }}
        }

        return areaFoundList;
    }

    public static HashMap<String, String> visualizzaAreaGeografica(String nomeArea) {

        Objects.requireNonNull(nomeArea);
        LinkedList<ClimateMonitor> areaFoundList = cercaAreaGeografica(nomeArea,0);
        if (areaFoundList.size() == 0) {
            return arrayResponseCreate("false", "Area non trovata");
        }

        LinkedList<ParametriClimatici> listAll = ParametriClimatici.readParametri();
        int[][] idMonitoraggio = new int[2][7];
        String[] notesMonitoraggio = {"\n","\n", "\n", "\n", "\n", "\n", "\n"};
        int numeroVolteAreaTrovata = 0;
        for (ParametriClimatici parametriClimatici : listAll) {
            if (parametriClimatici.areaInteresse.equals(nomeArea)) {
                int index = ParametriClimatici.climateCategory.valueOf(parametriClimatici.climateCategoryToString).ordinal();
                idMonitoraggio[0][index] += 1;
                idMonitoraggio[1][index] += parametriClimatici.score;
                notesMonitoraggio[index] += String.valueOf(idMonitoraggio[0][index])+". "+parametriClimatici.notes +" \n";
                numeroVolteAreaTrovata += 1;
            }
        }

        if (numeroVolteAreaTrovata == 0) {
            return arrayResponseCreate("false", "Area non trovata");
        }

        for(int i = 0; i < 6; i++){
            if (idMonitoraggio[0][i] != 0){
                idMonitoraggio[1][i] = idMonitoraggio[1][i]/idMonitoraggio[0][i];
            }

        }
        String climateCategory ="";
        for(int i=0; i<6; i++){
            if(idMonitoraggio[0][i] != 0){
                climateCategory += "\n"+ParametriClimatici.climateCategory.values()[i]+": \n" +
                        "Trovata: "+String.valueOf(idMonitoraggio[0][i])+" volte \n"+
                        "Media: "+String.valueOf(idMonitoraggio[1][i])+" su 5 \n"+
                        "Note: " + notesMonitoraggio[i];
            }else{
                climateCategory +=  "\n"+ ParametriClimatici.climateCategory.values()[i]+": \n" +
                        "Non Trovata \n";
            }

        }

        return arrayResponseCreate("true",climateCategory);



    }

    public static void main(String[] args) {

        int risposta = 0;
        do{
            System.out.println("Benvenuto nel programma di monitoraggio climatico");
            System.out.println("Premi 1 per cercare un'area geografica tramite nome");
            System.out.println("Premi 2 per cercare un'area geografica tramite stato");
            System.out.println("Premi 3 per cercare un'area geografica tramite latitudine e longitudine Precisa");
            System.out.println("Premi 4 per cercare un'area geografica tramite latitudine e longitudine Approssimata");
            System.out.println("Premi 5 per visualizzare le statistiche di un'area geografica");
            System.out.println("Premi 0 per uscire");
            Scanner scanner = new Scanner(System.in);
            risposta = scanner.nextInt();
            switch (risposta) {
                case 1 -> {
                    System.out.println("Inserisci il nome dell'area geografica");
                    scanner = new Scanner(System.in);
                    String nomeArea = scanner.nextLine();
                    LinkedList<ClimateMonitor> areaTrovata = cercaAreaGeografica(nomeArea, 0);
                    if (areaTrovata.size() == 0) {
                        System.out.println("Area non trovata");
                    } else {
                        for (ClimateMonitor climateMonitor : areaTrovata) {
                            System.out.println("Nome: " + climateMonitor.name + " " + climateMonitor.country_id + " " + climateMonitor.country + " " + climateMonitor.latitude + " " + climateMonitor.longitude);
                        }
                    }
                }
                case 2 -> {
                    System.out.println("Inserisci il nome dell'area geografica");
                    scanner = new Scanner(System.in);
                    String nomeArea = scanner.nextLine();
                    LinkedList<ClimateMonitor> areaTrovata = cercaAreaGeografica(nomeArea, 1);
                    if (areaTrovata.size() == 0) {
                        System.out.println("Area non trovata");
                    } else {
                        for (ClimateMonitor climateMonitor : areaTrovata) {
                            System.out.println("Nome: " + climateMonitor.name + " " + climateMonitor.country_id + " " + climateMonitor.country + " " + climateMonitor.latitude + " " + climateMonitor.longitude);
                        }
                    }
                }
                case 3 -> {
                    System.out.println("Inserisci la latitudine");
                    scanner = new Scanner(System.in);
                    String lat = scanner.nextLine();
                    System.out.println("Inserisci la longitudine");
                    scanner = new Scanner(System.in);
                    String lon = scanner.nextLine();
                    LinkedList<ClimateMonitor> areaTrovata = cercaAreaGeografica(lat, lon, 0);
                    if (areaTrovata.size() == 0) {
                        System.out.println("Area non trovata");
                    } else {
                        for (ClimateMonitor climateMonitor : areaTrovata) {
                            System.out.println("Nome: " + climateMonitor.name + " " + climateMonitor.country_id + " " + climateMonitor.country + " " + climateMonitor.latitude + " " + climateMonitor.longitude);
                        }
                    }
                }
                case 4 -> {
                    System.out.println("Inserisci la latitudine");
                    scanner = new Scanner(System.in);
                    String lat = scanner.nextLine();
                    System.out.println("Inserisci la longitudine");
                    scanner = new Scanner(System.in);
                    String lon = scanner.nextLine();
                    LinkedList<ClimateMonitor> areaTrovata = cercaAreaGeografica(lat, lon, 1);
                    if (areaTrovata.size() == 0) {
                        System.out.println("Area non trovata");
                    } else {
                        for (ClimateMonitor climateMonitor : areaTrovata) {
                            System.out.println("Nome: " + climateMonitor.name + " " + climateMonitor.country_id + " " + climateMonitor.country + " " + climateMonitor.latitude + " " + climateMonitor.longitude);
                        }
                    }
                }
                case 5 -> {
                    System.out.println("Inserisci il nome dell'area geografica");
                    scanner = new Scanner(System.in);
                    String nomeArea = scanner.nextLine();
                    HashMap<String,String> areaTrovata = visualizzaAreaGeografica(nomeArea);
                    if (areaTrovata.get("success").equals("false")) {
                        System.out.println("Area non trovata");
                    } else {
                        System.out.println(areaTrovata.get("error"));
                    }
                }
            }

        }while (risposta != 0);
        System.out.println("Grazie per aver usato il programma di monitoraggio climatico");



        /*
        LinkedList<ClimateMonitor> areaTrovata = cercaAreaGeografica("42","1");
        for (ClimateMonitor climateMonitor : areaTrovata) {
            System.out.println("Nome: " + climateMonitor.name + " " + climateMonitor.country_id + " " + climateMonitor.country + " " + climateMonitor.latitude + " " + climateMonitor.longitude);
        }
        LinkedList<ClimateMonitor> list = readClimateCoordinates();
        for (ClimateMonitor climateMonitor : list) {
            System.out.println(climateMonitor.name);
        }

         */
    }
}

