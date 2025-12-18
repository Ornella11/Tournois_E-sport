import java.util.ArrayList;

public class Programmeur {
    Integer id;
    String nom;
    String prenom;
    String adresse;
    String responsable;
    String hobby;
    Integer annaissance;
    double salaire;
    double prime;
    String pseudo;


    public Programmeur(Integer id,String nom, String prenom,String adresse, String pseudo, String responsable, String hobby, Integer annaissance, double salaire, double prime) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.adresse = adresse;
        this.pseudo = nom + " " + prenom;
        this.responsable = responsable;
        this.hobby = hobby;
        this.annaissance = annaissance;
        this.salaire = salaire;
        this.prime = prime;

    }


    @Override
    public String toString() {
        return id + " "+  nom + " " + prenom + " " + annaissance + " " + salaire + " " + prime +  " " + pseudo;
    }

}

