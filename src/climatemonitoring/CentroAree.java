package src.climatemonitoring;

import java.io.*;
import java.util.LinkedList;
import java.util.Objects;

public class CentroAree extends Login {

    static String filePathCentri = "data/CentroMonitoraggio.dati";
    int id;
    String nome;
    String indirizzo;
    String citta;

    public CentroAree(int id, String nome, String indirizzo, String citta) {
        this.id = id;
        this.nome = nome;
        this.indirizzo = indirizzo;
        this.citta = citta;
    }

    static boolean writeCentri(LinkedList<CentroAree> list) {

        Objects.requireNonNull(list, "list cannot be null");
        if (list.isEmpty()) return false;
        File file;
        try {
            file = new File(filePathCentri);
            if (!file.exists()) file.createNewFile();
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

    public static LinkedList<CentroAree> readCentri() {
        LinkedList<CentroAree> list = new LinkedList<>();
        File file = new File(filePathCentri);
        if (!file.exists()) return list;
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


    public static boolean insertCentro(CentroAree centro) {
        if(!isAutenticato()){
            System.out.println("Non sei autenticato");
            return false;
        }
        Objects.requireNonNull(centro, "centro cannot be null");
        int sizeList = ClimateMonitor.cercaAreaGeografica(centro.citta,0).size();
        if(sizeList == 0) return false;
        LinkedList<CentroAree> list = readCentri();
        assert list != null;
        for (CentroAree centroAree : list) {
            if (centroAree.id == centro.id) return false;
        }
        list.addLast(centro);
        return writeCentri(list);
    }

    public static CentroAree cercaCentro(int id){
        LinkedList<CentroAree> list = readCentri();
        for (CentroAree c : list) {
            if (c.id == id) return c;
        }
        return null;
    }


    public static void main(String[] args){
        Login login = new Login("ravnized", "ravnized");
        login.login();
        CentroAree centro = new CentroAree(4, "Centro1", "Via Roma", "Roma");
        insertCentro(centro);
    }
}
