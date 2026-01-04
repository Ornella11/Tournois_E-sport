import java.sql.Connection;
import java.util.Scanner;

public class Menu {
    Scanner input = new Scanner(System.in);

    public void demarrer() {
        boolean quitter = false;

        while (!quitter) {
            System.out.println("\n*********  MENU   ************* ");
            System.out.println("1. Afficher tous les programmeurs ");
            System.out.println("2. Afficher un programmeur ");
            System.out.println("3. Supprimer un programmeur");
            System.out.println("4. Ajouter un programmeur");
            System.out.println("5. Modifier le salaire");
            System.out.println("6. Afficher la liste des projets");
            System.out.println("7. Assigner un projet à un programmeur");
            System.out.println("8. Obtenir la liste des programmeurs qui travaillent sur le même projet");
            System.out.println("9. Quitter le programme");
            System.out.print("Votre choix : ");

            int choice = Integer.parseInt(input.nextLine());
            ActionBDD action = new ActionBDDImpl();

            Connection conn = action.connectToDatabase();


            switch (choice) {
                case 1:
                    action.ListeProgrammeurs(conn);
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
                    action.assignerProjet(conn);
                    break;
                case 8:
                    action.afficherProgrammeursByProjet(conn);
                    break;
                case 9:
                    quitter = true;
                    System.out.println("Au revoir !");
                    break;
                default:
                    System.out.println("Choix invalide, réessayez.");
            }
        }
    }
}
