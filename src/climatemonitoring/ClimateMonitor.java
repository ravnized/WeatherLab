package src.climatemonitoring;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Array;
import java.util.LinkedList;
import java.util.Objects;

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


    public static @NotNull LinkedList<ClimateMonitor> cercaAreaGeografica(String nomeArea) {
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

        for (ClimateMonitor climate : climateList) {
            String climateFull = climate.name + climate.country_id + climate.country;
            if (climateFull.contains(nomeArea)) {
                areaFoundList.addLast(new ClimateMonitor(climate.name, climate.country_id, climate.country, climate.latitude, climate.longitude));
            }
        }
        return areaFoundList;
    }
    public static @NotNull LinkedList<ClimateMonitor> cercaAreaGeografica(String lat, String lon) {
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

        for (ClimateMonitor climate : climateList) {
            String[] climateLongArr;
            climateLongArr = climate.longitude.split("\\.");
            climateLongArr[0] = climateLongArr[0].replaceAll("\\s", "");
            String[] climateLatArr;
            climateLatArr = climate.latitude.split("\\.");
            climateLatArr[0]=climateLatArr[0].substring(1);
            //System.out.println("LatGet: " + climateLatArr[0] + " " + "LatPassata: " +lat + " " + "LongGet: " + climateLongArr[0] + " " + "LongPassata: " + lon);
            if(climateLatArr[0].equals(lat) && climateLongArr[0].equals(lon)){
                areaFoundList.addLast(new ClimateMonitor(climate.name, climate.country_id, climate.country, climate.latitude, climate.longitude));
            }
        }
        return areaFoundList;
    }

    boolean visualizzaAreaGeografica() {
        return false;
    }

    boolean registrazione() {
        return false;
    }

    boolean registraCentroAree() {
        return false;
    }

    boolean inserisciParametriClimatici() {
        return false;
    }

    public static void main(String[] args) {
        System.out.println("Hello world");

        LinkedList<ClimateMonitor> areaTrovata = cercaAreaGeografica("42","1");
        for (ClimateMonitor climateMonitor : areaTrovata) {
            System.out.println("Nome: " + climateMonitor.name + " " + climateMonitor.country_id + " " + climateMonitor.country + " " + climateMonitor.latitude + " " + climateMonitor.longitude);
        }
        /*
        LinkedList<ClimateMonitor> list = readClimateCoordinates();
        for (ClimateMonitor climateMonitor : list) {
            System.out.println(climateMonitor.name);
        }

         */
    }
}