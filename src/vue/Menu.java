package vue;

import dao.ConsultationDao;
import dao.JoueurDao;
import dao.MatchDao;
import dao.TournoiDAO;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.*;
import java.sql.SQLException;

public class Menu extends Application {

    private VBox affichageZone;
    private TextArea consoleArea;
    private TextField inputField;
    private Button btnEnvoyer;
    private PipedOutputStream pipedOut;

    private JoueurDao joueur = new JoueurDao();
    private TournoiDAO tournoi = new TournoiDAO();
    private ConsultationDao consultation = new ConsultationDao();
    private MatchDao match = new MatchDao();

    public Menu() throws SQLException {
    }

    public void Menu() throws SQLException {
    }

    @Override
    public void start(Stage stage) {

        // ---- MENU PRINCIPAL ----
        Label bienvenue = new Label("Esport Manager");
        bienvenue.setStyle("-fx-font-size: 24pt; -fx-font-weight: bold; -fx-font-style: italic; -fx-text-fill:#8D927D;");

        Label choisirOption = new Label("Choisir une option :");
        choisirOption.setStyle("-fx-font-size: 14pt; -fx-text-fill: #888;");
        VBox.setMargin(choisirOption, new Insets(30, 0, 10, 0));

        // ---- BOUTONS MENU PRINCIPAL ----
        Button btnJoueurs   = creerBoutonMenu("1. Gestion des joueurs");
        Button btnEquipes   = creerBoutonMenu("2. Gestion des équipes");
        Button btnTournois  = creerBoutonMenu("3. Gestion des tournois");
        Button btnMatch     = creerBoutonMenu("4. Saisir le résultat d'un match");
        Button btnClassement= creerBoutonMenu("5. Classement d'un tournoi");
        Button btnStats     = creerBoutonMenu("6. Statistiques d'un joueur");
        Button btnQuitter   = creerBoutonMenu("7. Quitter");

        // ---- SOUS-MENUS ----
        VBox menuPrincipal = new VBox(8, bienvenue, choisirOption,
                btnJoueurs, btnEquipes, btnTournois, btnMatch, btnClassement, btnStats, btnQuitter);

        VBox menuJoueurs = creerSousMenu("Gestion des joueurs",
                "1. Ajouter un joueur",
                "2. Lister les joueurs",
                "3. Rechercher par pseudo",
                "4. Modifier un joueur",
                "5. Supprimer un joueur",
                "6. Retour");

        VBox menuEquipes = creerSousMenu("Gestion des équipes",
                "1. Inscrire une équipe à un tournoi",
                "2. Statistiques d'une équipe",
                "3. Retour");

        VBox menuTournois = creerSousMenu("Gestion des tournois",
                "1. Créer un tournoi",
                "2. Classement d'un tournoi",
                "3. Retour");

        VBox menuStats = creerSousMenu("Statistiques",
                "1. Saisir les statistiques d'un joueur",
                "2. Afficher les statistiques d'un match",
                "3. Retour");

        // ---- CONTENEUR DES MENUS (stackés) ----
        StackPane menuContainer = new StackPane(menuPrincipal, menuJoueurs, menuEquipes, menuTournois, menuStats);
        menuPrincipal.setVisible(true);
        menuJoueurs.setVisible(false);
        menuEquipes.setVisible(false);
        menuTournois.setVisible(false);
        menuStats.setVisible(false);

        for (VBox menu : new VBox[]{menuPrincipal, menuJoueurs, menuEquipes, menuTournois, menuStats}) {
            menu.setAlignment(Pos.TOP_CENTER);
            menu.setPadding(new Insets(30));
            menu.setStyle("-fx-background-color: white;");
            menu.setPrefWidth(400);
        }

        // ---- ACTIONS BOUTONS MENU PRINCIPAL ----
        btnJoueurs.setOnAction(e -> {
            menuPrincipal.setVisible(false);
            menuJoueurs.setVisible(true);
        });
        btnEquipes.setOnAction(e -> {
            menuPrincipal.setVisible(false);
            menuEquipes.setVisible(true);
        });
        btnTournois.setOnAction(e -> {
            menuPrincipal.setVisible(false);
            menuTournois.setVisible(true);
        });
        btnMatch.setOnAction(e -> runAction(() -> match.saisirScoreMatch()));
        btnClassement.setOnAction(e -> runAction(() -> {
            try { consultation.afficherClassementTournoi(); } catch (Exception ex) { System.out.println("Erreur : " + ex.getMessage()); }
        }));
        btnStats.setOnAction(e -> {
            menuPrincipal.setVisible(false);
            menuStats.setVisible(true);
        });
        btnQuitter.setOnAction(e -> Platform.exit());

        // ---- ACTIONS SOUS-MENU JOUEURS ----
        bouton(menuJoueurs, 0).setOnAction(e -> runAction(() -> {
            try {
                joueur.ajouterJoueur();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }));
        bouton(menuJoueurs, 1).setOnAction(e -> runAction(() -> {
            try { joueur.listerJoueurs(); } catch (Exception ex) { System.out.println("Erreur : " + ex.getMessage()); }
        }));
        bouton(menuJoueurs, 2).setOnAction(e -> runAction(() -> {
            try { consultation.rechercherParMotCle(); } catch (Exception ex) { System.out.println("Erreur : " + ex.getMessage()); }
        }));
        bouton(menuJoueurs, 3).setOnAction(e -> runAction(() -> {
            try {
                joueur.modifierJoueur();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }));
        bouton(menuJoueurs, 4).setOnAction(e -> runAction(() -> {
            try {
                joueur.supprimerJoueur();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }));
        bouton(menuJoueurs, 5).setOnAction(e -> retourMenu(menuJoueurs, menuPrincipal));

        // ---- ACTIONS SOUS-MENU EQUIPES ----
        bouton(menuEquipes, 0).setOnAction(e -> runAction(() -> tournoi.inscriptionEquipe()));
        bouton(menuEquipes, 1).setOnAction(e -> runAction(() -> {
            try { consultation.afficherStatsEquipe(); } catch (Exception ex) { System.out.println("Erreur : " + ex.getMessage()); }
        }));
        bouton(menuEquipes, 2).setOnAction(e -> retourMenu(menuEquipes, menuPrincipal));

        // ---- ACTIONS SOUS-MENU TOURNOIS ----
        bouton(menuTournois, 0).setOnAction(e -> runAction(() -> tournoi.ajouterTournoi()));
        bouton(menuTournois, 1).setOnAction(e -> runAction(() -> {
            try { consultation.afficherClassementTournoi(); } catch (Exception ex) { System.out.println("Erreur : " + ex.getMessage()); }
        }));
        bouton(menuTournois, 2).setOnAction(e -> retourMenu(menuTournois, menuPrincipal));

        // ---- ACTIONS SOUS-MENU STATS ----
        bouton(menuStats, 0).setOnAction(e -> runAction(() -> match.saisirStatistiqueIndividuelle()));
        bouton(menuStats, 1).setOnAction(e -> runAction(() -> match.afficherStatistiquesMatch()));
        bouton(menuStats, 2).setOnAction(e -> retourMenu(menuStats, menuPrincipal));

        // ---- ZONE CONSOLE ----
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

        consoleArea = new TextArea();
        consoleArea.setEditable(false);
        consoleArea.setWrapText(true);
        VBox.setVgrow(consoleArea, Priority.ALWAYS);
        consoleArea.setMaxHeight(Double.MAX_VALUE);

        affichageZone.getChildren().addAll(inputBar, consoleArea);

        // ---- LAYOUT PRINCIPAL ----
        HBox mainLayout = new HBox(menuContainer, affichageZone);
        HBox.setHgrow(affichageZone, Priority.ALWAYS);

        Scene scene = new Scene(mainLayout, 1200, 700);
        stage.setTitle("Esport Manager");
        stage.setScene(scene);
        stage.show();

        redirigerConsoleVersTextArea();
        redirigerSystemInDepuisUI();

        System.out.println("Bienvenue sur Esport Manager !");
    }

    // ---- UTILITAIRES ----

    private void retourMenu(VBox menuActuel, VBox menuCible) {
        consoleArea.clear();
        menuActuel.setVisible(false);
        menuCible.setVisible(true);
    }

    private Button bouton(VBox menu, int index) {
        // Les boutons commencent à l'index 1 (index 0 = label titre)
        return (Button) menu.getChildren().get(index + 1);
    }

    private VBox creerSousMenu(String titre, String... boutons) {
        Label label = new Label(titre);
        label.setStyle("-fx-font-size: 18pt; -fx-font-weight: bold; -fx-text-fill: #8D927D;");
        VBox box = new VBox(8, label);
        for (String texte : boutons) {
            box.getChildren().add(creerBoutonMenu(texte));
        }
        return box;
    }

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

    private void envoyerEntree() {
        String text = inputField.getText();
        if (text == null) text = "";
        inputField.clear();
        System.out.println("> " + text);
        try {
            pipedOut.write((text + "\n").getBytes());
            pipedOut.flush();
        } catch (IOException e) {
            System.out.println("Erreur : Retour au menu !");
        }
    }

    private void redirigerConsoleVersTextArea() {
        PrintStream ps = new PrintStream(new OutputStream() {
            @Override
            public void write(int b) {
                Platform.runLater(() -> consoleArea.appendText(String.valueOf((char) b)));
            }
        }, true);
        System.setOut(ps);
        System.setErr(ps);
    }

    private void redirigerSystemInDepuisUI() {
        try {
            PipedInputStream pipedIn = new PipedInputStream();
            pipedOut = new PipedOutputStream(pipedIn);
            System.setIn(pipedIn);
        } catch (IOException e) {
            System.out.println("Erreur init System.in : " + e.getMessage());
        }
    }

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

    public static void main(String[] args) {
        launch(args);
    }
}