import java.util.Date;

/**
 * Classe représentant un projet avec son intitulé,
 * ses dates de début et fin de projet,
 * et son état d'avancement.
 */
public class Projet {
     int id;
     String intitule;
     Date date_debut;
     Date date_fin_prevue;
     String etat;

    /**
     * Le constructeur permet d'initialiser un projet :
     * @param id l'identifiant du projet
     * @param intitule l'intitule du projet
     * @param date_debut la date de début du projet
     * @param date_fin_prevue  la date de fin prévu
     * @param etat l'état du projet (non débuté, en cours ou achevé)
     */
    public Projet(int id, String intitule, Date date_debut, Date date_fin_prevue, String etat) {
        this.id = id;
        this.intitule = "";
        this.date_debut = date_debut;
        this.date_fin_prevue = date_fin_prevue;
        this.etat = etat;
    }

    /**
     * La méthode toString renvoie une chaine de caractère qui décrit l'objet Projet
     */
    @Override
    public String toString() {
        return id + " "+  intitule + " " + date_debut + " " + date_fin_prevue + " " + etat;
    }
}
