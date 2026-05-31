package dao;

import modele.Joueur;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import util.ConnexionBDD;

public class JoueurDao {
    Connection conn = ConnexionBDD.getConnection();

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

            System.out.println("\n**** Liste des joueurs ****");
            System.out.printf("%-5s %-20s %-15s %-15s %-15s %-15s %-10s%n",
                    "ID", "Pseudo", "Nom", "Prénom", "Date naissance", "Nationalité", "ELO");
            System.out.println("-".repeat(100));

            while (rs.next()) {
                System.out.printf("%-5d %-20s %-15s %-15s %-15s %-15s %-10d%n",
                        rs.getInt("id_joueur"),
                        rs.getString("pseudo"),
                        rs.getString("nom_joueur"),
                        rs.getString("prenom_joueur"),
                        rs.getDate("date_naissance"),
                        rs.getString("nationalite"),
                        rs.getInt("niveau_elo")
                );
            }
            System.out.println("-".repeat(100));

        } catch (SQLException e) {
            System.out.println("Erreur SQL : " + e.getMessage());
        }
        return null;
    }
    // Modifier les informations d'un joueur
    public void modifierJoueur() throws SQLException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Entrez l'ID du joueur à modifier : ");
        int idJoueur = scanner.nextInt();
        scanner.nextLine();

        Joueur j = trouverJoueurParId(idJoueur);
        if (j == null) {
            System.out.println("Aucun joueur trouvé avec l'ID : " + idJoueur);
            return;
        }

        System.out.println("Laissez vide pour conserver la valeur actuelle.");

        String sql = """
            UPDATE joueurs
            SET pseudo         = ?,
                nom_joueur     = ?,
                prenom_joueur  = ?,
                nationalite    = ?,
                niveau_elo     = ?
            WHERE id_joueur = ?
            """;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            System.out.print("Pseudo du joueur: ");
            String pseudo = scanner.nextLine().trim();
            pstmt.setString(1, pseudo);

            System.out.print("Nom du joueur: ");
            String nom = scanner.nextLine().trim();
            pstmt.setString(2, nom);

            System.out.print("Prénom du joueur: ");
            String prenom = scanner.nextLine().trim();
            pstmt.setString(3, prenom);

            System.out.print("Nationalité du joueur: ");
            String nationalite = scanner.nextLine().trim();
            pstmt.setString(4, nationalite);

            System.out.print("Niveau du joueur: ");
            int niveau_elo = Integer.parseInt(scanner.nextLine().trim());
            pstmt.setInt(5, niveau_elo);

            pstmt.setInt(6, idJoueur);

            int lignesModifiees = pstmt.executeUpdate();
            if (lignesModifiees > 0) {
                System.out.println("Joueur modifié avec succès !");
            }

        } catch (SQLException e) {
            System.out.println("Erreur SQL : " + e.getMessage());
        }

    }

    // Méthode utilitaire pour retrouver un joueur par son ID
    private Joueur trouverJoueurParId(int id) throws SQLException {
        String sql = "SELECT * FROM joueurs WHERE id_joueur = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Joueur(
                        rs.getInt("id_joueur"),
                        rs.getString("pseudo"),
                        rs.getString("nom_joueur"),
                        rs.getString("prenom_joueur"),
                        rs.getDate("date_naissance"),
                        rs.getString("nationalite"),
                        rs.getInt("niveau_elo")
                );
            }
        }
        return null;
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
