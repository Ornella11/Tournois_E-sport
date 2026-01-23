import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;
import java.sql.Connection;

/**
 * Cette application sert d'interface graphique pour gérer des programmeurs et des projets via une base de données.
 */
public class Main extends Application {

    /**
     * Zone de droite qui contient la barre d'entrée (TextField + bouton) et la console.
     */
    private VBox affichageZone;

    /**
     * Objet qui contient les méthodes de "actionBDD"
     */
    private ActionBDD action;

    /**
     * Connexion à la base de données.
     */
    private Connection conn;

    /**
     * Cette zone de texte correspond à la console dans laquelle sera affiché le résultat de la requête.
     */
    private TextArea consoleArea;

    /**
     *  L'utilisateur saisit ses réponses dans cette zone.
     */
    private TextField inputField;

    /**
     * Le bouton "envoyer" permet de soumettre la requête à la base de donnée.
     */
    private Button btnEnvoyer;

    private PipedOutputStream pipedOut;

    /**
     * Point d'entrée JavaFX.
     *
     * @param stage fenêtre principale JavaFX
     */
    @Override
    public void start(Stage stage) {
        // Initialisation de l'accès à la BDD
        action = new ActionBDDImpl();
        conn = action.connectToDatabase();


        // Création du menu
        Label bienvenue = new Label("Bienvenue !");
        bienvenue.setStyle("-fx-font-size: 24pt; -fx-font-weight: bold; -fx-font-style: italic; -fx-text-fill:#8D927D;");

        Label choisirOption = new Label("Choisir une option :");
        choisirOption.setStyle("-fx-font-size: 14pt; -fx-text-fill: #888;");
        VBox.setMargin(choisirOption, new Insets(30, 0, 10, 0));

        // Création des boutons du menu
        Button btn1 = creerBoutonMenu("Afficher tous les programmeurs");
        Button btn2 = creerBoutonMenu("Afficher un programmeur (par ID)");
        Button btn3 = creerBoutonMenu("Supprimer un programmeur");
        Button btn4 = creerBoutonMenu("Ajouter un programmeur");
        Button btn5 = creerBoutonMenu("Modifier le salaire");
        Button btn6 = creerBoutonMenu("Afficher la liste des projets");
        Button btn7 = creerBoutonMenu("Ajouter un projet");
        Button btn8 = creerBoutonMenu("Assigner un projet");
        Button btn9 = creerBoutonMenu("Programmeurs sur le même projet");
        Button btn10 = creerBoutonMenu("Informations statistiques");
        Button btn11 = creerBoutonMenu("Quitter");


        // On associe chaque bouton à une méthode de la classe ActionBDD
        btn1.setOnAction(e -> runAction(() -> action.afficherProgrammeurs(conn)));
        btn2.setOnAction(e -> runAction(() -> action.affichageProgrammeurByID(conn)));
        btn3.setOnAction(e -> runAction(() -> action.supprimerProgrammeur(conn)));
        btn4.setOnAction(e -> runAction(() -> action.ajouterProgrammeur(conn)));
        btn5.setOnAction(e -> runAction(() -> action.modifierSalaire(conn)));
        btn6.setOnAction(e -> runAction(() -> action.ListeProjet(conn)));
        btn7.setOnAction(e -> runAction(() -> action.AjoutProjet(conn)));
        btn8.setOnAction(e -> runAction(() -> action.assignerProjet(conn)));
        btn9.setOnAction(e -> runAction(() -> action.afficherProgrammeursByProjet(conn)));
        btn10.setOnAction(e -> runAction(() -> action.stats(conn)));
        btn11.setOnAction(e -> Platform.exit());

        VBox menuBox = new VBox(
                8, bienvenue, choisirOption,
                btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn10, btn11
        );
        menuBox.setAlignment(Pos.TOP_CENTER);
        menuBox.setPadding(new Insets(30));
        menuBox.setStyle("-fx-background-color: white;");
        menuBox.setPrefWidth(400);

        // Zone de texte pour permettre à l'utilisateur de saisir son choix

        affichageZone = new VBox(10);
        affichageZone.setPadding(new Insets(20));
        affichageZone.setStyle("-fx-background-color: #f5f5f5;");

        inputField = new TextField();
        inputField.setPromptText("Saisir..");
        HBox.setHgrow(inputField, Priority.ALWAYS);
        btnEnvoyer = new Button("Envoyer");
        btnEnvoyer.setOnAction(e -> envoyerEntree());
        inputField.setOnAction(e -> envoyerEntree());

        btnEnvoyer.setStyle(
                "-fx-background-color: #8D927D;" +
                        "-fx-border-radius: 5;" +
                        "-fx-background-radius: 5;" +
                        "-fx-padding: 12;" +
                        "-fx-font-size: 11pt;" +
                        "-fx-text-fill: white;" +
                        "-fx-cursor: hand;"
        );

        HBox inputBar = new HBox(10, new Label("Entrée :"), inputField, btnEnvoyer);
        inputBar.setAlignment(Pos.CENTER_LEFT);
        inputBar.setPadding(new Insets(10));
        inputBar.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: #ddd;" +
                        "-fx-border-radius: 8;" +
                        "-fx-background-radius: 8;"
        );

