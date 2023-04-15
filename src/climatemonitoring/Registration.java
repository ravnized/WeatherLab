package src.climatemonitoring;

import java.io.*;
import java.util.LinkedList;
import java.util.Objects;

public class Registration {
    String nome; String cognome; String codiceFiscale; String email; String userid; String password; int centroMonitoraggio;
    public Registration(String nome, String cognome, String codiceFiscale, String email, String userid, String password, int centroMonitoraggio) {
        this.nome = nome;
        this.cognome = cognome;
        this.codiceFiscale = codiceFiscale;
        this.email = email;
        this.userid = userid;
        this.password = password;
        this.centroMonitoraggio = centroMonitoraggio;
    }
    static String filepathOperatoriRegistrati = "data/OperatoriRegistrati.dati";

    static boolean writeRegistrati(LinkedList<Registration> list) {
        Objects.requireNonNull(list, "list cannot be null");
        if (list.isEmpty()) return false;
        File file;
        try{
            file = new File(filepathOperatoriRegistrati);
            if (!file.exists()) file.createNewFile();
            BufferedWriter writer = new BufferedWriter(new FileWriter(filepathOperatoriRegistrati));
            for (Registration r : list) {
                writer.write(r.nome + ";" + r.cognome + ";" + r.codiceFiscale + ";" + r.email + ";" + r.userid + ";" + r.password + ";" + r.centroMonitoraggio);
                writer.newLine();
            }
            writer.close();
            return true;
        }catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }



    public static boolean registrazione(Registration utenteRegistrato) {
        Objects.requireNonNull(utenteRegistrato, "utenteRegistrato cannot be null");

        LinkedList<Registration> list = readRegistrati();
        for (Registration r : list) {
            if (r.userid.equals(utenteRegistrato.userid)) return false;
        }
        list.addLast(utenteRegistrato);
        return writeRegistrati(list);
    }

    public static LinkedList<Registration> readRegistrati(){
        LinkedList<Registration> list = new LinkedList<>();
        File file = new File(filepathOperatoriRegistrati);
        if (!file.exists()) return list;
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

    public static void main(String[] args) {
        Registration utenteRegistrato = new Registration("Mario", "Rossi", "RSSMRA00A01B123C", "mariorossi@test.com", "mrossi", "password", 1);
        Registration.registrazione(utenteRegistrato);
    }


}
