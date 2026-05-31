README.txt
===========

1. Lancement de l'application
-----------------------------
Pour lancer l'application principale (interface console) :

1. Assurez-vous d'avoir Java 17 ou supérieur installé.
2. Compilez le projet avec votre IDE (IntelliJ, Eclipse, etc.) ou en ligne de commande.
3. Exécutez la classe `MenuPrincipal`.
   L'application ouvrira directement le menu principal et vous n'aurez pas à répondre à d'autres questions pour lancer le projet.

Pour lancer l'interface graphique avec JavaFX :

1. Téléchargez le SDK JavaFX correspondant à votre système d'exploitation et décompressez l'archive dans un emplacement de votre choix.
   Lien officiel : https://gluonhq.com/products/javafx
2. Ouvrez le projet dans IntelliJ.
3. Allez dans `File > Project Structure > Libraries`.
4. Cliquez sur le `+` puis `Java` et naviguez vers le dossier `lib` du SDK JavaFX.
5. Insérez un par un tous les fichiers `.jar`.
6. Cliquez sur `Apply` puis `Ok`.
7. Allez dans `Run > Edit Configurations`.
8. Cliquez sur `+` puis `Application`.
9. Sélectionner "Menu"
10. Cliquez sur `Modify options` et sélectionnez `Add VM options`.
11. Dans `VM options`, ajoutez la ligne suivante (adaptez le chemin à votre installation) :

   --module-path "C:\chemin-vers\javafx-sdk-25.0.2\lib" --add-modules javafx.controls,javafx.fxml,javafx.media,javafx.web --enable-native-access=javafx.graphics -Dfile.encoding=UTF-8

12. Cliquez sur `Apply` puis `Run`.
    L'interface graphique devrait alors se lancer.


