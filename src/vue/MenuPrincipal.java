package vue;

import dao.ConsultationDao;
import dao.JoueurDao;
import dao.MatchDao;
import dao.TournoiDAO;

import java.sql.SQLException;
import java.util.Scanner;

public class MenuPrincipal {
    public static void main(String[] args) throws SQLException {
        Scanner input = new Scanner(System.in);
        boolean quitter = false;

        JoueurDao joueur = new JoueurDao();
        TournoiDAO tournoi = new TournoiDAO();
        ConsultationDao consultation = new ConsultationDao();
        MatchDao match = new MatchDao();

        while (!quitter) {
            System.out.println("\n*********  MENU   ************* ");
            System.out.println("1. Gestion des joueurs");
            System.out.println("2. Gestion des équipes");
            System.out.println("3. Gestion des tournois");
            System.out.println("4. Saisir le résultat d'un match");
            System.out.println("5. Classement d'un tournoi");
            System.out.println("6. Statistiques d'un joueur");
            System.out.println("7. Quitter");
            System.out.print("Votre choix : ");

            int choice = Integer.parseInt(input.nextLine());

            switch (choice) {
                case 1:
                    boolean quitterJoueur = false;
                    while (!quitterJoueur) {
                        System.out.println("\nChoisir : ");
                        System.out.println("1. Ajouter un nouveau joueur");
                        System.out.println("2. Lister les joueurs");
                        System.out.println("3. Rechercher un joueur par son pseudo");
                        System.out.println("4. Modifier les informations d'un joueur");
                        System.out.println("5. Supprimer un joueur");
                        System.out.println("6. Quitter");
                        System.out.print("Votre choix : ");

                        int selection = Integer.parseInt(input.nextLine());
                        switch (selection) {
                            case 1: joueur.ajouterJoueur(); break;
                            case 2: joueur.listerJoueurs(); break;
                            case 3: consultation.rechercherParMotCle(); break;
                            case 4: joueur.modifierJoueur(); break;
                            case 5: joueur.supprimerJoueur(); break;
                            case 6:
                                quitterJoueur = true;
                                System.out.println("Retour au menu principal.");
                                break;
                            default:
                                System.out.println("Choix invalide, réessayez.");
                        }
                    }
                    break;

                case 2:
                    boolean quitterEquipe = false;
                    while (!quitterEquipe) {
                        System.out.println("\nChoisir : ");
                        System.out.println("1. Inscrire une équipe à un tournoi");
                        System.out.println("2. Afficher les statistiques d'une équipe");
                        System.out.println("3. Quitter");
                        System.out.print("Votre choix : ");

                        int equipe = Integer.parseInt(input.nextLine());
                        switch (equipe) {
                            case 1: tournoi.inscriptionEquipe(); break;
                            case 2: consultation.afficherStatsEquipe(); break;
                            case 3:
                                quitterEquipe = true;
                                System.out.println("Retour au menu principal.");
                                break;
                            default:
                                System.out.println("Choix invalide, réessayez.");
                        }
                    }
                    break;

                case 3:
                    boolean quitterTournoi = false;
                    while (!quitterTournoi) {
                        System.out.println("\nChoisir : ");
                        System.out.println("1. Créer un nouveau tournoi");
                        System.out.println("2. Classement des équipes d'un tournoi");
                        System.out.println("3. Quitter");
                        System.out.print("Votre choix : ");

                        int t = Integer.parseInt(input.nextLine());
                        switch (t) {
                            case 1: tournoi.ajouterTournoi(); break;
                            case 2: consultation.afficherClassementTournoi(); break;
                            case 3:
                                quitterTournoi = true;
                                System.out.println("Retour au menu principal.");
                                break;
                            default:
                                System.out.println("Choix invalide, réessayez.");
                        }
                    }
                    break;

                case 4:
                    match.saisirScoreMatch();
                    break;

                case 5:
                    consultation.afficherClassementTournoi();
                    break;

                case 6:
                    boolean quitterStats = false;
                    while (!quitterStats) {
                        System.out.println("\nChoisir : ");
                        System.out.println("1. Saisir les statistiques d'un joueur");
                        System.out.println("2. Afficher les statistiques d'un match");
                        System.out.println("3. Quitter");
                        System.out.print("Votre choix : ");

                        int stats = Integer.parseInt(input.nextLine());
                        switch (stats) {
                            case 1: match.saisirStatistiqueIndividuelle(); break;
                            case 2: match.afficherStatistiquesMatch(); break;
                            case 3:
                                quitterStats = true;
                                System.out.println("Retour au menu principal.");
                                break;
                            default:
                                System.out.println("Choix invalide, réessayez.");
                        }
                    }
                    break;

                case 7:
                    quitter = true;
                    System.out.println("Au revoir !");
                    break;

                default:
                    System.out.println("Choix invalide, réessayez.");
            }
        }
    }
}