import java.sql.Connection;
import java.util.Scanner;

/**
 * Cette classe est un sous menu qui permet d'afficher différentes informations statistiques
 * sur les programmeurs et les projets.
 */

public class MenuStats {
    Scanner input = new Scanner(System.in);

    public void sousMenu(Connection conn) {
        boolean quitter = false;

        while (!quitter) {
            System.out.println("\n*********  LES STATISTIQUES ********* ");
            System.out.println("1. Afficher le salaire moyen");
            System.out.println("2. Afficher la prime max");
            System.out.println("3. Afficher la durée moyenne des projets");
            System.out.println("4. Afficher la/le plus jeune programmeur de la liste");
            System.out.println("5. Retour");

            System.out.print("Votre choix : ");

            int choice = Integer.parseInt(input.nextLine());
            ActionBDD action = new ActionBDDImpl();

            switch (choice) {
                case 1:
                    action.salaireMoyen(conn);
                    break;
                case 2:
                    action.maxPrime(conn);
                    break;
                case 3:
                    action.dureeMoyenneProjet(conn);
                    break;
                case 4:
                    action.plusJeuneProgrammeur(conn);
                    break;
                case 5:
                    quitter = true;
                    break;
                default:
                    System.out.println("Choix invalide, réessayez.");
            }
        }
    }
}

