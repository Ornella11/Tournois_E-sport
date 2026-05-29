import java.sql.Connection;
/**
 * Interface regroupant les actions possibles sur la base de données
 * concernant les programmeurs et les projets. Permet la gestion (ajout, suppression, modification) et l'affichage
 * d'informations ainsi que quelques statistiques.
 */
public interface ActionBDD {
    /**
     * Connexion à la base de donnée
     */
    Connection connectToDatabase();

    /**
     * Liste des programmeurs
     */
    String afficherProgrammeurs(Connection conn);

    /**
     * Affichage des programmeurs par leur ID
     */
    void affichageProgrammeurByID(Connection conn);

    /**
     *  Supprimer un programmeur
     */
    void supprimerProgrammeur(Connection conn);

    /**
     *  Ajouter un programmeur
     */
    void ajouterProgrammeur(Connection conn);

    /**
     *  Modifier le salaire d'un programmeur
     */
    void modifierSalaire(Connection conn);

    /**
     *  Liste des projets
     */
    void ListeProjet(Connection conn);

    /**
     *  Méthode permettant d'ajouter un projet
     */
    void AjoutProjet(Connection conn);

    /**
     *  Méthode pour assigner un projet à un programmeur
     */
    void assignerProjet(Connection conn);

    /**
     *  Liste des programmeurs qui travaillent sur le même projet
     */
    void afficherProgrammeursByProjet(Connection conn);

    /**
     *  Menu vers les questions statistiques
     */
    void stats(Connection conn);

    /**
     *  Salaire moyen de tout les programmeurs
     */
    void salaireMoyen(Connection conn);

    /**
     *  Prime maximum à un programmeur
     */
    void maxPrime(Connection conn);

    /**
     *  Durée moyenne des projets
     */
    void dureeMoyenneProjet(Connection conn);

    /**
     *  Affichage du plus jeune programmeur
     */
    void plusJeuneProgrammeur(Connection conn);

}
