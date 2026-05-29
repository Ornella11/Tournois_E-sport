package modele;

import java.util.Date;

public class Joueur {
    Integer id_joueur;
    String pseudo;
    String nom_joueur;
    String prenom_joueur;
    Date date_naissance;
    String nationalite;
    Integer niveau_elo;


    public Joueur(Integer id_joueur,String pseudo, String nom_joueur,String prenom_joueur, Date date_naissance, String nationalite, Integer niveau_elo) {
        this.id_joueur = id_joueur;
        this.pseudo = pseudo;
        this.nom_joueur = nom_joueur;
        this.prenom_joueur = prenom_joueur;
        this.date_naissance = date_naissance;
        this.nationalite = nationalite;

    }

    /**
     * La méthode toString renvoie une chaine de caractère qui décrit l'objet Joueur
     */
    @Override
    public String toString() {
        return "\nID du joueur: " + id_joueur + "\nPseudo : "+  pseudo + "\nNom du joueur : " + nom_joueur + "\nPrénom du joueur : " + prenom_joueur +  "\nDate de naissance : " + date_naissance + "\nNationalité : " + nationalite + "\nNiveau : " + niveau_elo +"\n" ;
    }

}
