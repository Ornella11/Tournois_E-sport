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

            switch (choice) {
                case 1:
                    ActionBDDImpl.ListeProgrammeurs(ActionBDDImpl.connectToDatabase());
                    break;
                case 2:
                    ActionBDDImpl.affichageProgrammeurByID(ActionBDDImpl.connectToDatabase());
                    break;
                case 3:
                    ActionBDDImpl.supprimerProgrammeur(ActionBDDImpl.connectToDatabase());
                    break;
                case 4:
                    ActionBDDImpl.ajouterProgrammeur(ActionBDDImpl.connectToDatabase());
                    break;
                case 5:
                    ActionBDDImpl.modifierSalaire(ActionBDDImpl.connectToDatabase());
                    break;
                case 6:
                    ActionBDDImpl.ListeProjet(ActionBDDImpl.connectToDatabase());
                    break;
                case 7:
                    ActionBDDImpl.assignerProjet(ActionBDDImpl.connectToDatabase());
                    break;
                case 8:
                    ActionBDDImpl.ProgrammeurByProjet(ActionBDDImpl.connectToDatabase());
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
