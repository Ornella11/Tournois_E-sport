package modele;

import java.util.Date;

public class Tournoi {
    Integer id_tournoi;
    String nom_tournoi;
    public Date date_debut_tournoi;
    public Date date_fin_tournoi;
    String type;
    Integer dotation;
    String statut;
    Integer id_jeu;


    public Tournoi(Integer id_tournoi, String nom_tournoi,Date date_debut_tournoi, Date date_fin_tournoi, String type, Integer dotation, String statut, Integer id_jeu) {
        this.id_tournoi = id_tournoi;
        this.nom_tournoi = nom_tournoi;
        this.date_debut_tournoi = date_debut_tournoi;
        this.date_fin_tournoi = date_fin_tournoi;
        this.type = type;
        this.dotation = dotation;
        this.statut = statut;
        this.id_jeu = id_jeu;

    }

    /**
     * La méthode toString renvoie une chaine de caractère qui décrit l'objet Joueur
     */
    @Override
    public String toString() {
        return "\nID du joueur: " + id_tournoi + "\nNom du tournoi : " + nom_tournoi + "\nDébut du tournoi : " + date_debut_tournoi + "\nFin du tournoi : " + date_fin_tournoi + "\nType : " + type +"\n" + "\nDotation : " + dotation + "\nStatut : " + statut;
    }

}
