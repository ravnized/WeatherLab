package src.climatemonitoring;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Main Class
 */
public class ClimateMonitor {

    static String filePathCoordinate = "data/CoordinateMonitoraggio.dati";
    String name;
    String country;
    String country_id;
    String latitude;
    String longitude;

    /**
     * Constructor to build the object
     * @param name String
     * @param country_id String
     * @param country String
     * @param latitude String
     * @param longitude String
     */
    public ClimateMonitor(String name, String country_id, String country, String latitude, String longitude) {
        this.name = name;
        this.country = country;
        this.country_id = country_id;
        this.latitude = latitude;
        this.longitude = longitude;
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
     * Function utility for the creation of file
     * @param filePath String
     *                 Example: data/CoordinateMonitoraggio.dati
     *
     */
    public static boolean createFile(String filePath) {
        File directory = new File(System.getProperty("user.dir")+ "/data");
        File file = new File(System.getProperty("user.dir")+"/"+filePath);
        int fileCreated = 0;
        int directoryCreated = 0;
        try {
        if (!directory.exists()){
            if(directory.mkdir())directoryCreated = 1;
        }
        if (!file.exists()) {

                if(file.createNewFile())
                    fileCreated = 1;
                    //System.out.println("File creato");

            }
        }
        catch (IOException e) {
            System.out.println("Errore nella creazione del file");
            return false;
        }
        return true;
    }


    /**
     * Function to read the coordinate
     * @return LinkedList
     */
    public static LinkedList<ClimateMonitor> readClimateCoordinates() {
        LinkedList<ClimateMonitor> list = new LinkedList<>();
        boolean fileCreated = ClimateMonitor.createFile(filePathCoordinate);
        if (!fileCreated) return list;

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

    /**
     * Function to search Area Geografica for nome switch between precise and lazy
     * @param nomeArea String
     * @param scelta int
     * @return LinkedList of the areaFound
     */
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
        if (scelta == 0) {
            for (ClimateMonitor climate : climateList) {
                if (climate.name.equals(nomeArea)) {
                    areaFoundList.addLast(new ClimateMonitor(climate.name, climate.country_id, climate.country, climate.latitude, climate.longitude));
                }
            }
        } else if (scelta == 1) {
            for (ClimateMonitor climate : climateList) {
                if (climate.country.equals(nomeArea)) {
                    areaFoundList.addLast(new ClimateMonitor(climate.name, climate.country_id, climate.country, climate.latitude, climate.longitude));
                }
            }
        }

        return areaFoundList;
    }

    /**
     * Function to Search AreaGeografica by lat and lon it can be precise or lazy
     * @param lat String
     * @param lon String
     * @param scelta int
     * @return LinkedList of the areaFound
     */
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
        if (scelta == 0) {
            for (ClimateMonitor climate : climateList) {
                //replace double quotes with empty string
                climate.latitude = climate.latitude.replaceAll("\\s", "");
                climate.longitude = climate.longitude.replaceAll("\\s", "");
                //System.out.println("LatGet: " + climateLatArr[0] + " " + "LatPassata: " +lat + " " + "LongGet: " + climateLongArr[0] + " " + "LongPassata: " + lon);
                if (climate.latitude.equals(lat) && climate.longitude.equals(lon)) {
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
                if (climateLatArr[0].contains(lat) && climateLongArr[0].contains(lon) && climateLatArr[0].length() == lat.length() && climateLongArr[0].length() == lon.length()) {
                    areaFoundList.addLast(new ClimateMonitor(climate.name, climate.country_id, climate.country, climate.latitude, climate.longitude));
                }
            }
        }

        return areaFoundList;
    }

    /**
     * Function to create a Summary of the Area passed by Name
     * @param nomeArea String
     * @return HashMap
     */
    public static HashMap<String, String> visualizzaAreaGeografica(String nomeArea) {

        Objects.requireNonNull(nomeArea);
        LinkedList<ClimateMonitor> areaFoundList = cercaAreaGeografica(nomeArea, 0);
        if (areaFoundList.size() == 0) {
            return arrayResponseCreate("false", "Area non trovata");
        }

        LinkedList<ParametriClimatici> listAll = ParametriClimatici.readParametri();
        int[][] idMonitoraggio = new int[2][7];
        String[] notesMonitoraggio = {"\n", "\n", "\n", "\n", "\n", "\n", "\n"};
        int numeroVolteAreaTrovata = 0;
        assert listAll != null;
        for (ParametriClimatici parametriClimatici : listAll) {
            if (parametriClimatici.areaInteresse.equals(nomeArea)) {
                int index = ParametriClimatici.climateCategory.valueOf(parametriClimatici.climateCategoryToString).ordinal();
                idMonitoraggio[0][index] += 1;
                idMonitoraggio[1][index] += parametriClimatici.score;
                notesMonitoraggio[index] += (!parametriClimatici.notes.equals("_") ? idMonitoraggio[0][index] + ". " + parametriClimatici.notes + " \n" : "");
                numeroVolteAreaTrovata += 1;
            }
        }

        if (numeroVolteAreaTrovata == 0) {
            return arrayResponseCreate("false", "Area non trovata");
        }

        for (int i = 0; i < 6; i++) {
            if (idMonitoraggio[0][i] != 0) {
                idMonitoraggio[1][i] = idMonitoraggio[1][i] / idMonitoraggio[0][i];
            }

        }
        String climateCategory = "";
        for (int i = 0; i < 6; i++) {
            if (idMonitoraggio[0][i] != 0) {
                climateCategory += "\n" + ParametriClimatici.climateCategory.values()[i] + ": \n" +
                        "Trovata: " + idMonitoraggio[0][i] + " volte \n" +
                        "Media: " + idMonitoraggio[1][i] + " su 5 \n" +
                        "Note: " + notesMonitoraggio[i];
            } else {
                climateCategory += "\n" + ParametriClimatici.climateCategory.values()[i] + ": \n" +
                        "Non Trovata \n";
            }

        }

        return arrayResponseCreate("true", climateCategory);

    }

    /**
     * Function that visualize the menu for every user , it gives fewer functions compared to the logged one
     * @param logged boolean
     */
    public static void menuUtente(boolean logged) {
        int risposta;
        do {
            System.out.println("Benvenuto nel programma di monitoraggio climatico");
            System.out.println("Premi 1 per cercare un'area geografica tramite nome");
            System.out.println("Premi 2 per cercare un'area geografica tramite stato");
            System.out.println("Premi 3 per cercare un'area geografica tramite latitudine e longitudine Precisa");
            System.out.println("Premi 4 per cercare un'area geografica tramite latitudine e longitudine Approssimata");
            System.out.println("Premi 5 per visualizzare le statistiche di un'area geografica");
            if (logged) {
                System.out.println("Premi 6 per creare un centro di monitoraggio");
                System.out.println("Premi 7 per inserire/aggiornare i parametri climatici");
            }
            System.out.println("Premi 0 per ritornare al menu precedente");
            Scanner scanner = new Scanner(System.in);
            risposta = tryScannerInt(scanner);
            switch (risposta) {
                case 1 -> {
                    System.out.println("Inserisci il nome dell'area geografica");
                    scanner = new Scanner(System.in);
                    String nomeArea =retryWhenEmpty("Inserisci il nome dell'area geografica", scanner);
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
                    String nomeArea = retryWhenEmpty("Inserisci il nome dell'area geografica", scanner);
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
                    String lat = retryWhenEmpty("Inserisci la latitudine", scanner);
                    System.out.println("Inserisci la longitudine");
                    scanner = new Scanner(System.in);
                    String lon = retryWhenEmpty("Inserisci la longitudine", scanner);
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
                    String lat = retryWhenEmpty("Inserisci la latitudine", scanner);
                    System.out.println("Inserisci la longitudine");
                    scanner = new Scanner(System.in);
                    String lon = retryWhenEmpty("Inserisci la longitudine", scanner);
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
                    String nomeArea = retryWhenEmpty("Inserisci il nome dell'area geografica", scanner);
                    HashMap<String, String> areaTrovata = visualizzaAreaGeografica(nomeArea);
                    if (areaTrovata.get("success").equals("false")) {
                        System.out.println("Area non trovata");
                    } else {
                        System.out.println(areaTrovata.get("error"));
                    }
                }
                case 6 -> {
                    if (logged) {
                        System.out.println("Inserisci il nome del centro");
                        scanner = new Scanner(System.in);
                        String nomeCentro = retryWhenEmpty("Inserisci il nome del centro", scanner);
                        System.out.println("Inserisci l'indirizzo del centro");
                        scanner = new Scanner(System.in);
                        String indirizzoCentro = retryWhenEmpty("Inserisci l'indirizzo del centro", scanner);
                        System.out.println("Inserisci la citta");
                        scanner = new Scanner(System.in);
                        String citta = retryWhenEmpty("Inserisci la citta", scanner);
                        CentroAree centroAree = new CentroAree(0, nomeCentro, indirizzoCentro, citta);
                        boolean centroResult = CentroAree.insertCentro(centroAree);
                        if (centroResult) {
                            System.out.println("Centro inserito correttamente");
                        } else {
                            System.out.println("Errore nell'inserimento del centro");
                        }
                    } else {
                        System.out.println("Non sei loggato");
                    }
                }
                case 7 -> {
                    if (logged) {
                        HashMap<String, String> response;
                        String date = ParametriClimatici.dateNow();
                        System.out.println("Inserisci l'id del centro");
                        scanner = new Scanner(System.in);
                        int idCentro = tryScannerInt(scanner);
                        System.out.println("Inserisci l'area d'interesse");
                        scanner = new Scanner(System.in);
                        String area = retryWhenEmpty("Inserisci l'area d'interesse", scanner);
                        System.out.println("Inserisci il numero corrispondente al tipo di dato che vuoi inserire");
                        System.out.println("1 - Vento: Velocità del vento (km/h), suddivisa in fasce ");
                        System.out.println("2 - Umidita: % di Umidità, suddivisa in fasce'");
                        System.out.println("3 - Pressione: In hPa, suddivisa in fasce ");
                        System.out.println("4 - Temperatura: In C°, suddivisa in fasce");
                        System.out.println("5 - Precipitazioni: In mm di pioggia, suddivisa in fasce");
                        System.out.println("6 - Altitudine dei ghiacciai: In m, suddivisa in piogge");
                        System.out.println("7 - Massa dei ghiacciai: In kg, suddivisa in fasce");
                        scanner = new Scanner(System.in);
                        int tipoDato = tryScannerInt(scanner);
                        while (tipoDato < 1 || tipoDato > 7) {
                            System.out.println("Numero Non corrispondente");
                            System.out.println("Inserisci il numero corrispondente al tipo di dato che vuoi inserire");
                            System.out.println("1 - Vento: Velocità del vento (km/h), suddivisa in fasce ");
                            System.out.println("2 - Umidita: % di Umidità, suddivisa in fasce'");
                            System.out.println("3 - Pressione: In hPa, suddivisa in fasce ");
                            System.out.println("4 - Temperatura: In C°, suddivisa in fasce");
                            System.out.println("5 - Precipitazioni: In mm di pioggia, suddivisa in fasce");
                            System.out.println("6 - Altitudine dei ghiacciai: In m, suddivisa in piogge");
                            System.out.println("7 - Massa dei ghiacciai: In kg, suddivisa in fasce");
                            scanner = new Scanner(System.in);
                            tipoDato = tryScannerInt(scanner);
                        }
                        System.out.println("Inserisci lo score per il parametro scelto");
                        scanner = new Scanner(System.in);
                        int score = tryScannerInt(scanner);
                        while (score > 5 || score < 1) {
                            System.out.println("Lo score inserito deve rientrare tra 1 e 5 compresi");
                            scanner = new Scanner(System.in);
                            score = tryScannerInt(scanner);
                        }
                        System.out.println("Inserisci le note");
                        scanner = new Scanner(System.in);
                        String note = scanner.nextLine();
                        while (note.length() > 256) {
                            System.out.println("Le note non possono superare i 256 caratteri");
                            System.out.println("Inserisci le note");
                            scanner = new Scanner(System.in);
                            note = scanner.nextLine();
                        }
                        response = ParametriClimatici.inserisciParametriClimatici(new ParametriClimatici(idCentro, area, date, tipoDato - 1, score, note));
                        if (response.get("success").equals("true")) {
                            System.out.println("Parametri inseriti correttamente");

                        }
                    }
                }
            }

        } while (risposta != 0);
    }

    /**
     * Function to insert a centro area if someone during the registration use a idCentro not found in the file
     * @param idCentro int
     * @param autenticato boolean
     */
    public static void insertCentroArea(int idCentro, boolean autenticato) {
        if (!autenticato) {
            System.out.println("Non sei loggato");
            menuUtente(false);
        }
        Scanner scanner = new Scanner(System.in);
        System.out.println("Inserisci il nome del centro");
        String nomeCentro = retryWhenEmpty("Inserisci il nome del centro", scanner);
        System.out.println("Inserisci l'indirizzo del centro");
        String indirizzoCentro = retryWhenEmpty("Inserisci l'indirizzo del centro", scanner);
        System.out.println("Inserisci la citta");
        String citta = retryWhenEmpty("Inserisci la citta", scanner);
        CentroAree centroAree = new CentroAree(idCentro, nomeCentro, indirizzoCentro, citta);
        boolean centroResult = CentroAree.insertCentro(centroAree);
        if (centroResult) {
            System.out.println("Centro inserito correttamente");
            menuUtente(autenticato);
        } else {
            System.out.println("Errore nell'inserimento del centro");
        }
    }

    /**
     * Utility Function to manage the Scanner InputMismatchException
     * @param scanner Scanner
     * @return int
     */
    public static int tryScannerInt(Scanner scanner) {
        int risposta = -1;
        while(risposta == -1) {
            try {
                scanner = new Scanner(System.in);
                risposta = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Formato Sbagliato Inserire un numero");
            }
        }
            return risposta;
    }

    /**
     * Utility function to do a cycle when the user doesn't enter an input
     */
    public static String retryWhenEmpty(String message, Scanner scanner) {
        String readLine = scanner.nextLine();
        while (readLine.isEmpty()) {
            System.out.println("Non puoi lasciare vuoto questo campo");
            System.out.println(message);
            scanner = new Scanner(System.in);
            readLine = scanner.nextLine();
        }
        return readLine;
    }

    /**
     * First menu that user see
     * @param login Login
     */
    public static void menuLogin(Login login) {
        int risposta;
        do {
            System.out.println("Benvenuto nel programma di monitoraggio climatico");
            System.out.println("Premi 1 per registrarti");
            System.out.println("Premi 2 per effettuare il login");
            System.out.println("Premi 3 per continuare come guest");
            System.out.println("Premi 0 per uscire");
            Scanner scanner = new Scanner(System.in);
            risposta = tryScannerInt(scanner);
            switch (risposta) {
                case 1 -> {
                    boolean insertCentro = false;
                    System.out.println("Inserisci il tuo nome");
                    scanner = new Scanner(System.in);
                    String nome = retryWhenEmpty("Inserisci il tuo nome", scanner);
                    System.out.println("Inserisci il tuo cognome");
                    scanner = new Scanner(System.in);
                    String cognome = retryWhenEmpty("Inserisci il tuo cognome", scanner);
                    System.out.println("Inserisci il tuo username");
                    scanner = new Scanner(System.in);
                    String username = retryWhenEmpty("Inserisci il tuo username", scanner);
                    System.out.println("Inserisci la tua password");
                    scanner = new Scanner(System.in);
                    String password = retryWhenEmpty("Inserisci la tua password", scanner);
                    System.out.println("Inserisci la tua email");
                    scanner = new Scanner(System.in);
                    String email = retryWhenEmpty("Inserisci la tua email", scanner);
                    System.out.println("Inserisci il tuo codice fiscale");
                    scanner = new Scanner(System.in);
                    String codiceFiscale = retryWhenEmpty("Inserisci il tuo codice fiscale", scanner);
                    System.out.println("Inserisci il tuo centro di appartenenza");
                    scanner = new Scanner(System.in);
                    int centro = tryScannerInt(scanner);




                    if (CentroAree.cercaCentro(centro) == null) {
                        System.out.println("Centro non trovato");
                        insertCentro = true;
                    } else {
                        System.out.println("Centro Trovato");
                    }
                    Registration registration = new Registration(nome, cognome, codiceFiscale, email, username, password, centro);
                    boolean registrationResult = Registration.registrazione(registration);
                    if (registrationResult) {
                        System.out.println("Registrazione effettuata con successo");
                    } else {
                        System.out.println("Registrazione fallita");
                    }
                    if (insertCentro) {
                        login = new Login(username, password);
                        boolean autenticato = login.login();
                        insertCentroArea(centro, autenticato);
                    }
                }
                case 2 -> {
                    System.out.println("Inserisci il tuo username");
                    scanner = new Scanner(System.in);
                    String username = retryWhenEmpty("Inserisci il tuo username", scanner);
                    System.out.println("Inserisci la tua password");
                    scanner = new Scanner(System.in);
                    String password = retryWhenEmpty("Inserisci la tua password", scanner);
                    login = new Login(username, password);
                    boolean logged = login.login();
                    if (logged) {
                        System.out.println("Login effettuato con successo");
                        menuUtente(logged);
                    } else {
                        System.out.println("Login fallito");
                    }
                }
                case 3 -> {
                    System.out.println("Continui come guest");
                    menuUtente(false);
                }
            }

        } while (risposta != 0);
    }

    /**
     * Main
     * @param args String[]
     */
    public static void main(String[] args) {
        Login login = new Login();
        menuLogin(login);
    }
}

