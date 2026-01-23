
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

    /**
     * Le constructeur permet d'initialiser un programmeur :
     *
     * @param id l'identifiant du programmeur
     * @param nom le nom du programmeur
     * @param prenom le prénom du programmeur
     * @param adresse l'adresse du programmeur
     * @param pseudo le pseudonyme du programmeur
     * @param responsable le nom du responsable du programmeur
     * @param hobby le loisir du programmeur
     * @param annaissance l'année de naissance du programmeur
     * @param salaire le salaire du programmeur
     * @param prime la prime du programmeur
     */
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

    /**
     * La méthode toString renvoie une chaine de caractère qui décrit l'objet Programmeur
     */
    @Override
    public String toString() {
        return "\nID : " + id + "\nNom : "+  nom + "\nPrénom : " + prenom + "\nAdresse : " + adresse +  "\nPseudo : " + pseudo + "\nResponsable : " + responsable + "\nHobby : " + hobby + "\nNaissance : " + annaissance + "\nSalaire : " + salaire + "\nPrime : " + prime + "\n" ;
    }

}

