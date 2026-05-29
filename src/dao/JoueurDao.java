package dao;

import modele.Joueur;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import util.ConnexionBDD;

public class JoueurDao {
    Connection conn = ConnexionBDD.getConnection();


    public static void main(String[] args) throws SQLException {
        JoueurDao dao = new JoueurDao();  // instancier le DAO
//        dao.ajouterJoueur();
        dao.listerJoueurs();
        dao.supprimerJoueur();
    }

    public JoueurDao() throws SQLException {
    }

    // Ajouter un nouveau joueur
    public void ajouterJoueur() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        final int MAX_TENTATIVES = 3;
        int tentatives = 0;
        boolean ajoutReussie = false;
        while (!ajoutReussie && tentatives < MAX_TENTATIVES) {
            try {
                System.out.print("Pseudo du joueur: ");
                String pseudo = scanner.nextLine().trim();
                if (pseudo.isEmpty()) {
                    tentatives++;
                    throw new IllegalArgumentException("Le pseudo ne peut pas être vide.");
                }

                System.out.print("Nom du programmeur: ");
                String nom = scanner.nextLine().trim();
                if (nom.isEmpty()) {
                    tentatives++;
                    throw new IllegalArgumentException("Le nom ne peut pas être vide.");
                }

                System.out.print("Prénom du joueur: ");
                String prenom = scanner.nextLine().trim();
                if (prenom.isEmpty()) {
                    tentatives++;
                    throw new IllegalArgumentException("Le prénom ne peut pas être vide.");
                }

                System.out.print("Date de naissance du joueur: ");
                LocalDate date_naissance = LocalDate.parse(scanner.nextLine().trim());

                System.out.print("Nationalité du joueur: ");
                String nationalite = scanner.nextLine().trim();

                System.out.print("Niveau du joueur: ");
                int niveau_elo = Integer.parseInt(scanner.nextLine().trim());


                PreparedStatement pstmt = conn.prepareStatement(
                        "INSERT INTO Joueurs " +
                                "(pseudo, nom_joueur, prenom_joueur, date_naissance, nationalite, niveau_elo) " +
                                "VALUES (?, ?, ?, ?, ?, ?)"
                );

                pstmt.setString(1, pseudo);
                pstmt.setString(2, nom);
                pstmt.setString(3, prenom);
                pstmt.setString(4, String.valueOf(date_naissance));
                pstmt.setString(5, nationalite);
                pstmt.setInt(6, niveau_elo);


                int rs = pstmt.executeUpdate();

                if (rs > 0) {
                    System.out.println("AJOUT REUSSI !");
                    ajoutReussie = true;
                } else {
                    tentatives++;
                    throw new SQLException("Aucune ligne insérée.");
                }
                pstmt.close();


            } catch (IllegalArgumentException e) {
                tentatives++;
                System.out.println("Erreur : " + e.getMessage());

            } catch (SQLException e) {
                tentatives++;
                System.out.println("Erreur SQL : " + e.getMessage());

            }
        }
    }


    // Lister tous les joueurs
    public List<Joueur> listerJoueurs() throws SQLException {
        try {
            Statement stmnt = conn.createStatement();
            ResultSet rs = stmnt.executeQuery("SELECT * FROM joueurs");

            System.out.println("**** Liste des joueur **** :");
            while (rs.next()) {
                Joueur j = new Joueur(
                        rs.getInt("id_joueur"),
                        rs.getString("pseudo"),
                        rs.getString("nom_joueur"),
                        rs.getString("prenom_joueur"),
                        rs.getDate("date_naissance"),
                        rs.getString("nationalite"),
                        rs.getInt("niveau_elo")

                );
                System.out.print(j);
                System.out.print("----------------------");
            }

        } catch (SQLException e) {
            System.out.println("Erreur SQL : " + e.getMessage());
        }
        return null;
    }

    // Rechercher un joueur par pseudo
    public Joueur rechercherParPseudo()
            throws SQLException {
        Joueur j = null;
        try {
            Scanner scanner = new Scanner(System.in);
            boolean trouve = false;
            final int MAX_TENTATIVES = 3;
            int tentatives = 0;

            while (!trouve && tentatives < MAX_TENTATIVES) {
                System.out.print("ID du joueur à afficher : ");

                String input = scanner.nextLine();
                int id;

                try {
                    id = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    System.out.println("Erreur : veuillez saisir un entier valide !");
                    tentatives++;
                    continue;
                }

                String sql = "SELECT * FROM `joueurs` WHERE id_joueur = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, id);

                    try (ResultSet rs = pstmt.executeQuery()) {
                        if (rs.next()) {  // on lit la ligne
                            j = new Joueur(
                                    rs.getInt("id_joueur"),
                                    rs.getString("pseudo"),
                                    rs.getString("nom_joueur"),
                                    rs.getString("prenom_joueur"),
                                    rs.getDate("date_naissance"),
                                    rs.getString("nationalite"),
                                    rs.getInt("niveau_elo")
                            );
                            System.out.println(j);
                            trouve = true;
                        } else {
                            System.out.println("Recherche KO : ID non trouvé. Réessayez.");
                            tentatives++;
                        }
                    }

                } catch (SQLException e) {
                    tentatives++;
                    System.out.println("Erreur SQL : " + e.getMessage());
                    break;
                }
                if (!trouve && tentatives >= MAX_TENTATIVES) {
                    System.out.println("Nombre maximum de tentatives atteint. Retour au menu principal.");
                    tentatives++;
                }
            }
        } catch (Exception e) {
            System.out.println("Erreur inattendue : " + e.getMessage());
        }
        return j;
    }

    // Modifier les informations d'un joueur
    public boolean modifierJoueur(Joueur j) throws SQLException {

        return false;
    }

    // Supprimer un joueur
    public boolean supprimerJoueur()
            throws SQLException {
        Scanner scanner = new Scanner(System.in);
        boolean suppressionReussie = false;
        final int MAX_TENTATIVES = 3;
        int tentatives = 0;

        while (!suppressionReussie && tentatives < MAX_TENTATIVES) {
            PreparedStatement pstmt = null;

            try {
                System.out.print("ID du joueur à supprimer : ");
                Integer id = Integer.valueOf(scanner.nextLine().trim());

                pstmt = conn.prepareStatement("DELETE FROM `joueurs` WHERE id_joueur = ?");
                pstmt.setInt(1, id);
                int rs = pstmt.executeUpdate();

                if (rs > 0) {
                    System.out.println("____________________________");
                    System.out.println("Suppression réussie !");
                    suppressionReussie = true;
                } else {
                    tentatives++;
                    throw new IllegalArgumentException("Suppression KO. Saisissez à nouveau l'id :");
                }

            } catch (NumberFormatException e) {
                System.out.println("Erreur : Veuillez saisir un nombre valide.");
                tentatives++;

            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                tentatives++;


            } catch (SQLException e) {
                System.out.println("Erreur SQL : " + e.getMessage());
                tentatives++;
                break;

            }
        }
        return false;
    }
}
