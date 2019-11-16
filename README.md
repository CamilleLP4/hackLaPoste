# Projet Hack à La Poste

Auteur : Camille

Date de début : 06/11/19

Contexte : Projet Java 3 Hack a La Poste

Développé en Java

## Description générale

Le programme lit et traite 2 fichiers pour extraire les utilisateurs, les connexions hors des horaires et les connexions depuis des IP interdites

## Description des méthodes

La classe possède 2 attributs :
* *warning* Chaine de caractère privé
* *buffer* Memoire tampon d'ecriture privé (BufferedWriter)

### readLog()

La méthode prend un paramètre *fileName*, envoie des exceptions et retourne une liste de tableau de chaine de caractère.
La méthode lit le fichier *fileName* et stocke toutes les lignes du fichier dans une liste de tableau de chaine de caractère.

### readWarningLog()

La méthode ne prend pas de paramètre, envoie des exceptions et retourne une liste de tableau de chaine de caractère.
La méthode lit l'attribut *warning* et stocke toutes les lignes du fichier dans une liste de tableau de chaine de caractère.

### openWriteLog()

La méthode prend un paramètre *fileName*, envoie des exceptions et ne retourne rien.
La méthode prépare le fichier à écrire et remplit l'attribut *buffer*.

### readLog()

La méthode prend un paramètre *fileName*, envoie des exceptions et retourne une liste de tableau de chaine de caractère.
La méthode lit le fichier *fileName* et stocke toutes les lignes du fichier dans une liste.

### closeBuffer()

La méthode ne prend pas de paramètre, envoie des exceptions et ne retourne rien.
La méthode ferme le *buffer*.

### transformList()

La méthode prend un paramètre *list*, envoie des exceptions et retourne une liste de tableau de chaine de caractère.
La méthode transforme *list* en découpant les chaines de caractères contenu dans la liste à chaque *;*.

### tryHour()

La méthode prend un paramètre *heure* et retourne un boolean.
La méthode transforme *heure* en le découpant 2 fois aux *espaces* puis aux *:*.
Le résultat est transforme en nombre puis testé pour savoir si il est dans les horaires de travail ou non.

### write()

La méthode prend un paramètre *aInserer*, envoie des exceptions et ne retourne rien.
Elle ajoute a la mémoire tampon le contenu de *aInserer* puis un saut de ligne.

### warningLog()

La méthode prend un paramètre *prohibited*, envoie des exceptions et retourne un boolean.
Elle compare *prohibited* avec la liste de chaine de caractère *forbidden* et retourne *true* si elle correspond. 

### userLog()

La méthode prend un paramètre *fileName*, envoie des exceptions et ne retourne rien.
Elle exécute les différentes méthodes permettant la création et le remplissage de *utilisateurs.txt*.
La méthode fait :
* readLog()
* openWriteLog()
* Lit le résultat de readLog() et recupère le nom d'utilisateur
* Elimine les doublons de la liste d'utilisateur
* Ajoute chaque utilisateur de la liste sans doublon dans la memoire tampon
* closeBuffer()

### suspectLog()

La méthode prend deuc paramètre *fileName* et *warning*, envoie des exceptions et ne retourne rien.
Elle exécute les différentes méthodes permettant la création et le remplissage de *suspect.txt*.
La méthode fait :
* readLog()
* openWriteLog()
* Lit le résultat de readLog() remplit la memoire tampon avec les utilisateur se connectant hors horaires de travail
* Remplit une memoire de données si l'utilisateur se connecte depuis une IP interdite
* Ajoute le contenu de la memoire de données dans la memoire tampon
* closeBuffer()

## Notice

Executer un Run sur la Classe HackerTracker.java