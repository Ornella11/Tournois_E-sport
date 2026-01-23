import java.util.Date;

/**
 * Classe représentant un projet avec son intitulé,
 * ses dates de début et fin de projet,
 * et son état d'avancement.
 */
public class Projet {
    public int id;
    public String intitule;
    public Date date_debut;
    public Date date_fin_prevue;
    public String etat;

    public Projet(int id, String intitule, Date date_debut, Date date_fin_prevue, String etat) {
        this.id = id;
        this.intitule = "";
        this.date_debut = date_debut;
        this.date_fin_prevue = date_fin_prevue;
        this.etat = etat;
    }

    @Override
    public String toString() {
        return id + " "+  intitule + " " + date_debut + " " + date_fin_prevue + " " + etat;
    }
}