        // Console dans laquelle le résultat sera affiché
        consoleArea = new TextArea();
        consoleArea.setEditable(false);
        consoleArea.setWrapText(true);

        VBox.setVgrow(consoleArea, Priority.ALWAYS);
        consoleArea.setMaxHeight(Double.MAX_VALUE);
        affichageZone.getChildren().addAll(inputBar, consoleArea);

        // Layout principal
        HBox mainLayout = new HBox(menuBox, affichageZone);
        HBox.setHgrow(affichageZone, Priority.ALWAYS);
        // Conteneur principal pour l'ensemble de l'interface
        Scene scene = new Scene(mainLayout, 1200, 700);

        stage.setTitle("Gestion des Programmeurs");
        stage.setScene(scene);
        stage.show();
        redirigerConsoleVersTextArea();
        // Recupère ce qui est écrit dans la zone de texte
        redirigerSystemInDepuisUI();

        System.out.println("Connexion réussie !");
    }

    /**
     * Exécute une action (souvent une action BDD) dans un thread séparé.
     * <p>
     * Objectifs :
     *  Cette méthode permet d'exécuter une méthode dans un therad séparé afin d'éviter de bloquer l'interface
     *   pendant le traitement des méthodes,d'empêcher le lancement de plusieurs actions en même temps
     */
     private void runAction(Runnable r) {
         consoleArea.clear();
         new Thread(() -> {
             try {
                 r.run();
             } catch (Exception ex) {
                 System.out.println("Erreur : " + ex.getMessage());
             }
         }).start();
     }

    /**
     * Envoie l'entrée utilisateur vers la console
     */
    private void envoyerEntree() {
        String text = inputField.getText();
        if (text == null) text = "";
        inputField.clear();

        // Affiche ce que l'utilisateur a tapé (utile pour suivre les interactions)
        System.out.println("> " + text);

        try {
            // Scanner lit ligne par ligne
            pipedOut.write((text + "\n").getBytes());
            pipedOut.flush();
        } catch (IOException e) {
            // Ic, on affiche un message simple pour l'utilisateur
            System.out.println("Erreur : Retour au menu !");
        }
    }

    /**
     * Redirige {@code System.out} et {@code System.err} vers la {@link #consoleArea}.
     * <p>
     * Chaque caractère écrit dans la console Java est ajouté à la TextArea.
     * </p>
     */
    private void redirigerConsoleVersTextArea() {
        PrintStream ps = new PrintStream(new OutputStream() {
            @Override
            public void write(int b) {
                // Mise à jour UI => Platform.runLater
                Platform.runLater(() -> consoleArea.appendText(String.valueOf((char) b)));
            }
        }, true);

        System.setOut(ps);
        System.setErr(ps);
    }

    /**
     * Redirige {@code} pour que les entrées viennent de l'interface graphique.
     * <p>
     * On crée un {@link PipedInputStream} et un {@link PipedOutputStream} connectés
     *
     */
    private void redirigerSystemInDepuisUI() {
        try {
            PipedInputStream pipedIn = new PipedInputStream();
            pipedOut = new PipedOutputStream(pipedIn);
            System.setIn(pipedIn);
        } catch (IOException e) {
            System.out.println("Erreur init System.in : " + e.getMessage());
        }
    }

    /**
     *On crée un bouton pour chaque option du menu
     *
     * @param texte texte affiché sur le bouton
     * @return bouton configuré
     */
    private Button creerBoutonMenu(String texte) {
        Button btn = new Button(texte);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setStyle(
                "-fx-background-color: #8D927D;" +
                        "-fx-border-radius: 5;" +
                        "-fx-background-radius: 5;" +
                        "-fx-padding: 12;" +
                        "-fx-font-size: 11pt;" +
                        "-fx-text-fill: white;" +
                        "-fx-cursor: hand;"
        );
        return btn;
    }

    /**
     * Méthode main classique : lance l'application JavaFX.
     *
     * @param args arguments de la ligne de commande (souvent inutilisés ici)
     */
    public static void main(String[] args) {
        launch(args);
    }
}
