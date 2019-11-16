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
    public HackerTracker() {
    }

    /**
     * Lit le fichier fileName, le stock dans une liste et transforme la list
     * 
     * @param fileName le fichier connexion a lire
     * @return une liste de tableau de chaine de caractere transforme
     * @throws IOException
     */
    private List<String[]> readLog(String fileName) throws IOException {
        List<String> read = Files.readAllLines(Paths.get(fileName)); // lit toutes les lignes du fichier et les stocks
                                                                     // dans une liste
        return this.transformList(read);
    }

    /**
     * Lit le fichiers warning et le stock dans une liste
     * 
     * @return une liste de chaine de caractere
     * @throws IOException
     */
    private List<String> readWarningLog() throws IOException {
        return Files.readAllLines(Paths.get(this.warning)); // lit toutes les lignes du fichier et les stocks dans une
                                                            // liste
    }

    /**
     * Prepare l'ecriture du fichier
     * 
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
        this.buffer.close(); // ferme le buffer
    }

    /**
     * Transforme la liste en effectuant un split pour preparer le contenu du
     * fichier
     * 
     * @param list le contenu brut du fichier
     * @return une liste de tableau de chaine de caractere split
     */
    private List<String[]> transformList(List<String> list) {
        List<String[]> listTransform = new ArrayList<>();
        for (String string : list) {
            listTransform.add(string.split(";")); // decoupe la liste au ;
        }
        return listTransform;
    }

    /**
     * Travaille le parametre pour extraire l'heure et verifie si elle est dans les
     * heures de travail
     * 
     * @param heure date complet a formater
     * @return true si l'heure est hors des heures de travail
     */
    private boolean tryHour(String heure) {
        String[] splitSpace = heure.split(" "); // decoupe la chaine de caractere aux espaces
        String[] splitHour = splitSpace[1].split(":"); // decoupe la chaine de caractere deja coupe au :
        int hour = Integer.parseInt(splitHour[0]); // transforme une partie de la chaine en int
        return (8 > hour || hour > 19);
    }

    /**
     * Ecrit dans la memoire tampon
     * 
     * @param aInserer chaine a caracter a ajouter
     * @param buffer   le buffer
     * @throws IOException
     */
    private void write(String aInserer) throws IOException {
        this.buffer.write(aInserer); // insere la chiane de caractere dans le buffer
        this.buffer.newLine(); // insere un saut de ligne
    }

    /**
     * Compare la chaine de caractere en parametre avec la list forbidden
     * 
     * @param prohibited IP a comparer
     * @return true si prohibited correspond a une des IP warning
     * @throws IOException
     */
    public boolean warningLog(String prohibited) throws IOException {
        List<String> forbidden = this.readWarningLog();
        for (String stringforbidden : forbidden) {
            if (stringforbidden.equals(prohibited)) { // si l'adresse IP fait partie de celles interdites
                return true;
            }
        }
        return false;
    }

    /**
     * Execute les differentes fonctions permettant de creer et remplir le fichier
     * avec les identififants de connexion
     * 
     * @param fileName le fichier connexion a lire
     * @throws IOException
     */
    public void userLog(String fileName) throws IOException {
        List<String[]> list = this.readLog(fileName);
        String newFile = "utilisateurs.txt";
        this.openWriteLog(newFile);
        List<String> users = new ArrayList<>();
        for (String[] strings : list) {
            users.add(strings[1]); // stocke dans une liste le nom d'utilisateur
        }
        Set<String> set = new HashSet<>();
        set.addAll(users); // elimine les doublons de la liste
        for (String string : set) {
            this.write(string); // parcours la liste sans doublon et l'insere dans le buffer
        }
        this.closeBuffer();
    }

    /**
     * Execute les differentes fonctions permettant de creer et remplir le fichier
     * avec les connexions hors des horaires de travail et les connexion depuis des
     * IP interdites
     * 
     * @param fileName le fichier connexion a lire
     * @param warning  le fichier warning a lire
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
                this.write("Utilisateurs : " + strings[1] + " IP : " + strings[0]); // insere dans le buffer
                                                                                    // l'utilisateur et l'ip si c'est
                                                                                    // hors horaire
            }
            if (this.warningLog(strings[0])) { // si l'ip correspond a une ip interdite
                if (suspect.containsKey(strings[1])) {
                    suspect.get(strings[1]).add(strings[0]); // si l'utilisateur est deja dans la HashMap on ajoute l'ip
                                                             // a la liste
                } else {
                    List<String> newList = new ArrayList<>();
                    newList.add(strings[0]);
                    suspect.put(strings[1], newList); // sinon on le crée dans la HashMap et on y ajoute la liste
                }

            }
        }
        this.write("");
        this.write("Connexion depuis IP interdites");
        for (String key : suspect.keySet()) {
            this.write(key + ";" + suspect.get(key).size()); // insere dans le buffer l'utilisateur et le taille de la
                                                             // liste des IP interdites utilise
        }
        this.closeBuffer();
    }

    public static void main(String[] args) throws IOException {
        HackerTracker track = new HackerTracker();
        String fileName = "connexion.log";
        String warning = "warning.txt";

        System.out.println("\nDebut de la recherche.");
        track.userLog(fileName);
        track.suspectLog(fileName, warning);
        System.out.println("Fin de la recherche.");
    }
}