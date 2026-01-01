import java.sql.SQLOutput;
import java.util.Scanner;

public class Menu {

    public static void main(String args[]) {
        Scanner input = new Scanner(System.in);
        int choice;
        Menu menu = new Menu();

        System.out.println("*********  MENU   ************* ");
        System.out.println("1. Afficher tous les programmeurs ");
        System.out.println("2. Afficher un programmeur ");
        System.out.println("3. Supprimer un programmeur");
        System.out.println("4. Ajouter un programmeur");
        System.out.println("5. Modifier le salaire");
        System.out.println("6. Afficher la liste des projets");
        System.out.println("7. Assigner un projet à un programmeur");
        System.out.println("8. Obtenir la liste des programmeurs qui travaillent sur le même projet");
        System.out.println("9. Quitter le programme");

    }
}
