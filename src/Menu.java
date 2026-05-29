import java.sql.Connection;
import java.util.Scanner;

/**
 * Classe gérant le menu principal de l'application.
 * Elle permet d'accéder aux différentes fonctionnalités liées
 * à la gestion des programmeurs et des projets via la base de données.
 */

public class Menu {
    Scanner input = new Scanner(System.in);

    public void demarrer() {
        boolean quitter = false;

        while (!quitter) {
            System.out.println("\n*********  MENU   ************* ");
            System.out.println("1. Gestion des joueurs ");
            System.out.println("2. Gestion des équipes ");
            System.out.println("3. Gestion des tournois");
            System.out.println("4. Saisir le résultat d'un match");
            System.out.println("5. Classement d'un tournoi");
            System.out.println("6. Statistiques d'un joueur");
            System.out.println("7. Quitter");

            System.out.print("Votre choix : ");

            int choice = Integer.parseInt(input.nextLine());
            ActionBDD action = new ActionBDDImpl();

            Connection conn = action.connectToDatabase();


            switch (choice) {
                case 1:
                    action.afficherProgrammeurs(conn);
                    break;
                case 2:
                    action.affichageProgrammeurByID(conn);
                    break;
                case 3:
                    action.supprimerProgrammeur(conn);
                    break;
                case 4:
                    action.ajouterProgrammeur(conn);
                    break;
                case 5:
                    action.modifierSalaire(conn);
                    break;
                case 6:
                    action.ListeProjet(conn);
                    break;
                case 7:
                    action.AjoutProjet(conn);
                    break;
                case 8:
                    action.assignerProjet(conn);
                    break;
                case 9:
                    action.afficherProgrammeursByProjet(conn);
                    break;
                case 10:
                    action.stats(conn);
                    break;
                case 11:
                    quitter = true;
                    System.out.println("Au revoir !");
                    break;
                default:
                    System.out.println("Choix invalide, réessayez.");
            }
        }
    }
}
