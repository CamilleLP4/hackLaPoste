import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HackerTracker {

    /**
     * Variables
     */
    private String warning;
    private BufferedWriter buffer;

    /**
     * Constructeur
     */
    public HackerTracker() {}

    /**
     * Lit le fichier fileName, le stock dans une liste et transforme la list
     * @param fileName le fichier connexion a lire
     * @return une liste de tableau de chaine de caractere transforme
     * @throws IOException
     */
    private List<String[]> readLog(String fileName) throws IOException {
        List<String> read = Files.readAllLines(Paths.get(fileName)); // lit toutes les lignes du fichier et les stocks dans une liste
        return this.transformList(read);
    }

    /**
     * Lit le fichiers warning et le stock dans une liste
     * @return une liste de chaine de caractere
     * @throws IOException
     */
    private List<String> readWarningLog() throws IOException {
        return Files.readAllLines(Paths.get(this.warning)); // lit toutes les lignes du fichier et les stocks dans une liste
    }

    /**
     * Prepare l'ecriture du fichier
     * @param fileName le fichier a ecrire
     * @return le buffer pour ecrire
     * @throws IOException
     */
    private void openWriteLog(String fileName) throws IOException {
        File file = new File(fileName);
        FileWriter fileWriter = new FileWriter(file);
        this.buffer = new BufferedWriter(fileWriter);
    }

    /**
     * Ferme le BufferedWriter
     * 
     * @throws IOException
     */
    private void closeBuffer() throws IOException {
        this.buffer.close();
    }

    /**
     * Transforme la liste en effectuant un split pour preparer le contenu du fichier
     * @param list le contenu brut du fichier
     * @return une liste de tableau de chaine de caractere split
     */
    private List<String[]> transformList(List<String> list) {
        List<String[]> listTransform = new ArrayList<>();
        for (String string : list) {
            listTransform.add(string.split(";"));
        }
        return listTransform;
    }

    /**
     * Travaille le parametre pour extraire l'heure et verifie si elle est dans les heures de travail
     * @param heure date complet a formater
     * @return true si l'heure est hors des heures de travail
     */
    private boolean tryHour(String heure) {
        String[] splitSpace = heure.split(" ");
        String[] splitHour = splitSpace[1].split(":");
        int hour = Integer.parseInt(splitHour[0]);
        return (8 > hour || hour > 19);
    }

    /**
     * Ecrit dans la memoire tampon
     * @param aInserer chaine a caracter a ajouter
     * @param buffer le buffer
     * @throws IOException
     */
    private void write(String aInserer) throws IOException {
        this.buffer.write(aInserer);
        this.buffer.newLine();
    }

    /**
     * Execute les differentes fonctions permettant de creer et remplir le fichier avec les identififants de connexion
     * @param fileName le fichier connexion a lire
     * @throws IOException
     */
    public void userLog(String fileName) throws IOException {
        List<String[]> list = this.readLog(fileName);
        String newFile = "utilisateurs.txt";
        this.openWriteLog(newFile);
        List<String> users = new ArrayList<>();
        for (String[] strings : list) {
            users.add(strings[1]);      
        }
        Set<String> set = new HashSet<>();
        set.addAll(users);
        for (String string : set) {
            this.write(string);
        }
        this.closeBuffer();
    }

    /**
     * Execute les differentes fonctions permettant de creer et remplir le fichier avec les connexions hors des horaires de travail et les connexion depuis des IP interdites
     * @param fileName le fichier connexion a lire
     * @param warning le fichier warning a lire
     * @throws IOException
     */
    public void suspectLog(String fileName, String warning) throws IOException {
        this.warning = warning;
        List<String[]> list = this.readLog(fileName);
        HashMap<String, List<String>> suspect = new HashMap<String, List<String>>();
        String newFile = "suspect.txt";
        this.openWriteLog(newFile);
        this.write("Connexion Hors des Heures de travail");
        for (String[] strings : list) {
            if (this.tryHour(strings[2])) {
                this.write("Utilisateurs : " + strings[1] + " IP : " + strings[0]);
            }
            if (this.warningLog(strings[0])) {
                if (suspect.containsKey(strings[1])) {
                    suspect.get(strings[1]).add(strings[0]);
                } else {
                    List<String> newList = new ArrayList<>();
                    newList.add(strings[0]);
                    suspect.put(strings[1], newList);
                }
                
            }
        }
        buffer.newLine();
        this.write("Connexion depuis IP interdites");
        for (String key : suspect.keySet()) {
            this.write(key + ";" + suspect.get(key).size());
        }
        this.closeBuffer();
    }

    /**
     * 
     * @param prohibited IP a comparer
     * @return true si prohibited correspond a une des IP warning
     * @throws IOException
     */
    public boolean warningLog(String prohibited) throws IOException {
        List<String> forbidden = this.readWarningLog();
        for (String stringforbidden : forbidden) {
            if (stringforbidden.equals(prohibited)) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) throws IOException {
        System.out.println("Debut de la recherche.\n");
        HackerTracker track = new HackerTracker();
        String fileName = "connexion.log";
        String warning = "warning.txt";
        track.userLog(fileName);
        track.suspectLog(fileName, warning);
        System.out.println("\nFin du programme.");
    }
}