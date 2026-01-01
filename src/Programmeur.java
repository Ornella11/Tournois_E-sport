
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
        this.pseudo = pseudo;
        this.responsable = responsable;
        this.hobby = hobby;
        this.annaissance = annaissance;
        this.salaire = salaire;
        this.prime = prime;

    }


    @Override
    public String toString() {
        return "-ID " + id + " | "+  nom + " " + prenom + " | née en " + annaissance + " | Salaire : " + salaire + "€ | Prime : " + prime +  "€ | Pseudo : " + pseudo;
    }

}

