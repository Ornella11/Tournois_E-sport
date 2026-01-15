import java.sql.Connection;

public interface ActionBDD {
    // Connexion à la base de donnée
    Connection connectToDatabase();

    String afficherProgrammeurs(Connection conn);

    // Affichage des programmeurs
    void affichageProgrammeurByID(Connection conn);

    // Supprimer un programmeur
    void supprimerProgrammeur(Connection conn);

    // Ajouter un programmeur
    void ajouterProgrammeur(Connection conn);

    // Modifier le salaire
    void modifierSalaire(Connection conn);

    // Liste des projets
    void ListeProjet(Connection conn);

    // Assigner un projet à un programmeur
    void assignerProjet(Connection conn);

    // Liste des programmeurs qui travaillent sur le même projet
    void afficherProgrammeursByProjet(Connection conn);
}
