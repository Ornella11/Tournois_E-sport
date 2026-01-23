import java.sql.Connection;

public interface ActionBDD {
    /**
     *
     * Cette méthode permet de se connecter à la base de donnée
     */
    Connection connectToDatabase();

    /**
     *
     * Cette méthode permet d'afficher la liste des programmeurs
     */
    String afficherProgrammeurs(Connection conn);

    /**
     *
     * Cette méthode permet d'afficher un programmeur selon son id
     */
    void affichageProgrammeurByID(Connection conn);

    // Supprimer un programmeur
    void supprimerProgrammeur(Connection conn);

    // Ajouter un programmeur
    void ajouterProgrammeur(Connection conn);

    // Modifier le salaire
    void modifierSalaire(Connection conn);

    // Liste des projets
    void ListeProjet(Connection conn);

    // Ajouter un projet
    void AjoutProjet(Connection conn);

    // Assigner un projet à un programmeur
    void assignerProjet(Connection conn);

    // Liste des programmeurs qui travaillent sur le même projet
    void afficherProgrammeursByProjet(Connection conn);

    // Questions statistiques
    void stats(Connection conn);

    void salaireMoyen(Connection conn);

    void maxPrime(Connection conn);

    void dureeMoyenneProjet(Connection conn);

    void plusJeuneProgrammeur(Connection conn);

}
